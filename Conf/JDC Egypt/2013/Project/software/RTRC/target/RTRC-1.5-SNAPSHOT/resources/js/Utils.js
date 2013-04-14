/* 
 * This file include all utility methods
 */

var isGeoSupported, isWebsocketSupported = true;

var username;

// reference to the Web Socket
var socket;
var url = "ws://localhost:2020/RTRC/server/register";
var login = true;

// General logging function
log = function() {
    document.getElementById("log").style = "display: anything";
    var p = document.createElement("p");
    var message = Array.prototype.join.call(arguments, " ");
    p.innerHTML = message;
    document.getElementById("info").appendChild(p);
};

logRunners = function() {
    var p = document.createElement("p");
    var message = Array.prototype.join.call(arguments, " ");
    p.innerHTML = message;
    document.getElementById("otherRunners").appendChild(p);
};

// Function to check if HTML5 features are suported or not.
function checkHTML5Features() {
    // Check for required browser features
    if (!navigator.geolocation) {
        log("HTML5 Geolocation is not supported in your browser.");
        isGeoSupported = false;
        return;
    }

    if (!"Websocket" in window) {
        log("HTML5 Websocket is not supported in your browser.");
        isWebsocketSupported = false;
        return;
    } else {

        if (this.MozWebSocket) {
            WebSocket = MozWebSocket;
        }
    }
    
    document.getElementById("disconnect").onclick = function() {
            socket.close();
            document.getElementById("loginBtn").disabled = true;
            document.getElementById("connectBtn").disabled = false;
            document.getElementById("username").value = "";
            document.getElementById("password").value = "";
            document.getElementById("loginContainer").style = "display: anything";
        document.getElementById("geoContainer").style = "display: none";
        document.getElementById("otherRunners").innerHTML = "";
          
        login = true;
        };
    
 document.getElementById("sendData").onclick = function() {
             navigator.geolocation.watchPosition(updateLocation, handleLocationError,
                {timeout: 1000});
        };
    
        document.getElementById("connectBtn").onclick = function() {
        // websocket definition and event messages
        socket = new WebSocket(url);

        socket.onopen = function() {

            document.getElementById("loginBtn").disabled = false;
            document.getElementById("connectBtn").disabled = true;
            
        };

        socket.onmessage = function(e) {
            parseMessage(e.data.toString());

        };

        socket.onclose = function(e) {
            log("closed");
        };

        socket.onerror = function(e) {
            log("error");
        };
    };

    document.getElementById("loginBtn").onclick = function() {
        message = '{\"username\":\"' + document.getElementById("username").value + '\",\"password\":\"'
                + document.getElementById("password").value + '\"}';
        username = document.getElementById("username").value;
        document.getElementById("name").innerHTML = "You: " + username;
        socket.send(message);
    };
}

// disable login button in case of websocket API is not supported
function disableButton() {

    document.getElementById("connectBtn").disabled = !isWebsocketSupported;
    document.getElementById("loginBtn").disabled = true;
}

function parseMessage(message) {

if(login){
    if (message === 'true') {
        document.getElementById("loginContainer").style = "display: none";
        document.getElementById("geoContainer").style = "display: anything";
        document.getElementById("info").innerHTML = "";
        login = false;
        
    } else {
        log(message);
    }
}else{
        logRunners(message);
}
}


// geolocation functions

var totalDistance = 0.0;
var lastLat;
var lastLong;

Number.prototype.toRadians = function() {
    return this * Math.PI / 180;
};

function distance(latitude1, longitude1, latitude2, longitude2) {
    // R is the radius of the earth in kilometers
    var R = 6371;

    var deltaLatitude = (latitude2 - latitude1).toRadians();
    var deltaLongitude = (longitude2 - longitude1).toRadians();
    latitude1 = latitude1.toRadians(), latitude2 = latitude2.toRadians();

    var a = Math.sin(deltaLatitude / 2) *
            Math.sin(deltaLatitude / 2) +
            Math.cos(latitude1) *
            Math.cos(latitude2) *
            Math.sin(deltaLongitude / 2) *
            Math.sin(deltaLongitude / 2);

    var c = 2 * Math.atan2(Math.sqrt(a),
            Math.sqrt(1 - a));
    var d = R * c;
    return d;
}

function updateErrorStatus(message) {
    document.getElementById("status").style.background = "papayaWhip";
    document.getElementById("status").innerHTML = "<strong>Error</strong>: " + message;
}

function updateStatus(message) {
    document.getElementById("status").style.background = "paleGreen";
    document.getElementById("status").innerHTML = message;
}

function updateLocation(position) {
    var latitude = position.coords.latitude;
    var longitude = position.coords.longitude;
    var accuracy = position.coords.accuracy;
    var timestamp = position.timestamp;

    var message = "{\"name\":\""+username+"\",\"longitude\":"+longitude+",\"latitude\":"+latitude+","
                + "\"timestamp\":"+timestamp+",\"notes\":\"Accuracy = "+accuracy+"\"}";
    
    document.getElementById("latitude").innerHTML = "Latitude: " + latitude;
    document.getElementById("longitude").innerHTML = "Longitude: " + longitude;
    document.getElementById("accuracy").innerHTML = "Accuracy: " + accuracy + " meters";
    document.getElementById("timestamp").innerHTML = "Timestamp: " + timestamp;

        socket.send(message);

    // sanity test... don't calculate distance if accuracy
    // value too large
    if (accuracy >= 30000) {
        updateStatus("Need more accurate values to calculate distance.");
        return;
    }

    // calculate distance
    if ((lastLat != null) && (lastLong != null)) {
        var currentDistance = distance(latitude, longitude, lastLat, lastLong);

        document.getElementById("currDist").innerHTML =
                "Current distance traveled: " + currentDistance.toFixed(2) + " km";

        totalDistance += currentDistance;
        document.getElementById("totalDist").innerHTML =
                "Total distance traveled: " + currentDistance.toFixed(2) + " km";
        updateStatus("Location successfully updated.");

    }

    lastLat = latitude;
    lastLong = longitude;

}

function handleLocationError(error) {
    switch (error.code)
    {
        case 0:
            updateErrorStatus("There was an error while retrieving your location. Additional details: " + error.message);
            break;
        case 1:
            updateErrorStatus("The user opted not to share his or her location.");
            break;
        case 2:
            updateErrorStatus("The browser was unable to determine your location. Additional details: " + error.message);
            break;
        case 3:
            updateErrorStatus("The browser timed out before retrieving the location.");
            break;
    }
}







