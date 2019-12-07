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

function register(){
    require 'views/register.php';
}
function sign_up($username, $age, $gender, $password, $password2){

    $err = 0;
    setlocale(LC_TIME, ['fr', 'fra', 'fr_FR']);
    $month = date('m');
    $day = date('d');
    $year = date('Y');

    $today = $day.'-'.$month.'-'.$year;
    $d1 =strtotime($today);
    $d2 =strtotime($age);

    if(strcmp($password,$password2)!=0 | $d2>$d1)
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
            $gender = 0;
        else if(strcmp($gender,'female')==0)
            $gender = 1;
        else
            $gender = 2;
        $hashed_password = hash("sha1", $password);

        $url = implode("/", array(HTTP_SERVER_URL, REQUEST_HANDLER));
        $url .= "?code=8&login=" . $username . "&password=" . $hashed_password."&birthdate=".$age . "&gender=". $gender;

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
                $_SESSION["type"] = 3;
                list_challenge($_SESSION["id"]);
            }

            else {
                $err = 1;
                require("./views/login.php");
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
        if (isset($decoded["id_utilisateur"])) {
            session_start();
            $_SESSION["user"] = $username;
            $_SESSION["id"] = $decoded["id_utilisateur"];
            $_SESSIONS["type"] = $decoded["type"];
            list_challenge($_SESSION["id"]);
        }

        else {
            $err = 1;
            require("./views/login.php");
        }
    }
}
