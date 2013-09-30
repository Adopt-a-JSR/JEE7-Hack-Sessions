/* 
 * This file include The program business logic 
 * and all utility methods
 * 
 * @Author: Mohamed Taman
 * 
 */

// Defining global variables and functions

var isGeo = true;
var isWebsocket = true;
var isLogin = true;

// Global reference to the WebSocket object
var ws;

// Global reference for geolocation object
var geoLoc;

// Websocket connection URL
var url = "ws://localhost:2020/RTRCT/server/runners/engage";

// Defining the runner registration data object
var runnerData = {action: "", userId: "", 
                  name: "", latitude:0, 
				  longitude:0,timestamp:0,
				  accuracy:0};
var serverData;

// Reference to google map canvas and map
var mapCanvas, map, bounds, zoom, markers, infoWindow, gDirService, dirDisplay;

// General logging function
log = function() {
    var p = document.createElement("p");
    var message = Array.prototype.join.call(arguments, " ");
    p.innerHTML = message;
    document.getElementById("info").appendChild(p);
};


// Logic

$(document).ready(function(e) {

    if (checkHTML5Features()) {
		
		$("#connectBtn").removeAttr("disabled");

        
		// Test
		$("#addMarker").click(function() {
           
		  runnerData = {action: "", userId: "New Marker", 
                  name: "Marker added", latitude:37.785054, 
				  longitude:-122.411161,timestamp:0,
				  accuracy:0};
		   createMarker(runnerData);
		   
        });
		
		$("#connectBtn").click(function() {
            initConnection(event);
        });

        $("#registerBtn").click(function() {
            sendRegMessage();
        });

        $("#toggleLog").click(function() {

            $("#log").toggle("slow");

            value = $(this).text().match("Show");
            if (value == "Show") {
                $(this).text("Hide log");
            } else {
                $(this).text("Show log");
            }
        });

        mapCanvas = document.getElementById('mapCanvas');
    }
});


// Function to check if HTML5 required features are suported or not.
function checkHTML5Features() {

    // Check for required Geolocation features
    if (!Modernizr.geolocation) {
        log("HTML5 <b>Geolocation</b> is not supported in your browser.");
        isGeo = false;
    }
    // Check for required Websockets features
    if (!Modernizr.websockets) {
        log("HTML5 <b>Websocket</b> is not supported in your browser.");
        isWebsocket = false;
    }


    if (isGeo && isWebsocket) {
        geoLoc = navigator.geolocation;
        return true;
    }

    return false;
}


/* 
 * If HTML5 features are Initialized  and supported in current browser, 
 * connects to the server and open conection for further processing, 
 * then get user geolocation and enable registration based on that.
 */
function initConnection(event) {

    //  Instantiating the websocket object
    ws = new WebSocket(url);

    ws.onopen = onOpen;
    ws.onmessage = onMessage;
    ws.onclose = onClose;
    ws.onerror = onError;

    //Initializeing geolocation watching

    geoLoc.getCurrentPosition(updateLocation,
            handleLocationError,
            {timeout: 10000});

}

/************************************
 Websocket method handlers
 *************************************/

onOpen = function() {
    log("Connected...");

    //If Connected successfully enable buttons and fields
    $("#userId").removeAttr("disabled");
    $("#name").removeAttr("disabled");
    $("#registerBtn").removeAttr("disabled");

    serverData = new Object();
    serverData.action = "CONNECT";
    serverData.message = "thank you for accepting this WebSocket request.";

    // send one thanks message when the socket opens
    ws.send(JSON.stringify(serverData));

    $("#connectBtn").attr("disabled", "disabled");
};

onMessage = function(event) {

    serverData = JSON.parse(event.data);

    if (serverData.action == "CONNECT") {

        log(serverData.message + " You can register now in the Champion....");

    } else if (serverData.action == "RIGISTER" && serverData.result == "SUCCEED") {

        log(serverData.message);

        // Hide login page and enable geo info page.

        $("#loginPage").slideUp("slow", function() {
            isLogin = false;
			$("#urName").html("<b>You: </b>"+runnerData.name);
			$("#curLatitude").html("<b>Latitude </b>"+runnerData.latitude);
			$("#curlongitude").html("<b>Longitude: </b>"+runnerData.longitude);
			$("#accuracy").html("<b>Accuracy: </b>"+ runnerData.accuracy +" meters");
			$("#timestamp").html("<b>Time of update: </b>"+ new Date(runnerData.timestamp).toString());
			
			$("#RunnersInfoPage").slideDown("slow", function(){
				initMap();
				createMarker(runnerData);
				});
        });

    } else if (serverData.action == "UPDATE") {
		
		alert(serverData.toString());
	}
};

onClose = function() {

    log("Connection Closed...");
    $("#connectBtn").removeAttr("disabled");
};


onError = function() {

    log("<em style='color:red'>Error connecting to the server: check connection!!!</em>");
};


function disconnect() {
    ws.close();
}

function processMessage(data) {

}

/*
 * Function that will be used for preparation of runner registiration data
 * To be sent to the server in form of JSON data. 
 */
function sendRegMessage() {

    runnerData.action = "RIGISTER";
    runnerData.userId = $("#userId").val();
    runnerData.name = $("#name").val();

    /*
     * Generate a new JSON string based on current JS object.
     * And snd it to the server for registration.
     */
    ws.send(JSON.stringify(runnerData));
}

function sendUpdateMessage(data) {

}


/************************************
 Geolocation method handlers
 *************************************/

