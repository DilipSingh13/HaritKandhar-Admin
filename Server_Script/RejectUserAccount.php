<?php
include 'config.php';

// Create connection
$conn = new mysqli($host, $username, $password, $dbname);

if ($conn->connect_error) {
 
 die("Connection failed: " . $conn->connect_error);
}

$email = $_POST['email'];

$sql = "UPDATE haritkandharapp_user SET admin_approved = 'rejected' WHERE email = '$email'";
$sql2 = "UPDATE haritkandharapp_user SET block_status = 'Not Available' WHERE email = '$email'";
if ($conn->query($sql)&&$conn->query($sql2)) {
        $response["error"] = false;
        $response["message"] = "User rejected successfully";
        die(json_encode($response));
 
} else {
        $response["error"] = true;
        $response["message"] = "Database error";
        die(json_encode($response));
}
 echo $json;
$conn->close();
?>