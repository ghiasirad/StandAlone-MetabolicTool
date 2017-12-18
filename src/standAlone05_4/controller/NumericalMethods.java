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
import java.util.*;


public class NumericalMethods {
    
    static Hashtable<String,ArrayList<Double>> hashOut = new Hashtable<String,ArrayList<Double>>();
    static Hashtable<String,Double> initialCons = new Hashtable<String,Double>();
    static ArrayList<String> speciesList = new ArrayList<String>();
    static int geneStep;
    static int geneDure;
    static ArrayList<Integer> intervals;
    
    //Functions defined here
    
    public Hashtable<String,ArrayList<Double>> TaylorSimulator(Double duration, Integer steps, ArrayList<String> species,
            Hashtable<String,ArrayList<ArrayList<ArrayList<String>>>> odeData,Hashtable<String,Double> initial, Hashtable<String,Boolean> bounded,
            Hashtable<String,Boolean> constant, ArrayList<Hashtable<String,ArrayList<String>>> totalEnzymeExpressions,
            Hashtable<String,ArrayList<Integer>> sortEnz, String geneExpressions, String feedingInput) throws IOException{
        
        //Hashtable<String,ArrayList<Double>> hashOut = new Hashtable<String,ArrayList<Double>>();
        Hashtable<String,ArrayList<Double>> minGeneEffect = new Hashtable<String,ArrayList<Double>>();
        Hashtable<String,ArrayList<ArrayList<Double>>> geneEffect = new Hashtable<String,ArrayList<ArrayList<Double>>>();
        
        //Set the intial concentrations and species in a hashtable to be used in the boundry check
        for (String key:initial.keySet()){
            initialCons.put(key, initial.get(key));
        }
        for (String spc:species){
            speciesList.add(spc);
        }
        
        //Expression Process Begins
        if (geneExpressions.equals("Nan")){
            geneEffect = geneExpressionProcess("expression1Modified.txt","expression2Modified.txt","expression3Modified.txt");
        }
        else if (geneExpressions.equals("NanConst")){
            geneEffect = geneExpressionProcess("expression1Modified.txt","expression2Modified.txt","expression3Modified.txt");
        }
        else {
            geneEffect = geneExpressionProcessUpdated(totalEnzymeExpressions,sortEnz);
        }
        
        minGeneEffect = minOfExpression(geneEffect);
        //Expression Process Ends
        
        //Process feeding by calling the function in Feeding.java class to process data and output a proper arraylist
        Feeding feedingFunctions = new Feeding();
        ArrayList<ArrayList<String>> foodData = new ArrayList<ArrayList<String>>();
        foodData = feedingFunctions.feedingProcessedData(feedingInput);
        //System.out.println(foodData);
        
        //Initiate the output hashtable
        hashOut = initiateConcentrations(species,initial);
        //To modify the initiation with other data sources

        double nextValue = 0;
        int geneEffecPlace = 0;
        double placeInSection = 0; //in ratio or total section duration
        for (int i = 0 ; i < steps ; i++){
        	//System.out.println(i);
        	if (geneExpressions.equals("NanConst")){
        		//The easiest method is to set the placements equal to the begin of the simulation
        		geneEffecPlace = 0;
        		placeInSection = 0;
        	}
        	else{
	            //Find the place of simulation in the time frame for the gene Expression
	            geneEffecPlace = (int) (duration*i/(steps*geneStep*60));
	            //Now how deep in that section
	            placeInSection = ((duration*i/steps)-geneEffecPlace*geneStep*60)/(geneStep*60);
	            //System.out.println(placeInSection);
	            //System.out.println(geneEffecPlace);
        	}
            if (i < Double.parseDouble(foodData.get(1).get(0))){
            	hashOut = feedingFunctions.applyFood(hashOut);
            }
            for (String spc:species){
                
                double addedValue = taylorValueAddUp(odeData.get(spc),hashOut,duration,steps,geneEffect,geneEffecPlace,placeInSection,minGeneEffect);
                double currentValue = hashOut.get(spc).get(hashOut.get(spc).size()-1);
                nextValue = checkBoundry(spc,currentValue + addedValue);
                hashOut.get(spc).add(nextValue);
                //System.out.println("--------------------");
                /*System.out.println("Current Value is: " + currentValue);
                System.out.println("Next Value is: " + nextValue);
                System.out.println("The Specie is: " + spc + " and its added value is: " + addedValue);*/
            }

            //System.out.println("+++++++++++++++++++++++");
        }
        
        return hashOut;
    }
    
