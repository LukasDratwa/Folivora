var webappDataObj = {
	newSearchRequestClicked: false,
	newSrObj: {
		title: "",
		description: "",
		costsAndReward: 0.0,
		lat: 0,
		lng: 0,
		address: "",
		possibleDelivery: [],
		preferredDelivery: []
	},
	updateNewSrObj: function() {
		this.newSrObj.title = $("#srform-title").val();
		this.newSrObj.description = $("#srform-description").val();
		this.newSrObj.costsAndReward = $("#srform-final-costs").val();
	}
};

$(document).ready(function() {
	$("#btn-new-searchrequest").click(function() {
		webappDataObj.newSearchRequestClicked = true;
	});
	initDateTimeRange();
	
	$("#srform-maxcosts").keyup(function() {
		updateInputCostFields();
	});
});

function addSearchRequest(lat, lng, address) {
	if(webappDataObj.newSearchRequestClicked) {
		webappDataObj.newSearchRequestClicked = false;
		
		webappDataObj.newSrObj.lat = lat;
		webappDataObj.newSrObj.lng = lng;
		if(address != null) {
			webappDataObj.newSrObj.address = address;
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
	        "daysOfWeek": [
	            "So",
	            "Mo",
	            "Di",
	            "Mi",
	            "Do",
	            "Fr",
	            "Sa"
	        ],
	        "monthNames": [
	            "Januar",
	            "Feburar",
	            "März",
	            "April",
	            "Mai",
	            "Juni",
	            "Juli",
	            "August",
	            "September",
	            "Oktober",
	            "November",
	            "Dezember"
	        ],
	        "firstDay": 1
	    },
	    "minDate": new Date(),
	    "startDate": new Date(),
	    "endDate": new Date(),
	    "opens": "left"
	}, function(start, end, label) {
		webappDataObj.newSrObj.possibleDelivery[0] = new Date(start.format('DD.MM.YYYY hh:mm'));
		webappDataObj.newSrObj.possibleDelivery[1] = new Date(start.format('DD.MM.YYYY hh:mm'));
		
		console.log("New date range selected:" + start.format('DD.MM.YYYY hh:mm') + ' to ' + end.format('DD.MM.YYYY hh:mm') + ' (predefined range: ' + label + ")");
	});
}

function initMap() {
	var myLatlng = new google.maps.LatLng(48.874497,8.655883);
	var imagePath = "http://maps.google.com/mapfiles/ms/icons/green-dot.png" // blue-dot, orange oder red-dot
	var mapOptions = {
		zoom: 15,
		center: myLatlng,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	}
	
	var map = new google.maps.Map(document.getElementById('webapp-map'), mapOptions);
	//Callout Content
	var contentString = 'Some address here..';
	//Set window width + content
	var infowindow = new google.maps.InfoWindow({
		content: contentString,
		maxWidth: 500
	});
	
	//Add Marker
	var marker = new google.maps.Marker({
		position: myLatlng,
		map: map,
		icon: imagePath,
		title: 'image title'
	});
	
	var geocoder = new google.maps.Geocoder();
	
	google.maps.event.addListener(marker, 'click', function() {
		infowindow.open(map,marker);
	});
	
	google.maps.event.addListener(map, 'click', function(e) {
		var lat = e.latLng.lat();
		var lng = e.latLng.lng();
		
		geocoder.geocode({
		    'latLng': e.latLng
		  }, function(results, status) {
		    if (status == google.maps.GeocoderStatus.OK) {
		      if (results[0]) {
		        addSearchRequest(lat, lng, results[0].formatted_address);
		      } else {
		    	  addSearchRequest(lat, lng, null);
		      }
		    } else {
		    	addSearchRequest(lat, lng, null);
		    }
		  });
	});
	
	//Resize Function
	google.maps.event.addDomListener(window, "resize", function() {
		var center = map.getCenter();
		google.maps.event.trigger(map, "resize");
		map.setCenter(center);
	});
	
	webappDataObj.map = map;
}