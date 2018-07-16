

/*

 */




import static j2html.TagCreator.article;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.b;
import static j2html.TagCreator.p;
import static j2html.TagCreator.span;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.eclipse.jetty.websocket.api.Session;




public class UnaConexion  {
    
    
      private ArrayList<String> mensajes = null;
      private boolean stop = false;
      private String origen = "";
      
    
    private Session sesion;
   // private DataOutputStream dos;
   // private BufferedWriter dos_buff = null;
   // private DataInputStream dis;
   // private BufferedReader dis_buff = null;
    private int idSessio;
    private int tiempo;

    
    
    public String getOrigen() {
//        if (origen.length() == 0) 
//            return ""+idSessio;
//                else
            return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    
    public void incrementaTiempo() {
        tiempo++;
    }
    
    public int getTiempo() {
        return tiempo;
    }
    
    private void resetTiempo() {
        tiempo = 0;
    }
    
    
    
    public UnaConexion(Session sesion, int id) {
        this.sesion = sesion;
        this.idSessio = id;
//        try {
//            socket.setKeepAlive(true);
//            
//            dos = new DataOutputStream(socket.getOutputStream());
//            dos_buff =  new BufferedWriter(new OutputStreamWriter(dos) ) ;
//
//            dis = new DataInputStream(socket.getInputStream());
//            dis_buff  = new BufferedReader(new InputStreamReader(dis));
//            
//            
//        } catch (IOException ex) {
//            System.out.println("-------- error obteniendo IS o OS :"+this.getOrigen());
//        }

        //TODO xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        resetTiempo();
    }
    
    
    
    public int getID() {
        return idSessio;
    }
    
    /**
     * esta parado ?
     * @return 
     */
    public boolean getStop() {
        return stop;
    }
    
    public void desconnectar() {
          stop = true;
        DEBUG.log("UnaConexion","desconectando..."+this.getOrigen());//-------------------------
//          
//        try {
//            socket.shutdownInput();
//            socket.shutdownOutput();
//            socket.close();
//        } catch (IOException ex) {
//            System.out.println("error cerrando socket");
//        }

        //TODO xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        
        
    }
    
    
    
    
//   // @Override
//    public void run() {
////        String accion = "";
////        try {
////            accion = dis.readUTF();
////            if(accion.equals("hola")){
////                System.out.println("El cliente con idSesion "+this.idSessio+" saluda");
////                dos.writeUTF("adios");
////            }
////        } catch (IOException ex) {
////            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
////        }
////        desconnectar();
//        
//        mensajes = new ArrayList<>();
//        
//        while (!stop && socket.isConnected()) {
//                try {
//                    //String read = dis.readLine();
//                    String read = dis_buff.readLine();
//                    
//                    //xxxxxxxx
//                            
//                    
//                    
//                    
//                    if (read == null) 
//                        desconnectar();
//                    else  {
//                        mensajes.add(read);
//                        resetTiempo();
//                    }
//                    System.out.println("__Recibido:"+read);//-------------------------
//                    
//                } catch (Exception e) {
//                    System.out.println("----------------- error en READ");
//                    desconnectar();
//                    return;
//                }
//                      
//            }
//        desconnectar();
//        
//    }
        

    
    public boolean estaConectado() {
        
        return sesion.isOpen();    //socket.isConnected();
    }
    
    
    void borra_mensajes() {
        mensajes.clear();
    }

    ArrayList<String> lee_mensaje() {
        
        if (mensajes.isEmpty()) return null;
        
        ArrayList<String> res = new ArrayList<>();
        for (int x = 0; x<mensajes.size(); x++ ) 
            res.add(mensajes.get(x));
        borra_mensajes();
        return res;
    }
    
    void envia_mensaje(Mensaje msg) {
        
        if (sesion == null) return;
        if (msg == null) return;
        
        try {
            sesion.getRemote().sendString(msg.toString()
            
//            String.valueOf(new JSONObject()
//                    .put("userMessage", createHtmlMessageFromSender(
//                            origen,
//                            msg.toString()
//                    ) )
//                    //.put("userlist", userUsernameMap.values())
//                    )
            
            
            
            
            );
        } catch (Exception ex) {
            DEBUG.log("UnaConexion","----- error escribiendo");
        }
        
        resetTiempo();
       
   
                
    }
    
    
        void envia_mensaje(String msg) {
        
        if (sesion == null) return;
        if (msg == null) return;
        if (msg.length() == 0) return;
        
        try {
            sesion.getRemote().sendString(msg
            
//            String.valueOf(new JSONObject()
//                    .put("userMessage", createHtmlMessageFromSender(
//                            origen,
//                            msg.toString()
//                    ) )
//                    //.put("userlist", userUsernameMap.values())
//                    )
            
            
            
            
            );
        } catch (Exception ex) {
            DEBUG.log("UnaConexion","----- error escribiendo");
        }
        
        resetTiempo();
       
   
                
    }
    
    

    //Builds a HTML element with a sender-name, a message, and a timestamp,
    private static String createHtmlMessageFromSender(String sender, String message) {
        return article(
            b(sender + " says:"),
            span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
            p(message)
        ).render();
    }

    @Override
    public String toString() {
        return "C (" + idSessio+") " + origen ;
    }




}