    public Hashtable<String,ArrayList<Double>> runge4(Double duration, Integer steps, ArrayList<String> species, Hashtable<String,
            ArrayList<ArrayList<ArrayList<String>>>> odeData,Hashtable<String,Double> initial, Hashtable<String,Boolean> bounded,
            Hashtable<String,Boolean> constant, ArrayList<Hashtable<String,ArrayList<String>>> totalEnzymeExpressions,
            Hashtable<String,ArrayList<Integer>> sortEnz, String geneExpressions){
        Hashtable<String,ArrayList<Double>> hashOut = new Hashtable<String,ArrayList<Double>>();
        ArrayList<Double> rg4Out = new ArrayList<Double>();
        //Initiate the output hashtable
        hashOut = initiateConcentrations(species,initial);
        
        
        
        return hashOut;
    }
    
    public Hashtable<String,ArrayList<Double>> initiateConcentrations(ArrayList<String> species, Hashtable<String,Double> initial){
        Hashtable<String,ArrayList<Double>> initiatedOut = new Hashtable<String,ArrayList<Double>>();
        ArrayList<Double> temp = new ArrayList<Double>();
        
        for (String spc:species){
            temp = new ArrayList<Double>();
            temp.add(initial.get(spc));
            initiatedOut.put(spc, temp);
        }
        
        return initiatedOut;
    }
    
    public double taylorValueAddUp(ArrayList<ArrayList<ArrayList<String>>> odeDataOfSpecie, Hashtable<String,
            ArrayList<Double>> hashOut,Double duration, Integer steps,Hashtable<String,ArrayList<ArrayList<Double>>> geneEffect,
            int simulationPlace, double placeInSection,Hashtable<String,ArrayList<Double>> minGeneEffect){
        double addedValue = 0;
        double tempValue = 0;
        
        //System.out.println(odeDataOfSpecie);
        //System.out.println(hashOut);
        for (int i = 0 ; i < odeDataOfSpecie.get(0).get(0).size() ; i++){
            tempValue = Double.parseDouble(odeDataOfSpecie.get(0).get(0).get(i)) *
                    Double.parseDouble(odeDataOfSpecie.get(0).get(1).get(i)) * 
                    duration/(double) steps;
            //Consider the randomized rates by including an if else into the account
            //If -1 means, it is in forward direction
            if(odeDataOfSpecie.get(0).get(0).get(i).equals("-1.0")){
                //System.out.println(i);
                //System.out.println(simulationPlace);
                //System.out.println(odeDataOfSpecie.get(0).get(2).get(i));
                //System.out.println(geneEffect.get(odeDataOfSpecie.get(0).get(2).get(i)));
                
                //System.out.println("ODE of Specie: \n" + odeDataOfSpecie);
                //System.out.println();
                double before = geneEffect.get(odeDataOfSpecie.get(0).get(2).get(i)).get(0).get(simulationPlace);
                //System.out.println(before);
                double after = geneEffect.get(odeDataOfSpecie.get(0).get(2).get(i)).get(0).get(simulationPlace+1);
                double effect = ((after-before)*placeInSection + before) *
                        Double.parseDouble(odeDataOfSpecie.get(0).get(1).get(i)) /
                        minGeneEffect.get(odeDataOfSpecie.get(0).get(2).get(i)).get(0);
                //System.out.println(effect);
                tempValue = tempValue * effect;
            }
            //If 1 means, it is in backward direction
            else if (odeDataOfSpecie.get(0).get(0).get(i).equals("1.0")){
                double before = geneEffect.get(odeDataOfSpecie.get(0).get(2).get(i)).get(1).get(simulationPlace);
                double after = geneEffect.get(odeDataOfSpecie.get(0).get(2).get(i)).get(1).get(simulationPlace+1);
                double effect = ((after-before)*placeInSection + before) *
                        Double.parseDouble(odeDataOfSpecie.get(0).get(1).get(i)) /
                        minGeneEffect.get(odeDataOfSpecie.get(0).get(2).get(i)).get(1);
                //System.out.println(effect);
                tempValue = tempValue * effect;
            }
            //End of considering the randomization
            for (int j = 0 ; j < odeDataOfSpecie.get(1).get(i).size() ; j++){
            	//System.out.println(hashOut);
                tempValue =  tempValue * Math.pow((hashOut.get(odeDataOfSpecie.get(1).get(i).get(j)).get(hashOut.get(odeDataOfSpecie.get(1).get(i).get(j)).size()-1)),
                        Double.parseDouble(odeDataOfSpecie.get(2).get(i).get(j)));
                //System.out.println(j);
                //System.out.println(tempValue);
            }
            //System.out.println("addedValue: \t" + addedValue);
            addedValue = addedValue + tempValue;
        }
        //System.out.println("addedValue: " + addedValue);
        
        return addedValue;
    }
    
