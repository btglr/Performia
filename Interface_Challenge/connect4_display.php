<?php

function getColor($col){
  if($col === 1){
    return "yellow";
  }else if($col === 2){
    return "red";
  }else{
    return "white";
  }
}

$json = file_get_contents("./testConnect.json");
$grille =json_decode($json)->{'grille'}; 



$php="<html style='display:flex'><head><script type='text/javascript' src='./script.js'></script><title>Connect 4</title><body style='width:100%;display:flex;flex-direction: column;height:100%'><h1 style='display:flex; margin:auto'>CONNECT 4</h1>";
$php.="<div style='display:flex; flex-direction:column;height:80%';justify-content:center;'>";


for($i =0; $i < 6; $i++){
  for($j =0; $j < 7; $j++){
    if($i ==0 ){       
      if($j == 0){
        $php .= "<div style='display:flex; flex-direction:column; justify-content:center;'>";
        $php .= "<div style='background-color:blue; display:flex; flex-direction:row; justify-content:space-between; width:80%;height:75px;border-top:10px solid;border-left:10px solid;border-right:10px solid;margin:auto'>";
        $php .= "<div onclick='choose_col(".$j.")' id=".$j."-".$i." style='background-color:".getColor($grille[$j+$i*7]).";display:flex;flex-direction:column; justify-content:center; border:10px solid DarkBlue ;border-radius:100px; width:50px;height:50px;margin-top:auto;margin-bottom:auto'>  </div>";
      }else if($j==6){
        $php .= "<div onclick='choose_col(".$j.")' id=".$j."-".$i." style='background-color:".getColor($grille[$j+$i*7]).";display:flex;flex-direction:column; justify-content:center; border:10px solid DarkBlue ;border-radius:100px; width:50px;height:50px;margin-top:auto;margin-bottom:auto'>  </div>";
        $php .= "</div></div>";

      }else{
        $php .= "<div onclick='choose_col(".$j.")' id=".$j."-".$i." style='background-color:".getColor($grille[$j+$i*7]).";display:flex;flex-direction:column; justify-content:center; border:10px solid DarkBlue ;border-radius:100px; width:50px;height:50px;margin-top:auto;margin-bottom:auto'>  </div>";
      }
    
    }else if($i == 5){
     
      if($j == 0){
        $php .= "<div style='display:flex; flex-direction:column; justify-content:center;'>";
        $php .= "<div style='background-color:blue; display:flex; flex-direction:row; justify-content:space-between; width:80%;height:75px;border-left:10px solid;border-right:10px solid;border-bottom:10px solid;;margin:auto'>";
        $php .= "<div onclick='choose_col(".$j.")' id=".$j."-".$i." style='background-color:".getColor($grille[$j+$i*7]).";display:flex;flex-direction:column; justify-content:center; border:10px solid DarkBlue ;border-radius:100px; width:50px;height:50px;margin-top:auto;margin-bottom:auto'>  </div>";
      }else if($j==6){
        $php .= "<div onclick='choose_col(".$j.")' id=".$j."-".$i." style='background-color:".getColor($grille[$j+$i*7]).";display:flex;flex-direction:column; justify-content:center; border:10px solid DarkBlue ;border-radius:100px; width:50px;height:50px;margin-top:auto;margin-bottom:auto'>  </div>";
        $php .= "</div></div>";

      }else{
        $php .= "<div onclick='choose_col(".$j.")' id=".$j."-".$i." style='background-color:".getColor($grille[$j+$i*7]).";display:flex;flex-direction:column; justify-content:center; border:10px solid DarkBlue ;border-radius:100px; width:50px;height:50px;margin-top:auto;margin-bottom:auto'>  </div>";
      }
          
     
    }else{
      
      if($j == 0){
        $php .= "<div style='display:flex; flex-direction:column; justify-content:center;'>";
        $php .= "<div style='background-color:blue; display:flex; flex-direction:row; justify-content:space-between; width:80%;height:75px;border-left:10px solid;border-right:10px solid;;margin:auto'>";
        $php .= "<div onclick='choose_col(".$j.")' id=".$j."-".$i." style='background-color:".getColor($grille[$j+$i*7]).";display:flex;flex-direction:column; justify-content:center; border:10px solid DarkBlue ;border-radius:100px; width:50px;height:50px;margin-top:auto;margin-bottom:auto'>  </div>";
      }else if($j==6){
        $php .= "<div onclick='choose_col(".$j.")' id=".$j."-".$i." style='background-color:".getColor($grille[$j+$i*7]).";display:flex;flex-direction:column; justify-content:center; border:10px solid DarkBlue ;border-radius:100px; width:50px;height:50px;margin-top:auto;margin-bottom:auto'>  </div>";
        $php .= "</div></div>";

      }else{
        $php .= "<div onclick='choose_col(".$j.")' id=".$j."-".$i." style='background-color:".getColor($grille[$j+$i*7]).";display:flex;flex-direction:column; justify-content:center; border:10px solid DarkBlue ;border-radius:100px; width:50px;height:50px;margin-top:auto;margin-bottom:auto'>  </div>";  
      }
     
    }
  }
}






$php .= "</div></body></html>";

echo($php);