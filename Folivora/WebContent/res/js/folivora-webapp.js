var webappDataObj = {
	newSearchRequestClicked: false,
	newSrObj: {},
	updateNewSrObj: function() {
		this.newSrObj.title = $("#srform-title").val();
		this.newSrObj.description = $("#srform-description").val();
		this.newSrObj.costsAndReward = $("#srform-maxcosts").val();
		this.newSrObj.fee = $("#srform-charges").val();
	},
	resetNewSrObj: function() {
		this.newSrObj.title = "";
		this.newSrObj.description = "";
		this.newSrObj.costsAndReward = 0.0;
		this.newSrObj.fee = 0.0;
		this.newSrObj.lat = 0;
		this.newSrObj.lng = 0;
		this.newSrObj.address = "";
		this.newSrObj.possibleDelivery_from = 0;
		this.newSrObj.possibleDelivery_to = 0;
	},
	srData: {
		srs: [],
		getSrWithId: function(id) {
			for(var i in this.srs) {
				var sr = this.srs[i];
				if(sr.id == id) {
					return sr;
				}
			}
			return null;
		},
		add: function(sr) {
			if(this.getSrWithId(sr.id) == null) {
				this.srs.push(sr);
			}
		}
	},
	userData: {
		id: -1,
		balance: 0
	},
	mapData: {
		map: null,
		markers: [],
		removeMarker: function(srId) {
			var indexToRemove = -1;
			for(var i in this.markers) {
				var marker = this.markers[i];
				
				if(marker.sr.id == srId) {
					indexToRemove = i;
					marker.setMap(null);
				}
			}
			
			if(indexToRemove != -1) {
				webappDataObj.removeElementOfArray(indexToRemove, this.markers);
				webappDataObj.removeElementOfArray(indexToRemove, webappDataObj.srData.srs);
			}
		},
		getMarkerWithSrId: function(srId) {
			for(var i in this.markers) {
				var marker = this.markers[i];
				
				if(marker.sr.id == srId) {
					return marker;
				}
			}
			
			return null;
		}
	},
	removeElementOfArray: function(index, array) {
		var result = [];
		for(var i in array) {
			if(index != i) {
				result.push(array[i]);
			}
		}
		return result;
	}
};
webappDataObj.resetNewSrObj();

var updateViewCounter = 0;
function updateViewIfNeeded(){
	updateViewCounter++;
	var payload = {
		userCallingId: webappDataObj.userData.id,
		onlyUnseen: true
	};
	
	// Check notification banner (unread msgs)
	if(typeof payload.userCallingId != "undefined" && payload.userCallingId != -1) {
		createRest("POST", "getmessagesservlet", JSON.stringify(payload), function(response) {
			var responseObj = JSON.parse(response);
			var unreadMsgsLength = new Number(responseObj.userMessages.length);
			
			if(unreadMsgsLength > 0) {
				$("#messages-notification-number").html(unreadMsgsLength);
				$("#messages-notification-number").removeClass("hidden")
			} else {
				$("#messages-notification-number").html("");
				$("#messages-notification-number").addClass("hidden")
			}
			
			return;
		});
	}
	
	// Check if there are new requests
	if(updateViewCounter % 6 == 0) {
		if(window.location.href.indexOf("webapp.jsp") != -1) {
			createRest("POST", "getsrdataservlet", null, function(response) {
				var srArray = JSON.parse(response);
				
				// Check for loaded sr's which are not on the map yet
				for(var i in srArray) {
					var loadedSr = srArray[i];
					
					if(webappDataObj.mapData.getMarkerWithSrId(loadedSr.id) == null) {
						var choice = confirm("Es liegen neue Gesuche vor, neu laden?");
						if(choice) {
							window.location.reload(true);
						}
						// TODO könnte man hier adden, müsste aber noch regeln dass die jeweiligen btns
						// in den windowinfos ein- bzw. ausgeblendet werden.
						/*console.log("Added marker for " , loadedSr);
						
						// Because all active and in progress search request are saved in the client,
						// but in progress search requests are only displayed to involved and logged in
						// users
						var isDisplayed = addMarker(loadedSr, webappDataObj.mapData.map);
						if(! isDisplayed) {
							webappDataObj.mapData.markers.push({map: null, sr: loadedSr});
						}
						
						webappDataObj.srData.add(loadedSr);*/
					}
				}
				
				// Check for markers which actually are not active any more
				for(var x in webappDataObj.mapData.markers) {
					var existingMarker = webappDataObj.mapData.markers[x];
					var existingSr = existingMarker.sr;
					var existingSrStillActive = false;
					
					for(var i in srArray) {
						var loadedSr = srArray[i];
						
						if(existingSr.id == loadedSr.id) {
							existingSrStillActive = true;
							break;
						}
					}
					
					if(! existingSrStillActive) {
						webappDataObj.mapData.removeMarker(existingSr.id);
						console.log("Removed marker which referes to " , existingSr);
					}
				}
			});
		}
	}
}

