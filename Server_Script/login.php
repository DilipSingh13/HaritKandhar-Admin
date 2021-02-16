<?php

require("config.php");

if (!empty($_POST)) {
    $response = array("error" => FALSE);
    
    $query = "SELECT * FROM  haritkandharapp_user_admin WHERE mobile =:mobile OR email=:email";
    
    $query_params = array(
        ':email' => $_POST['email'],
        ':mobile' => $_POST['email']);
    try {
        $stmt = $db->prepare($query);
        $result = $stmt->execute($query_params);
}

    catch (PDOException $ex) {
        $response["error"] = true;
        $response["message"] = "Database Error. Please Try Again!";
        die(json_encode($response));
    }
    $row = $stmt->fetch();
    if (password_verify($_POST['password'], $row['encrypted_password'])) {
                    $login_ok = true;
        if ($login_ok == true) {
        $response["error"] = false;
        $response["message"] = "Login successful!";
        $response["user"]["uid"] = $row["unique_id"];
        $response["user"]["name"] = $row["name"];
        $response["user"]["mobile"] = $row["mobile"];
        $response["user"]["email"] = $row["email"];
        $response["user"]["created_at"] = $row["created_at"];
        die(json_encode($response));

    }
    }
    else {
                $response["error"] = true;
                $response["message"] = "Invalid Credentials!";
                die(json_encode($response));
             
         }
        }
   else{
        $response["error"] = true;
        $response["message"] = "error in login!";
        die(json_encode($response));
    }
?>