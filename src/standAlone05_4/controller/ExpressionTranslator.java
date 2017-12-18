/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package standAlone05_4.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;

/**
 *
 * @author MiRad
 */

public class ExpressionTranslator {
    
    public Hashtable<String,ArrayList<String>> storeReactionsAndEnzymes (String reactionDataFile) 
            throws FileNotFoundException, IOException{
        
        Hashtable<String,ArrayList<String>> reactionEnzyme = new Hashtable<String,ArrayList<String>>();
        ArrayList<String> txtData = new ArrayList<String>();
        
        //Read file and store in txtData as an ArrayList
        try (BufferedReader br = new BufferedReader(new FileReader(reactionDataFile))) {
            String line = "";
            
            while ((line = br.readLine()) != null) {
               txtData.add(line);
            }
        }
        txtData.remove(0);
        //System.out.println(txtData);
        
        ArrayList<String> temp = new ArrayList<String>();
        //Create a hashtable of the expressions
        for (String lineData:txtData){
            temp = new ArrayList<String>();
            for (String splitData:lineData.split("\t")){
                temp.add(splitData);
            }
            
            //Put data in the hashtable
            if (!temp.isEmpty()){
                reactionEnzyme.put(temp.get(2), temp);
            }
        }
        //System.out.println(reactionEnzyme.get("R02189"));
        return reactionEnzyme;
    }
    
    public Hashtable<String,ArrayList<String>> readTheExpressions (String expressionFile, Hashtable<String,
            ArrayList<String>> reactionEnzyme) throws FileNotFoundException, IOException{
        Hashtable<String,ArrayList<String>> enzymeExpressions = new Hashtable<String,ArrayList<String>>();
        ArrayList<String> txtData = new ArrayList<String>();
        
        //Read file and store in txtData as an ArrayList
        try (BufferedReader br = new BufferedReader(new FileReader(expressionFile))) {
            String line = "";
            
            while ((line = br.readLine()) != null) {
               txtData.add(line);
            }
        }
        txtData.remove(0);
        
        for (String reaction:reactionEnzyme.keySet()){
            ArrayList<String> enzymeTemp = new ArrayList<String>();
            //Three genes are enough
            enzymeTemp.add("0.0");
            enzymeTemp.add("0.0");
            enzymeTemp.add("0.0");
            
            for (String txtLine:txtData){
                ArrayList<String> temp = new ArrayList<String>();
                for (String splited:txtLine.split("\t")){
                    temp.add(splited);
                }
                //System.out.println(temp);
                for (int i = 0 ; i < 3 ; i++){
                    if (reactionEnzyme.get(reaction).size() >= (8+i*3)){
                        if (temp.get(0).equals(reactionEnzyme.get(reaction).get(8+i*3))){
                            enzymeTemp.set(i,temp.get(4));
                        }
                    }
                }
            }
            enzymeExpressions.put(reaction, enzymeTemp);
        }
        //System.out.println(enzymeExpressions);
        
        return enzymeExpressions;
    }
    
    public Hashtable<String,ArrayList<Integer>> sortEnzymes(Hashtable<String,ArrayList<String>> enzymeExpressions){
        Hashtable<String,ArrayList<Integer>> indices = new Hashtable<String,ArrayList<Integer>>();
        ArrayList<Integer> indexList;
        Hashtable<Integer,Double> indexListHash;
        ArrayList<Double> tempForDouble;
        
        for (String reaction:enzymeExpressions.keySet()){
            indexList = new ArrayList<Integer>();
            indexListHash = new Hashtable<Integer,Double>();
            tempForDouble = new ArrayList<Double>();
            
            //Put the expressions in a list of double for the sort
            for (String expressString:enzymeExpressions.get(reaction)){
                tempForDouble.add(Double.parseDouble(expressString));
            }
            
            //Creat a hashtable for the indices
            for (int i = 0 ; i < tempForDouble.size() ; i++){
                indexListHash.put(i,tempForDouble.get(i));
            }
            //System.out.println(reaction);
            //System.out.println(indexListHash);
                    
            Collections.sort(tempForDouble);
            Collections.reverse(tempForDouble);
            
            /*if (reaction.equals("R00959")){
                System.out.println("to test:");
                System.out.println(tempForDouble);
            }*/
            
            for (Double express:tempForDouble){
                for (Integer key:indexListHash.keySet()){
                    if (indexListHash.get(key).equals(express) && !indexList.contains(key)){
                        indexList.add(key);
                        break;
                    }
                }
            }
            
            indices.put(reaction, indexList);
        }
        
        return indices;
    }
}
