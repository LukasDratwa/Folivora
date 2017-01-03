function scrollTo(idTo, speed) {
	$('html, body').animate({
		scrollTop: $(idTo).offset().top
	}, speed);
}

$(document).ready(function() {
});