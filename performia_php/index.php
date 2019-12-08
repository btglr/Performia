<?php
require('controller.php');
require './public/define.php';

try {
    if (isset($_GET['action'])) {
        if (strcmp($_GET['action'], 'sign_in') == 0) {
            if(isset($_POST['username'])&& (!empty($_POST['username'])))
            {
                if(isset($_POST['password'])&&(!empty($_POST['password']))) {
                    $err = -1;
                    login($_POST['username'], $_POST['password']);
                }
                else
                {
                    $err = 0;
                    require('views/login.php');

                }
            }
            else if (isset($_POST['ok']))
            {

                $err = 0;
                require('views/login.php');

            }
            else
            {
                $err = -1;
                require ('views/login.php');
            }

        }

        else if(strcmp($_GET['action'],'challenge') == 0) {
            if(isset($_GET['id']) && $_GET['id'] > 0) {
                challenge($_GET['id']);
            }
        }
        else if (strcmp($_GET['action'], 'list_challenge') == 0) {
            if (session_status() == PHP_SESSION_NONE) {
                session_start();
            }

            if (isset($_SESSION["id"])) {
                list_challenge($_SESSION["id"]);
            }
        }
        else if (strcmp($_GET['action'],'statistique')==0)
        {
            stats();
        }
        else if (strcmp($_GET['action'], 'admin') == 0) {
            admin();
        }
        else if (strcmp($_GET['action'], 'sign_up') == 0)
        {

            if(isset($_POST['username_register']) && (!empty($_POST['username_register'])) && isset($_POST['age_register'])&& (!empty($_POST['age_register'])) && isset($_POST['gender_register'])&& (!empty($_POST['gender_register'])) && isset($_POST['password_register']) && (!empty($_POST['password_register'])) && isset($_POST['password2_register'])&& (!empty($_POST['password2_register'])))
            {
                $err = -1;
                sign_up($_POST['username_register'],$_POST['age_register'], $_POST['gender_register'], $_POST['password_register'], $_POST['password2_register']);
            }
            else if (isset($_POST['ok'])){
                $err = 0;
                require("views/register.php");
            }
            else
            {
                require("views/register.php");
            }

        }

        else if (strcmp($_GET['action'], 'sign_out') == 0) {
            sign_out();
        }

        else if (strcmp($_GET['action'], 'start_ai') == 0) {
            if (isset($_POST["ai_id"]) && isset($_POST["ai_login"]) && isset($_POST["ai_password"]) && isset($_POST[$_POST["ai_id"]])) {
                $err = -1;

                $exploded_host_port = explode("_", $_POST[$_POST["ai_id"]]);
                request_start_ai($_POST["ai_id"], $_POST["ai_login"], $_POST["ai_password"], $exploded_host_port[0], $exploded_host_port[1]);
            }
        }
    }
    else {
        $err = -1;
        require('views/login.php');
    }
}
catch(Exception $e) {
    echo 'Error : ' . $e->getMessage();
}
