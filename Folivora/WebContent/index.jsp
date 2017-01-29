<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html><head>
    <meta charset="utf-8">
    <title>Folivora - Home</title>
    <link rel="shortcut icon" href="res/img/FolivoraIcon.ico"/>
    
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script type="text/javascript" src="res/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="res/js/bootstrap.min-3.3.7.js"></script>
    <link href="res/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="res/css/bootstrap-pigendo-default-theme.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="res/js/folivora-utility.js"></script>
    <script type="text/javascript" src="res/js/folivora.js"></script>
    <script type="text/javascript" src="res/js/notify.min.js"></script>
    <link href="res/css/folivora.css" rel="stylesheet" type="text/css">
  </head>
  
  <body>
    <div class="cover">
      <div class="navbar">
        <div class="container">
          <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-ex-collapse">
              <span class="sr-only">Toggle navigation</span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
            <!--<a class="navbar-brand" href="#"><span>Brand</span></a>-->
            <img src="res/img/brand.png" class="img-responsive index-brand">
          </div>
          <div class="collapse navbar-collapse" id="navbar-ex-collapse">
            <ul class="nav navbar-nav navbar-right">
              <li>
                <a href="webapp.jsp">WebApp</a>
              </li>
              <li>
                <a id="navbar-benefits" href="#benefits" onclick="scrollTo('#section-benefits', 1500)">Leistungen</a>
              </li>
              <li>
                <a href="#team" id="navbar-team" onclick="scrollTo('#section-team', 1500)">Team</a>
              </li>
              <li>
                <a href="#contact" id="navbar-contact" onclick="scrollTo('#section-contact', 1500)">Kontakt</a>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="cover-image" style="background-image: url(res/img/Landing-forrest-background.jpg)"></div>
      <div class="container">
        <div class="row">
          <div class="col-md-12 text-center">
            <h1 class="text-inverse">Folivora</h1>
            <p class="text-inverse">Got it? Share and sell it!</p>
            <br>
            <a class="btn btn-lg btn-primary" id="startTheTour" onclick="scrollTo('#section-problem', 2000)">Starte die Tour</a>
          </div>
        </div>
      </div>
    </div>
    <div class="section" id="section-problem">
      <div class="container">
        <div class="row">
          <div class="col-md-6">
            <img src="res/img/index-closed.jpg" class="img-responsive">
          </div>
          <div class="col-md-6">
            <h1 class="text-primary">Wo wir ansetzen</h1>
            <h3>Fixe Öffnungszeiten, Vergesslichkeit &amp; Bequemlichkeit</h3>
            <p>Jeder befand sich gewiss schon einmal in der Situation, dass man bestimmte Verbrauchsgüter benötigt, 
            diese aber nicht selbst besorgen kann, oder will. Das Bier auf der Party ist ausgegangen, aber alle Geschäfte
            in der Umgebgung sind bereits geschlossen? Man hat bestimmte Produkte beim Einkaufen vergessen, hat aber keine Lust
            noch einmal einkaufen zu gehen?!</p>
            <p>Kein Problem! Mit Folivora bist Du nicht mehr auf die fixen Öffnungszeiten angeweisen und kannst passend zu den
            heutig dynamischen Lebensstylen flexibel bleiben.</p>
          </div>
        </div>
      </div>
    </div>
    <div class="section" id="section-solution">
      <div class="container">
        <div class="row">
          <div class="col-md-6">
            <h1 class="text-primary">Was wir dagegen machen</h1>
            <h3>Eine Plattform die Dich mit netten Menschen aus Deiner Umgebung vernetzt</h3>
            <p>Wir schaffen eine solche Plattform, auf der Suchende und Liefernde Personen
            vernetzen können, um sich so gemeinsam das obig beschriebene Problem zu lösen.</p>
            <p>Der Suchende erhält seine gesuchten Waren, der Liefernde kann sich etwas dazuverdienen,
            hilft anderen und beide lernen endlich Leute in ihrer Umgebung kennen.</p>
            <p>Für alle ein Gewinn.</p>
          </div>
          <div class="col-md-6">
            <img src="res/img/index-network.jpeg" class="img-responsive">
          </div>
        </div>
      </div>
    </div>
    <div class="section" id="section-benefits">
      <div class="container">
        <div class="row">
          <div class="col-md-6">
            <img src="res/img/index-moneypig.jpg" class="img-responsive">
          </div>
          <div class="col-md-6">
            <h1 class="text-primary">Was wir bieten</h1>
            <h3>Eine Handelsplattform für den kurzfristigen Bedarf von Verbrauchsgütern</h3>
            <p>Mithilfe unserer Plattform kannst Du Gesuche einstellen, bei denen Du selbst definieren 
            kannst wie viel diese Güter Dir zu dem speziellen Zeitpunkt wert sind und wie lange eine Lieferung
            möglich ist, wobei der Maximalzeitraum 24 Stunden beträgt.</p>
            <p>Der angegebene Preis enthält sowohl die Produktkosten, als auch die Belohnung für den Lieferanten, 
            weswegen dieser nicht zu niedrig gewählt werden sollte. Aufgeschlagen werden noch Gebühren von 10%,
            die jedoch maximal einen Euro betragen können.</p>
            <p>Neugierig geworden? Dann registriere Dich gleich kostenlos <a href="signup.jsp">hier</a> und
            probiere unsere <a href="webapp.jsp">WebApp</a> aus! Wir freuen uns über jeden Nutzer!</p>
          </div>
        </div>
      </div>
    </div>
    <div class="section" id="section-future">
      <div class="container">
        <div class="row">
          <div class="col-md-6">
            <h1 class="text-primary">Wo wir zukünftig stehen möchten</h1>
            <h3>Deutschlandweite Nutzung, neue Features & Apps</h3>
            <p>Zukünftig möchten wir die Nutzung von Folivora detuschlandweit etablieren. Dafür 
            entwickeln wir weiter fleißig neue Features für die Plattform und führen Apps für Android 
            und IOS ein, um noch die Nutzung über mobile Endgeräte zu optimieren. Bleibt gespannt was 
            kommt!</p>
          </div>
          <div class="col-md-6">
            <img src="res/img/index-coding.jpeg" class="img-responsive">
          </div>
        </div>
      </div>
    </div>
    <div class="section" id="section-team">
      <div class="container">
        <div class="row">
          <div class="col-md-12">
            <h1 class="text-center text-primary">Wer wir sind</h1>
            <p class="text-center">Eine Gruppe höchstmotiverter Masterstudenten der TU Darmstadt</p>
          </div>
        </div>
        
        <div class="row">
	        <div class="col-md-4">
	            <img src="res\img\Benjamin.png" class="center-block img-circle img-responsive team-picture" width="300px">
	            <h3 class="text-center">Benjamin M. Abdel-Karim</h3>
	            <p class="text-center">Finanzen</p>
	        </div>
          <div class="col-md-4">
            <img class="center-block img-circle img-responsive team-picture" src="res\img\Federico.jpg">
            <h3 class="text-center">Federico Andres Castro</h3>
            <p class="text-center">Marketing & Vertrieb</p>
          </div>
          <div class="col-md-4">
            <img src="res\img\Lukas.png" class="center-block img-circle img-responsive team-picture">
            <h3 class="text-center">Lukas Dratwa</h3>
            <p class="text-center">Entwicklung</p>
          </div>
        </div>
        <br>
        <div class="row">
          <div class="col-md-4">
            <img class="center-block img-circle img-responsive team-picture" src="res\img\Patrick.png">
            <h3 class="text-center">Patrick Peeck</h3>
            <p class="text-center">Marketing & Vertrieb</p>
          </div>
          <div class="col-md-4">
            <img src="res\img\Felix.png" class="center-block img-circle img-responsive team-picture">
            <h3 class="text-center">Felix Maximilian Heller</h3>
            <p class="text-center">Entwicklung</p>
          </div>
          <div class="col-md-4">
            <img src="res\img\Friedrich.png" class="center-block img-circle img-responsive team-picture">
            <h3 class="text-center">Friedrich Henno Odin Köhler</h3>
            <p class="text-center">Markt- & Wettbewerbsanalyse</p>
          </div>
        </div>
      </div>
    </div>
    <div class="section" id="section-contact">
      <div class="container">
        <div class="row">
          <h1 class="text-center text-primary">So erreichst Du uns</h1>
          <p class="text-center">Ob über Mail an unsere Kontaktadresse
            <a href="mailto:contact@folivora.de">Folivora</a>, oder über die unten abgebildeten Netzwerke , wir freuen uns über Deine
            Nachricht!</p>
        </div>
      </div>
      <br>
      <div class="container">
        <div class="row text-center">
          <div class="col-xs-3 text-center">
            <a><i class="fa fa-5x fa-fw fa-instagram"></i></a>
          </div>
          <div class="col-xs-3">
            <a><i class="fa fa-5x fa-fw fa-twitter"></i></a>
          </div>
          <div class="col-xs-3">
            <a><i class="fa fa-5x fa-facebook fa-fw"></i></a>
          </div>
          <div class="col-xs-3 text-center">
            <a><i class="fa fa-5x fa-fw fa-github"></i></a>
          </div>
        </div>
      </div>
    </div>
</body>

</html>