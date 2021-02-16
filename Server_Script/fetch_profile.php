<?php

require("config.php");

if (!empty($_POST)) {
    $response = array("error" => FALSE);
    $email = $_POST['email'];
    
    $query = "SELECT * FROM haritkandharapp_user_admin WHERE email = :email";
    
    $query_params = array(
        ':email' => $_POST['email']
    );
    
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
    $checkvalue=strcmp("No",$row['profile']);
    if($checkvalue==0){
    $response["error"] = true;
    $response["message"] = "No";
    die(json_encode($response));
    }
    else{
        $response["error"] = false;
        $response["profile"] = $row["profile"];
        die(json_encode($response));
    }
}
?>