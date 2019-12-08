<?php
if (!empty($data) && array_key_exists("code", $data) && ($data["code"] >= 500) && $data["code"] < 1000) {
	$challenge = $data["data"];
	$title = $challenge['challenge_name'];
	$css = "public/challenge.css";
}
//Addresse du serveur http
//Handler de recuperation de la grille
//et d'envoi de la colonne

// Envoi au serveur HTTP que l'utilisateur a choisi ce challenge

$ajax_url = HTTP_REQUEST_URL;
$user_id = $_SESSION["id"];
$room_id = -1;

$handle = curl_init(HTTP_REQUEST_URL . "?code=2&user_id=" . $user_id . "&challenge_id=2");
curl_setopt($handle, CURLOPT_RETURNTRANSFER, true);

$response = curl_exec($handle);

if ($response) {
    $json = json_decode($response, true);

    $room_id = $json["room_id"];
}

$content = <<<HTML
  	<link rel="stylesheet" href="public/reflex.css">
	<div class="challengebox">
		<div class="title">{$title}</div>
		<hr width="30%" color="-webkit-linear-gradient(left, #b721ff, #21d4fd);">
		<div id="challenge"></div>
	</div>
	<div class="actionsbox">
		<h1>Informations</h1>
		<hr width="40%" color="-webkit-linear-gradient(left, #b721ff, #21d4fd);" align="left">
			<p>Votre but ?</p>
			<p>Devinez a l'issue de la partie si vous avez joué contre un vrai joueur !</p>
			<hr width="40%" color="-webkit-linear-gradient(left, #b721ff, #21d4fd);" align="left">
			<button id="stats">Statistiques</button>
			<a href="index.php"><button id="return">Quitter</button></a>
	</div>
	<div class="rulesbox">
		<h1>Règles</h1>
		<hr width="15%" color="-webkit-linear-gradient(left, #b721ff, #21d4fd);" align="left">
		<p>Le principe du jeu est de frapper à l'aide d'un marteau sur le plus grand nombre de taupes parmi celles qui sortent pour un temps très limité et aléatoirement des trous situés sur un terrain de jeu.</p>
	</div>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.js"></script>
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.css" />
	<script>
	    let intervalIDPlateau;
	
		$( document ).ready(function() {
		    let intervalID = setInterval(waitChallenge, 500);
			
			let cpt = 0;
			let s = "";
			function waitChallenge() {
				$.ajax({
					url: "$ajax_url",
					type: "GET",
					data: "code=5&user_id=$user_id&room_id=$room_id",
					dataType: "json"
				}).done(function(res) {
					if (res["code"] === 504) {
						console.log("Challenge can start");
						clearInterval(intervalID);
						updatePlateau();
						
						intervalIDPlateau = setInterval(updatePlateau, 1000);
					}
					else {
						cpt++;
						if(cpt === 4) cpt = 1;
						if(cpt === 1) s = ".";
						if(cpt === 2) s = ". .";
						if(cpt === 3) s = ". . .";

						$("#challenge").html("<h2 class='waiting-opponent'>Waiting for an opponent</h2><h2 class='waiting-opponent'>" + s + " </h2>");
						console.log("Challenge cannot start");
					}
				});
			}
		});

		//Mise a jour du plateau
		//Pour le moment lors du click sur le bouton "Actualiser le plateau"
		$("#refresh").click(function() {
			updatePlateau();
		});

		//Fonction de mise a jour visuel
		//Parametre json -> recuperation de la grille sur le serveur http
		function updatePlateau() {
			$.ajax({
			  url: "challenges/reflex/reflex.php",
			  data: "url=$ajax_url?room_id=$room_id",
			  dataType: "json"
			}).done(function(res) {
			 	 if (res["code"] === 507) {
					clearInterval(intervalIDPlateau);

				if (res["id_player"] !== -1) {
				    $("<div class='modal'><p>Fin du jeu!</p></div>").appendTo("body").modal();
				}
			}
			$("#challenge").replaceWith(res["php"]);
			console.log('update done');
			});
		}

		//Fonction de selection de la case du plateau pour l'envoi au serveur http
		function choose_case(c){
			console.log("case select : ",c);
			$.ajax({
			  url: "$ajax_url",
			  type: "GET",
			  data: "code=3&user_id=$user_id&room_id=$room_id&case=" + c
			});
			updatePlateau()
		}
	</script>
HTML;

require 'template.php';