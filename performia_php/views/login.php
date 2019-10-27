<?php
$title = 'Performia';
ob_start();
require './public/define.php';
?>
<div class="login">
    <div class ="login_title">
        Welcome in PERFORMIA
    </div>
    <form name = "connection" action="index.php?action=sign_up" method="post">



        <div class="wrap_input">
            <input name="username" type="text" placeholder="Username" id="username" />
        </div>

        <div class="wrap_input">
            <input type="password" name="password" placeholder="Password" id="password" />
        </div>
        <? if(isset($err) && $err != -1)
         {?>
            <div class="input_error"><?php echo ERROR_[$err];?> </div>
         <?}?>

        <p><input type="submit" value="Login" /></p>
    </form>
</div>


<?php
$css = "public/css.css";
$content = ob_get_clean();
ob_clean();
require 'template.php';
?>
