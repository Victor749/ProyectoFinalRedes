/* PILAAAS: https://www.youtube.com/watch?v=m2dFjkZVlVw
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.lang.Math;

/**
 *
 * @author pablo
 */
public class Hamming2 {
    
    
    private  static String tramaLlegada;
    private  static byte[] bitsRedundanciaSalida;
    private  int cantidaBitsRedundancia;
    private  ArrayList<ArrayList<Integer>> indicesRedundantes = new ArrayList<ArrayList<Integer>>();
    private  final ArrayList<Boolean> indicesErroresPosibles = new ArrayList<Boolean>();
    
    
    
    public static void main(String[] args){
        
        byte[] arrayBits = {1,18,2,4,7,4,5,44,14,12,-8,41,-55,4};
        byte[] bitsRedundantes = {1,0,0,1,1};
        //byte[] arrayBits = {1,2,3,4,5,};
        /*String s1 = "";
        for(int i=0; i <arrayBits.length; i++){
            System.out.println("Hola: " + arrayBits[i]);
            s1 = s1 + String.format("%8s", Integer.toBinaryString(arrayBits[i] & 0xFF)).replace(' ', '0');
        }
        System.out.println("Hola 2: " + s1);*/
        
        //String tramaLlegada = convertirBytesToString(arrayBits, bitsRedundantes);
        
        Hamming2 hm = new Hamming2(arrayBits.length);
        bitsRedundanciaSalida = hm.introducirBitsRedundancia(arrayBits);
        bitsRedundanciaSalida[1] = 0;
        hm.convertirBytesToTramaLlegada(arrayBits, bitsRedundanciaSalida);
        byte[] informacionError = hm.detectarError();
        if(informacionError.length == 1){
            System.out.println("No hay error en la trama");
        }else if(informacionError.length == 2){
            System.out.println("El error esta en el bit de paridad: " + informacionError[1] );
        }else{
            System.out.println("El error esta en el byte: " + informacionError[1] + " en el bit: " + informacionError[2] );
        }
    }

    public Hamming2(int cantidadBytes) {
        
        int i=0;
        int totalBits = (cantidadBytes*8);
        while((totalBits)+1 >= Math.pow(2,i)-i ){
            i++;
        }
        System.out.println("Cantidad de bits de redundancia: " + i);
        this.cantidaBitsRedundancia = i;
        this.bitsRedundanciaSalida = new byte[cantidaBitsRedundancia];
    }
    
    
    
    public byte[] detectarError(){
        int paridad = 0;
        char[] arrayBits = tramaLlegada.toCharArray();
        
        int i = 1;
        while(i<=this.tramaLlegada.length()){
            ArrayList<Integer> indices = new ArrayList<Integer>();
            for(int j = i; j <=this.tramaLlegada.length(); j+=i){
                for(int k=0;k<i;k++){
                    
                    
                    if(j>this.tramaLlegada.length()){
                        System.out.println("heyyyy");
                        break;
                    }
                    System.out.println(j);
                    if(j-1!=i-1){
                        if(arrayBits[j-1]=='1'){
                            paridad++;
                        }else if(arrayBits[j-1]=='0'){
                        
                        }
                    }
                    indices.add(j);
                    j++;
                }
                
            }
            System.out.println("i: " +  i);
            indicesRedundantes.add(indices);
            if(String.valueOf(arrayBits[i-1]).equals(String.valueOf(paridad%2))){
                System.out.println("true " + paridad + " " + arrayBits[i-1]);
                indicesErroresPosibles.add(true);
            }else{
                System.out.println("false " + paridad + " " + arrayBits[i-1]);
                indicesErroresPosibles.add(false);
            }
            i=i*2;
            paridad = 0;
        }
        
        int cantidadRedundantes = indicesRedundantes.size() ;
        
        for(i=0; i<cantidadRedundantes; i++){
            if(indicesErroresPosibles.get(i) == true){
                for (Integer indiceRedundanteCorrecto :  indicesRedundantes.get(i)) {
                    System.out.println("indiceRedundanteCorrecto:" + indiceRedundanteCorrecto);
                    for(int j=0; j<cantidadRedundantes; j++){
                        if(indicesErroresPosibles.get(j) == false){
                            int sizeIndicesRedundates = indicesRedundantes.get(j).size();
                            for(int k = 0 ; k<sizeIndicesRedundates; k++){
                                if(indicesRedundantes.get(j).get(k) == indiceRedundanteCorrecto ){
                                    System.out.println("Se ha sacado este: "+indicesRedundantes.get(j).get(k));
                                    indicesRedundantes.get(j).set(k, -1);
                                }
                            }
                            
                        }
                    }
                }
                System.out.println("");
            }
        }
    
    ArrayList<Integer> sobrantes = new ArrayList<Integer>();     
    
    for(i=0;i<cantidadRedundantes;i++){
        if(indicesErroresPosibles.get(i) == false){
            for(Integer sobrante: indicesRedundantes.get(i) ){
                if(sobrante==-1){
                    System.out.println("pppp");
                }else{
                    System.out.println("hhhh: " +sobrante);
                    sobrantes.add(sobrante);
                    
                }
            }
        }
    }

    return byteAndBitInformation(elementoMasComun(sobrantes));
 
    }
    
