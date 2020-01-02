/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;

/**
 *
 * @author USUARIO
 */
public class Trama implements Serializable {

    public Trama(int numero) {
        this.numero = numero;
        this.ultimo = false;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getTramaBin() {
        return tramaBin;
    }

    public void setTramaBin(String tramaBin) {
        this.tramaBin = tramaBin;
    }

    public String getTrama() {
        return trama;
    }

    public void setTrama(String trama) {
        this.trama = trama;
    }
    
    public boolean isUltimo() {
        return ultimo;
    }

    public void setUltimo(boolean ultimo) {
        this.ultimo = ultimo;
    }
    
    private int numero;
    private String tramaBin;
    private String trama;
    private boolean ultimo;
    
}
