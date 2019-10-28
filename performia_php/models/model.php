<?php

function get_user_info($username)
{
    $db = db_connect();
    $req = $db->prepare('SELECT id, username, password FROM user WHERE username = :user');
    $req->bindValue(':user', $username,PDO::PARAM_STR);
    $req->execute();
    return $req;
}

function get_challenge_list()
{
    $db = db_connect();
    $req =$db->query('SELECT * FROM challenge ');

    return $req;
}
function get_challenge($id_challenge)
{
    $db = db_connect();
    $req = $db->prepare('SELECT * FROM challenge WHERE challenge_id = :id');
    $req->bindValue(':id', $id_challenge, PDO::PARAM_INT);
    $req->execute();

    return $req;
}
function db_connect()
{
    $db = new PDO('mysql:host=mysql;dbname=performia;charset=utf8', 'performia', 'performia');
    return $db;
}
