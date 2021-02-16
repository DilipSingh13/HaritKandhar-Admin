<?php
include 'config.php';

// Create connection
$conn = new mysqli($host, $username, $password, $dbname);

if ($conn->connect_error) {
 
 die("Connection failed: " . $conn->connect_error);
}

$name = $_POST['name'];
$email = $_POST['email'];
$mobile = $_POST['mobile'];
$count = $_POST['count'];
$tree_name1 = $_POST['tree1'];
$tree_name2 = $_POST['tree2'];

$unique_code = rand(100000, 999999);
$unique_code2 = rand(100000, 999999);

$sql = "INSERT INTO Assign_Tree_Details (name, email, mobile, tree_count,tree_name1, tree_name2, approval_date, approval_time, unique_code) VALUES ('$name','$email','$mobile','$count','$tree_name1','$tree_name2',NOW(),NOW(),'$unique_code')";

$sql2 = "INSERT INTO Approved_Plantation_Plan (Unique_code,Name, Email, Mobile, Tree_name, Month_Slot, Grant_Status,Date) VALUES ('$unique_code','$name','$email','$mobile','$tree_name1',1,'Yes',NOW()),('$unique_code2','$name','$email','$mobile','$tree_name2',1,'Yes',NOW())";

$sql3 = "INSERT INTO Approved_Plantation_Plan (Unique_code,Name, Email, Mobile, Tree_name, Month_Slot, Grant_Status,Date) VALUES ('$unique_code','$name','$email','$mobile','$tree_name1',1,'Yes',NOW())";

$sql4 = "UPDATE haritkandharapp_user SET admin_approved = 'approved' WHERE email = '$email'";

$sql5 = "UPDATE haritkandharapp_user SET block_status = 'unblocked' WHERE email = '$email'";


$checkdetails=strcmp("Not Available",$tree_name2);

if($checkdetails==0){
if ($conn->query($sql3) && $conn->query($sql) && $conn->query($sql4) && $conn->query($sql5)) {
    
    $subject = "Harit Kandhar Tree Approve";
    $message = "Dear User,\n\nYour request for approval of tree has been approved. Now you can acess your tree plantation plan using application. Kindly login to application inorder to upload tree picture.\n\nRegards,\nHarit Kandhar Parivar";
   
    $from = "support@anviinfotechs.com";
    $headers = "From:" . $from;
    mail($email,$subject,$message,$headers);
    $response["error"] = false;
    $response["message"] = "Plantation request approved successfully";
    die(json_encode($response));
} else {
        $response["error"] = true;
        $response["message"] = "Database error";
        die(json_encode($response));
}
}
else{
    if ($conn->query($sql) && $conn->query($sql2) && $conn->query($sql4) && $conn->query($sql5)){
    $subject = "Harit Kandhar Tree Approve";
    $message = "Dear User,\n\nYour request for approval of tree has been approved. Now you can acess your tree plantation plan using application. Kindly login to application inorder to upload tree picture.\n\nRegards,\nHarit Kandhar Parivar";
    $from = "support@anviinfotechs.com";
    $headers = "From:" . $from;
    mail($email,$subject,$message,$headers);
    $response["error"] = false;
    $response["message"] = "Plantation request approved successfully";
    die(json_encode($response));
} else {
        $response["error"] = true;
        $response["message"] = "Database2 error";
        die(json_encode($response));
}
}
 echo $json;
$conn->close();
?>