    public Hashtable<String,ArrayList<ArrayList<Double>>> geneExpressionProcess (String file1, String file2, String file3) throws IOException{
        Hashtable<String,ArrayList<ArrayList<Double>>> geneOut = new Hashtable<String,ArrayList<ArrayList<Double>>>();
        
        Scanner sc = new Scanner(System.in);
        ModelWithFile model = new ModelWithFile();
        
        Hashtable<String,ArrayList<Double>> geneExp0h = new Hashtable<String,ArrayList<Double>>();
        Hashtable<String,ArrayList<Double>> geneExp2h = new Hashtable<String,ArrayList<Double>>();
        Hashtable<String,ArrayList<Double>> geneExp4h = new Hashtable<String,ArrayList<Double>>();
        
        //Request User for the Input
        /*System.out.println("What is the step size of the Gene Expression simulation (in minutes):");
        geneStep =  sc.nextInt();
        System.out.println("What is the duration until the next expression (in minutes)?");
        geneDure =  sc.nextInt();*/
        
        //Considered fixed for Bruno
        geneStep = 10;
        geneDure = 120;
        
        //End User Input
        
        //File Reader for the gene expression
        geneExp0h = model.readExpression(file1);
        geneExp2h = model.readExpression(file2);
        geneExp4h = model.readExpression(file3);
        
        /*System.out.println(geneExp0h);
        System.out.println(geneExp2h);
        System.out.println(geneExp4h);*/
        
        geneOut = expressGenerator(geneStep,geneDure,geneExp0h,geneExp2h,geneExp4h);
        //System.out.println("The Randomized Expressions: " + geneOut.get("R07399").get(0).size());
        
        return geneOut;
    }

