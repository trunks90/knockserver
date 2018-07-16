/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author trunks
 */
public class DEBUG {
    
    
    static private boolean d = true;  //estado DEBUG o no.

    static private StringBuilder texto = new StringBuilder();
    
    static public void log (String tag, String mensaje) {
        if (mensaje == null) mensaje = "";
        if (d) System.out.println("INFO:"+tag + mensaje);
        texto.append("<li>" + mensaje + "</li>");
    }
    
    static public String get_log() {
        return texto.toString();
    }
    
    static public String flush_log() {
        texto.setLength(0);
        return "Log is empty.";
    }
}
