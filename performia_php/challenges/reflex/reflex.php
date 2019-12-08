<?php

if (session_status() == PHP_SESSION_NONE) {
    session_start();
}

$php = "<p> Impossible de créer le challenge :</p>
		<p>     -> serveur http injoignable ?</p>
		<p>     -> donnees recues invalides ?</p>";

if(isset($_GET["url"])) {
	$url = $_GET["url"] . "?code=4&user_id=" . $_SESSION["id"];

	$handle = curl_init($url);
	curl_setopt($handle,  CURLOPT_RETURNTRANSFER, TRUE);

	$response = curl_exec($handle);
	if($response == FALSE) {
		$json = "0";
	} else {
		$json = file_get_contents($url);
	}
if($json != "0" && strcmp($json,"") != 0)  {
	$data = json_decode(json_encode($json));
	$html = <<<HTML
		<div class="container">
			<div class="game-container">
				<div class="column">
					<div class="case" id="0">
					</div>
					<div class="case" id="5">
					</div>
					<div class="case" id="10">
					</div>
					<div class="case" id="15">
					</div>
					<div class="case" id="20">
					</div>
				</div>
				<div class="column">
					<div class="case" id="1">
					</div>
					<div class="case" id="6">
					</div>
					<div class="case" id="11">
					</div>
					<div class="case" id="16">
					</div>
					<div class="case" id="21">
					</div>
				</div>
				<div class="column">
					<div class="case" id="2">
					</div>
					<div class="case" id="7">
					</div>
					<div class="case" id="12">
					</div>
					<div class="case" id="17">
					</div>
					<div class="case" id="22">
					</div>
				</div>
				<div class="column">
					<div class="case" id="3">
					</div>
					<div class="case" id="8">
					</div>
					<div class="case" id="13">
					</div>
					<div class="case" id="18">
					</div>
					<div class="case" id="23">
					</div>
				</div>
				<div class="column">
					<div class="case" id="4">
					</div>
					<div class="case" id="9">
					</div>
					<div class="case" id="14">
					</div>
					<div class="case" id="19">
					</div>
					<div class="case" id="24">
					</div>
				</div>
			</div>
			<div class="score">
				<div>
					<h2>Score : </h2>
				</div>
				<div class="playerscore">
					<p>J1 : <strong>Pas</strong> points</p>
					<p>J2 : <strong>encore</strong> points</p>
					<p>J3 : <strong>implementé</strong> points</p>
					<p>J4 : <strong>!</strong> points</p>
				</div>
			</div>
		</div>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.js"></script>
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.css" />
	<script type="text/javascript">
		var data = $json;
		var game = data["data"];

		$( document ).ready(function() {
			showGrille(game["grille"]);
		});

		//Affichage du plateau de jeu
		function showGrille(grille) {
			$( ".case" ).each(function( index ) {
			  var id = $(this).attr("id");
			  $(this).css("background-image",swichImage(grille,id));
			});
		}

		//Switch entre les images (allume/eteint)
		function swichImage(grille, idCase) {
			if(grille[idCase])
				return "url(challenges/reflex/images/on.PNG)";
			return "url(challenges/reflex/images/off.PNG)";
		}


		$( ".case" ).click(function() {
		  	choose_case($(this).attr("id"));
		});
	</script>
	</html>
HTML;
	$php = $html;
	}
}
echo $php;