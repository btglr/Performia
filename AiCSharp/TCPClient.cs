using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Sockets;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace AiCSharp
{

    class TCPClient
    {
        private TcpClient client;
        private NetworkStream stream;
        private int userID;

        public TCPClient()
        {
            JObject config = JObject.Parse(File.ReadAllText("config/config.json"));
            client = new TcpClient("localhost",(int)config["port_tcp"]);
            stream = client.GetStream();
        }

        static string Hash(string input)
        {
            using (SHA1Managed sha1 = new SHA1Managed())
            {
                var hash = sha1.ComputeHash(Encoding.UTF8.GetBytes(input));
                var sb = new StringBuilder(hash.Length * 2);

                foreach (byte b in hash)
                {
                    sb.Append(b.ToString("x2"));
                }

                return sb.ToString();
            }
        }

        public void Connect(String login, String password)
        {
            JObject jo = new JObject();
            jo.Add("login", login);
            jo.Add("password", Hash(password));

            JObject message = new JObject();
            message.Add("code", 1);
            message.Add("data", jo.ToString());
            SendData(message);

            JObject res = RetrieveData();
            if((int)res.GetValue("code") != 503 )
            {
                Console.Error.WriteLine("Connection failed");
                Environment.Exit(-1);
            }
            userID = (int)JObject.Parse((String)res.GetValue("data")).GetValue("id_utilisateur");
        }

        private void SendData(JObject mes)
        {
            String message = mes.ToString();
            byte[] data = System.Text.Encoding.ASCII.GetBytes(message);
            stream.Write(data, 0, data.Length);
        }

        public JObject RetrieveData()
        {
            byte[] data = new Byte[256];

            String responseData = String.Empty;

            Int32 bytes = stream.Read(data, 0, data.Length);
            responseData = System.Text.Encoding.ASCII.GetString(data, 0, bytes);
            return JObject.Parse(responseData);
        }

        public JObject ChooseChallenge(int numeroChallenge)
        {
            JObject jo = new JObject();
            jo.Add("id_utilisateur", this.userID);
            jo.Add("numero_challenge", numeroChallenge);

            JObject message = new JObject();
            message.Add("code", 2);
            message.Add("data", jo.ToString());
            SendData(message);

            JObject res = RetrieveData();
            if ((int)res.GetValue("code") != 500)
            {
                Console.Error.WriteLine("Choosing a challenge failed");
                Environment.Exit(-1);
            }
            return JObject.Parse((String)res.GetValue("data"));
        }

        public JObject GetChallengeState()
        {
            JObject jo = new JObject();
            jo.Add("id_utilisateur", this.userID);

            JObject message = new JObject();
            message.Add("code", 4);
            message.Add("data", jo.ToString());
            SendData(message);

            JObject res = RetrieveData();
            if ((int)res.GetValue("code") != 502)
            {
                Console.Error.WriteLine("Couldn't retrieve the challenge state");
                Environment.Exit(-1);
            }
            return JObject.Parse((String)res.GetValue("data"));
        }

        public void SendTurn(JObject action)
        {
            JObject message = new JObject();
            message.Add("code", 3);
            message.Add("data", action.ToString());
            SendData(message);
        }

        public JObject ReceiveTurn()
        {
            JObject res = RetrieveData();
            if ((int)res.GetValue("code") != 501)
            {
                Console.Error.WriteLine("Incorrect move");
                Environment.Exit(-1);
            }
            return JObject.Parse((String)res.GetValue("data"));
        }

        public JObject PlayTurn(JObject action)
        {
            SendTurn(action);

            return ReceiveTurn();
        }

        public int GetUserID()
        {
            return userID;
        }

        public void CloseSocket()
        {
            stream.Close();
            client.Close();
        }
    }
}
