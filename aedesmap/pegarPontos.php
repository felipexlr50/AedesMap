<?php

$mysqli = mysqli_connect("localhost", "root", "","aedesmap");
 
    
    $sql = "select latitude,longitude, dataInserida from imagem ";
    $result = $mysqli->query($sql); //mysqli_query($mysqli,$sql);
    // printf("Error: %s\n", mysqli_error($mysqli));
    $resultArray =array();
    
    while($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
        
        $resultArray[] = array($latitude = $row['latitude'],$longitude = $row['longitude'], $dataInserida = $row['dataInserida']); 
    }
    
    $json = array("status" => 1, "info" => $resultArray);
 
 @mysqli_close($mysqli);
 
 /* Output header */
 header('Content-type: application/json');
 echo json_encode($json);