/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package standAlone05_4.controller;

import java.util.ArrayList;
import java.util.Hashtable;

// What do we need for feeding? the ingredients, the three meals, and the number of steps in each meal, and the weight of the subject
// The output of this class is to generate a list of the proper feeding arrays

public class Feeding {
	static ArrayList<ArrayList<String>> processedFoodData;
	
    public ArrayList<ArrayList<String>> feedingProcessedData(String feedingInput){
    	processedFoodData = new ArrayList<ArrayList<String>>();
    	
    	parseFood(feedingInput);
    	
    	return processedFoodData;
    }
    
    //Parse the feeding information
    public void parseFood (String in){
    	ArrayList<ArrayList<String>> foodData = new ArrayList<ArrayList<String>>();
    	
    	for (String section : in.split(";")){
    		ArrayList<String> temp = new ArrayList<String>();
    		for (String subsection : section.split(",")){
    			temp.add(subsection);
    		}
    		processedFoodData.add(temp);
    	}
    	//System.out.println(processedFoodData);
    }
    
    public Hashtable<String,ArrayList<Double>> applyFood (Hashtable<String,ArrayList<Double>> currentConcentrations){
    	
    	Hashtable<String,ArrayList<Double>> newConcentrations = new Hashtable<String,ArrayList<Double>>();
    	// List of the metabolites receiving food
    	ArrayList<ArrayList<String>> listOfFoodReceivers = new ArrayList<ArrayList<String>>();
    	ArrayList<String> temp;
    	
    	if (Double.parseDouble(processedFoodData.get(2).get(0)) != 0){
    		
	    	// Protein, Lipid, Carb, Sugar
	    	
	    	//Carb
	    	temp = new ArrayList<String>();
	    	temp.add("C00267");
	    	listOfFoodReceivers.add(temp);
	    	
	    	//Protein
	    	temp = new ArrayList<String>();
	    	temp.add("C00065");
	    	temp.add("C00183");
	    	temp.add("C00407");
	    	temp.add("C00047");
	    	temp.add("C00082");
	    	listOfFoodReceivers.add(temp);
	    	
	    	//Molar Calculation (7% of boly is blood)
	        double blood = Double.parseDouble(processedFoodData.get(2).get(0))*0.07/2;          // Liters
	        //System.out.println(blood);
	        
	        // Added Carbs in Molar added to the concentration
	        Double M3 = Double.parseDouble(processedFoodData.get(0).get(2))/Double.parseDouble(processedFoodData.get(1).get(0));
	        double C1 = M3/(180.16*blood);
	        
	        //Add to the current values
	        int currentValueIndex = currentConcentrations.get(listOfFoodReceivers.get(0).get(0)).size()-1;
	        double newValue = currentConcentrations.get(listOfFoodReceivers.get(0).get(0)).get(currentValueIndex) + C1;
	        currentConcentrations.get(listOfFoodReceivers.get(0).get(0)).set(currentValueIndex, newValue);
	        
	        
	        // Carb (Glucose is 180.16 g/mole and we affect alpha-D-glucose C00267)
	        // Protein (Devided between 5 amino acids of Serine 105.09 and C00065, 
	        // Valine 117.15 and C00183, Isolucine 131.18 and C00407, Lysine 146.19
	        // and C00047, Tyrosine 181.19 and C00082)
	        
	        // Added Proteins in Molar added to the concentration
	        Double M1 = Double.parseDouble(processedFoodData.get(0).get(0))/Double.parseDouble(processedFoodData.get(1).get(0));
	        double P1 = M1/(5*105.09*blood);
	        double P2 = M1/(5*117.15*blood);
	        double P3 = M1/(5*131.18*blood);
	        double P4 = M1/(5*146.19*blood);
	        double P5 = M1/(5*181.19*blood);
	        
	        //Add to the current values
	        currentValueIndex = currentConcentrations.get(listOfFoodReceivers.get(1).get(0)).size()-1;
	        newValue = currentConcentrations.get(listOfFoodReceivers.get(1).get(0)).get(currentValueIndex) + P1;
	        currentConcentrations.get(listOfFoodReceivers.get(1).get(0)).set(currentValueIndex, newValue);
	        
	        currentValueIndex = currentConcentrations.get(listOfFoodReceivers.get(1).get(1)).size()-1;
	        newValue = currentConcentrations.get(listOfFoodReceivers.get(1).get(1)).get(currentValueIndex) + P2;
	        currentConcentrations.get(listOfFoodReceivers.get(1).get(1)).set(currentValueIndex, newValue);
	        
	        currentValueIndex = currentConcentrations.get(listOfFoodReceivers.get(1).get(2)).size()-1;
	        newValue = currentConcentrations.get(listOfFoodReceivers.get(1).get(2)).get(currentValueIndex) + P3;
	        currentConcentrations.get(listOfFoodReceivers.get(1).get(2)).set(currentValueIndex, newValue);
	        
	        currentValueIndex = currentConcentrations.get(listOfFoodReceivers.get(1).get(3)).size()-1;
	        newValue = currentConcentrations.get(listOfFoodReceivers.get(1).get(3)).get(currentValueIndex) + P4;
	        currentConcentrations.get(listOfFoodReceivers.get(1).get(3)).set(currentValueIndex, newValue);
	        
	        currentValueIndex = currentConcentrations.get(listOfFoodReceivers.get(1).get(4)).size()-1;
	        newValue = currentConcentrations.get(listOfFoodReceivers.get(1).get(4)).get(currentValueIndex) + P5;
	        currentConcentrations.get(listOfFoodReceivers.get(1).get(4)).set(currentValueIndex, newValue);
        
    	}
       
    	
    	return currentConcentrations;
    }
    
}