//This method handles change position notifications
function updateLocation(position) {

    var latitude = position.coords.latitude;
    var longitude = position.coords.longitude;
 
    if (!latitude || !longitude) {
        log("HTML5 Geolocation is supported in your browser, but your location is currently not available.");
        return;
    }

    runnerData.latitude = latitude;
    runnerData.longitude = longitude;
	runnerData.accuracy = position.coords.accuracy;
	runnerData.timestamp = position.timestamp;

    if (!isLogin) {
        sendUpdateMessage(runnerData);
    } else {
        $("#latitude").attr("value", runnerData.latitude);
        $("#longitude").attr("value", runnerData.longitude);
    }

}


function handleLocationError(error) {
    switch (error.code)
    {
        case 0:
            log("There was an error while retrieving your location. Additional details: " + error.message);
            break;
        case 1:
            log("The user opted not to share his or her location.");
            break;
        case 2:
            log("The browser was unable to determine your location. Additional details: " + error.message);
            break;
        case 3:
            log("The browser timed out before retrieving the location.");
            break;
    }
}

/****************************************
 Google map method handlers and logic
 *****************************************/
function initMap() {
// Defining zoom level starting with 5
   zoom = 13;

// Creating markers array for clustring
    markers = [];

// Creating a MapOptions object with the required properties
    var options = {
        zoom: zoom,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        
        disableDefaultUI: false,
        mapTypeControl: true,
        mapTypeControlOptions: {
            style: google.maps.MapTypeControlStyle.DROPDOWN_MENU,
            position: google.maps.ControlPosition.RIGHT,
            mapTypeIds: [google.maps.MapTypeId.ROADMAP, google.maps.MapTypeId.SATELLITE]
        },
        navigationControl: true,
        navigationControlOptions: {
            position: google.maps.ControlPosition.TOP_RIGHT,
            style: google.maps.NavigationControlStyle.ANDROID
        },
        backgroundColor: '#CCCCCC',
        draggableCursor: 'move'
    };

    // Creating the map
    map = new google.maps.Map(mapCanvas, options);

     // Creating a variable that will hold the InfoWindow object once
    infoWindow;

    // Creating a LatLngBounds object
    bounds = new google.maps.LatLngBounds();

	//Declaring google Directions Service variable
	gDirService = new google.maps.DirectionsService();
	
	// Object that will draw the route on the map
	dirDisplay = new google.maps.DirectionsRenderer();
	dirDisplay.setMap(map);
			
	// Define Champion running start point and end point.
	// Starting point
	 var startPoint = new google.maps.LatLng(37.785733,-122.405968);
	 
	 // End Point
	 var endPoint = new google.maps.LatLng(37.786029,-122.411365);
	 
	 requestDirection(startPoint, endPoint);
}

//Adding multiple markers
function createMarker(runner) {

    var title = runner.name;

    var contents = null;
    // First we create the container for the content of the InfoWindow
    contents = document.createElement('div');
    // We then create a paragraph element that will contain some text
    var p = document.createElement('p');
    p.innerHTML = '<b>Name:</b> ' + runner.name + '<br /> <b>User Id:</b> ' + runner.userId + '<br /> Latitude:' + runner.latitude + '<br /> Longitude:' + runner.longitude;
    // We then create a second paragraph element that will contain the clickable link
    var p2 = document.createElement('p');
    // Creating the clickable link
    var a = document.createElement('a');
    a.innerHTML = 'Zoom in';
    a.href = '#';

    var a1 = document.createElement('a');
    a1.innerHTML = 'Zoom out';
    a1.href = '#';

    var text = document.createElement('span');
    text.innerHTML = ' | ';

    // Adding a click event to the link that performs
    // the zoom in, and cancels its default action
    a.onclick = function() {
        // Setting the center of the map to the same as the clicked marker
        map.setCenter(marker.getPosition());

        // Setting the zoom level to zoom + 1 till 23
        if (zoom != 23) {
            zoom += 1;
        }

        map.setZoom(zoom);
        // Canceling the default action
        return false;
    };

    // Adding a click event to the link that performs
    // the zoom out, and cancels its default action
    a1.onclick = function() {
        // Setting the center of the map to the same as the clicked marker
        map.setCenter(marker.getPosition());

        // Setting the zoom level to zoom - 1 till 1
        if (zoom != 0) {
            zoom -= 1;
        }

        map.setZoom(zoom);

        // Canceling the default action
        return false;
    };

    // Appending the links to the second paragraph element
    p2.appendChild(a);
    p2.appendChild(text);
    p2.appendChild(a1);

    // Appending the two paragraphs to the content container
    contents.appendChild(p);
    contents.appendChild(p2);
    contents.appendChild(document.createElement('br'));

    var latLang = new google.maps.LatLng(runner.latitude, runner.longitude);
    // Extending the bounds object with each LatLng
    bounds.extend(latLang);

    var marker = new google.maps.Marker({
        position: latLang,
        title: title,
        map: map
    });

    markers.push(marker);

    // Wrapping the event listener inside an anonymous function
    // that we immediately invoke and passes the variable marker and its contents to.
    (function(marker, contents) {

        google.maps.event.addListener(marker, 'click', function() {

            if (!infoWindow) {
                infoWindow = new google.maps.InfoWindow();
            }
            // Setting the content of the InfoWindow
            infoWindow.setContent(contents);

            // Tying the InfoWindow to the marker
            infoWindow.open(map, marker);
        });
    })(marker, contents);
}


// Function used to draw the driving request
function requestDirection(startPoint, endPoint) {

	var request = {
		origin : startPoint,
		destination : endPoint,
		travelMode : google.maps.DirectionsTravelMode.WALKING
	}
	
	gDirService.route(request, plotDirection);
	
}

function plotDirection(response, status) {
	//alert(response +", "+ status);
	if (status == google.maps.DirectionsStatus.OK) {
		//dirDisplay.setPanel(document.getElementById('myDirInfo'));
		dirDisplay.setDirections(response);
	}
}