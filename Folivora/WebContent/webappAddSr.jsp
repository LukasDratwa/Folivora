<h4><a href="#" id="sr-toggle-btn"><i class="glyphicon glyphicon-menu-down"></i> Gesuch einstellen</a></h4>
			
<form role="form" id="srform"  method="post" class="hidden">
	<div class="form-group">
		<label for="title">Titel:</label>
		<input name="title" class="form-control" id="srform-title" placeholder="Titel eingeben" required>
	</div>
	<div class="form-group">
		<label for="description">Beschreibung:</label>
		<input name="description" class="form-control" id="srform-description" placeholder="Beschreibung eingeben" required>
	</div>
	<label for="address" style="width: 100%;">Lieferadresse:</label>
	<div class="input-group">
	   <input type="text" name="address" id="srform-address" class="form-control" placeholder="Adresse wählen" disabled required>
	   <span class="input-group-btn">
	        <button class="btn btn-default" id="btn-select-address" type="button">+</button>
	   </span>
	</div>
	
	<!--<div class="form-group">
		<label for="daterange-possible">Möglicher Lieferzeitraum:</label>
		<input name="daterange-possible" id="srform-delivery-possible" required>
	</div>-->
	<br />
	<div class="dropdown">
		<button class="btn btn-default dropdown-toggle dropdown-menu-left text-left" type="button" data-toggle="dropdown" id="btn-possible-delivery-dropdown" style="width: 100%;">
			Mögliche Lieferzeit <span class="caret"></span>
		</button>
	  	<ul class="dropdown-menu">
	  	<li><a href="#" onclick="updatePossibleDelivery(1)">1h</a></li>
	    	<li><a href="#" onclick="updatePossibleDelivery(2)">2h</a></li>
	    	<li><a href="#" onclick="updatePossibleDelivery(3)">3h</a></li>
	    	<li><a href="#" onclick="updatePossibleDelivery(4)">4h</a></li>
	    	<li><a href="#" onclick="updatePossibleDelivery(8)">8h</a></li>
	    	<li><a href="#" onclick="updatePossibleDelivery(12)">12h</a></li>
	    	<li><a href="#" onclick="updatePossibleDelivery(24)">24h</a></li>
	  	</ul>
	</div>
	<br>
	
	<div class="form-group">
		<label for="maxcosts">Kosten:</label>
		<input min="0.10" max="<% if(request.getAttribute("user") != null) out.write("" + ((User)request.getAttribute("user")).getCredit().getMaxPossiblePriceForSr()); %>" step="0.1" type="number" name="maxcosts" class="form-control" id="srform-maxcosts" placeholder="Max. Kosten" required>
	</div>
	
	<div class="form-group">
		<label for="charges">Gebühren:</label>
		<input type="text" name="charges" class="form-control" id="srform-charges" placeholder="Gebühren" disabled>
	</div>
	
	<div class="form-group">
		<label for="final-costs">Gesamtkosten:</label>
		<input type="text" name="final-costs" class="form-control" id="srform-final-costs" placeholder="Gesamtkosten" disabled>
	</div>
	
	<input type="submit" value="Einstellen" class="btn btn-default btn-block" id="btn-srform-submit">
</form>