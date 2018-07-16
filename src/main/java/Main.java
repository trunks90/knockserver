/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author trunks
 * 
 */

import static spark.Spark.*;


public class Main {

 
    final static boolean HEROKU = true;



    
    static Chat chat;
    static SQL sql;

    public static void main(String[] args) {
        
        
        
        if (HEROKU)
            port(Integer.valueOf(System.getenv("PORT")));
        else
            port(8080);
        
    //staticFiles.location("/public"); //index.html is served at localhost:4567 (default port)
    //externalStaticFileLocation("/public"); //index.html is served at localhost:4567 (default port)
    
    
        staticFiles.expireTime(60*15);  // en segundos.  
        
        //response.redirect("test.html"); return null;
        
        
        
        
        
        //get("/hello", (req, res) -> "Hello World");
        
//        get("/helloworld", (req, res) -> 
//                
//                "Hello " + req.queryParams("name")
//        
//        );
        

//        port(8080);
//        get("/helloworld", (req, res) ->
//        {		
//                res.status(200);
//                res.type("application/json");
//                return "{message: 'Hello " + req.queryParams("name") + "'}";
//        });


        //webSocket("/status", StatusWebSocketHandler.class);

        //(new Chat()).main( new String[1]);

        chat = new Chat();


        sql = new SQL();

        
        
        
        //new StatusWebSocketHandler(chat,sql);
        
        
        init();
        

    }
    
    

    

/*
https://devcenter.heroku.com/articles/heroku-postgresql#connecting-in-java

private static Connection getConnection() throws URISyntaxException, SQLException {
    String dbUrl = System.getenv("JDBC_DATABASE_URL");
    return DriverManager.getConnection(dbUrl);
}

&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory
jdbc:postgresql://host:port/database?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory




*/

/*
 your pom.xml:


<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <version>9.4.1208</version>
</dependency>
*/


/*
https://devcenter.heroku.com/articles/heroku-postgresql#connecting-in-php
*/




}



