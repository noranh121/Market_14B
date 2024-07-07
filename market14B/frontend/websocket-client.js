var ws;

function connect() {
	var username = document.getElementById("user").value; //get this user name from the login username text box
	ws = new WebSocket('ws://localhost:8080/notifications?username=' + username);
	ws.onmessage = function(data) {
		alert(data.data);
	}
	setConnected(true);
	console.log("Bing!!!");
}

function disconnect() {
	if (ws != null) {
		ws.close();
	}

	setConnected(false);
	console.log("Websocket is in disconnected state");
}

function setConnected(connected) {
	document.getElementById("connect").disabled = connected;
	document.getElementById("disconnect").disabled = !connected;
}
