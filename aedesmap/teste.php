<?php
    
    if($_SERVER['REQUEST_METHOD'] == "POST"){
	// Get data
	$imageBlob = $_POST['imageBlob'];
	$latitude = floatval($_POST['latitude']);
	$longitude = floatval($_POST['longitude']);
    
    echo " $imageBlob, $latitude, $longitude "; 
    
    } 