$(document).ready(function() {
	// Check if the displayed data is actual
	setInterval(function() {
		updateViewIfNeeded();
	}, 5000);
	
	$(".filter-urgency").click(function(el) {
		$(this).toggleClass('active');
		var showRed = $('#filter-urgency-red').hasClass('active');
		var showYellow = $('#filter-urgency-yellow').hasClass('active');
		var showGreen = $('#filter-urgency-green').hasClass('active');
		$.each(webappDataObj.srData.srs, function(index, value) {
			var iconPath = value.marker_icon_path;
			if (
					(iconPath.includes('red') && !showRed) ||
					(iconPath.includes('orange') && !showYellow) ||
					(iconPath.includes('green') && !showGreen)
			) {
				webappDataObj.mapData.removeMarker(value.id);
			}
			else if (webappDataObj.mapData.getMarkerWithSrId(value.id).map == null) {
				addMarker(value, webappDataObj.mapData.map);
			}
		});
		// webappDataObj.mapData.markers = [];		
	});
	
	$("#btn-select-address").click(function() {
		webappDataObj.newSearchRequestClicked = true;
	});
	// initDateTimeRange();
	
	$("#srform-maxcosts").keyup(function() {
		updateInputCostFields();
	});
	
	$("#sr-toggle-btn").click(function() {
		if($("#sr-toggle-btn").val() == "Zum Filter") {
			$("#sr-toggle-btn").val("Neues Gesuch erstellen");
			
			$("#sr-filter-container").removeClass("hidden");
			$("#srform").addClass("hidden");
		} else {
			$("#sr-toggle-btn").val("Zum Filter");
			
			$("#sr-filter-container").addClass("hidden");
			$("#srform").removeClass("hidden");
		}
	});
	
	$("#srform").submit(function(e) {
		webappDataObj.updateNewSrObj();
		webappDataObj.newSrObj.token = $.cookie("token");
		
		createRest("POST", "newsrservlet", JSON.stringify(webappDataObj.newSrObj), function(response) {
			if(lastRestStatus != null && lastRestStatus == 201 || lastRestStatus == 200) {
				var responseObj = JSON.parse(response);
				var sr = responseObj.sr;
				
				$.notify("Gesuch \"" + webappDataObj.newSrObj.title + "\" erfolgreich erstellt.", "success");
				
				console.log("Created new search request: " , sr);
				
				webappDataObj.srData.srs.push(sr);
				
				addMarker(sr, webappDataObj.mapData.map);
				updateNavbarBalance((sr.costsAndReward + sr.fee) * -1);
				
				webappDataObj.resetNewSrObj();
				document.getElementById("srform").reset();
			} else {
				$.notify("Fehler beim Erstellen des Gesuchs!", "error");
			}
			
			lastRestStatus = null;
		});
		
		e.preventDefault();
	});
	
	$("#btn-srform-submit").mouseover(function() {
		if($("#btn-possible-delivery-dropdown").text() === "Mögliche Lieferzeit") {
			updatePossibleDelivery(3);
		}
	});
});

function stasifySr(signedInUserId, srId) {
	var payloadObj = {
		userCallingId: signedInUserId,
		srId: srId
	};
	
	createRest("POST", "statisfysrservlet", JSON.stringify(payloadObj));
	
	setTimeout(function() {
		if(lastRestStatus != null && lastRestStatus == 201 || lastRestStatus == 200) {
			$.notify("Erfolg, Du erhälst in Kürze eine Nachricht.", "success");
			$("#btn-map-infowindow-statisfy").prop("disabled", true);
			console.log(signedInUserId + " want to statisfy " + signedInUserId);
		} else {
			$.notify("Fehler beim Erstellen des Gesuchs!", "error");
		}
		
		lastRestStatus = null;
	}, 1000);
}

function cancelSr(signedInUserId, srId) {
	var choice = confirm("Möchten Sie wirklich dieses Gesuch zurückziehen?");
	
	if(choice) {
		console.log(signedInUserId + " want to cancel: " + srId);
		
		var payloadObj = {
			userCallingId: signedInUserId,
			srId: srId
		};
		
		createRest("POST", "cancelsrservlet", JSON.stringify(payloadObj), function() {
			if(lastRestStatus != null && lastRestStatus == 201 || lastRestStatus == 200) {
				$.notify("Gesuch erfolgreich zurückgezogen.", "success");
				
				webappDataObj.mapData.removeMarker(srId);
				var sr = webappDataObj.srData.getSrWithId(srId);
				updateNavbarBalance(sr.costsAndReward + sr.fee);
				
				$("#btn-map-infowindow-cancel").prop("disabled", true);
			} else {
				$.notify("Fehler beim Zurückziehen!", "error");
			}
			
			lastRestStatus = null;
		});
	}
}

