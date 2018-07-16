
import static spark.Spark.get;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import spark.Request;

import org.json.JSONArray;
import org.json.JSONObject;


/*


 */

/**
 *
 * @author trunks
 */
public class SQL {

    

    static final String db_table = "mi_tabla";


    static final String LOCAL_db_driver = "com.mysql.jdbc.Driver";  // mySQL   local
    static final String LOCAL_db_URL = "jdbc:mysql://localhost:3306/knock"; // mySQL   local
    static final String LOCAL_db_user = "root";
    static final String LOCAL_db_pass = "";
    
    
    static final String HEROKU_db_driver = "org.postgresql.Driver";
    static final String HEROKU_db_URL = 
           "jdbc:postgresql://"
            + "ec2-54-217-222-254.eu-west-1.compute.amazonaws.com"
            + ":5432"
            + "/dt1tfd7v2s7id";
    
    static final String HEROKU_db_user = "uulaniunkdzwtw";
    static final String HEROKU_db_pass = "3199c15a64225143c69ca54063184a479725f601cd04748ab74debfa967bc3ce";

    
    
    //$_GET['op']);
    static final String key_operacion = "op";
    static final String op_read_tel = "read_tel";
    static final String op_modify = "modify";
    static final String op_list = "list";
    static final String op_create = "create";
    
    static final String campo_telefono = "telefono";
    static final String campo_nombre = "nombre";
    static final String campo_ip = "ip";
    static final String campo_port = "port";



