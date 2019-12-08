<?php
require('./models/model.php');

function stats() {
    $url = implode("/", array(HTTP_SERVER_URL, REQUEST_HANDLER));
    session_start();
    $url .= "?code=10&id_utilisateur=" . $_SESSION["id"];

    $handle = curl_init($url);
    curl_setopt($handle,  CURLOPT_RETURNTRANSFER, TRUE);

    $response = curl_exec($handle);

    if ($response) {
        $data = json_decode($response, true);
    }

    require 'views/statistics.php';
}

function list_challenge($user_id)
{
    $url = implode("/", array(HTTP_SERVER_URL, REQUEST_HANDLER));
    $url .= "?code=6&user_id=" . $user_id;

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
    session_start();

    $handle = curl_init(HTTP_REQUEST_URL . "?code=7&user_id=" . $_SESSION["id"] . "&challenge_id=" . $challenge_id);
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

function register(){
    require 'views/register.php';
}
function sign_up($username, $birthdate, $gender, $password, $password2){

    $err = 0;
    setlocale(LC_TIME, ['fr', 'fra', 'fr_FR']);
    $month = date('m');
    $day = date('d');
    $year = date('Y');

    $today = $day.'-'.$month.'-'.$year;
    $d1 =strtotime($today);
    $d2 =strtotime($birthdate);

    if(strcmp($password,$password2)!=0 || $d2>$d1)
    {
        if ($d2>$d1)
            $err = 4;
        else
            $err = 3;
        require ('views/register.php');
    }
    else
    {
        if(strcmp($gender,'male') == 0)
            $gender = 1;
        else if(strcmp($gender,'female')==0)
            $gender = 2;
        else
            $gender = 3;
        $hashed_password = hash("sha1", $password);

        $url = HTTP_REQUEST_URL;
        $url .= "?code=8&login=" . $username . "&password=" . $hashed_password."&birthdate=" . $birthdate . "&gender=" . $gender;

        $handle = curl_init($url);
        curl_setopt($handle, CURLOPT_RETURNTRANSFER, TRUE);

        $response = curl_exec($handle);

        if ($response) {
            $decoded = json_decode($response, true);

            // User is now connected
            if (isset($decoded["user_id"])) {
                session_start();
                $_SESSION["user"] = $username;
                $_SESSION["id"] = $decoded["user_id"];
                $_SESSION["type"] = $decoded["account_type"];

                login($username, $password);
            }

            else {
                $err = 5;
                require("./views/register.php");
            }
        }

    }
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
        if (isset($decoded["user_id"])) {
            session_start();
            $_SESSION["user"] = $username;
            $_SESSION["id"] = $decoded["user_id"];
            $_SESSION["type"] = $decoded["account_type"];
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