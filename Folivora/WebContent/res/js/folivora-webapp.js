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
		},
		updateMarker: function(srId) {
			var marker = this.getMarkerWithSrId(srId);
			if(marker != null) {
				this.removeMarker(srId);
				addMarker(marker.sr, this.map);
			}
		},
		updateSrOfMarker: function(srId, newSr) {
			for(var i in this.markers) {
				var marker = this.markers[i];
				
				if(marker.sr.id == srId) {
					this.markers[i].sr = newSr;
				}
			}
		},
		getAllSrs: function() {
			var result = [];
			for(var i in this.markers) {
				if(typeof this.markers[i].sr != "undefined") {
					result.push(this.markers[i].sr);
				}
			}
			
			return result;
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

function countUnseenMessages(msgArray) {
	var counter = 0;
	for(var i in msgArray) {
		var msg = msgArray[i];
		
		if(webappDataObj.userData.id != msg.sender.id) {
			counter++;
		}
	}
	return counter;
}

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
			var unreadMsgsLength = countUnseenMessages(responseObj.userMessages);
			
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
	
	// Check if there are new requests (every 30 sec)
	if(updateViewCounter % 6 == 0) {
		if(window.location.href.indexOf("webapp.jsp") != -1) {
			createRest("POST", "getsrdataservlet", null, function(response) {
				var srArray = JSON.parse(response);
				
				// Check for loaded sr's which are not on the map yet
				for(var i in srArray) {
					var loadedSr = srArray[i];
					var savedMarker = webappDataObj.mapData.getMarkerWithSrId(loadedSr.id);
					var savedSr = (savedMarker == null) ? null : savedMarker.sr;
					
					if(savedSr == null) {
						// New found marker
						console.log("Added marker for " , loadedSr);
						
						addMarker(loadedSr, webappDataObj.mapData.map);
					} else {
						// Check if the actual data is saved
						if(savedSr.status != loadedSr.status) {
							console.log("Changed status of " , savedSr , " from " + savedSr.status + " to " + loadedSr.status);
							webappDataObj.mapData.updateSrOfMarker(loadedSr.id, loadedSr);
							webappDataObj.mapData.updateMarker(loadedSr.id);
						}
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
	}, 1000);
	
	$(".filter-urgency").click(function(el) {
		$(this).toggleClass('active');
	});
	$("#filter-reset").click(function(el) {
		$(".filter-urgency").addClass('active');
		$(".filter-reward").val("");
		$("#filter-apply").click();
	});
	$("#filter-apply").click(function(el) {
		var showRed = $('#filter-urgency-red').hasClass('active');
		var showYellow = $('#filter-urgency-yellow').hasClass('active');
		var showGreen = $('#filter-urgency-green').hasClass('active');
		var minReward = +($('#filter-reward-min').val()) || 0;
		var maxReward = +($('#filter-reward-max').val()) || Number.MAX_VALUE;
		$.each(webappDataObj.mapData.markers, function(index, value) {
			var removeMarker = false;
			removeMarker |= 
				(value.icon.includes('red') && !showRed) ||
				(value.icon.includes('orange') && !showYellow) ||
				(value.icon.includes('green') && !showGreen);
			removeMarker |=
				value.sr.costsAndReward < minReward ||
				value.sr.costsAndReward > maxReward;
				
			if (removeMarker) {
				webappDataObj.mapData.removeMarker(value.sr.id);
			}
			else if (webappDataObj.mapData.getMarkerWithSrId(value.sr.id).map == null) {
				addMarker(value.sr, webappDataObj.mapData.map);
			}
		});
	});
	
	$("#btn-select-address").click(function() {
		webappDataObj.newSearchRequestClicked = true;
		$.notify("Bitte klicken Sie den Lieferort auf der Karte an.", "info");
	});
	// initDateTimeRange();
	
	$("#srform-maxcosts").keyup(function() {
		updateInputCostFields();
	});
	
	$("#sr-toggle-btn").click(function() {
		if ($("#srform").hasClass("hidden")) {
			$("#sr-toggle-btn i").addClass("glyphicon-menu-up");
			$("#sr-toggle-btn i").removeClass("glyphicon-menu-down");
			$("#srform").removeClass("hidden");
		}
		else {
			$("#sr-toggle-btn i").addClass("glyphicon-menu-down");
			$("#sr-toggle-btn i").removeClass("glyphicon-menu-up");
			$("#srform").addClass("hidden");
		}
	});
	
	$("#srform").submit(function(e) {
		if ($("#srform-address").val() === "") {
			$.notify("Fehler beim Erstellen des Gesuchs - du hast noch keinen Lieferort bestimmt!", "error");
			e.preventDefault();
			return;
		}
		
		webappDataObj.updateNewSrObj();
		webappDataObj.newSrObj.token = $.cookie("token");
		
		createRest("POST", "newsrservlet", JSON.stringify(webappDataObj.newSrObj), function(response) {
			if(lastRestStatus != null && lastRestStatus == 201 || lastRestStatus == 200) {
				var responseObj = JSON.parse(response);
				var sr = responseObj.sr;
				
				$.notify("Gesuch \"" + webappDataObj.newSrObj.title + "\" erfolgreich erstellt.", "success");
				
				console.log("Created new search request: " , sr);
				
				addMarker(sr, webappDataObj.mapData.map);
				updateNavbarBalance((sr.costsAndReward + sr.fee) * -1);
				
				webappDataObj.resetNewSrObj();
				document.getElementById("srform").reset();
				
				if(dummyMarker != null) {
					dummyMarker.setMap(null);
				}
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
				var sr = webappDataObj.mapData.getMarkerWithSrId(srId).sr;
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

var dummyMarker = null;
function updateGeoData(lat, lng, address) {
	if(webappDataObj.newSearchRequestClicked) {
		webappDataObj.newSearchRequestClicked = false;
		
		webappDataObj.newSrObj.lat = lat;
		webappDataObj.newSrObj.lng = lng;
		if(address != null) {
			webappDataObj.newSrObj.address = address;
			$("#srform-address").val(address);
		}
		
		// Dummy marker for visual impact of the place
		if(dummyMarker != null) {
			dummyMarker.setMap(null);
		}
		dummyMarker = new google.maps.Marker({
			position: new google.maps.LatLng(lat, lng),
			map: webappDataObj.mapData.map,
			icon: "http://maps.google.com/mapfiles/ms/icons/yellow-dot.png",
			title: "Neues Gesuch an: " + address,
		});
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
	var marker = new google.maps.Marker({
		position: new google.maps.LatLng(sr.lat , sr.lng),
		map: map,
		icon: webappDataObj.userData.id != sr.userCreator.id
			? sr.marker_icon_path 
			: "http://maps.google.com/mapfiles/ms/icons/blue-dot.png",
		title: sr.title,
		sr: sr
	});
	
	// Show sr's which are in progress only the creator
	var disableCancelBtn = false;
	var disableStatisfyBtn = false;
	if(sr.status == "IN_PROGRESS" && sr.userCreator.id != webappDataObj.userData.id) {
		if(typeof sr.userStatisfier != "undefined" && sr.userStatisfier.id == webappDataObj.userData.id) {
			disableStatisfyBtn = true;
		} else {
			webappDataObj.mapData.markers.push(marker);
			webappDataObj.mapData.removeMarker(sr.id)
			return false;
		}
	} else if(sr.status == "IN_PROGRESS") {
		disableCancelBtn = true;
	}
	
	marker.addListener('click', function() {
		var sr = this.sr;
		console.log("Marker clicked, referenced sr: ", sr);
		
		var btn = "";
		if(typeof webappDataObj.userData.id != "undefined" && webappDataObj.userData.id != -1) {
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
	return true;
}
