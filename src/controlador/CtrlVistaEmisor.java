/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import modelo.Emisor;
import vista.VistaEmisor;

/**
 *
 * @author USUARIO
 */
public class CtrlVistaEmisor implements ActionListener {
    
    private VistaEmisor vista;
    private Emisor emisor;
    
    public void iniciar() {
        this.vista = new VistaEmisor();
        this.vista.JBotonAbrir.addActionListener(this);
        this.vista.JBotonEnviar.addActionListener(this);
        this.vista.JBotonEnviar.setEnabled(false);
        this.vista.setTitle("Emisor");
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
        this.emisor = new Emisor();
    }
    
    public void reportar(String trama) {
        this.vista.JTextLog.setText(this.vista.JTextLog.getText() + trama);
    }
    
    public void error(String ex) {
        JOptionPane.showMessageDialog(null, ex, "¡ADVERTENCIA!", JOptionPane.WARNING_MESSAGE);
    }
    
    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.JBotonAbrir) { 
            String texto;
            try {
                JFileChooser file = new JFileChooser();
                int returnVal = file.showOpenDialog(this.vista);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File abre = file.getSelectedFile();
                    String ruta = abre.getAbsolutePath();
                    this.vista.JTextRuta.setText(ruta);
                    texto = readFile(ruta, StandardCharsets.ISO_8859_1);
                    this.emisor.setTexto(texto);
                    this.vista.JBotonEnviar.setEnabled(true);
                }
            } catch(IOException ex) {
                error("Error de Archivo: " + ex.getMessage());
            }
        } else {
            try {
                reportar(emisor.iniciarConexion());
                reportar(emisor.emitir());
                reportar(emisor.cerrarConexion());
            } catch (IOException | ClassNotFoundException ex) {
                error("Error de Envío: " + ex.getMessage());
            }
        }
    }
    
}
