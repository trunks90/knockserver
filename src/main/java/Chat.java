/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author trunks
 */
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import static j2html.TagCreator.*;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import static spark.Spark.*;
import spark.Request;
import spark.Spark;
import spark.utils.IOUtils;

public class Chat {
    
    
           
     //public static String PORT = System.getenv("PORT");     
   // final static int SERVER_PORT = Integer.valueOf(System.getenv("PORT")); //5020;
    
    final static String numero_telefono_servidor = "000";
    final static String status_tel = "_status_";
    
    
    final static int milisSleepRecepcionMensajes = 200;
    final static int milisTimeoutRecepcionMensajes = milisSleepRecepcionMensajes*5*60*1;
    
    final static String cmdHOLA = (char)30+"hOlA";
    final static String cmdADIOS = (char)30+"aDiOs";
    final static String cmdPING = (char)30+"PING";
    final static String cmdPONG = (char)30+"PONG";
    
    final static String cmd_serv_conexiones = "c" ; //"conexiones";
    final static String cmd_serv_buzones = "b"; //buzones";
    final static String cmd_serv_log = "log";
    final static String cmd_serv_kill = "k";
    final static String cmd_serv_limpia = "limpia";
    final static String cmd_serv_salir = "salir";
    final static String cmd_serv_ayuda = "ayuda";
    final static String[] cmd_serv_comandos = {cmd_serv_ayuda,cmd_serv_conexiones,cmd_serv_buzones,cmd_serv_kill,cmd_serv_limpia,cmd_serv_log,cmd_serv_salir};
   
    

    
    static Map<Session, UnaConexion> lista = new ConcurrentHashMap<>();
    
    static ArrayList<Session> lista_a_cerrar = new ArrayList<>();
   
    static HashMap<String, ArrayList<Mensaje> > listaTelefonosMensajes  = new HashMap<>();
    
    static UnaConexion log_serv = null;
    
    static UnaConexion status_conex = null;
            

    
    
    
    

    // this map is shared between sessions and threads, so it needs to be thread-safe (http://stackoverflow.com/a/2688817)
   // static Map<Session, String> userUsernameMap = new ConcurrentHashMap<>();
    static int nextUserNumber = 1; //Assign to username for next connecting user

    
    
    
    
//    public static void main(String[] args) {
//        //staticFiles.location("/public"); //index.html is served at localhost:4567 (default port)
//        staticFiles.expireTime(600);
//        webSocket("/chat", ChatWebSocketHandler.class);
//        init();
//    }
// 
//    
    
    
    public Chat (){
    
        webSocket("/chat", ChatWebSocketHandler.class);
        //init();
        
        //get("/", (req, res) -> " Chat server ");
        get("/conexiones", (req, res) ->    "Conexiones Actuales| "+sin_raros(get_lista_conexiones()) );   
        get("/mailboxes", (req, res) ->     "buzones| "+sin_raros(get_lista_mailboxes()) );   
        get("/log", (req, res) ->           DEBUG.get_log());   
        get("/flushlog", (req, res) ->      DEBUG.flush_log());   
        
        get("/help", (path, res) ->       "conexiones "
                                        + "mailboxes "
                                        + "log "
                                        + "flushlog "
                      );   
        
        get("/status", (q, a) ->

                //IOUtils.toString(Spark.class.getResourceAsStream("/public/index.html"))

                //new String(Files.readAllBytes(Paths.get(getClass().getResource(htmlFile).toURI())), StandardCharsets.UTF_8)        
                
                renderContent("index.html")
        );
        
    }
 
    
    
