<?php
$title = 'Statistique';
ob_start();
require './public/define.php';
?>
    Page de statistique

<?php
$css = "public/css.css";
$content = ob_get_clean();
ob_clean();
require 'template.php';
?>
<?php
