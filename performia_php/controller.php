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
    $data = get_user_info($username);
    $data = $data->fetch();
    if (strcmp($data['password'], hash(sha1,$pass,false))==0)
    {
        session_start();
        $_SESSION['user'] = $data['username'];
        $_SESSION['id'] = $data['id'];
        list_challenge();
    }
    else{
        $err = 1;
        require('./views/login.php');
    }

}
