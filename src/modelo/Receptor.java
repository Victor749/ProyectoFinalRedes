/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controlador.CtrlVistaReceptor;
import java.io.IOException;
import java.net.ServerSocket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Receptor extends Thread {
    
    public Receptor(CtrlVistaReceptor ctrl) {
        this.ctrl = ctrl;
    }

    public int getTamanioBufer() {
        return tamanioBufer;
    }

    public void setTamanioBufer(int tamanioBufer) {
        this.tamanioBufer = tamanioBufer;
    }
    
    public String iniciarConexion() {
        mensaje = "";
        return "¡Receptor Listo y a la Espera!\n\n";
    }
    
    public String cerrarConexion() {
        return "Archivo Recibido: " + mensaje + "\n\n";
    }
    
    public void recibir(String trama) {
        ctrl.reportar(trama);
    }
    
    public void reportarError(String ex) {
        ctrl.error(ex);
    }
    
    @Override
    public void run() {
        try {
            ServerSocket receptor = new ServerSocket(4500);
            while (true) {
                Socket clienteNuevo = receptor.accept();
                ObjectInputStream entrada = new ObjectInputStream(clienteNuevo.getInputStream());
                Trama trama = (Trama) entrada.readObject();
                mensaje += trama.getTrama();
                ctrl.graficar(trama.getTramaBin());
                String reporte = "Trama " + trama.getNumero() + " Recibida: Cadena (" + trama.getTrama() + ") ASCII: "  + trama.getTramaBin() + '\n';
                reporte += "Enviando ACK al Emisor... " + '\n';
                ObjectOutputStream salida = new ObjectOutputStream(clienteNuevo.getOutputStream());
                salida.writeObject("OK: Trama " + trama.getNumero());
                reporte += "ACK enviado con éxito." + "\n\n";
                recibir(reporte);
                if (trama.isUltimo()) {
                    ctrl.reportar(cerrarConexion());
                    ctrl.reportar(iniciarConexion());
                }
                clienteNuevo.close(); 
            }
        } catch(IOException | ClassNotFoundException ex) {
            reportarError("Error de Recepción: " + ex.getMessage());
        }
    }
    
    private int tamanioBufer;
    private String mensaje;
    private final CtrlVistaReceptor ctrl;
    
}
