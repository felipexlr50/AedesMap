<?php

$mysqli = mysqli_connect("localhost", "root", "","aedesmap");

if($_SERVER['REQUEST_METHOD'] == "POST"){
	// Get data
	//$imageBlob = mysqli_real_escape_string($mysqli,$_POST['imageBlob']);
	$latitude = floatval($_POST['latitude']);
	$longitude = floatval($_POST['longitude']);
	
	// Insert data into data base
	$sql = "INSERT INTO imagem ( latitude, longitude) 
			VALUES ($latitude, $longitude);";
	$qur = $mysqli->query($sql);
	if($qur){
		$json = array("status" => 1, "msg" => "OK");
	}else{
		$json = array("status" => 0, "msg" => "Error");
	}
}

else{
	$json = array("status" => 0, "msg" => "Request method not accepted");
}

mysqli_close($mysqli);

/* Output header */
	header('Content-type: application/json');
	echo json_encode($json);