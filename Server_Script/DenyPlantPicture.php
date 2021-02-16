<?php
include 'config.php';

// Create connection
$conn = new mysqli($host, $username, $password, $dbname);

if ($conn->connect_error) {
 
 die("Connection failed: " . $conn->connect_error);
}

$code = $_POST['code'];
$Picture = $_POST['Picture'];

$sql = "UPDATE Approved_Plantation_Plan SET Grant_Status = 'Yes' WHERE Unique_code = '$code'";

$sql2 = "DELETE FROM Approve_Plant_Picture WHERE Unique_code = '$code' and plant_picture='$Picture'";


if ($conn->query($sql)) {
    if ($conn->query($sql2)) {
        $response["error"] = false;
        $response["message"] = "Picture rejected successfully";
        die(json_encode($response));
    }
    else{
    $response["error"] = true;
        $response["message"] = "Update 1 error";
        die(json_encode($response));
    }
    
} else {
        $response["error"] = true;
        $response["message"] = "Database error";
        die(json_encode($response));
}
 echo $json;
$conn->close();
?>