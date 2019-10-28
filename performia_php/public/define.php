<?php

if (!defined("ERROR_")) {
    define('ERROR_', array(
        'Please fill all fields',
        'Wrong username/password combination',
        'Sorry there is no challenge here for the moment'
    ));
}

if (!defined("HTTP_SERVER_URL")) {
    define("HTTP_SERVER_URL", "http://http_server:25000/request");
}
