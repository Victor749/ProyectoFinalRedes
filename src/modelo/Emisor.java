/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.IOException;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Emisor {

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    public String iniciarConexion() {
        return "Iniciando Emisor...\n¡Emisor Listo para Transmitir!\n";
    }
     
    public String cerrarConexion() {
        return "Apagando Emisor...\n¡Emisor Apagado!\n";
    }
    
    public String emitir() throws IOException, ClassNotFoundException {
        String reporte;
        Socket emisor = new Socket("localhost"/*"192.168.96.10"*/, 4500);
        reporte = "Enviando mensaje al Receptor... " + '\n';
        ObjectOutputStream salida = new ObjectOutputStream(emisor.getOutputStream());
        salida.writeObject(texto);
        reporte += "Mensaje enviado con éxito." + '\n';
        ObjectInputStream entrada = new ObjectInputStream(emisor.getInputStream());
        String respuesta = (String) entrada.readObject();
        reporte += "ACK: " + respuesta + '\n';
        emisor.close();
        return reporte;
    }
    
    private String texto; 
    
}
