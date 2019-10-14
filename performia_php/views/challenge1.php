<?php
if (!empty($data))
{
    $challenge = $data->fetch();
    $title = $challenge['challenge_name'];
	$css = "public/challenge.css";
}

//Addresse du serveur http
//Handler de recuperation de la grille : 
$urlRecupGrille = "http://localhost:25633/request";
$urlSend = "http://localhost:25633/";

$handle = curl_init($urlRecupGrille);
curl_setopt($handle,  CURLOPT_RETURNTRANSFER, TRUE);

$response = curl_exec($handle);

if($response == FALSE) {
	$json = 0;
} else {
    $json = file_get_contents($urlRecupGrille);
}

$content = <<<HTML
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
			<button id="guess">→</button>
			<hr width="40%" color="-webkit-linear-gradient(left, #b721ff, #21d4fd);" align="left">
			<button id="stats">Statistiques</button>
			<p>
			<button id="refresh">Actualiser le plateau</button>
		<br>
		<br>
			<a href="index.php"><button id="return">Quitter</button></a>
	</div>
	<div class="rulesbox">
		<h1>Règles</h1>
		<hr width="15%" color="-webkit-linear-gradient(left, #b721ff, #21d4fd);" align="left">
		<p>Pour jouer au connect 4, il vous faut :</p>
		<ul>
			<li/>Le plateau du jeu et ses 42 emplacements pour jetons répartis en 6 lignes et 7 colonnes
			<li/>42 jetons de 2 couleurs différentes
			<li/>Être 2 joueurs
		</ul>
		<p>Commencer une partie de connect 4 :
		<ul>
			<li/>Pour commencer une partie de connect 4, on désigne le premier joueur. Celui­ci met un de ses jetons de couleur dans l’une des colonnes de son choix. Le jeton tombe alors en bas de la colonne.

			<li/>Le deuxième joueur insère à son tour son jeton, de l’autre couleur dans la colonne de son choix. Et ainsi de suite jusqu’à obtenir une rangée de 4 jetons de même couleur.
		</ul>
		<p>Pour gagner une partie de connect 4, il suffit d’être le premier à aligner 4 jetons de sa couleur horizontalement, verticalement et diagonalement.</p>
		<p>Si lors d’une partie, tous les jetons sont joués sans qu’il y est d’alignement de jetons, la partie est déclaré nulle.</p>
	</div>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	<script>
		$( document ).ready(function() {
		    updatePlateau();
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
			  url: "challenges/connect4/connect4.php",
			  data: $json
			}).done(function(res) {
			  $("#challenge").replaceWith(res);
			  console.log('update done');
			});
		}

		//Fonction pour l'affichage du plateau
		function change_color(i,j){
			document.getElementById(j+'-'+i).style.backgroundColor="red";    
		}

		//Fonction de selection de la colonne du plateau pour l'envoi au serveur http
		function choose_col(col){
			console.log("column select : ",col);
			$.ajax({
			  url: $urlSend,
			  data: col
			});
			updatePlateau()
		}
	</script>
HTML;

require 'template.php';