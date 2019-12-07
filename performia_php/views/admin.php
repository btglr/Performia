<?php
session_start();

if (!isset($_SESSION["id"])) {
    header("Location: index.php");
}

$title = "Performia - Page d'administration";
$css = "public/css.css";

$content = <<<HTML
	<p>Hello World</p>
HTML;

require 'template.php';