    public byte[] byteAndBitInformation(int indice){
        if(indice == -1){
            byte[] respuesta = {0};
            return respuesta;
        }else{
            int restar=0;
            for(int i = 0; i<cantidaBitsRedundancia+1; i++){
                if(Math.pow(2,i)==indice){
                    byte[] respuesta = {1,(byte)indice};
                    return respuesta;
                }else if(Math.pow(2,i)<indice){
                    restar = i;
                }
            }
            indice = indice-(restar+1);
            int byteConError = (indice / 8);
            int bitConError = indice - (8 * byteConError);
            
            byte[] respuesta = {1,(byte)bitConError,(byte)byteConError};
            return respuesta;
        }
    }
    
    public Integer elementoMasComun(ArrayList<Integer> sobrantes){
        Map<Integer, Integer> map = new HashMap<>();

        for (Integer t : sobrantes) {
            System.out.println("El valor sobrante es: " + t);
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Entry<Integer, Integer> max = null;
        
        Integer maximoVeces = -1;
        Integer indiceError = -1;
        
        
        for (Entry<Integer, Integer> e : map.entrySet()) {
            System.out.println("El valor es: " + e.getValue());
            System.out.println("El entry set es: " + String.valueOf(map.entrySet()));
            if (max == null || e.getValue() > maximoVeces)
                maximoVeces = e.getValue();
                indiceError = e.getKey();
        }
        
        
        return indiceError;
    }
    
    public void convertirBytesToTramaLlegada(byte[] bytesArray, byte[] redundantBytes){
        
        String tramaTrabajando = "";
        for(int i=0; i <bytesArray.length; i++){
            System.out.println("Hola: " + bytesArray[i]);
            tramaTrabajando = tramaTrabajando + String.format("%8s", Integer.toBinaryString(bytesArray[i] & 0xFF)).replace(' ', '0');
        }
        System.out.println("Hola 2: " + tramaTrabajando);
        for(int i=0; i<redundantBytes.length; i++){
            System.out.println("Poniendo: " + String.valueOf(redundantBytes[i])+ " en la posicion " + String.valueOf(Math.pow(2,i)-1) );
            tramaTrabajando = insertString(tramaTrabajando, String.valueOf(redundantBytes[i]), ((int)Math.pow(2,i))-1);
        }
        System.out.println("Hola 3: " + tramaTrabajando);
        tramaLlegada = tramaTrabajando;
    }
    
    
    public byte[] introducirBitsRedundancia(byte[] tramaSalida){
        
        String tramaTrabajando = "";
        for(int i=0; i <tramaSalida.length; i++){
            System.out.println("TramaSalida: " + tramaSalida[i]);
            tramaTrabajando = tramaTrabajando + String.format("%8s", Integer.toBinaryString(tramaSalida[i] & 0xFF)).replace(' ', '0');
        }
        System.out.println("TramSalida 2: " + tramaTrabajando);
        byte[] auxiliarRedundantBits = new byte[cantidaBitsRedundancia];
        for(int i = 0; i<this.cantidaBitsRedundancia; i++ ){
            auxiliarRedundantBits[i] = 2;
        }
        for(int i=0; i<auxiliarRedundantBits.length; i++){
            System.out.println("Poniendo: " + String.valueOf(auxiliarRedundantBits[i])+ " en la posicion " + String.valueOf(Math.pow(2,i)-1) );
            tramaTrabajando = insertString(tramaTrabajando, String.valueOf(auxiliarRedundantBits[i]), ((int)Math.pow(2,i))-1);
        }
        System.out.println("Hola 3: " + tramaTrabajando);
        
        char[] arrayBits = tramaTrabajando.toCharArray();
        
        
        int i = 1;
        int indice = 0;
        int paridad = 0;
        while(i<=tramaTrabajando.length()){
            ArrayList<Integer> indices = new ArrayList<Integer>();
            for(int j = i; j <=tramaTrabajando.length(); j+=i){
                for(int k=0;k<i;k++){
                    
                    
                    if(j>tramaTrabajando.length()){
                        System.out.println("heyyyy");
                        break;
                    }
                    System.out.println(j);
                    if(j-1!=i-1){
                        if(arrayBits[j-1]=='1'){
                            paridad++;
                        }else if(arrayBits[j-1]=='0'){
                        
                        }
                    }
                    indices.add(j);
                    j++;
                }
            }
            System.out.println("i: " +  i);
            if(paridad%2 == 0){
                bitsRedundanciaSalida[indice]=0;
            }else{
                bitsRedundanciaSalida[indice]=1;
            }
            i=i*2;
            indice ++;
            paridad = 0;
        }
        for(int j=0; j<bitsRedundanciaSalida.length; j++){
            System.out.println("Bits Redundancia:" + bitsRedundanciaSalida[j] );
        }
        
        return bitsRedundanciaSalida;
        
    }
    
    public static String insertString( String originalString, String stringToBeInserted, int index) { 
  
        String newString = new String(); 
  
        for (int i = 0; i < originalString.length(); i++) { 
            
            if (i == index) { 
 
                newString += stringToBeInserted; 
            } 
            newString += originalString.charAt(i); 
        } 
  
        return newString; 
    }

    public String getTramaLlegada() {
        return tramaLlegada;
    }

    public void setTramaLlegada(String tramaLlegada) {
        this.tramaLlegada = tramaLlegada;
    }

    public byte[] getBitsRedundanciaSalida() {
        return bitsRedundanciaSalida;
    }
    
    
    
}
