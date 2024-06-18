var ws;

function connect() {
	var username = $("#user").val() //get this user name from the login username text box
	ws = new WebSocket('ws://localhost:8080/notifications?username=' + username);
	ws.onmessage = function(data) {
		alert(data.data);
	}
	setConnected(true);
}

function disconnect() {
	if (ws != null) {
		ws.close();
	}
	setConnected(false);
	console.log("Websocket is in disconnected state");
}
