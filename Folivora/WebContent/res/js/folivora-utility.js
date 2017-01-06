/**
 * A cache-variable to store the status-code of the last REST-Call
 */
var lastRestStatus = null;
var lastResponse = null;

function formatLongDate(longDate) {
	var d = new Date(longDate);
	var year = d.getFullYear();
	var month = d.getMonth() + 1;
	var day = d.getDate();
	var hours = d.getHours();
	var minutes = d.getMinutes();
	
	return day + "." + month + "." + year + ", " + hours + ":" + minutes + " Uhr";
}

function getTimeLeftAsString(longDate) {
	var d = new Date(longDate);
	var actual = new Date();
	var diffMs = (d - actual);
	var diffDays = Math.round(diffMs / 86400000);
	var diffHrs = Math.round((diffMs % 86400000) / 3600000);
	var diffMins = Math.round(((diffMs % 86400000) % 3600000) / 60000);
	
	return diffHrs + ":" + diffMins;
}

function getTimeAsString(longDate) {
	var d = new Date(longDate);
	return d.getHours() + ":" + d.getMinutes();
}

/**
 * Method to create a REST-call. The returned status will be stored 
 * in "lastRestStatus".
 * 
 * @param method - the method like "GET", "POST", ...
 * @param servlet - the servlet-name you want to call
 * @param payload - the payload. If there is no payload, provide null
 * @returns {Boolean} false if the HttpRequest could not be created
 */
function createRest(method, servlet, payload, callback) {
	var http_request = new XMLHttpRequest();
	if (!http_request) {
		alert('Cannot create an XMLHTTP instance');
			return false;
	}
	
	
	http_request.onreadystatechange = function() {
		if (http_request.readyState == 4) {
			lastRestStatus = http_request.status;
			lastResponse = JSON.parse(http_request.response);
			
			if(typeof callback != "undefined" && callback != null) {
				callback(http_request.response);
			}
			
			return http_request;
		}
	}
	
	http_request.open(method, servlet, true);
	http_request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	
	if(typeof payload != "undefined") {
		http_request.send(payload);
	}
}

/**
 * Function to call the SignOutServlet to sign out the logged 
 * in user.
 * 
 * @returns {Boolean} false if the HttpRequest could not be created
 */
function signOut() {
	var http_request = new XMLHttpRequest();
	 if (!http_request) {
	  alert('Cannot create an XMLHTTP instance');
	   return false;
	 }
	 
	 http_request.open("POST", "signoutservlet");
	 http_request.send(null);
}

// From: http://cwestblog.com/2011/07/25/javascript-string-prototype-replaceall/
String.prototype.replaceAll = function(target, replacement) {
	  return this.split(target).join(replacement);
};

//From: https://developer.mozilla.org/en-US/docs/Web/API/WindowBase64/Base64_encoding_and_decoding
function b64EncodeUnicode(str) {
    return btoa(encodeURIComponent(str).replace(/%([0-9A-F]{2})/g, function(match, p1) {
        return String.fromCharCode('0x' + p1);
    }));
}

// From: http://stackoverflow.com/questions/4060004/calculate-age-in-javascript
function calculateAge(birthday) { // birthday is a date
    var ageDifMs = Date.now() - birthday.getTime();
    var ageDate = new Date(ageDifMs); // miliseconds from epoch
    return Math.abs(ageDate.getUTCFullYear() - 1970);
}