    static MysqlConnect mysqlConnect;
    int rs_legth;
    
    
    public SQL() {
        
        
        get("/hi", (path, res) ->           //   http://localhost:8080/hi?uno=1&dos=rrr
                    //path.attributes()
               // "  h"+path.headers()
               // +"  p"+path.params()
                "  pi:"+path.pathInfo()  // /hi
                +"  qs:"+path.queryString()  //uno=1&dos=rrr
                +"  qpUNO:"+path.queryParams("uno")  //<---------------
                +"  qpDOS:"+path.queryParams("dos")

        );  
            
        
        get("/SQL", (path, res) ->            //hi(req)
                    con( path.queryParams(key_operacion),
                            path
                        )
                    );  
        
//  http://localhost:8080/SQL?op=list
    
//  http://knockserver.herokuapp.com/SQL?op=list
//  http://knockserver.herokuapp.com/SQL?op=modify&telefono=695497nngo&nombre=%22daviduxxxxxx%22&ip=%2211.22.33.44%22&port=%2255%22
    

        
        
        
//        get("/helloworld", (req, res) ->
//        {		
//                res.status(200);
//                res.type("application/json");
//                return "{message: 'Hello " + req.queryParams("name") + "'}";
//        });
        
        // !_ note _! this is just init
// it will not create a connection
        mysqlConnect = new MysqlConnect();
        
        
    }
    
    
    private String con(String op, Request params) {
        
        String resultado = null;
        String sql;
        
        //http://localhost/sql-script.php?op=list
        
        //[{"telefono":"010101","nombre":"servidorJAVA_5020","ip":"95.20.253.134","port":"5020"},{"telefono":"010102020303","nombre":"ServidorJava LAN","ip":"192.168.0.117","port":"5020"},{"telefono":"0123","nombre":"yomismo","ip":"83.54.250.244","port":"0"},{"telefono":"695497998","nombre":"David","ip":"88.5.78.50","port":"0"},{"telefono":"687262170","nombre":"Victor_m","ip":"82.213.168.32","port":"0"},{"telefono":"441","nombre":"rrr","ip":"","port":"5020"},{"telefono":"625816536","nombre":"vic","ip":"85.219.93.226","port":"0"},{"telefono":"695","nombre":"nfffff","ip":"83.54.250.244","port":"0"},{"telefono":"00666","nombre":"nnnnnnnnvvv","ip":"83.54.250.244","port":"0"},{"telefono":"000885","nombre":"nnnncff","ip":"83.54.250.244","port":"48702"},{"telefono":"617796850","nombre":"Javier","ip":"88.8.20.222","port":"0"},{"telefono":"687087376","nombre":"Eduardo","ip":"31.4.184.72","port":"0"},{"telefono":"61779850","nombre":"Javier","ip":"88.5.78.50","port":"0"}]
        
        //http://localhost:8080/SQL?op=list
        //http://localhost:8080/SQL?op=read_tel
        //http://localhost:8080/SQL?op=modify
        
        
        Connection cc = mysqlConnect.connect();
        Statement statement;
        try {
            statement = cc.createStatement();
        } catch (SQLException ex) {
            return "ERROR creando Statement";  //  xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        }
        
        
        switch (op) {
            //--------------------------------------------------    
            case op_read_tel:
                //http://localhost/sql-script.php?op=read_tel&telefono=010101
                //[{"telefono":"010101","nombre":"servidorJAVA_5020","ip":"95.20.253.134","port":"5020"}]
                
                resultado = read_tel(statement,params);
                if (rs_legth == 0) {
                    resultado = "ERROR read_tel : no se ha encontrado telefono.";
                }
                
                break;
            //--------------------------------------------------    
            case op_modify:
                //localhost:8080/SQL?op=modify&telefono=695497998&nombre="david"&ip="11.22.33.44"&port="55"
                //[{"telefono":"010101","nombre":"servidorJAVA_5020","ip":"95.20.253.134","port":"5020"}]
                
                
//        ResultSet uprs = stmt.executeQuery(
//            "SELECT * FROM " + dbName + ".COFFEES");
//
//        while (uprs.next()) {
//            float f = uprs.getFloat("PRICE");
//            uprs.updateFloat( "PRICE", f * percentage);
//            uprs.updateRow();                
                
                
                
                String telefono = sin_comillas(params.queryParams(campo_telefono));
                String nombre = sin_comillas(params.queryParams(campo_nombre));
                String ip = sin_comillas(params.queryParams(campo_ip));
                String port = sin_comillas(params.queryParams(campo_port));
                
                resultado = read_tel(statement,params);
                if (rs_legth == 0) {
                    // insertar
//                    $sql    = "INSERT INTO " . $db_table . " (telefono,nombre,ip,port) VALUES ('"
//			. $tel . "','"  . $nom . "','"  . $ip .  "','"  . $port . "')";

                    sql = "INSERT INTO "+db_table+" (telefono,nombre,ip,port) VALUES ('"
                            +telefono+"','"
                            +nombre+"','"
                            +ip+"','"
                            +port+"')"                            
                            ;

                    try {
                        //Statement statement = cc.createStatement();
                        //    ... go on ...

                        //statement.execute();
                        boolean rs = statement.execute(sql);
                        resultado =  "INSERT : "+(rs?"fallo":"ok");

                    } catch (SQLException ex) {
                        resultado =  "ERROR en SELECT op_modify insertar :"+sql;
                    }

                    //return resultado;
                    
                    
                } else
                if (rs_legth == 1) {
                    // modificar
//			$sql    = "UPDATE " . $db_table . " SET telefono='" . $tel . 
//			"',nombre='" . $nom . "',ip='" . $ip . "',port='"  . $port ."' WHERE telefono='" . $tel . "'";
                    sql = "UPDATE "+db_table
                            +" SET telefono='"+telefono
                            +"',nombre='"+nombre
                            +"',ip='"+ip
                            +"',port='"+port   
                            +"' WHERE telefono='"+telefono+"'";
                            ;

                    try {
                        //PreparedStatement statement = cc.prepareStatement(sql);
                        //    ... go on ...

                        //statement.execute();
                        boolean rs = statement.execute(sql);
                        resultado =  "UPDATE : "+(rs?"fallo":"ok");

                    } catch (SQLException ex) {
                        resultado =  "ERROR en SELECT op_modify modificar :"+sql;
                    }

                    //return resultado;
                                        
                    
                    
                } else {
                    //error
                    resultado = "ERROR: mas de una coincidencia. BD corrupta.";
                }
                    
                
                break;

            //--------------------------------------------------    
            case op_list:
                
                sql = "SELECT * FROM "+db_table;//+" WHERE telefono";
        
                try {
                    //PreparedStatement statement = cc.prepareStatement(sql);
                    //    ... go on ...

                    //statement.execute();
                    ResultSet rs = statement.executeQuery(sql);
                    resultado =  parse(rs);
                    
                } catch (SQLException ex) {
                    resultado =  "ERROR en SELECT op_list : "+sql;
                }

                
                break;

            //--------------------------------------------------    
            case op_create:
                
                sql = 
                        "CREATE TABLE mi_tabla (\n" +
                        "    telefono     varchar(12),\n" +
                        "    nombre     varchar(40),\n" +
                        "    ip     varchar(16),\n" +
                        "    port     varchar(3),\n" +
                        "    PRIMARY KEY(telefono)\n" +
                        ");";
                       
        
                try {
                    //PreparedStatement statement = cc.prepareStatement(sql);
                    //    ... go on ...

                    //statement.execute();
                    int rs = statement.executeUpdate(sql);
                    resultado =  "resultado CREATE = "+rs;
                    
                } catch (SQLException ex) {
                    resultado =  "ERROR en CREATE table : "+sql;
                }

                
                break;
                
                
//                
//                CREATE TABLE mi_tabla1 (
//    telefono     varchar(12),
//    nombre     varchar(12),
//    ip     varchar(12),
//    port     varchar(12),
//    PRIMARY KEY(telefono)
//);
                
                
                
                
            default:
                resultado = "** Operacion no permitida : "+op;
                
    }
        

       
  
            mysqlConnect.disconnect();
        
        if (resultado == null) 
            resultado = "ERROR";
        
        return resultado;
    }
    
    
    
    
    private String read_tel( Statement sta ,Request params) {
        
        String resultado;
        String sql;
        String telefono = params.queryParams(campo_telefono);
        if (telefono.length() == 0 )
            resultado = "ERROR: No se haespecificado ningun telefono.";

//        if (Main.HEROKU) {
            if (telefono.contains(",")) {
                StringBuffer s = new StringBuffer("SELECT * FROM "+db_table+" WHERE telefono = '"); //+telefono+"'");
                String[] partes = telefono.split(",");
                s.append(partes[0]+"'");
                for (int i=1; i<partes.length; i++) {
                    s.append(" OR telefono = '"+partes[i]+"'");
                }
                sql = s.toString();
            } else {
                sql = "SELECT * FROM "+db_table+" WHERE telefono = '"+telefono+"'";
            }
//        } else {
//                sql = "SELECT * FROM "+db_table+" WHERE telefono = '"+telefono+"'";
//                }
        
        try {
            //PreparedStatement statement = co.prepareStatement(sql);
            //    ... go on ...

            //statement.execute();
            //System.out.println(sql);
            ResultSet rs = sta.executeQuery(sql);
            resultado =  parse(rs);

        } catch (SQLException ex) {
            resultado =  "ERROR en SELECT op_read_tel : "+sql;
        }

        return resultado;
    }
    
    
    /**
     * 
     * @param rs
     * @return 
     * 
     * http://localhost/sql-script.php?op=list
     * //[{"telefono":"010101","nombre":"servidorJAVA_5020","ip":"95.20.253.134","port":"5020"},{"telefono":"010102020303","nombre":"ServidorJava LAN","ip":"192.168.0.117","port":"5020"},{"telefono":"0123","nombre":"yomismo","ip":"83.54.250.244","port":"0"},{"telefono":"695497998","nombre":"David","ip":"88.5.78.50","port":"0"},{"telefono":"687262170","nombre":"Victor_m","ip":"82.213.168.32","port":"0"},{"telefono":"441","nombre":"rrr","ip":"","port":"5020"},{"telefono":"625816536","nombre":"vic","ip":"85.219.93.226","port":"0"},{"telefono":"695","nombre":"nfffff","ip":"83.54.250.244","port":"0"},{"telefono":"00666","nombre":"nnnnnnnnvvv","ip":"83.54.250.244","port":"0"},{"telefono":"000885","nombre":"nnnncff","ip":"83.54.250.244","port":"48702"},{"telefono":"617796850","nombre":"Javier","ip":"88.8.20.222","port":"0"},{"telefono":"687087376","nombre":"Eduardo","ip":"31.4.184.72","port":"0"},{"telefono":"61779850","nombre":"Javier","ip":"88.5.78.50","port":"0"}]
     */
    private String parse (ResultSet rs)     {
        
        StringBuilder s = new StringBuilder();
        try {

               // while (rs.next()) {
                    //s.append(rs.getRow()+",");
                    // telefono 	nombre 	ip 	port
//                    String t = rs.getString(campo_telefono);
//                    String n = rs.getString(campo_nombre);
//                    String i = rs.getString(campo_ip);
//                    String p = rs.getString(campo_port);
//                    s.append(t+","+n+","+i+","+p+"; ");
                    
                    JSONArray jsonArray = new JSONArray();
                    rs_legth = 0;
                    while (rs.next()) {
                        int total_rows = rs.getMetaData().getColumnCount();
                        JSONObject obj = new JSONObject();
                        for (int i = 0; i < total_rows; i++) {
                            obj.put(rs.getMetaData().getColumnLabel(i + 1).toLowerCase(), rs.getObject(i + 1));
                        }
                      jsonArray.put(obj);
                      rs_legth++;
                    }

           
//                    JSONObject j = new JSONObject();
//                    j.put("arrayName",jsonArray);
        
                    s.append(jsonArray.toString());
        
        
              //  }

                //return s.toString();

            } catch (Exception e) {
                    s.append("ERROR");
            }
         return s.toString();
    }

    
    private String sin_comillas (String cadena) {
        if (cadena.length() == 0) 
            return cadena;
        if (cadena.charAt(0) == (char)34) {
            return cadena.substring(1,cadena.length()-1);
        }
        return cadena;
    }
    
    
}


//-----------------------------------------------------------------------------

class MysqlConnect {

