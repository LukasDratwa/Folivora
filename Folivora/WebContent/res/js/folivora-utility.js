/**
 * @author: Lukas Dratwa
 */

/**
 * A cache-variable to store the status-code of the last REST-Call
 */
var lastRestStatus = null;

/**
 * Method to create a REST-call. The returned status will be stored 
 * in "lastRestStatus".
 * 
 * @param method - the method like "GET", "POST", ...
 * @param servlet - the servlet-name you want to call
 * @param payload - the payload. If there is no payload, provide null
 * @returns {Boolean} false if the HttpRequest could not be created
 */
function createRest(method, servlet, payload) {
	var http_request = new XMLHttpRequest();
	if (!http_request) {
		alert('Cannot create an XMLHTTP instance');
			return false;
	}
	
	
	http_request.onreadystatechange = function() {
		if (http_request.readyState == 4) {
			lastRestStatus = http_request.status;
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