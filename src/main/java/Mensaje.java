





/**
 * Created by trunks on 02/08/2015.
 */
public class Mensaje {

    
    private final static String cabecera = "From:";

    private String origen;
    private String destino;
    private String comando;
    private String extra;
    
    


    public Mensaje (String origen, String destino, String comando, String extra) {
        this.comando = comando;
        this.destino = destino;
        this.extra = extra;
        this.origen = origen;
        String res = this.toString();
        //DEBUG.log("mensaje creado","["+res.length()+"]"+res);
    }


    /**
     * de String a Mensaje
     * @param linea
     */
    public Mensaje (String linea) {

        if (linea == null) {
            this.comando = null;
            this.destino = null;
            this.extra = null;
            this.origen = null;
            return;
        }

        // origen

        int pos = linea.indexOf(cabecera); // ******************************
        if (pos == -1)
            return;  // error
        pos+=cabecera.length();
        int fin = linea.indexOf("*",pos);
        String res = linea.substring(pos, fin - 0);
        //DEBUG.log("mensaje_getOrigen","["+res.length()+"]"+res);
        origen = res;

        // getDestino

         pos = linea.indexOf("To:");
        if (pos == -1)
            return;  // error
        pos+=3;
         fin = linea.indexOf("*",pos);
         res = linea.substring(pos, fin - 0);
        //DEBUG.log("mensaje_getDestino","["+res.length()+"]"+res);
        destino = res;


        //getCOMANDO

         pos = linea.indexOf("Comando:");
        if (pos == -1)
            return;  // error
        pos+=8;
         fin = linea.indexOf("*",pos);
         res = linea.substring(pos, fin-0);
        //DEBUG.log("mensaje_getCOMANDO", "[" + res.length() + "]" + res);
        comando = res;

        //getEXTRA

        pos = linea.indexOf("Extra:");
        if (pos == -1)
            return;  // error
        pos+=6;
        fin = linea.length();
        res = linea.substring(pos, fin-0);
        //DEBUG.log("mensaje_getEXTRA", "[" + res.length() + "]" + res);
        extra = res;


    }



    public String toString () {
        String res = "From:"+origen+"*To:"+destino + "*Comando:" + comando + "*Extra:" + extra;
        //DEBUG.log("mensaje creado","["+res.length()+"]"+res);
        return res;
    }


    public String getComando() {
        return comando;
    }

    public String getDestino() {
        return destino;
    }

    public String getExtra() {
        return extra;
    }

    public String getOrigen() {
        return origen;
    }







//
//    public static String getMensajeAccion ( Accion ac, boolean on) {
//        if (on)
//            return ac.get_mensaje_ON() ;  //creaMensaje(ID, ac.get_mensaje_ON());  // ID
//        else
//            return ac.get_mensaje_OFF() ;  // creaMensaje(ID, ac.get_mensaje_OFF());  //ID
//    }


}
