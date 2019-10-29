<?php

session_start();

function getColor($col, $p1, $p2) {
	if ($col === $p1) {
		return "yellow";
	}
	else if ($col === $p2) {
		return "red";
	}
	else {
		return "white";
	}
}

$php = "<p> Impossible de créer le challenge :</p>
		<p>     -> serveur http injoignable ?</p>
		<p>     -> donnees recues invalides ?</p>";

if (isset($_GET["url"])) {
	$url = $_GET["url"] . "?code=4&id_utilisateur=" . $_SESSION["id"];

	$handle = curl_init($url);
	curl_setopt($handle, CURLOPT_RETURNTRANSFER, TRUE);

	$response = curl_exec($handle);

	if ($response) {
		$decoded = json_decode($response, true);

		if (array_key_exists("data", $decoded)) {
			if (array_key_exists("grille", $decoded["data"])) {
				if (array_key_exists("fini", $decoded["data"]) && $decoded["data"]["fini"] === true) {
					echo $decoded["data"]["id_player"];
					die();
				}

				$grille = $decoded["data"]["grille"];
				$players = $decoded["data"]["players"];
				$p1 = $players[0]["id"];
				$p2 = $players[1]["id"];

				$php = "<div style='display:flex; flex-direction:column;height:80%';justify-content:center'>";
				for ($i = 0; $i < 6; $i++) {
					for ($j = 0; $j < 7; $j++) {
						if ($i == 0) {
							if ($j == 0) {
								$php .= "<div style='display:flex; flex-direction:column; justify-content:center;'>";
								$php .= "<div style='background-color:blue; display:flex; flex-direction:row; justify-content:space-between; width:80%;height:75px;border-top:10px solid;border-left:10px solid;border-right:10px solid;margin:auto;max-width:550px'>";
								$php .= "<div onclick='choose_col(".$j.")' id=".$j."-".$i." style='background-color:".getColor($grille[$j + $i * 7], $p1, $p2).";display:flex;flex-direction:column; justify-content:center; border:10px solid DarkBlue ;border-radius:100px; width:50px;height:50px;margin-top:auto;margin-bottom:auto'>  </div>";
							}
							elseif ($j == 6) {
								$php .= "<div onclick='choose_col(".$j.")' id=".$j."-".$i." style='background-color:".getColor($grille[$j + $i * 7], $p1, $p2).";display:flex;flex-direction:column; justify-content:center; border:10px solid DarkBlue ;border-radius:100px; width:50px;height:50px;margin-top:auto;margin-bottom:auto'>  </div>";
								$php .= "</div></div>";
							}
							else {
								$php .= "<div onclick='choose_col(".$j.")' id=".$j."-".$i." style='background-color:".getColor($grille[$j + $i * 7], $p1, $p2).";display:flex;flex-direction:column; justify-content:center; border:10px solid DarkBlue ;border-radius:100px; width:50px;height:50px;margin-top:auto;margin-bottom:auto'>  </div>";
							}

						}
						elseif ($i == 5) {

							if ($j == 0) {
								$php .= "<div style='display:flex; flex-direction:column; justify-content:center;'>";
								$php .= "<div style='background-color:blue; display:flex; flex-direction:row; justify-content:space-between; width:80%;height:75px;border-left:10px solid;border-right:10px solid;border-bottom:10px solid;;margin:auto;max-width:550px'>";
								$php .= "<div onclick='choose_col(".$j.")' id=".$j."-".$i." style='background-color:".getColor($grille[$j + $i * 7], $p1, $p2).";display:flex;flex-direction:column; justify-content:center; border:10px solid DarkBlue ;border-radius:100px; width:50px;height:50px;margin-top:auto;margin-bottom:auto'>  </div>";
							}
							elseif ($j == 6) {
								$php .= "<div onclick='choose_col(".$j.")' id=".$j."-".$i." style='background-color:".getColor($grille[$j + $i * 7], $p1, $p2).";display:flex;flex-direction:column; justify-content:center; border:10px solid DarkBlue ;border-radius:100px; width:50px;height:50px;margin-top:auto;margin-bottom:auto'>  </div>";
								$php .= "</div></div>";
							}
							else {
								$php .= "<div onclick='choose_col(".$j.")' id=".$j."-".$i." style='background-color:".getColor($grille[$j + $i * 7], $p1, $p2).";display:flex;flex-direction:column; justify-content:center; border:10px solid DarkBlue ;border-radius:100px; width:50px;height:50px;margin-top:auto;margin-bottom:auto'>  </div>";
							}


						}
						else {

							if ($j == 0) {
								$php .= "<div style='display:flex; flex-direction:column; justify-content:center;'>";
								$php .= "<div style='background-color:blue; display:flex; flex-direction:row; justify-content:space-between; width:80%;height:75px;border-left:10px solid;border-right:10px solid;;margin:auto;max-width:550px'>";
								$php .= "<div onclick='choose_col(".$j.")' id=".$j."-".$i." style='background-color:".getColor($grille[$j + $i * 7], $p1, $p2).";display:flex;flex-direction:column; justify-content:center; border:10px solid DarkBlue ;border-radius:100px; width:50px;height:50px;margin-top:auto;margin-bottom:auto'>  </div>";
							}
							elseif ($j == 6) {
								$php .= "<div onclick='choose_col(".$j.")' id=".$j."-".$i." style='background-color:".getColor($grille[$j + $i * 7], $p1, $p2).";display:flex;flex-direction:column; justify-content:center; border:10px solid DarkBlue ;border-radius:100px; width:50px;height:50px;margin-top:auto;margin-bottom:auto'>  </div>";
								$php .= "</div></div>";
							}
							else {
								$php .= "<div onclick='choose_col(".$j.")' id=".$j."-".$i." style='background-color:".getColor($grille[$j + $i * 7], $p1, $p2).";display:flex;flex-direction:column; justify-content:center; border:10px solid DarkBlue ;border-radius:100px; width:50px;height:50px;margin-top:auto;margin-bottom:auto'>  </div>";
							}

						}
					}
				}
				$php .= "</div><br></body></html>";
			}
		}
	}
}
echo $php;