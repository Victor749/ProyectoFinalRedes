/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import modelo.Receptor;
import vista.VistaReceptor;

/**
 *
 * @author USUARIO
 */
public class CtrlVistaReceptor implements ActionListener {
    
    private VistaReceptor vista;
    private Receptor receptor;
    
    public void iniciar() {
        this.vista = new VistaReceptor();
        this.vista.JBotonDefinir.addActionListener(this);
        this.vista.JBotonIniciar.addActionListener(this);
        this.vista.jButtonDetener.addActionListener(this);
        this.vista.JBotonIniciar.setEnabled(false);
        this.vista.jButtonDetener.setEnabled(false);
        this.vista.setTitle("Receptor");
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
        this.receptor = new Receptor(this);
    }
    
    public void reportar(String trama) {
        this.vista.JTextLog.setText(this.vista.JTextLog.getText() + trama);
    }
    
    public void error(String ex) {
        JOptionPane.showMessageDialog(null, ex, "¡ADVERTENCIA!", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.JBotonDefinir) { 
            try {
                receptor.setTamanioBufer(Integer.parseInt(this.vista.JTextBuffer.getText()));
                this.vista.JBotonIniciar.setEnabled(true);
                this.vista.JTextBuffer.setEnabled(false);
                this.vista.JBotonDefinir.setEnabled(false);
            } catch(NumberFormatException ex) {
                error("Debe ingresarse un entero. Error: " + ex.getMessage());
            }
        } else if(e.getSource() == this.vista.JBotonIniciar)  {
            this.vista.JBotonIniciar.setEnabled(false);
            this.vista.jButtonDetener.setEnabled(true);
            reportar(receptor.iniciarConexion());
            receptor.start();
        } else {
            System.exit(0);
        }
    }
    
}
