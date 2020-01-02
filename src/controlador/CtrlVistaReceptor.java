/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import modelo.Receptor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import vista.VistaReceptor;

/**
 *
 * @author USUARIO
 */
public class CtrlVistaReceptor implements ActionListener {
    
    private VistaReceptor vista;
    private Receptor receptor;
    public static final ArrayList<Float[]> playMap = new ArrayList<Float[]>(); 
    
    public void iniciar() {
        this.vista = new VistaReceptor();
        this.vista.JBotonDefinir.addActionListener(this);
        this.vista.JBotonIniciar.addActionListener(this);
        this.vista.jButtonDetener.addActionListener(this);
        this.vista.JBotonIniciar.setEnabled(false);
        this.vista.jButtonDetener.setEnabled(false);
        this.vista.setSize(300, 150);
        this.vista.setTitle("Receptor");
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
        this.vista.pack();
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
    
    private JFreeChart createGraph(String cadena){
        
        setData(cadena);
        
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
    
    private void setData(String cadena){
       
        String bits = cadena;
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
    
    public void graficar(String cadena) {
        JFreeChart chart = createGraph(cadena);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
        vista.jInternalFrameGrafico.setContentPane(chartPanel);
        vista.pack();
    }
    
}
