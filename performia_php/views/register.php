<?php
$title = 'Performia register';
ob_start();
require './public/define.php';


$month = date('m');
$day = date('d');
$year = date('Y');

$today = $day.'/'.$month.'/'.$year;

?>
    <script type="text/javascript">
        function change_type(){
            document.getElementById('age_register').type = "date";
            document.getElementById('age').value = "<?php echo$today;?>";

        }
        function change_color(){
            document.getElementById('gender_register').style.color = "#555555";
        }
    </script>
    <div class="login">
        <div class ="login_title">
            Register on Performia
        </div>
        <form name = "register" action="index.php?action=sign_up" method="post">



            <div class="wrap_input">
                <input name="username_register" type="text" placeholder="Username" id="username_register" />
            </div>
            <div class="wrap_input">
                <input name="age_register" type="text" placeholder="Birthdate"  id="age_register" onclick="change_type()"/>
            </div>
            <div class="wrap_input">
                <select name = 'gender_register' id ='gender_register' onclick="change_color()">
                    <option value = "">Gender</option>
                    <option value = "male">Male</option>
                    <option value = "female">Female</option>
                    <option value = "other">Other</option>
                </select>
            </div>
            <div class="wrap_input">
                <input type="password" name="password_register" placeholder="Password_register" id="password_register" />
            </div>
            <div class="wrap_input">
                <input type="password" name="password2_register" placeholder="Password verification" id="password2_register" />
            </div>
            <?
            if(isset($err) && $err != -1)
            {?>
                <div class="input_error"><?php echo ERROR_[$err];?> </div>
            <?}?>

            <p><input type="submit" value="Sign up" name="ok" id="ok"/></p>
        </form>
        <input type="submit" value="Sign in" name ='ok' id="ok"
               onclick="window.location='index.php?action=sign_in';" />
    </div>


<?php
$css = "public/css.css";
$content = ob_get_clean();
ob_clean();
require 'template.php';
?>
