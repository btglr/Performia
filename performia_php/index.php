<?php
require('controller.php');
require './public/define.php';

try {
    if (isset($_GET['action'])) {
        if (strcmp($_GET['action'], 'sign_up') == 0) {
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
            else
            {
                $err = 0;
                require('views/login.php');
            }
        }

        else if(strcmp($_GET['action'],'challenge') == 0) {
            if(isset($_GET['id']) && $_GET['id'] >0) {
                challenge($_GET['id']);
            }
        }

        else if (strcmp($_GET['action'], 'admin') == 0) {
            admin();
        }

        else if (strcmp($_GET['action'], 'sign_out') == 0) {
            sign_out();
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