    public Hashtable<String,ArrayList<ArrayList<Double>>> geneExpressionProcessUpdated (ArrayList<Hashtable<String,ArrayList<String>>> totalEnzymeExpressions,
            Hashtable<String,ArrayList<Integer>> sortEnz) 
            throws IOException{
        Hashtable<String,ArrayList<ArrayList<Double>>> geneOut = new Hashtable<String,ArrayList<ArrayList<Double>>>();
        intervals = new ArrayList<Integer>();
        
        Scanner sc = new Scanner(System.in);
        ModelWithFile model = new ModelWithFile();
        
        /*Hashtable<String,ArrayList<Double>> geneExp0h = new Hashtable<String,ArrayList<Double>>();
        Hashtable<String,ArrayList<Double>> geneExp2h = new Hashtable<String,ArrayList<Double>>();
        Hashtable<String,ArrayList<Double>> geneExp4h = new Hashtable<String,ArrayList<Double>>();*/
        
        //Request User for the Input
        System.out.println("What is the step size of the Gene Expression simulation (in minutes):");
        geneStep =  sc.nextInt();
        System.out.println("The total number of Expressions Uploaded is: " + totalEnzymeExpressions.size() + ". So you need to put "
         + Integer.toString(totalEnzymeExpressions.size()-1) + " comma seperated intervals (in minutes)!");
        String durations = sc.next();
        //geneDure =  sc.nextInt();

        for (String dure:durations.split(",")){
            intervals.add(Integer.parseInt(dure));
        }

        //End User Input
        
        //There is no need for readExpression Function here
        
        geneOut = expressGeneratorUpdated(geneStep,intervals,totalEnzymeExpressions,sortEnz);
        //System.out.println("The Randomized Expressions: " + geneOut);
        
        return geneOut;
    }
    public Hashtable<String,ArrayList<ArrayList<Double>>> expressGenerator(int geneStep, int geneDure, Hashtable<String,ArrayList<Double>> exp1, Hashtable<String,ArrayList<Double>> exp2, Hashtable<String,ArrayList<Double>> exp3){
        Random rand = new Random();
        
        Hashtable<String,ArrayList<ArrayList<Double>>> randomRates = new Hashtable<String,ArrayList<ArrayList<Double>>>();
        
        ArrayList<ArrayList<Double>> temp = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> temptemp = new ArrayList<Double>();
        
        
        double coeff = 0.01;
        
        for (String key:exp1.keySet()){
            //Initialize the set
            temp = new ArrayList<ArrayList<Double>>();
            temptemp = new ArrayList<Double>();
            temptemp.add(exp1.get(key).get(0));
            temp.add(temptemp);
            temptemp = new ArrayList<Double>();
            temptemp.add(exp1.get(key).get(1));
            temp.add(temptemp);
            randomRates.put(key, temp);
            //Start the expression calculation
            //0h to 2h
            for (int i = 1 ; i < geneDure/geneStep ; i++){
                //find the slop between two expressions
                double rateAtTime = ((exp2.get(key).get(0)-exp1.get(key).get(0))*i/geneStep + exp1.get(key).get(0)) + rand.nextDouble() * coeff * exp1.get(key).get(0);
                randomRates.get(key).get(0).add(rateAtTime);
                
                rateAtTime = ((exp2.get(key).get(1)-exp1.get(key).get(1))*i/geneStep + exp1.get(key).get(1)) + rand.nextDouble() * coeff * exp1.get(key).get(1);
                randomRates.get(key).get(1).add(rateAtTime);
            }
            for (int i = 0 ; i < geneDure/geneStep+1 ; i++){
                //find the slop between two expressions
                double rateAtTime = ((exp3.get(key).get(0)-exp2.get(key).get(0))*i/geneStep + exp2.get(key).get(0)) + rand.nextDouble() * coeff * exp2.get(key).get(0);
                randomRates.get(key).get(0).add(rateAtTime);
                
                rateAtTime = ((exp3.get(key).get(1)-exp2.get(key).get(1))*i/geneStep + exp2.get(key).get(1)) + rand.nextDouble() * coeff * exp2.get(key).get(1);
                randomRates.get(key).get(1).add(rateAtTime);
            }
        }
        
        return randomRates;
    }
    
