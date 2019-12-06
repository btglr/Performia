<?php

if (!defined("ERROR_")) {
    define('ERROR_', array(
        'Please fill all fields',
        'Wrong username/password combination',
        'Sorry there is no challenge here for the moment'
    ));
}

if (!defined("HTTP_SERVER_URL")) {
    define("HTTP_SERVER_URL", "http://ec2-3-87-99-176.compute-1.amazonaws.com:25000");
}

if (!defined("REQUEST_HANDLER")) {
	define("REQUEST_HANDLER", "request");
}

if (!defined("HTTP_REQUEST_URL")) {
    define("HTTP_REQUEST_URL", implode("/", array(HTTP_SERVER_URL, REQUEST_HANDLER)));
}