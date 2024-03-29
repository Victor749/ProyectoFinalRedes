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
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import modelo.Emisor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import vista.VistaEmisor;

/**
 *
 * @author USUARIO
 */
public class CtrlVistaEmisor implements ActionListener {
    
    private VistaEmisor vista;
    private Emisor emisor;
    public static final ArrayList<Float[]> playMap = new ArrayList<Float[]>(); 
    
    public void iniciar() {
        this.vista = new VistaEmisor();
        this.vista.JBotonAbrir.addActionListener(this);
        this.vista.JBotonEnviar.addActionListener(this);
        this.vista.JBotonEnviar.setEnabled(false);
        this.vista.setSize(300, 150);
        this.vista.setTitle("Emisor");
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
        this.vista.pack();
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
                    emisor.entramar();
                    reportar(emisor.iniciarConexion());
                }
            } catch(IOException ex) {
                error("Error de Archivo: " + ex.getMessage());
            }
        } else {
            try {
                JFreeChart chart = createGraph();
                ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
                vista.jInternalFrameGrafico.setContentPane(chartPanel);
                vista.pack();
                reportar(emisor.emitir());
                if (emisor.getTramaActual() == emisor.getNumeroTramas()) {
                    this.vista.JBotonEnviar.setEnabled(false);
                    reportar(emisor.cerrarConexion());
                }
            } catch (IOException | ClassNotFoundException ex) {
                error("Error de Envío: " + ex.getMessage());
            }
        }
    }
    
    
    private JFreeChart createGraph(){
        setData();
        String playResults = "Play Results";
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries  data = new XYSeries("Trama");
        for (Float[] arregloFloats : playMap) {
            data.add(arregloFloats[0],arregloFloats[1]);  //(y,x)
            System.out.println("X: " + arregloFloats[0] + " Y: "+ arregloFloats[1] );
        }
        dataset.addSeries(data);
        JFreeChart chart = ChartFactory.createXYLineChart("Codificación Manchester", "bits", "V",  dataset);
        XYPlot  plot = (XYPlot)chart.getPlot();
        ValueAxis domainAxis = plot.getDomainAxis();
        ValueAxis rangeAxis = plot.getRangeAxis();
        domainAxis.setVisible(false);
        rangeAxis.setVisible(false);
        rangeAxis.setRange(-1, 1.5 );
        return chart;      
    }
    
    private void setData(){
        
        String bits = emisor.getCadenaTramaActual();
        char[] arrayBits = bits.toCharArray();
        
        playMap.removeAll(playMap);
        
        for(int i=0; i<bits.length(); i++){
            if(arrayBits[i]=='0'){
                Float[] array1 = {Float.valueOf(i)*10, 1f};
                playMap.add(array1);
                Float[] array2 = {Float.valueOf(i)*10+5, 1f};
                playMap.add(array2);
                Float[] array3 = {Float.valueOf(i)*10+5, 0f};
                playMap.add(array3);
                Float[] array4 = {Float.valueOf(i+1)*10, 0f};
                playMap.add(array4);
            }else if(arrayBits[i]=='1'){
                Float[] array1 = {Float.valueOf(i)*10, 0f};
                playMap.add(array1);
                Float[] array2 = {Float.valueOf(i)*10+5, 0f};
                playMap.add(array2);
                Float[] array3 = {Float.valueOf(i)*10+5, 1f};
                playMap.add(array3);
                Float[] array4 = {Float.valueOf(i+1)*10, 1f};
                playMap.add(array4);
            }else{
                playMap.removeAll(playMap);
                vista.pack();
            }
        }
        
    }
    
}
