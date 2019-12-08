<?php

session_start();

if (!isset($_SESSION["id"])) {
    header("Location: index.php");
}

$title = "Performia - Admin Page";
ob_start();
require './public/define.php';

?>
<script type="text/javascript">
    function show_and_add_fields(host, port) {
        let elems = document.getElementsByClassName("hidden");
        for (let i = elems.length - 1; i >= 0; --i) {
            elems[i].classList.remove("hidden");
        }
    }
</script>

<div class="admin">
    <div class="login_title">
        Manage your AIs
    </div>
    <form name="manage_ai" id="manage_ai" action="index.php?action=start_ai" method="post">
        <div class="wrap_input">
            <select name='ai_id' id='ai_id'>
                <option value="" disabled selected>Type</option>
                <?php

                foreach ($ai_types as $t) {
                    $id = $t["ai_id"];
                    $name = $t["ai_name"];
                    $host = $t["ai_host"];
                    $port = $t["ai_port"];
                    echo "<option value='$id' onclick='show_and_add_fields(\"$host\", $port)'>$name</option>";
                }

                ?>
            </select>
        </div>
        <div class="wrap_input hidden">
            <input type="text" name="ai_login" id="ai_login" placeholder="Login">
        </div>

        <div class="wrap_input hidden">
            <input type="password" name="ai_password" id="ai_password" placeholder="Password">
        </div>

        <?php

        foreach ($ai_types as $t) {
            $id = $t["ai_id"];
            $host = $t["ai_host"];
            $port = $t["ai_port"];

            echo "<input type='hidden' name='$id' value='" . $host . "_" . $port ."'>";
        }

        ?>

        <?php
        if (isset($err) && $err != -1) {
            echo "<div class=\"input_error\">" . ERROR_[$err] . "</div>";
        }
        ?>

        <p><input type="submit" value="Start" name="ok" id="ok"/></p>
    </form>
</div>
<script src="https://code.jquery.com/jquery-3.4.1.min.js" integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js" integrity="sha384-FzT3vTVGXqf7wRfy8k4BiyzvbNfeYjK+frTVqZeNDFl8woCbF0CYG6g2fMEFFo/i" crossorigin="anonymous"></script>
<script>
    $(function() {
        $('#manage_ai').ajaxForm(function(data) {
            alert(data);
        });
    });
</script>

<?php
$css = "public/css.css";
$content = ob_get_clean();
ob_clean();
require 'template.php';
?>
