var webappDataObj = {
	newSearchRequestClicked: false,
	newSrObj: {},
	updateNewSrObj: function() {
		this.newSrObj.title = $("#srform-title").val();
		this.newSrObj.description = $("#srform-description").val();
		this.newSrObj.costsAndReward = $("#srform-maxcosts").val();
		this.newSrObj.charges = $("#srform-charges").val();
	},
	resetNewSrObj: function() {
		this.newSrObj.title = "";
		this.newSrObj.description = "";
		this.newSrObj.costsAndReward = 0.0;
		this.newSrObj.charges = 0.0;
		this.newSrObj.lat = 0;
		this.newSrObj.lng = 0;
		this.newSrObj.address = "";
		this.newSrObj.possibleDelivery_from = null;
		this.newSrObj.possibleDelivery_to = null;
		this.newSrObj.preferredDelivery_from = null;
		this.newSrObj.preferredDelivery_to = null;
	},
	mapData: {
		map: null,
		infowindow: null
	}
};
webappDataObj.resetNewSrObj();

$(document).ready(function() {
	$("#btn-select-address").click(function() {
		webappDataObj.newSearchRequestClicked = true;
	});
	initDateTimeRange();
	
	$("#srform-maxcosts").keyup(function() {
		updateInputCostFields();
	});
	
	$("#srform").submit(function(e) {
		webappDataObj.updateNewSrObj();
		webappDataObj.newSrObj.token = $.cookie("token");
		
		createRest("POST", "newsrservlet", JSON.stringify(webappDataObj.newSrObj));
		
		setTimeout(function() {
			if(lastRestStatus != null && lastRestStatus == 201 || lastRestStatus == 200) {
				$.notify("Gesuch \"" + webappDataObj.newSrObj.title + "\" erfolgreich erstellt.", "success");
				
				console.log("Created new search request: " , webappDataObj.newSrObj);
				
				webappDataObj.resetNewSrObj();
				document.getElementById("srform").reset();
				
				setTimeout(function() {
					location.reload();
				}, 1500);
			} else {
				$.notify("Fehler beim Erstellen des Gesuchs!", "error");
			}
			
			lastRestStatus = null;
		}, 1000);
		
		
		e.preventDefault();
	});
});

function updateGeoData(lat, lng, address) {
	if(webappDataObj.newSearchRequestClicked) {
		webappDataObj.newSearchRequestClicked = false;
		
		webappDataObj.newSrObj.lat = lat;
		webappDataObj.newSrObj.lng = lng;
		if(address != null) {
			webappDataObj.newSrObj.address = address;
			$("#srform-address").val(address);
		}
		console.log(webappDataObj.newSrObj);
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

function initDateTimeRange() {
	$("#srform-delivery-possible").daterangepicker({
	    "showDropdowns": true,
	    "timePicker": true,
	    "timePicker24Hour": true,
	    "timePickerIncrement": 15,
	    "dateLimit": {
	        "days": 7
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
	    "minDate": new Date(),
	    "startDate": new Date(),
	    "endDate": new Date(),
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

var infowindowAppendixHtml = "";
var infowindow;
function initMap(payload) {
	var myLatlng = new google.maps.LatLng(49.874505,8.655980);
	var imagePath = "http://maps.google.com/mapfiles/ms/icons/green-dot.png" // blue-dot, orange oder red-dot
	var mapOptions = {
		zoom: 15,
		center: myLatlng,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	}
	var map = new google.maps.Map(document.getElementById('webapp-map'), mapOptions);
	
	//Add Marker from payload
	infowindow = new google.maps.InfoWindow({
		content: "",
		maxWidth: 500
	});
	var srArray = JSON.parse(payload);
	for(var i in srArray) {
		var sr = srArray[i];
		
		var marker = new google.maps.Marker({
			position: new google.maps.LatLng(sr.lat , sr.lng),
			map: map,
			icon: sr.marker_icon_path,
			title: sr.title,
			sr: sr
		});
		
		marker.addListener('click', function() {
			var sr = this.sr;
			console.log(sr);
			infowindow.setContent("<div><p><b>" + sr.title + "</b></p></div>"
					+ "<div><p>" + sr.description + "</p>"
					+ "<p>Lieferung möglich bis " + formatLongDate(sr.possibleDelivery_to) + "</p></div>"
					+ infowindowAppendixHtml);
		    infowindow.open(map, this);
		});
	}
	
	var geocoder = new google.maps.Geocoder();
	
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