function updatePossibleDelivery(h) {
	var oneH = 3600000;
	webappDataObj.newSrObj.possibleDelivery_from = new Date().valueOf();
	var to = new Date().valueOf() + oneH * h;
	webappDataObj.newSrObj.possibleDelivery_to = to;
	
	$("#btn-possible-delivery-dropdown").text(h + "h - bis " + getTimeAsString(to) + " Uhr");
}

function updateGeoData(lat, lng, address) {
	if(webappDataObj.newSearchRequestClicked) {
		webappDataObj.newSearchRequestClicked = false;
		
		webappDataObj.newSrObj.lat = lat;
		webappDataObj.newSrObj.lng = lng;
		if(address != null) {
			webappDataObj.newSrObj.address = address;
			$("#srform-address").val(address);
		}
	}
}

function updateInputCostFields() {
	var costs = $("#srform-maxcosts").val();
	var charges = parseFloat(costs * 0.10).toFixed(2);
	
	if(charges > 1) {
		charges = 1;
	} else if(charges < 0.10){
		charges = 0.10;
	}
	
	var finalCosts = parseFloat(new Number(costs) + new Number(charges)).toFixed(2);
	
	$("#srform-charges").val(charges);
	$("#srform-final-costs").val(finalCosts);
}

function updateNavbarBalance(delta) {
	$("#credit-animation-span").css("display", "none");
	
	if(delta < 0) {
		$("#credit-animation-span").css("color", "red");
		$("#credit-animation-span").text("   " + delta + " €");
	} else {
		$("#credit-animation-span").css("color", "green");
		$("#credit-animation-span").text("   +" + delta + " €");
	}
	
	
	$("#credit-animation-span").fadeIn(1000, function() {
		setTimeout(function() {
			$("#credit-animation-span").fadeOut(500, function() {
				$("#credit-animation-span").text("");
				var spanHtml = $("#credit-animation-span").wrap('<p/>').parent().html();
				$("#credit-animation-span").unwrap();
				
				webappDataObj.userData.balance = parseFloat(new Number(webappDataObj.userData.balance) + new Number(delta)).toFixed(2);
				$("#navbar-li-element-credit-link").html("Guthaben: " + webappDataObj.userData.balance + " €" + spanHtml);
			});
		}, 1000);
	});
}

function initDateTimeRange() {
	$("#srform-delivery-possible").daterangepicker({
	    "showDropdowns": true,
	    "timePicker": true,
	    "timePicker24Hour": true,
	    "timePickerIncrement": 15,
	    "dateLimit": {
	        "days": 1
	    },
	    "ranges": {
	        "3h": [
	            new Date(),
	            new Date(new Date().valueOf() + 3600000*3)
	        ],
	        "4h": [
		        new Date(),
		        new Date(new Date().valueOf() + 3600000*4)
		    ],
		    "5h": [
		        new Date(),
		        new Date(new Date().valueOf() + 3600000*5)
		    ],
		    "6h": [
		        new Date(),
		        new Date(new Date().valueOf() + 3600000*6)
		    ]
	    },
	    "locale": {
	        "format": "DD/MM/YYYY",
	        "separator": " - ",
	        "applyLabel": "Ok",
	        "cancelLabel": "Abbrechen",
	        "fromLabel": "Von",
	        "toLabel": "Bis",
	        "customRangeLabel": "Individuell",
	        "weekLabel": "W",
	        "daysOfWeek": ["So", "Mo", "Di", "Mi", "Do", "Fr", "Sa"],
	        "monthNames": [
	            "Januar", "Feburar", "März", "April", "Mai", "Juni", "Juli",
	            "August", "September", "Oktober", "November", "Dezember"
	        ],
	        "firstDay": 1
	    },
	    "minDate": moment(),
	    "startDate": moment(),
	    "endDate": moment().add(1, "days"),
	    "opens": "left"
	}, function(start, end, label) {
		webappDataObj.newSrObj.possibleDelivery_from = new Date(start).valueOf();
		webappDataObj.newSrObj.possibleDelivery_to = new Date(end).valueOf();
		
		console.log("New date range selected:" + start.format('DD.MM.YYYY hh:mm') + ' to ' + end.format('DD.MM.YYYY hh:mm') + ' (predefined range: ' + label + ")");
	});
}

function loadMapData() {
	createRest("POST", "getsrdataservlet", null, initMap);
}

