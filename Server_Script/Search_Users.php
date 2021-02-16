<?php

$username = "username here"; 
$password = "password here"; 
$host = "localhost"; 
$dbname = "database name here"; 
// Create connection
$conn = new mysqli($host, $username, $password, $dbname);

if ($conn->connect_error) {
 
 die("Connection failed: " . $conn->connect_error);
}

$email=$_POST['email'];

$sql = "SELECT * FROM haritkandharapp_user WHERE email='$email' OR mobile='$email' AND admin_approved='unapproved'";

$result = $conn->query($sql);

if ($result->num_rows >0) {
 
 
 while($row[] = $result->fetch_assoc()) {
 
 $tem = $row;
 
 $json = json_encode($tem);
 
 
 }
 
} else {
 echo "No Results Found";
}
 echo $json;
$conn->close();
?>