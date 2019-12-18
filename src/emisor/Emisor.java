/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emisor;

import java.io.IOException;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class Emisor {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException { 
        System.out.println("Iniciando Emisor... ");
        System.out.println("!Emisor Listo!");
        String mensajeCadena = "";
        while (mensajeCadena.compareTo("salir")!= 0) {
            Scanner escaner = new Scanner(System.in);
            System.out.print("> ");
            mensajeCadena = escaner.nextLine();
            if (mensajeCadena.compareTo("salir") != 0) {
                Socket emisor = new Socket("localhost"/*"192.168.96.10"*/,4500);
                System.out.println("Enviando mensaje al Receptor... ");
                ObjectOutputStream mensaje = new ObjectOutputStream(emisor.getOutputStream());
                mensaje.writeObject(mensajeCadena);
                System.out.println("Mensaje enviado con éxito.");
                ObjectInputStream entrada = new ObjectInputStream(emisor.getInputStream());
                String respuesta = (String) entrada.readObject();
                System.out.println("ACK: " + respuesta);
                emisor.close();
            } else {
                System.out.println("Conexión Terminada.");
            }
        }
    }
    
}