    public Hashtable<String,ArrayList<ArrayList<Double>>> expressGeneratorUpdated(int geneStep, ArrayList<Integer> intervals, 
            ArrayList<Hashtable<String,ArrayList<String>>> totalEnzymeExpressions,Hashtable<String,ArrayList<Integer>> sortEnz){
        Random rand = new Random();
        
        Hashtable<String,ArrayList<ArrayList<Double>>> generatedRates = new Hashtable<String,ArrayList<ArrayList<Double>>>();
        
        ArrayList<ArrayList<Double>> temp = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> temptemp = new ArrayList<Double>();
        
        
        double coeff = 0.1;
        
        for (String key:totalEnzymeExpressions.get(0).keySet()){
            //Initialize the set
            temp = new ArrayList<ArrayList<Double>>();
            temptemp = new ArrayList<Double>();
            temptemp.add(Double.parseDouble(totalEnzymeExpressions.get(0).get(key).get(sortEnz.get(key).get(0))));  //the highest expression as the forward expression
            temp.add(temptemp);
            temptemp = new ArrayList<Double>();
            temptemp.add(Double.parseDouble(totalEnzymeExpressions.get(0).get(key).get(sortEnz.get(key).get(1))));
            temp.add(temptemp);
            generatedRates.put(key, temp);
            //Start the expression calculation
            //for loop to consider all the files
            for (int j = 0 ; j < totalEnzymeExpressions.size()-1 ; j++){
                for (int i = 1 ; i < intervals.get(j)/geneStep+1 ; i++){
                    //find the slop between two expressions
                    double rateAtTime = ((Double.parseDouble(totalEnzymeExpressions.get(j+1).get(key).get(sortEnz.get(key).get(0)))-Double.parseDouble(totalEnzymeExpressions.get(j).get(key).get(sortEnz.get(key).get(0))))*i/geneStep + 
                            Double.parseDouble(totalEnzymeExpressions.get(j+1).get(key).get(sortEnz.get(key).get(0))) + 
                            rand.nextDouble() * coeff * Double.parseDouble(totalEnzymeExpressions.get(j).get(key).get(sortEnz.get(key).get(0))));
                    generatedRates.get(key).get(0).add(rateAtTime);

                    rateAtTime = ((Double.parseDouble(totalEnzymeExpressions.get(j+1).get(key).get(sortEnz.get(key).get(1)))-Double.parseDouble(totalEnzymeExpressions.get(j).get(key).get(sortEnz.get(key).get(1))))*i/geneStep + 
                            Double.parseDouble(totalEnzymeExpressions.get(j+1).get(key).get(sortEnz.get(key).get(1))) + 
                            rand.nextDouble() * coeff * Double.parseDouble(totalEnzymeExpressions.get(j).get(key).get(sortEnz.get(key).get(1))));
                    generatedRates.get(key).get(1).add(rateAtTime);
                }
            }
        }
        return generatedRates;
    }
    
    public Hashtable<String,ArrayList<Double>> minOfExpression (Hashtable<String,ArrayList<ArrayList<Double>>> randomRates){
        Hashtable<String,ArrayList<Double>> minRandomRates = new Hashtable<String,ArrayList<Double>>();
        ArrayList<Double> temp = new ArrayList<Double>();
        
        for (String reac:randomRates.keySet()){
            temp = new ArrayList<Double>();
            double forward = findMinOfList(randomRates.get(reac).get(0));
            double backward = findMinOfList(randomRates.get(reac).get(1));
            if (forward < 0.05){
                temp.add(0.05);
            }
            else{
                temp.add(forward);
            }
            if (backward < 0.05){
                temp.add(0.05);
            }
            else{
                temp.add(backward);
            }
            minRandomRates.put(reac,temp);
        }
        
        return minRandomRates;
    }
    
    public Double findMinOfList (ArrayList<Double> rates){
        double min = rates.get(0);
        
        for (double rate:rates){
            if (rate < min){
                min = rate;
            }
        }
        
        return min;
    }
    
    public Double checkBoundry (String spc,Double val){
        Random rand = new Random();
        Double value = val;
        
        if (val < 0.0){
            value = 0.0000001;
        }
        if (val > 0.01){
            //value = 0.01-rand.nextDouble()*0.005;
            value = initialCons.get(spc)*20*rand.nextDouble();
        }
        
        
        
        return value;
    }
    
    public Double checkRate (Double rte, Double low, Double high){
        Random rand = new Random();
        Double rate = rte;
        
        if (rte < -0.000001){
            rate = 0.000001*rand.nextDouble();
            //rate = 0.000001;
        }
        if (rte > 0.000001){
            rate = 0.000001*rand.nextDouble();
            //rate = -0.000001;
        }
        
        return rate;
    }
}
