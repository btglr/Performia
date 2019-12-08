<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title><?= $title ?></title>
    <link href=<?= $css ?> rel="stylesheet" />
    <?php require './public/define.php';?>
    <script type="text/javascript">
        function swipe()   {
            var div = document.getElementById('menu'),
                display = getComputedStyle(div, null).display;

            if(display == "block")  {
                document.getElementById("menu").style.display = "none";
            }else   {
                document.getElementById("menu").style.display = "block";
            }
        }
    </script>
</head>

<body>
<?php
    if (isset($_SESSION['id'])) {
    ?>

    <input type="submit" value="" name = "menu" id="bouton_menu" onclick="swipe()" />
    <div class="menu" id="menu">
        <div class="item">
        <?php
        if($_SESSION['type'] == 2) {
            ?>
            <li class="menu_item">
                <div class="link"><a href="./index.php?action=statistique">Statistics</a></div>
                <div class="border"></div>
            </li>
            <li class="menu_item">
                <div class="link"><a href="./index.php?action=list_challenge">Challenges</a></div>
                <div class="border"></div>
            </li>
            <li class="menu_item">
                <div class="link"><a href="./index.php?action=admin">Admin</a></div>
                <div class="border"></div>
            </li>
            <?php
        }
        ?>
            <li class="menu_item">
                <div class="link"><a href="./index.php?action=sign_out">Log out</a></div>
                <div class="border"></div>
            </li>
        </div>
    </div>

    <?php
    }
    ?>
<?= $content ?>
</body>
</html>
