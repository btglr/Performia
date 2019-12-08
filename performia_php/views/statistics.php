<?php
$title = 'Statistique';
ob_start();
require './public/define.php';

echo "<div class='wrap_list_stat'>
    <div class='list_title'>Statistiques</div>";
    if (!empty($data) && array_key_exists("code", $data) && ($data["code"] >= 500) && $data["code"] < 1000) {

        $data = $data["data"];
        $search = ["_","-"];
        echo "<table>";
        echo" <tr>
                    <th>
                    &nbsp
                    </th>
                    <th>
                        partie jouée
                    </th>
                    <th>
                        partie gagnée
                    </th>
                    <th>
                        % victoire
                    </th>
                    <th>
                        découverte
                    </th>
                    <th>
                        % de découverte
                    </th>
                </tr>";
        foreach($data as $ai)
        {
            $name = str_replace($search," ",$ai['name_ai'], $count);
            $victoire = (int)((int)$ai['nb_win'] *100 / (int)$ai['nb_played']);
            $decouverte = (int)((int)$ai['nb_prediction'] *100 / (int)$ai['nb_played']);

            echo" <tr>
                    <td>
                        ".$name.
                    "</td>
                    <td>
                        ".$ai['nb_played']."
                    </td>
                    <td>
                        ".$ai['nb_win']."
                    </td>
                    <td>
                        ".$victoire."%
                    </td>
                    <td>
                        ". $ai['nb_prediction']."
                    </td>
                    <td>
                        ".$decouverte."%
                    </td>
                </tr>";

        }
        echo "</table>";
    }
    else
        echo "Vous n'avez pas encore d'IA";

echo "</div>";

$css = "public/css.css";
$content = ob_get_clean();
ob_clean();
require 'template.php';
?>