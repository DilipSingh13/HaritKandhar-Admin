<?php 

define('host', 'localhost');
define('user', 'username here');
define('pass', 'password here');
define('db', 'database name here');
$conn = mysqli_connect(host, user, pass, db) or die('Unable to Connect');
?>