var infowindow;
function initMap(payload) {
	var geocoder = new google.maps.Geocoder();
	
	// Default on Darmstadt
	var myLatlng = new google.maps.LatLng(49.874505,8.655980);
	var mapOptions = {
		zoom: 13,
		center: myLatlng,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	}
	var map = new google.maps.Map(document.getElementById('webapp-map'), mapOptions);
	
	// Try HTML5 geolocation to get the location of the user.
	if(navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
		    myLatlng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
		    map.setCenter(myLatlng);
		}, function() {
			console.error("Could not get geoinformation of user");
		});
	}
	
	//Add Marker from payload
	infowindow = new google.maps.InfoWindow({
		content: "",
		maxWidth: 500
	});
	var srArray = JSON.parse(payload);
	for(var i in srArray) {
		var sr = srArray[i];
		addMarker(sr, map);
		webappDataObj.srData.srs.push(sr);
	}
	
	google.maps.event.addListener(map, 'click', function(e) {
		var lat = e.latLng.lat();
		var lng = e.latLng.lng();
		
		geocoder.geocode({
		    'latLng': e.latLng
		  }, function(results, status) {
		    if (status == google.maps.GeocoderStatus.OK) {
		      if (results[0]) {
		        updateGeoData(lat, lng, results[0].formatted_address);
		      } else {
		    	  updateGeoData(lat, lng, null);
		      }
		    } else {
		    	updateGeoData(lat, lng, null);
		    }
		  });
	});
	
	google.maps.event.addListener(map, 'mousemove', function(event) {
		if(webappDataObj.newSearchRequestClicked) {
			map.setOptions({ draggableCursor: 'crosshair' });
		} else {
			map.setOptions({ draggableCursor: 'default' });
		}
	});
	
	//Resize Function
	google.maps.event.addDomListener(window, "resize", function() {
		var center = map.getCenter();
		google.maps.event.trigger(map, "resize");
		map.setCenter(center);
	});
	
	webappDataObj.mapData.map = map;
}

function addMarker(sr, map) {
	// Show sr's which are in progress only the creator
	var disableCancelBtn = false;
	var disableStatisfyBtn = false;
	if(sr.status == "IN_PROGRESS" && sr.userCreator.id != webappDataObj.userData.id) {
		if(typeof sr.userStatisfier != "undefined" && sr.userStatisfier.id == webappDataObj.userData.id) {
			disableStatisfyBtn = true;
		} else {
			webappDataObj.mapData.markers.push({map: null, sr: sr});
			return false;
		}
	} else if(sr.status == "IN_PROGRESS") {
		disableCancelBtn = true;
	}
	
	var marker = new google.maps.Marker({
		position: new google.maps.LatLng(sr.lat , sr.lng),
		map: map,
		icon: webappDataObj.userData.id != sr.userCreator.id
			? sr.marker_icon_path 
			: "http://maps.google.com/mapfiles/ms/icons/blue-dot.png",
		title: sr.title,
		sr: sr
	});
	
	marker.addListener('click', function() {
		var sr = this.sr;
		console.log("Marker clicked, referenced sr: ", sr);
		
		var btn = "";
		if(typeof webappDataObj.userData.id != "undefined") {
			if(webappDataObj.userData.id != sr.userCreator.id) {
				btn = "<input type='button' class='btn btn-default' value='Wird erledigt!' onclick='stasifySr("
						+ "\"" + webappDataObj.userData.id + "\", \"" + sr.id + "\")'"+ (disableStatisfyBtn ? " disabled " : "")
						+ "id='btn-map-infowindow-statisfy'>";
			} else {
				btn = "<input type='button' class='btn btn-default' value='Zurückziehen' onclick='cancelSr("
						+ "\"" + webappDataObj.userData.id + "\", \"" + sr.id + "\")'" + (disableCancelBtn ? " disabled " : "")
						+ "id='btn-map-infowindow-cancel'>";
			}
		}
		
		infowindow.setContent("<div><p><b>" + sr.title + "</b> - für " + sr.costsAndReward + "€"
				+ " - übrige Lieferungszeit: " + getTimeLeftAsString(sr.possibleDelivery_to) + "</p></div>"
				+ "<div><p>" + sr.description + "</p>"
				+ "<p>Lieferung möglich bis " + formatLongDate(sr.possibleDelivery_to) + " an "
				+ sr.address + "</p>"
				+ "<p>Von \"" + sr.userCreator.name + "\"</p></div>"
				+ btn
				+ (disableStatisfyBtn ? "<br><br><div><p>Sie wollen dieses Gesuch bereits befriedigen, setzten Sie sich mit X in Verbindung.</p></div>" : "")
				+ (disableCancelBtn ? "<br><br><div><p>Jemand möchte bereits liefern, einfaches Zurückziehen ist leider nicht mehr möglich.</p></div>" : "")
		);
	    infowindow.open(map, this);
	});
	
	webappDataObj.mapData.markers.push(marker);
}

