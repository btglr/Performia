<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title><?= $title ?></title>
    <link href=<?= $css ?> rel="stylesheet" />
    <?php require './public/define.php';?>
</head>

<body>
	<?= $content ?>

    <?php
    if (session_status() == PHP_SESSION_NONE) {
        session_start();
    }

    if (isset($_SESSION["id"])) {
        ?>
        <div class="menu">
            <a href="index.php?action=admin">Administration</a>
            <a href="index.php?action=sign_out">Déconnexion</a>
        </div>
        <?php
    }
    ?>
</body>
</html>