    private static String DATABASE_DRIVER ;
    private static  String DATABASE_URL;

    private static  String USERNAME;
    private static  String PASSWORD;

    
    
    private static final String MAX_POOL = "250";

    // init connection object
    private Connection connection;
    // init properties object
    private Properties properties;

    // create properties
    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
            properties.setProperty("MaxPooledStatements", MAX_POOL);
        }
        return properties;
    }

    // connect database
    public Connection connect() {
        
        DATABASE_DRIVER = Main.HEROKU?SQL.HEROKU_db_driver:SQL.LOCAL_db_driver;
        DATABASE_URL = Main.HEROKU?SQL.HEROKU_db_URL:SQL.LOCAL_db_URL;
        USERNAME = Main.HEROKU?SQL.HEROKU_db_user:SQL.LOCAL_db_user;
        PASSWORD = Main.HEROKU?SQL.HEROKU_db_pass:SQL.LOCAL_db_pass;
        
        if (connection == null) {
            try {
                Class.forName(DATABASE_DRIVER);
                connection = DriverManager.getConnection(DATABASE_URL, getProperties());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    // disconnect database
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}



/*

$db_host = "localhost";
$db_uid = "root";
$db_pass = "";
$db_name = "knock";
$db_table = "mi_tabla";
        
        

$db_con = mysql_connect($db_host, $db_uid, $db_pass) or die('could not connect');
if (!$db_con) {
    echo "No pudo conectarse a la BD: " . mysql_error();
    exit;
}

if (!mysql_select_db($db_name)) {
    echo "No ha sido posible seleccionar la BD: " . mysql_error();
    exit;
}


$op = mysql_real_escape_string($_GET['op']);

switch ($op) {
	case "read_tel":
		$tel = mysql_real_escape_string($_GET['telefono']);

		$array = explode(',', $tel);
		$telefonos = implode("' OR telefono='", $array);
		//print ($telefonos);


		$sql    = "SELECT * FROM " . $db_table . " WHERE telefono = '" . $telefonos . "'";
		$result = mysql_query($sql) or die(mysql_error());
		if (!$result) {
			echo "No se pudo ejecutar con exito la consulta ($sql) en la BD: " . mysql_error();
			exit;
		}
		if (mysql_num_rows($result) == 0) {
			echo "No se han encontrado filas, nada a imprimir, asi que voy a detenerme.". mysql_error();
			exit;
		}

		//$fila = mysql_fetch_assoc($result);
		//$output[] = $fila;
		//print(json_encode($output));

		while ($row = mysql_fetch_assoc($result))
			$output[] = $row;
		print(json_encode($output));



		break;

	case "modify":
		$tel = mysql_real_escape_string($_GET['telefono']);
		$sql    = "SELECT * FROM " . $db_table . " WHERE telefono = '" . $tel . "'";
		$result = mysql_query($sql) or die(mysql_error());
		if (!$result) {
			echo "No se pudo ejecutar con exito la consulta ($sql) en la BD: " . mysql_error();
			exit;
		}

		// comun
		$nom = "";
		if (isset($_GET['nombre']))
			{
			$nom = mysql_real_escape_string($_GET['nombre']) ;
			}
		$ip =  mysql_real_escape_string($_GET['ip']) ;
		$port =  mysql_real_escape_string($_GET['port']) ;

		if (mysql_num_rows($result) == 0) {
			// insertar
			$sql    = "INSERT INTO " . $db_table . " (telefono,nombre,ip,port) VALUES ('"
			. $tel . "','"  . $nom . "','"  . $ip .  "','"  . $port . "')";
		}
		else
		{
			// modificar
			//print("mod");
			$sql    = "UPDATE " . $db_table . " SET telefono='" . $tel .
			"',nombre='" . $nom . "',ip='" . $ip . "',port='"  . $port ."' WHERE telefono='" . $tel . "'";
		}

		// echo $sql;
		$result = mysql_query($sql) or die(mysql_error());
		if (!$result) {
			echo "Error el insertar/modificar en la BD: " . mysql_error();
			exit;
		}
		else
		{
			//echo "Insercion/modificacion en BD correcta.";
		}

		break;


	case "list":
		$sql    = "SELECT * FROM " . $db_table . " WHERE telefono" ;
		$result = mysql_query($sql) or die(mysql_error());
		//mysqli_set_charset($resul, 'utf8');
		while ($row = mysql_fetch_assoc($result))
			$output[] = $row;
		print(json_encode($output));
		break;




	default:
		echo "Operacion no permitida". mysql_error();
}


mysql_close();

*/
