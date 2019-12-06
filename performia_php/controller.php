<?php
require('./models/model.php');

function list_challenge($user_id)
{
    $url = implode("/", array(HTTP_SERVER_URL, REQUEST_HANDLER));
    $url .= "?code=6&id_utilisateur=" . $user_id;

    $handle = curl_init($url);
    curl_setopt($handle,  CURLOPT_RETURNTRANSFER, TRUE);

    $response = curl_exec($handle);

    if ($response) {
        $data = json_decode($response, true);
    }

    require 'views/list_challenge.php';
}

function challenge($challenge_id)
{
    $url = implode("/", array(HTTP_SERVER_URL, REQUEST_HANDLER));
    session_start();
    $url .= "?code=7&id_utilisateur=" . $_SESSION["id"] . "&challenge_id=" . $challenge_id;

    $handle = curl_init($url);
    curl_setopt($handle,  CURLOPT_RETURNTRANSFER, TRUE);

    $response = curl_exec($handle);

    if ($response) {
        $data = json_decode($response, true);
    }

    require 'views/challenge'. $challenge_id .'.php';
}

function admin() {
    require 'views/admin.php';
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
            list_challenge($_SESSION["id"]);
        }

        else {
            $err = 1;
            require("./views/login.php");
        }
    }
}

function sign_out() {
    if (session_status() == PHP_SESSION_NONE) {
        session_start();
    }

    // TODO Envoyer un message de déconnexion au serveur pour qu'il supprime la partie en cours ?

    session_unset();
    session_destroy();

    header("Location: index.php");
}