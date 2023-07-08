/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import javax.swing.JOptionPane;

/**
 *
 * @author diogo e eric
 */
public class Links {

//    public void OpenWebsite(String sitelink)
//    {
//        try {
//            Process P;
//            P = Runtime.getRuntime().exec("cmd /c start " + sitelink);
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(null, "Error: " + e);
//        }
//    }
    public static void abrirEnviarEmail() {
        String destinatario = "suporte.vaultsystems@gmail.com";
        String assunto = "Assunto do email";
        String corpo = "Corpo do email";

        try {
            String encodedDestinatario = URLEncoder.encode(destinatario, "UTF-8");
            String encodedAssunto = URLEncoder.encode(assunto, "UTF-8");
            String encodedCorpo = URLEncoder.encode(corpo, "UTF-8");

            String url = "https://mail.google.com/mail/?view=cm&to=" + encodedDestinatario
                    + "&su=" + encodedAssunto + "&body=" + encodedCorpo;
            URI uri = new URI(url);
            Desktop.getDesktop().browse(uri);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void abrirInstagram() {
        String perfilInstagram = "https://www.instagram.com/";
        try {
            URI uri = new URI(perfilInstagram);
            Desktop.getDesktop().browse(uri);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    
     public static void abrirPerfilFacebook() throws URISyntaxException {
        String perfilFacebook = "https://www.facebook.com/people/Vault-Systems-Brasil/100093192679133/";
        try {
            URI uri = new URI(perfilFacebook);
            Desktop.getDesktop().browse(uri);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
