/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receptor;

import java.io.IOException;
import java.net.ServerSocket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Receptor {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException{
        ServerSocket receptor = new ServerSocket(4500);
        System.out.println("Iniciando Receptor... ");
        System.out.println("!Receptor Listo y a la Espera!");
        while (true) {
                Socket clienteNuevo = receptor.accept();
                ObjectInputStream entrada = new ObjectInputStream(clienteNuevo.getInputStream());
                String mensaje = (String) entrada.readObject();
                System.out.println("Mensaje Recibido: " + mensaje);
                System.out.println("Enviando ACK al Receptor... ");
                ObjectOutputStream resp = new ObjectOutputStream(clienteNuevo.getOutputStream());
                resp.writeObject("OK");
                System.out.println("ACK enviado con éxito.");
                clienteNuevo.close();
        }
    }
    
}
