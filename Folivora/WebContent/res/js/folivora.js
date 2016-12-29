function scrollTo(idTo, speed) {
		console.log(idTo);
		$('html, body').animate({
			scrollTop: $(idTo).offset().top
		}, speed);
}

$(document).ready(function() {
	/* Register listener for smooth scroll down
	var lData = {
		data: [],
		add: function(_listenTo, _scrollTo, _speed) {
			var obj = {
				listenTo: _listenTo,
				scrollTo: _scrollTo,
				speed: _speed
			}
			this.data.push(obj);
		},
		initListeners: function() {
			for(i in this.data) {
				var listener = this.data[i];
				$(listener.listenTo).click(function() {
					$('html, body').animate({
						scrollTop: $(listener.scrollTo).offset().top
					}, listener.speed);
				});
			}			
		}
	};
	
	lData.add("#startTheTour", "#section-problem", 2000);
	lData.add("#navbar-benefits", "#section-benefits", 1000);
	lData.add("#navbar-team", "#section-team", 1000);
	lData.initListeners();*/
});