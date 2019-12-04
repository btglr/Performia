<?php
require('./models/model.php');

function list_challenge()
{
    $data = get_challenge_list();
    require 'views/list_challenge.php';
}

function challenge($id)
{
    $data = get_challenge($id);
    require 'views/challenge'.$id.'.php';
}
function login($username,$pass)
{
    $err=0;

    $hashed_password = hash("sha1", $pass);

    $url = implode("/", array(HTTP_SERVER_URL, REQUEST_HANDLER));
    $url .= "?code=1&login=" . $username . "&password=" . $hashed_password;

    $handle = curl_init($url);
    curl_setopt($handle,  CURLOPT_RETURNTRANSFER, TRUE);

    $response = curl_exec($handle);

    if ($response) {
        $decoded = json_decode($response, true);

        // User is now connected
        if (isset($decoded["id_utilisateur"])) {
            session_start();
            $_SESSION["user"] = $username;
            $_SESSION["id"] = $decoded["id_utilisateur"];
            list_challenge();
        }

        else {
            $err = 1;
            require("./views/login.php");
        }
    }
}
