<?php

require 'config.php';

$request=$_SERVER['REQUEST_URI'];
$path=parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);


$db = new PDO( $config['dbname'], $config['dbuser'], $config['dbpassword'], array(PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8"));
mysql_query("SET NAMES 'utf8'", $db);

switch($path){
	case '/v1/register':
		
		$sth = $db->prepare('SELECT * FROM na_user where email=?;');
	    $sth->execute(array($_GET['email']));

	    $res = $sth->fetch(PDO::FETCH_ASSOC);
    
	    $user_id = $res['id'];
		if(!isset($user_id)){
		
			$sth = $db->prepare('INSERT INTO na_user (email, lastupdated) VALUES (?, now());');
    		$sth->execute(array(
        		$_GET['email']
    			));
    		$user_id=$db->lastInsertId();	
		}

    	echo json_encode(
            array("id" => $user_id));
		
		break;

	case '/v1/track':

		$user_id=$_GET['id'];
		$battery=$_GET['battery'];
		if(isset($_GET['email'])){
			$sth = $db->prepare('SELECT * FROM na_user where email=?;');
	    	$sth->execute(array($_GET['email']));
	    	$res = $sth->fetch(PDO::FETCH_ASSOC);
	    	$user_id = $res['id'];
		}
	
		$sth = $db->prepare('INSERT INTO na_track (user_id, time_updated, battery) VALUES (?, now(), ?);');
    	$sth->execute(array($user_id, $battery));
		$sth = $db->prepare('UPDATE na_user set lastupdated=now() where id=?;');
    	$sth->execute(array($user_id));

		echo "TRACK";
		break;

	case '/v1/alarm':
		echo "(ALARM)";
		
		$user_id=$_GET['id'];
		$email=$_GET['email'];
		$msg=$_GET['msg'];
		
		$sth = $db->prepare('SELECT * FROM na_user where email=?;');
	    $sth->execute(array($email));

	    $res = $sth->fetch(PDO::FETCH_ASSOC);
    
	    $user_id = $res['id'];
	    	
	    $sth = $db->prepare('UPDATE na_user set lastalarm=now() where id=?;');
	    $sth->execute(array($user_id));
		
		$to = $email;
		$subject = "Notificación NeverAlone";
		$txt = $msg;
		$headers = "From: x@x.com" . "\r\n";

		$ok= mail($to,$subject,$txt,$headers);
		
		echo "Mail to $email ($user_id). Result: $ok.";

		break;

	case '/v1/cron':
	
	    echo "CRON";
	    
		//MÉS D'1 HORA
		$sentence = $db->prepare('SELECT id, email, TIMESTAMPDIFF(MINUTE, lastupdated, now()) as mins  FROM na_user where TIMESTAMPDIFF(MINUTE, lastupdated, now())>60 and lastalarm<lastupdated;');
	    $sentence->execute();
	    $list = $sentence->fetchAll(PDO::FETCH_ASSOC);

	    foreach ($list as &$user) {
	        $email=$user["email"];
			echo $email."<br>";
			
			//ALARMA
			$user_id=$user["id"];
			$sth = $db->prepare('UPDATE na_user set lastalarm=now() where id=?;');
	    	$sth->execute(array($user_id));

			$to = $email;
			//$to = 'joan.lasierra@gmail.com';
			$subject = "Notificación NeverAlone";
			$txt = "El móvil del abuelo no está accesible desde hace ". $user['mins'] . " minutos... \n".
			        "Puedes seguir su itinerário en nuestra web.\n\n\nUn saludo\nEl equipo de NeverAlone";
			$headers = "From: x@x.com";

		    $ok= mail($to,$subject,$txt,$headers);
		    echo $txt;
	    }

		break;

	default:
		echo 'PATH '.$path;
}