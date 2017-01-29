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

/**
 * From: https://forum.selfhtml.org/self/2005/jul/19/html-sonderzeichen-mit-javascript-umwandeln/841557
 * 
 * htmlEntities
 *
 * Convert all applicable characters to HTML entities
 *
 * object string
 * return string
 *
 * example:
 *   test = 'äöü'
 *   test.htmlEntities() //returns '&auml;&ouml;&uuml;'
 */
String.prototype.htmlEntities = function() {
  var chars = new Array ('&','à','á','â','ã','ä','å','æ','ç','è','é',
                         'ê','ë','ì','í','î','ï','ð','ñ','ò','ó','ô',
                         'õ','ö','ø','ù','ú','û','ü','ý','þ','ÿ','À',
                         'Á','Â','Ã','Ä','Å','Æ','Ç','È','É','Ê','Ë',
                         'Ì','Í','Î','Ï','Ð','Ñ','Ò','Ó','Ô','Õ','Ö',
                         'Ø','Ù','Ú','Û','Ü','Ý','Þ','€','"','ß','<',
                         '>','¢','£','¤','¥','¦','§','¨','©','ª','«',
                         '¬','­','®','¯','°','±','²','³','´','µ','¶',
                         '·','¸','¹','º','»','¼','½','¾');

  var entities = new Array ('amp','agrave','aacute','acirc','atilde','auml','aring',
                            'aelig','ccedil','egrave','eacute','ecirc','euml','igrave',
                            'iacute','icirc','iuml','eth','ntilde','ograve','oacute',
                            'ocirc','otilde','ouml','oslash','ugrave','uacute','ucirc',
                            'uuml','yacute','thorn','yuml','Agrave','Aacute','Acirc',
                            'Atilde','Auml','Aring','AElig','Ccedil','Egrave','Eacute',
                            'Ecirc','Euml','Igrave','Iacute','Icirc','Iuml','ETH','Ntilde',
                            'Ograve','Oacute','Ocirc','Otilde','Ouml','Oslash','Ugrave',
                            'Uacute','Ucirc','Uuml','Yacute','THORN','euro','quot','szlig',
                            'lt','gt','cent','pound','curren','yen','brvbar','sect','uml',
                            'copy','ordf','laquo','not','shy','reg','macr','deg','plusmn',
                            'sup2','sup3','acute','micro','para','middot','cedil','sup1',
                            'ordm','raquo','frac14','frac12','frac34');

  newString = this;
  for (var i = 0; i < chars.length; i++) {
    myRegExp = new RegExp();
    myRegExp.compile(chars[i],'g')
    newString = newString.replace (myRegExp, '&' + entities[i] + ';');
  }
  return newString;
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

$.dateFromString = function(string) {
	var dateParts = string.split('.');
	return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}