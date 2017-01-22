/**
 * @author Lukas Dratwa
 */


/**
 * A cache-variable to store the status-code of the last REST-Call
 */
var lastRestStatus = null;
var lastResponse = null;

/**
 * Method to format a date to a wished format.
 * 
 * @param longDate - the date
 * @returns {String} the formatted date
 */
function formatLongDate(longDate) {
	var d = new Date(longDate);
	var year = d.getFullYear();
	var month = d.getMonth() + 1;
	var day = d.getDate();
	var hours = d.getHours();
	var minutes = d.getMinutes();
	
	return day + "." + month + "." + year + ", " + hours + ":" + minutes + " Uhr";
}

/**
 * Method to calculate the left possible delivery time for a search request.
 * 
 * @param longDate - the date (end date)
 * @returns {String} - the left time
 */
function getTimeLeftAsString(longDate) {
	var d = new Date(longDate);
	var actual = new Date();
	var diffMs = (d - actual);
	var diffDays = Math.round(diffMs / 86400000);
	var diffHrs = Math.round((diffMs % 86400000) / 3600000);
	var diffMins = Math.round(((diffMs % 86400000) % 3600000) / 60000);
	
	return diffHrs + ":" + diffMins;
}

/**
 * Method to get the time of a date
 * 
 * @param longDate - the long date
 * @returns {String} the formatted time
 */
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
	
	if (method == "GET" && typeof payload != "undefined") {
		servlet += "?";
		$.each(payload, function(key, val) {
			servlet += "&" + key + "=" + val;
		});
		delete payload;
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

// From: http://stackoverflow.com/a/25359264
$.urlParam = function(name) {
    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    if (results==null) {
       return null;
    }
    else {
    	return decodeURI(results[1]) || 0;
    }
}