    private String renderContent(String htmlFile) {
    try {
        // If you are using maven then your files
        // will be in a folder called resources.
        // getResource() gets that folder
        // and any files you specify.
        URL url = getClass().getResource(htmlFile);

        // Return a String which has all
        // the contents of the file.
        Path path = Paths.get(url.toURI());
        return new String(Files.readAllBytes(path), Charset.defaultCharset());
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
    
    
    
    
    
 
    public String hi(Request req) {

      String alias = req.queryParams("al");


     return alias; 
    }

    
    
    
    
    
    public  static void nuevaConexion(Session sesion) {
        lista.put(sesion,new UnaConexion(sesion, nextUserNumber));
        DEBUG.log("Chat", "Nueva conexion:"+(nextUserNumber));
        nextUserNumber++;
        if (nextUserNumber == Integer.MAX_VALUE-1)
            nextUserNumber = 1;
    }
    
    public static void cierraConexion(Session sesion, int statusCode, String reason) {
        DEBUG.log("Chat", "Cierra conexion:"+(lista.get(sesion)).getOrigen());
        lista.remove(sesion);
        
    }
    
    
    

    
    public static void recibeMensaje(Session sesion, String mensajeString) {
        UnaConexion conex = lista.get(sesion);
        String origen = conex.getOrigen();
         if (origen.length() == 0) 
             origen = "anonimo";  //xxxxxxxxxxxxxxxxxxxxxxxxx
//        Mensaje msg = new Mensaje(origen,
//                                    "todos",             //xxxxxxxxxxxxxxxxxxxxxxx
//                                    mensajeString
//                                    ,null);
        Mensaje msg = new Mensaje( mensajeString );

        String destino = msg.getDestino();
//         if (destino == null) {
//             destino = "todos";
//         }
        
        DEBUG.log("Chat", "Recibido mensaje: ["+origen+"]"+ msg.toString() );
     
        if (msg.getOrigen() != null)
            if (msg.getOrigen().equals(status_tel)) {
                status_conex = conex;
                DEBUG.log("Chat", "----status session");
                //TODO  : mas cosas

               // return;


            }
                //TODO
         //QUITAR !!!!!!!!
//         if ( !numero_telefono_servidor.equals(msg.getDestino()) ) {
//             escribe_en_log("Recibido (Thr): {"+serv.getID()+"-"+serv.getOrigen()+"}"+msg+"\n");
//          }

           String comando = msg.getComando();

     // lo procesa


        if (comando == null) {
            escribe_en_log("Recibido comando vacio !!!!!!!!!!");
            
        } else {
                                                                                        
                if (comando.equals(cmdHOLA)) { //=====================================
                    escribe_en_log("Recibido HOLA : ---- ");
                    //toma el origen
                    String tel = msg.getOrigen();

                    // busca si habia otra conexion anterior
                    if (conex.getOrigen().length() == 0 ) {
                        // es nueva. no se ha identificado.

                        // busca si habia otra conexion anterior
                        for (UnaConexion con:lista.values()) {
                            if (tel.equals(con.getOrigen()))
                                    // ya existe!!!  
                                    // borrarlo.
                                    escribe_en_log("__cerrar conexion anterior mismo numero:"+tel);
//                                    lista_a_cerrar.add(serv_busca);
                         }
                    }



                    conex.setOrigen(tel);

                    // si hay mensajes guardados, se los envia
//                    if (listaTelefonosMensajes.containsKey(tel)) {
//                        ArrayList<Mensaje> mailbox = listaTelefonosMensajes.get(tel);
//                        for (Mensaje mailbox_msg : mailbox){
//                            envia(mailbox_msg);
//                            escribe_en_log("__envia mensaje guardado:"+mailbox.toString());
//                        }
//                        mailbox.clear();
//                        listaTelefonosMensajes.remove(tel);
//                    } ;

                } else

                if (comando.equals(cmdPING)) { //=====================================
                    //escribe_en_log("Recibido PING : ---- ");
                    if (msg.getDestino().equals(numero_telefono_servidor)) {
                        // PING al Servidor.
                        // devuelve PONG
                        Mensaje pong = new Mensaje(
                                        msg.getOrigen(),
                                        numero_telefono_servidor,
                                        cmdPONG,
                                        ""
                                );
                                envia(pong);

                    } else {

                        // busca, a ver si está en la lista el destinatario
                        boolean esta = false;
                        String destin = msg.getDestino();
                        for (UnaConexion con:lista.values()) {
                            if (con.getOrigen().equals(destin)) {
                                
                                //TODO : enviarselo, para que lo devuelva.
                                
                                // devuelve PONG
                                Mensaje pong = new Mensaje(
                                        destin,
                                        msg.getOrigen(),
                                        cmdPONG,
                                        ""
                                );
                                envia(pong);
                                esta = true;
                                break;  // no busca mas.
                            }
                        }
                        if (!esta) {
                            if (destin.equals(msg.getOrigen())) {
                                escribe_en_log("PING a si mismo.");
                            } else 
                            if (!destin.equals(numero_telefono_servidor)) {
                                escribe_en_log("Host destino NO CONECTADO : ---- ");
                            }
                        }
                        
                        


                    }

                } else   


                if (comando.equals(cmdADIOS)) { //=====================================
                    escribe_en_log("Recibido ADIOS : ---- ");
                    //cierra conexion
                    escribe_en_log("__cerrar conexion anterior ADIOS:"+conex.getOrigen());
                    lista_a_cerrar.add(sesion);
                    cierra_conexion(sesion);
                } else 

                if (msg.getDestino().equals(numero_telefono_servidor)) {

                    //-----------------------------------------------------
                    // comando al servidor !!!!

                    if (comando.equals(cmd_serv_ayuda)) {
                        StringBuilder ss = new StringBuilder("Comandos disponibles  ");
                        for (String cm:cmd_serv_comandos)
                            ss.append(cm+" ");

                        Mensaje resp = new Mensaje(
                                    msg.getDestino(),
                                    msg.getOrigen(),
                                    ss.toString(),
                                    ""
                            );
                            envia(resp);
                    } else 

                    if (comando.equals(cmd_serv_conexiones)) {
                        Mensaje resp = new Mensaje(
                                    msg.getDestino(),
                                    msg.getOrigen(),
                                    "Conexiones Actuales| "+sin_raros(get_lista_conexiones()),
                                    ""
                            );
                            envia(resp);
                    } else 

                    if (comando.equals(cmd_serv_buzones)) {
                        String lis = get_lista_mailboxes();
                        if (lis.length() == 0)
                            lis = "- 0 -";
                        Mensaje resp = new Mensaje(
                                    msg.getDestino(),
                                    msg.getOrigen(),
                                    "buzones| "+sin_raros(lis),
                                    ""
                            );
                            envia(resp);
                    } else

                    if (comando.equals(cmd_serv_log)) {
                        if (log_serv == null ){
                            log_serv = conex;
                        } else {
                            log_serv = null;
                        }

                        Mensaje resp = new Mensaje(
                                    msg.getDestino(),
                                    msg.getOrigen(),
                                    (log_serv==null)?"log NO redireccionado":"log redireccionado aqui. ",
                                    ""
                            );
                            envia(resp);
                    } else

                    if (comando.startsWith(cmd_serv_kill) ) {
                        //escribe_en_log("__cerrar conexion KILL-----------");
                        String numeroS = comando.substring(cmd_serv_kill.length(), comando.length());
                        int numero = Integer.parseInt(numeroS);
                        escribe_en_log("__cerrar conexion KILL:"+numero);

                        boolean hay = false;
                        for (Map.Entry<Session,UnaConexion> entry : lista.entrySet()) {
                                if (entry.getValue().getID() == numero) {
                                    lista_a_cerrar.add(entry.getKey());
                                    hay = true;
                                }
                                
                        }
                        if (!hay) {
                           escribe_en_log("__numero no valido"); 
                        }
                    } else

                    if (comando.equals(cmd_serv_limpia) ) {
                        escribe_en_log("__Limpia conexiones y Mailboxes___");

                        limpia_conexiones_y_mailboxes();

                    } else

                    if (comando.equals(cmd_serv_salir) ) {
                        escribe_en_log("__CERRAR SERVIDOR _____");

                        // grabar mailboxes...
                        //TODO
//
//                        cerrar_y_salir_de_la_aplicacion();

                                     // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                    } else

                    {
                        Mensaje resp = new Mensaje(
                                    msg.getDestino(),
                                    msg.getOrigen(),
                                    " ---- comando desconocido ---",
                                    ""
                            );
                            envia(resp);
                    }  



                } else
                {  



                    envia(msg);  // NO reenvia ni hola ni adios....
                }

    
//            } else {
//
//             //(comando == null ) {
//                    escribe_en_log("Recibido mensaje no valido ***");
//            }


        } // comando != null
     
        
        
                // cierra las conexiones que hay que cerrar.
                for (Session serv_busca:lista_a_cerrar) {
                    UnaConexion con = lista.get(serv_busca);
                        escribe_en_log("__cierra conexion anterior:("+con.getID()+")"+con.getOrigen());
                        cierra_conexion(serv_busca);
                }
                lista_a_cerrar.clear();
        
        
        



        
        //userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
        

            
        


//TODO
        
        
        //Chat.broadcastMessage(sender = Chat.userUsernameMap.get(user), msg = message);
        //        lista.forEach(( Session ses, UnaConexion con) ->{
//            
//                    conex.envia_mensaje(new Mensaje(con.getOrigen(),
//                                                    "todos",
//                                                    msg,
//                                                    null));
//                    ;
//        });
        
        
        
    }
    
    

    //Sends a message from one user to all users, along with a list of current usernames
    public static void updateStatus(Session session, String message) {
        //System.out.println(sender+":"+message);
        //userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
        
        if (status_conex == null)
            return;
        
        DEBUG.log("Chat", "JSONm:"+message);
        DEBUG.log("Chat", "JSONl:"+lista.values());

        if ((message == null) || (lista.values() == null) )
            return;
        
        Mensaje m = new Mensaje(message);
        String[] conexiones = new String [lista.size()];
        int i=0;
        for (UnaConexion c:lista.values()) {
            conexiones[i] = c.toString();
            i++;
        }

        try {
            status_conex.envia_mensaje(  //message);
//                        .getRemote()
//                        .sendString(
//                        "holas");
                String.valueOf(new JSONObject()
                    .put("userMessage", createHtmlMessageFromSender(m.getOrigen(), message))
                    .put("userlist", conexiones)
                )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    //});
    }

    //Builds a HTML element with a sender-name, a message, and a timestamp,
    private static String createHtmlMessageFromSender(String sender, String message) {
        return article(
            b(sender + " says:"),
            span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
            p(message)
        ).render();
    }




    
    
    
    
    
    
     
     static void envia(Mensaje msg) {
         if (lista.isEmpty()) return;
         
         // buscar a quien enviarlo
         
         String destino = msg.getDestino();
         
        for (UnaConexion con:lista.values()) {
             if (con.getOrigen().equals(destino)) {
                con.envia_mensaje(msg);
                //escribe_en_log("  Envio: {"+serv.getID()+"-"+serv.getOrigen()+"->"+destino+"}"+msg.getComando());
                return;
             }
         }
         
         if ((msg.getDestino()).equals(numero_telefono_servidor)) {
            // nada, Heartbeat.
            escribe_en_log("   _ HearBeat de "+msg.getOrigen());
            return;
         }
         
         // NO hay destinatario conectado!!!!
         // grabar en BUZON
         escribe_en_log("      -> Guardo: {"+msg.toString());
        
         String tel = msg.getDestino();
         if (listaTelefonosMensajes.containsKey(tel)) {
             ArrayList<Mensaje> mailbox = listaTelefonosMensajes.get(tel);
             mailbox.add(msg);
             escribe_en_log("      -> Guardo en mailbox (existe): ["+mailbox.size()+"] "+mailbox.get(mailbox.size()-1));
         } else {
             ArrayList<Mensaje> mailbox = new ArrayList<>();
             mailbox.add(msg);
             listaTelefonosMensajes.put(tel, mailbox);
             escribe_en_log("      -> Guardo en mailbox (Nuevo): ["+mailbox.size()+"] "+mailbox.get(mailbox.size()-1));

         };
         
     }
     
    
    
    
    
       
        
    static private void  limpia_conexiones_y_mailboxes() {
        
        if (lista != null) {
            
            // hace copia, pues "cerrar" borra el elmento.
            ArrayList<UnaConexion> lis = new ArrayList<>();
            
            
            lista.forEach((k,co) -> cierra_conexion(k));
            
//            for (UnaConexion serv:lista.values()) {
//                lis.add(serv);xx
//                //cierra_conexion(serv);
//            }
//            for (UnaConexion serv:lis) {
//                cierra_conexion(serv);
//            }
            lista.clear();
        }
        
        if (listaTelefonosMensajes != null) {
            for (Map.Entry<String, ArrayList<Mensaje> > entry:listaTelefonosMensajes.entrySet()) {

            //String tel = entry.getKey();
            entry.getValue().clear();
            }
        
            listaTelefonosMensajes.clear();
        }
    }

         
    static void cierra_conexion (Session s) {
         
        UnaConexion con = lista.get(s);
         con.desconnectar();
        escribe_en_log("conexion cerrada: "+con.getID());
        lista.remove(s);

//        actualiza_lista_conexiones();
     }
     
    
    static  private String get_lista_conexiones() {

        StringBuilder ss = new StringBuilder("");
        lista.forEach((k,co) -> 
            ss.append(co.getID()+":"+co.getOrigen()+(char)13+(char)10)
        );
        return ss.toString();  
    }
    
    
    static private String get_lista_mailboxes() {
         
        StringBuilder ss = new StringBuilder("");
         
        for (Map.Entry<String, ArrayList<Mensaje> > entry:listaTelefonosMensajes.entrySet()) {

            String tel = entry.getKey();
            StringBuilder lista_mensajes = new StringBuilder();

            ArrayList<Mensaje> mailbox = entry.getValue();
            for (Mensaje mailbox_msg : mailbox){
                lista_mensajes.append(">"+mailbox_msg.getComando()+(char)13+(char)10);
            }


            ss.append(tel+":"+lista_mensajes+(char)13+(char)10);
        }
         
         
        return ss.toString();
     }
     
    static  private String sin_raros(String s) {
         String weird_chars = "[,.:$%&/()=!·?¿]*\r\n";
        return s.replaceAll(weird_chars, "_");
     }

    
    
    
    
    
    
    
    
    

     
     static void escribe_en_log(String msg) {
         
                //getting current date and time using Date class
       DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
       Date dateobj = new Date();
       //System.out.println(df.format(dateobj));
       
       String texto = "["+df.format(dateobj)+"] "+msg;
       
//       if (!solo_texto)
//       frame.escribe_en_log_FRAME(msg);
       
        
        DEBUG.log("Chat", texto);
        //System.out.println(texto);
        

//        actualiza_lista_conexiones();
       


         
        if (log_serv!= null) {
         
            Mensaje resp = new Mensaje(
                     numero_telefono_servidor,
                     log_serv.getOrigen(),
                     texto,
                     ""
             );
            
            try {
             envia(resp);  
            } catch (Exception e) {
                // cierra conexion
                log_serv = null;
            }
        }
        
        if (status_conex!= null) {
            
            Mensaje resp = new Mensaje(
                     status_tel,
                     status_conex.getOrigen(),
                     texto,
                     ""
             );
            try {
             envia(resp);  
            } catch (Exception e) {
                // cierra conexion
                status_conex = null;
            }
        }
        
                
         
         
     }
     





}


    