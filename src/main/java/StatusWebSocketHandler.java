

import static j2html.TagCreator.attrs;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import static j2html.TagCreator.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author trunks
 */

@WebSocket
public class StatusWebSocketHandler {
    
    static Chat chat;
    static SQL sql;
    
    private String sender, msg;
    

    
    static Map<Session, String> userUsernameMap = new ConcurrentHashMap<>();
    static int nextUserNumber = 1; //Assign to username for next connecting user

    
    
    public StatusWebSocketHandler(Chat chat, SQL sql) {
    
        this.chat = chat;
        this.sql = sql;
        
    }
    

   
    
    
    
       @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = "User" + Chat.nextUserNumber++;
        userUsernameMap.put( user, username);  // cast!!
        broadcastMessage(sender = "Server", msg = (username + " joined the chat"));
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = userUsernameMap.get(user);
        userUsernameMap.remove(user);
        broadcastMessage(sender = "Server", msg = (username + " left the chat"));
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        broadcastMessage(sender = userUsernameMap.get(user), msg = message);
    }
    
    
    
    
    
       public static void broadcastMessage(String sender, String message) {
        System.out.println(sender+":"+message);
        userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                    .put("userMessage", createHtmlMessageFromSender(sender, message))
                    .put("userlist", userUsernameMap.values())
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //Builds a HTML element with a sender-name, a message, and a timestamp,
    private static String createHtmlMessageFromSender(String sender, String message) {
        return article(
            b(sender + " says:"),
            span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
            p(message)
        ).render();
    }

    
    
    
    
    
    
    
}
