<?php
$title = 'Challenges List';

ob_start();

if (!empty($data) && array_key_exists("code", $data) && ($data["code"] >= 500) && $data["code"] < 1000) {
    $data = $data["data"];

    echo '<div class="wrap_list">
<div class="list_title">Challenges</div><ul class="challenge_list">';
    foreach ($data as $ch)
    { ?>

            <li class="challenge_name">
                <div class="link"><a href="./index.php?action=challenge&amp;id=<?php echo($ch['challenge_id']);?>"><?php echo($ch['challenge_name']);?></a></div>
                <div class="border"></div>
            </li>
            <div class="challenge_description">
                <?php echo($ch['challenge_description']);?>
            </div>

        <?php
    }

    echo '</ul></div>';
}
else
{
    echo ERROR_[2];
}
?>
<?php 
$css = "public/css.css";
$content = ob_get_clean();
ob_end_clean();?>


<?php require('template.php'); ?>

