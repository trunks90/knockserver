/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import org.eclipse.jetty.websocket.api.annotations.*;

import org.eclipse.jetty.websocket.api.Session;

/**
 *
 * @author trunks
 */

@WebSocket
public class ChatWebSocketHandler {

    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        Chat.nuevaConexion(user);
//        String username = "User" + Chat.nextUserNumber++;
//        Chat.userUsernameMap.put( user, username);  // cast!!
//        Chat.broadcastMessage(sender = "Server", msg = (username + " joined the chat"));
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        Chat.cierraConexion(user,statusCode,reason);
//        String username = Chat.userUsernameMap.get(user);
//        Chat.userUsernameMap.remove(user);
//        Chat.broadcastMessage(sender = "Server", msg = (username + " left the chat"));
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        Chat.recibeMensaje(user,message);
        Chat.updateStatus(user,message);
        
    }
    
    
    

}