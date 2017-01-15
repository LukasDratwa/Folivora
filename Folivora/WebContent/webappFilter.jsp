<h4>Filtereinstellungen</h4>
<p>Dringlichkeit <a href="#" title="Rot: 0-1 Std, Gelb: 1-4 Std, Grün: 4-12 Std.">[?]</a></p>
<div class="btn-group btn-group-justified" role="group" aria-label="...">
	<div class="btn-group" role="group"><button type="button" class="btn btn-default btn-danger active filter-urgency" id="filter-urgency-red">Rot</button></div>
	<div class="btn-group" role="group"><button type="button" class="btn btn-default btn-warning active filter-urgency" id="filter-urgency-yellow">Gelb</button></div>
	<div class="btn-group" role="group"><button type="button" class="btn btn-default btn-success active filter-urgency" id="filter-urgency-green">Grün</button></div>
</div>
<br />
<p>Zahlungsbereitschaft</p>
<div class="input-group">
	<input type="text" id="filter-reward-min" class="form-control">
	<span class="input-group-addon">-</span>
	<input type="text" id="filter-reward-max" class="form-control">
	<span class="input-group-addon">&euro;</span>
</div>
<br />
<p>
	<button class="btn btn-default btn-block" id="filter-apply">Filter anwenden</button>
</p>