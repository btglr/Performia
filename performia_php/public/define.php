<?php

if (!defined("ERROR_")) {
    define('ERROR_', array(
        'Please fill all fields',
        'Wrong username/password combination',
        'Sorry there is no challenge here for the moment',
        'Password verification doesn\'t match your password',
        'Date isn\'t valid',
        'Username is already used'
    ));
}

if (!defined("HTTP_SERVER_URL")) {
    define("HTTP_SERVER_URL", "http://ec2-35-180-228-52.eu-west-3.compute.amazonaws.com:25000");
}

if (!defined("REQUEST_HANDLER")) {
	define("REQUEST_HANDLER", "request");
}

if (!defined("COMMAND_HANDLER")) {
    define("COMMAND_HANDLER", "command");
}

if (!defined("HTTP_REQUEST_URL")) {
    define("HTTP_REQUEST_URL", implode("/", array(HTTP_SERVER_URL, REQUEST_HANDLER)));
}

if (!defined("HTTP_COMMAND_URL")) {
    define("HTTP_COMMAND_URL", implode("/", array(HTTP_SERVER_URL, COMMAND_HANDLER)));
}