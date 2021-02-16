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

$sql = "SELECT name,email,mobile,address FROM `haritkandharapp_user` ORDER BY unique_id LIMIT 5000";

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