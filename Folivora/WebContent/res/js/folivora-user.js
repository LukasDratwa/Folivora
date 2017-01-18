$(document).ready(function() {
	createRest("GET", "getuserservlet", {id: $.urlParam('id')}, function(res) {
		user = JSON.parse(res);
		$('#user-name').text(user.name);
		$('#user-age').text(user.birthday > 0 ? calculateAge(new Date(user.birthday)) : "keine Angabe");
		$('#user-registered').text(formatLongDate(new Date(user.creationTimestamp)));
		$('#user-credit').text(user.credit + " €");
		$('#user-hometown').text(user.hometown);
		$('#user-gender').text(user.gender == "MALE" ? "männlich" : (user.gender == "FEMALE" ? "weiblich" : "keine Angabe"));
	});
});