<?php

/* Erstellt 2007 by Institute for geoinformatics and remote sensing, university of osnabrück
*  This program is free software under the GNU General Public
* License (>=v2) */






$host = "localhost";
$port = "5432";
$dbname = "routingdb";
$user = "postgres";
$password = "postgres";





$con_string = "host=$host port=$port dbname=$dbname user=$user password=$password";
$con = pg_connect ($con_string);

//Hier der Code für das Ermitteln von x1 und y1
 $id_check = "SELECT max(gid)as gid from fridastreets";
 $res_id_check = pg_query($con,$id_check);
   $count = pg_result($res_id_check,"gid");
   echo "Anzahl der Eintraegege in der DB: ".$count;
   echo "<br>";


for ($x=1;$x<=$count;$x++)
{

 $start = "SELECT astext(StartPoint(the_geom))as startpoint from fridastreets where gid='$x'";
 $res_start= pg_query($con,$start);
   $start_ergebnis = pg_result($res_start,"startpoint");
   echo "<b>Geometrie $x</b></br>";
  echo "Anfangspunkte (x1,y1): ".$start_ergebnis;
  // echo "<br>";
  $array_01=array("POINT(",")");
  $array_02=array("","");
 
    for($r=0;$r<sizeof($array_01);$r++)
   {
    $start_ergebnis=str_replace($array_01[$r],$array_02[$r],$start_ergebnis);

    }

$explode=explode(" ",$start_ergebnis);
$x1=$explode[0];
$y1=$explode[1];
  echo "<br>";


 
//Hier der Code für das Ermitteln von x2 und y2
 
 
  $end = "SELECT astext(EndPoint(the_geom))as endpoint from fridastreets where gid='$x'";
 $res_end= pg_query($con,$end);
   $end_ergebnis = pg_result($res_end,"endpoint");
   echo "Endpunkte (x2,y2): ".$end_ergebnis;
   echo "<br>"; 
   echo "--------------";
   echo "<br>";
   
    $array_01=array("POINT(",")");
  $array_02=array("","");
 
    for($r=0;$r<sizeof($array_01);$r++)
   {
    $end_ergebnis=str_replace($array_01[$r],$array_02[$r],$end_ergebnis);

    }

$explode=explode(" ",$end_ergebnis);
$x2=$explode[0];
$y2=$explode[1];

   
  //Hier werden dann die Werte in die Spalten geschrieben
   $werte_in_tabelle_schreiben="UPDATE fridastreets SET x1='$x1',y1='$y1',x2='$x2',y2='$y2' where gid='$x'";
 $res = pg_query($werte_in_tabelle_schreiben);
   
   
  }

?> 
