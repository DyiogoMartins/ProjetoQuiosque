/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import java.sql.DriverManager;

/**
 *
 * @author diogo and eric
 */
public class ConnectionClass {
    
         public static java.sql.Connection conector() {
        java.sql.Connection conexao = null;
        try {
            // The newInstance() call is a work around for some
            // broken Java implementationsjava.sql.ConnectionClass conexao = null;
            //linha abaixo e o jar do mysql
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // linha abaixo e conexao ao bd. dbquiosque = nome do banco no aplicativo mysql
            // obs: mudar a senha do root do bd e pelo cmd
            // link de ajuda: 
            conexao = DriverManager.getConnection("caminho do bd", 
                    "nome login", "senha");
            return conexao;
            
        } catch (Exception ex) {

            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex);
            System.out.println("VendorError: " + ex);

            return null;

        }

    }
    
}
