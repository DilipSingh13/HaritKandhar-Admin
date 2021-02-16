<?php
include 'config.php';

// Create connection
$conn = new mysqli($host, $username, $password, $dbname);

if ($conn->connect_error) {
 
 die("Connection failed: " . $conn->connect_error);
}

$email = $_POST['email'];
$code = $_POST['code'];

$sql = "UPDATE Approve_Plant_Picture SET approve_status = 'approved' WHERE email = '$email' and Unique_code='$code'";

if ($conn->query($sql)) {
        $response["error"] = false;
        $response["message"] = "Picture approved successfully";
        die(json_encode($response));
} else {
        $response["error"] = true;
        $response["message"] = "Database error";
        die(json_encode($response));
}
 echo $json;
$conn->close();
?>