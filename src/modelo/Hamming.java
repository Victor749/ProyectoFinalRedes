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

/**
 *
 * @author pablo
 */
public class Hamming {
    
    
    private String bits;
    private ArrayList<ArrayList<Integer>> indicesRedundantes = new ArrayList<ArrayList<Integer>>();
    private final ArrayList<Boolean> indicesErroresPosibles = new ArrayList<Boolean>();
    
    public static void main(String[] args){
        
        Hamming hm = new Hamming("011100101110");
        Integer indiceErrorDetectado = hm.detectarError();
        System.out.println("El error esta en el indice: " + indiceErrorDetectado );
        
    }

    public Hamming(String bits) {
        this.bits = bits;
    }
    
    public Integer detectarError(){
        int paridad = 0;
        char[] arrayBits = bits.toCharArray();
        
        int i = 1;
        while(i<=this.bits.length()){
            ArrayList<Integer> indices = new ArrayList<Integer>();
            for(int j = i; j <=this.bits.length(); j+=i){
                for(int k=0;k<i;k++){
                    
                    
                    if(j>this.bits.length()){
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
            System.out.println("");
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
    
    

    return elementoMasComun(sobrantes);
    
    
        
        
    }
    
    public Integer elementoMasComun(ArrayList<Integer> sobrantes){
        Map<Integer, Integer> map = new HashMap<>();

        for (Integer t : sobrantes) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Entry<Integer, Integer> max = null;

        for (Entry<Integer, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }
        
        return max.getKey();
        
    }
    
}
