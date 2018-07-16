//Establish the WebSocket connection and set up event handlers
//var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat");
var webSocket = new WebSocket("ws://localhost:8080/chat");
webSocket.onmessage = function (msg) { updateChat(msg); };
webSocket.onclose = function () { alert("WebSocket connection closed") };

//Send message if "Send" is clicked
id("send").addEventListener("click", function () {
    sendMessage(id("message").value);
});

//Send message if enter is pressed in the input field
id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) { sendMessage(e.target.value); }
});

//Send a message if it's not empty, then clear the input field
function sendMessage(message) {
    if (message !== "") {
		var origen = "0000";
		var destino = "xxxx";
		var comando = message;
		var extra = "";
		var mens = "From:"+origen+"*To:"+destino + "*Comando:" + comando + "*Extra:" + extra;
        webSocket.send( // "From:0000*To:xxx*Comando:"+comando+"*Extra:eee");
		mens);
        id("message").value = "";
    }
}

//Update the chat-panel, and the list of connected users
function updateChat(msg) {
    var data = JSON.parse(msg.data);
    insert("chat", data.userMessage);
    id("userlist").innerHTML = "";
    data.userlist.forEach(function (user) {
        insert("userlist", "<li>" + user + "</li>");
    });

	//insert("chat", msg);
  
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}
