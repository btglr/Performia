using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace AiCSharp
{
    class Program
    {
        static int randomlyChoose(int[] grille)
        {
            int res;
            var rand = new Random(Guid.NewGuid().GetHashCode());

            res = rand.Next(7);
            while(grille[res] != 0)
            {
                res = rand.Next(7);
                Console.Out.WriteLine("Res = " + res);
            }

            return res;
        }
        static void Main(string[] args)
        {
            JObject config = JObject.Parse(File.ReadAllText("config/ia.json"));
            TCPClient client = new TCPClient();
            int choice;
            int[] grid;
            var rand = new Random(Guid.NewGuid().GetHashCode());
            bool onGoingChallenge = true;
            int challengeId = 1;

            JObject info;
            JObject result;
            client.Connect((String)config.GetValue("login"), (String)config.GetValue("password"));

            JObject initialGameState = client.ChooseChallenge(challengeId);
            info = JObject.Parse((String)initialGameState.GetValue("data"));

            while (onGoingChallenge)
            {
                JObject res = new JObject();
                onGoingChallenge = !(bool)info.GetValue("fini");

                JArray gridArray = JArray.Parse((String)info.GetValue("grille"));
                grid = new int[gridArray.Count];

                for (int i = 0; i < gridArray.Count; ++i)
                {
                    grid[i] = (int)gridArray[i];
                }

                if (client.GetUserID() == (int)info.GetValue("id_player") && onGoingChallenge)
                {
                    choice = randomlyChoose(grid);
                    res.Add("id_utilisateur", client.GetUserID());
                    res.Add("colonne", choice);

                    info = client.PlayTurn(res);
                }
                else
                {
                    info = JObject.Parse((String)client.GetChallengeState().GetValue("data"));
                }

                Thread.Sleep(rand.Next(1000, 5000));
            }
            client.CloseSocket();
        }
    }
}
