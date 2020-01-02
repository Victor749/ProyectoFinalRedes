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
        return "Emisor Listo para Transmitir Archivo con " + getNumeroTramas() + " tramas.\n\n";
    }
     
    public String cerrarConexion() {
        return "\n¡Archivo Enviado!\n\n";
    }
    
    public String codificar(String trama) {
        String tramaBin = "";
        for (int i = 0; i < trama.length(); i++) {
            int charASCII = (int) trama.charAt(i);
            tramaBin += binario(charASCII, 8);    
        }
        return tramaBin;
    }
    
    private String binario(int num, int bits) {
        String bin;
        bin = Integer.toBinaryString(num);
        int len = bin.length();
        for (int i = len; i < bits; i++) {
            bin = "0" + bin;
        }
        return bin;
    }
    
    public void entramar() {
        tramaActual = 0;
        int tamanioMensaje = texto.length();
        mensaje = new Trama[tamanioMensaje];
        for (int i = 0; i < tamanioMensaje; i++) {
            Trama trama = new Trama(i + 1);
            trama.setTrama(String.valueOf(texto.charAt(i)));
            trama.setTramaBin(codificar(trama.getTrama()));
            if (i == tamanioMensaje - 1) {
                trama.setUltimo(true);
            }
            mensaje[i] = trama;
        }
    }
    
    public int getNumeroTramas() {
        return mensaje.length;
    }
    
    public int getTramaActual() {
        return tramaActual;
    }
    
    public String getCadenaTramaActual() {
        return mensaje[tramaActual].getTramaBin();
    }
            
    
    public String emitir() throws IOException, ClassNotFoundException {
        String reporte;
        Socket emisor = new Socket("localhost"/*"192.168.96.10"*/, 4500);
        reporte = "Enviando Trama " + String.valueOf(tramaActual + 1) + " ... " + '\n';
        ObjectOutputStream salida = new ObjectOutputStream(emisor.getOutputStream());
        salida.writeObject(mensaje[tramaActual]);
        reporte += "Trama enviada con éxito." + '\n';
        ObjectInputStream entrada = new ObjectInputStream(emisor.getInputStream());
        String respuesta = (String) entrada.readObject();
        reporte += "ACK: " + respuesta + "\n\n";
        emisor.close();
        tramaActual++;
        return reporte;
    }
    
    private String texto; 
    private Trama[] mensaje;
    private int tramaActual;
    
}
