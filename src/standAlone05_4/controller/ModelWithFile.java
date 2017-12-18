/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package standAlone05_4.controller;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;

import static standAlone05_4.controller.NumericalMethods.hashOut;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import javax.xml.stream.XMLStreamException;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;

/**
 *
 * @author MiRad
 */
public class ModelWithFile {
    
    static ArrayList<String> species = new ArrayList<String>();
    
    public Hashtable<String,ArrayList<Double>> readAndSimulateFile(Double dure, Integer steps, String method, 
            String whatToWhatch, String fileName, String reactionsData, String geneExpressions, String feedingInput) throws XMLStreamException, IOException{
        SBMLReader reader = new SBMLReader();
        SBMLDocument document;
        long start, stop, size;
        
        start = System.currentTimeMillis();
        document = reader.readSBML(fileName);
        stop = System.currentTimeMillis();

        System.out.println("        filename: " + fileName);
        System.out.println("        error(s): " + document.getNumErrors());
        
        if (document.getNumErrors() > 0){
          document.printErrors(null);
          System.out.println("Printing skipped. Please correct the above problems first.");
          System.exit(1);
        }
        
         Model convertedSBMLModel = document.getModel();
         
        //Extract Data from xml file
         
        //Species
        //ArrayList<String> species = new ArrayList<String>();
        Hashtable<String,Double> speciesInitialConcent = new Hashtable<String,Double>();
        Hashtable<String,Boolean> speciesIsBounded = new Hashtable<String,Boolean>();
        Hashtable<String,Boolean> speciesIsConstant = new Hashtable<String,Boolean>();
        
        for (int i = 0 ; i < convertedSBMLModel.getSpeciesCount() ; i++){
            species.add(convertedSBMLModel.getSpecies(i).getId());
            speciesInitialConcent.put(convertedSBMLModel.getSpecies(i).getId(),convertedSBMLModel.getSpecies(i).getInitialConcentration());
            speciesIsBounded.put(convertedSBMLModel.getSpecies(i).getId(),convertedSBMLModel.getSpecies(i).getBoundaryCondition());
            speciesIsConstant.put(convertedSBMLModel.getSpecies(i).getId(),convertedSBMLModel.getSpecies(i).getConstant());
        }
        
        System.out.println("******************");
        System.out.println("List of Species: " + species.size());
        //System.out.println("Initial Concentrations: " + speciesInitialConcent);
        /*System.out.println("Is Specie Bounded? " + speciesIsBounded);
        System.out.println("Is Specie Constant? " + speciesIsConstant);*/
        
        // If there is no initial Concentration, we need to assign some
        
        if (speciesInitialConcent.get(whatToWhatch).isNaN()){
            speciesInitialConcent = assignInitialConcent(species);
        }
        //Modify if it is needed
        //speciesInitialConcent = modifyInitiation("SubstratesDatabase-05-04-2017-Modifications.txt");    //Fix Later
        //System.out.println("Initial Concentrations: " + speciesInitialConcent);
        
        //Reactions
        ArrayList<String> reactions = new ArrayList<String>();
        Hashtable<String,Boolean> reactionReversible = new Hashtable<String,Boolean>();
        Hashtable<String,ArrayList<String>> reactionSubstrates = new Hashtable<String,ArrayList<String>>();
        Hashtable<String,ArrayList<Double>> reactionSubstratesStoch = new Hashtable<String,ArrayList<Double>>();
        Hashtable<String,ArrayList<String>> reactionProducts = new Hashtable<String,ArrayList<String>>();
        Hashtable<String,ArrayList<Double>> reactionProductsStoch = new Hashtable<String,ArrayList<Double>>();
        Hashtable<String,ArrayList<Double>> reactionRates = new Hashtable<String,ArrayList<Double>>();
        
        for (int i = 0 ; i < convertedSBMLModel.getReactionCount() ; i++){
            reactions.add(convertedSBMLModel.getReaction(i).getId());
            
            reactionReversible.put(convertedSBMLModel.getReaction(i).getId(), convertedSBMLModel.getReaction(i).getReversible());
            
            //Substrates (Names and Stochiometery)
            ArrayList<String> substrates = new ArrayList<String>();
            ArrayList<Double> substratesStoch = new ArrayList<Double>();
            for (int j = 0 ; j < convertedSBMLModel.getReaction(i).getReactantCount() ; j++){
                substrates.add(convertedSBMLModel.getReaction(i).getListOfReactants().get(j).getSpecies());
                substratesStoch.add(convertedSBMLModel.getReaction(i).getListOfReactants().get(j).getStoichiometry());
            }
            reactionSubstrates.put(convertedSBMLModel.getReaction(i).getId(), substrates);
            reactionSubstratesStoch.put(convertedSBMLModel.getReaction(i).getId(), substratesStoch);
            //End Substrates
            
            //Products (Names and Stochiometery)
            ArrayList<String> products = new ArrayList<String>();
            ArrayList<Double> productsStoch = new ArrayList<Double>();
            for (int j = 0 ; j < convertedSBMLModel.getReaction(i).getProductCount() ; j++){
                products.add(convertedSBMLModel.getReaction(i).getListOfProducts().get(j).getSpecies());
                productsStoch.add(convertedSBMLModel.getReaction(i).getListOfProducts().get(j).getStoichiometry());
            }
            reactionProducts.put(convertedSBMLModel.getReaction(i).getId(), products);
            reactionProductsStoch.put(convertedSBMLModel.getReaction(i).getId(), productsStoch);
            //End Products
            
            //Rates
            ArrayList<Double> rates = new ArrayList<Double>();
            for (int j = 0 ; j < convertedSBMLModel.getReaction(i).getKineticLaw().getLocalParameterCount() ; j++){
                rates.add(convertedSBMLModel.getReaction(i).getKineticLaw().getListOfLocalParameters().get(j).getValue());
            }
            reactionRates.put(convertedSBMLModel.getReaction(i).getId(), rates);
            //End Rates
        }
        System.out.println("******************");
        System.out.println("List of Reactions: " + reactions.size());
        //System.out.println("The Substrates: " + reactionSubstrates);
        //System.out.println("Stoichiometery of Substrates: " + reactionSubstratesStoch);
        //System.out.println("List of Products: " + reactionProducts);
        //System.out.println("Stoichiometery of Products: " + reactionProductsStoch);
        //System.out.println("List of Reaction Rates: " + reactionRates);
        //System.out.println("Is the Reaction Reversible? " + reactionReversible);
        //System.out.println("******************");
        
        //ODEs (Can be upgrade speed by reforming the database)
        Hashtable<String,ArrayList<ArrayList<ArrayList<String>>>> odeData = new Hashtable<String,ArrayList<ArrayList<ArrayList<String>>>>();
        Hashtable<String,String> odes = new Hashtable<String,String>();
        for (int i = 0 ; i < species.size() ; i++){
            String ode = "";
            odeData.put(species.get(i),findODE(species.get(i), reactions, reactionSubstrates, reactionSubstratesStoch, reactionProducts, reactionProductsStoch, reactionRates, reactionReversible));
            ArrayList<ArrayList<Double>> temp = new ArrayList<ArrayList<Double>>();
            for (ArrayList<String> stoch:odeData.get(species.get(i)).get(2)){
                temp.add(toDouble(stoch));
            }
            ode = printODE(species.get(i),toDouble(odeData.get(species.get(i)).get(0).get(0)),
                    toDouble(odeData.get(species.get(i)).get(0).get(1)),temp,
                    odeData.get(species.get(i)).get(1));
            odes.put(species.get(i), ode);
        }
        //ODEs are ready to be used
        //System.out.println("Ode of " + whatToWhatch + " is: \n " + odes.get(whatToWhatch));
        //System.out.println("And its raw data is: \n" + odeData.get(whatToWhatch));
        
        //Call the ExpressionTranslator
        ExpressionTranslator expressionMethods = new ExpressionTranslator();
        
        //First create a Hashtable of the reaction data and the enzymes
        Hashtable<String,ArrayList<String>> reactionsAndEnzymes = new Hashtable<String,ArrayList<String>>();
        ArrayList<Hashtable<String,ArrayList<String>>> totalEnzymeExpressions = new ArrayList<Hashtable<String,ArrayList<String>>>();
        Hashtable<String,ArrayList<String>> enzymeExpressions;
        ArrayList<String> geneExpressionFiles = new ArrayList<String>();
        Hashtable<String,ArrayList<Integer>> sortEnz = new Hashtable<String,ArrayList<Integer>>();  //index of sorted enzymes will be here
        
        
        
        reactionsAndEnzymes = expressionMethods.storeReactionsAndEnzymes(reactionsData);
        // The list of reactions are made by the model, not the database. So, everything will be checked back in the database
        // and will be added to the enzymeExpressions if the reaction is available in the model, otherwise, the data will only
        // remain in the database to prevent the useless search
        //The files must be "," seperated!
        
        System.out.println("******************");
        if (geneExpressions.equals("Nan")){    
            //Create the random generated expressions if needed
            randomGeneCreator("expression1Modified.txt",reactions,reactionReversible);
            randomGeneCreator("expression2Modified.txt",reactions,reactionReversible);
            randomGeneCreator("expression3Modified.txt",reactions,reactionReversible);
        }
        else if (geneExpressions.equals("nan")){
            randomGeneCreator("expression1Modified.txt",reactions,reactionReversible);
            randomGeneCreator("expression2Modified.txt",reactions,reactionReversible);
            randomGeneCreator("expression3Modified.txt",reactions,reactionReversible);
        }
        else{
            for (String splitData:geneExpressions.split(",")){
                geneExpressionFiles.add(splitData);
            }

            System.out.println("Gene Expression Files: ");
            for (String file:geneExpressionFiles){
                System.out.println(file);
                enzymeExpressions = new Hashtable<String,ArrayList<String>>();
                enzymeExpressions = expressionMethods.readTheExpressions(file,reactionsAndEnzymes);
                totalEnzymeExpressions.add(enzymeExpressions);
            }
            sortEnz = expressionMethods.sortEnzymes(totalEnzymeExpressions.get(0));
            //End of Expression Analysis
        }
        
        //Return the output
        Hashtable<String,ArrayList<Double>> output = new Hashtable<String,ArrayList<Double>>();
        
        output = simulation(dure,steps,method,whatToWhatch,species,odeData,speciesInitialConcent,speciesIsBounded,speciesIsConstant,
                totalEnzymeExpressions, sortEnz, geneExpressions, feedingInput);
        
        return output;
    }
    
    public Hashtable<String,ArrayList<Double>> simulation(Double duration, Integer steps, String method, String whatToWhatch, ArrayList<String> species, Hashtable<String,
            ArrayList<ArrayList<ArrayList<String>>>> odeData,Hashtable<String,Double> initial, Hashtable<String,Boolean> bounded,
            Hashtable<String,Boolean> constant, ArrayList<Hashtable<String,ArrayList<String>>> totalEnzymeExpressions,
            Hashtable<String,ArrayList<Integer>> sortEnz, String geneExpressions, String feedingInput) throws IOException{
        
        System.out.println("Simulation Duration: " + duration);
        System.out.println("Steps: " + steps);
        System.out.println("******************");
        
        NumericalMethods methods = new NumericalMethods();
        
        Hashtable<String,ArrayList<Double>> output = new Hashtable<String,ArrayList<Double>>();
        //Taylor Method is here. All the materials needed for this method is the input of this function
        Double[][] totalOut = new Double[10][10];
        if (method.equals("taylor")){
            output = methods.TaylorSimulator(duration, steps, species, odeData, initial, bounded, 
                    constant, totalEnzymeExpressions, sortEnz, geneExpressions,feedingInput);
        }
        else if(method.equals("rg4")){
            output = methods.runge4(duration, steps, species, odeData, initial, bounded, 
                    constant, totalEnzymeExpressions, sortEnz, geneExpressions);
        }
        
        return output;
    }

    public ArrayList<ArrayList<ArrayList<String>>> findODE(String specie, ArrayList<String> reacs, Hashtable<String,ArrayList<String>> subs,
            Hashtable<String,ArrayList<Double>> subsStoch, Hashtable<String,ArrayList<String>> prods, Hashtable<String,ArrayList<Double>> prodsStoch,
            Hashtable<String,ArrayList<Double>> rates, Hashtable<String,Boolean> reverse){
        String ode = "";
        ArrayList<ArrayList<ArrayList<String>>> dataOfODE = new ArrayList<ArrayList<ArrayList<String>>>();
        ArrayList<ArrayList<String>> listOfSpeciesInvolved = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<Double>> listOfStochsInvolved = new ArrayList<ArrayList<Double>>();
        ArrayList<String> concentList = new ArrayList<String>();
        ArrayList<Double> ratesList = new ArrayList<Double>();
        ArrayList<Double> signList = new ArrayList<Double>();
        ArrayList<Double> stochList = new ArrayList<Double>();
        ArrayList<String> reactionList = new ArrayList<String>();
        
        for (String reac:reacs){
            if (subs.get(reac).contains(specie)){
                reactionList.add(reac); //add reaction names to the data
                if (reverse.get(reac) == true){
                    reactionList.add(reac); //one more time
                    ratesList.add(rates.get(reac).get(0));
                    ratesList.add(rates.get(reac).get(1));
                    signList.add(-1.0);
                    signList.add(1.0);
                    
                    //Begin listOfSpeciesInvolved
                    concentList = new ArrayList<String>();
                    for (String sub:subs.get(reac)){
                        concentList.add(sub);
                    }
                    listOfSpeciesInvolved.add(concentList);
                    
                    concentList = new ArrayList<String>();
                    for (String pro:prods.get(reac)){
                        concentList.add(pro);
                    }
                    listOfSpeciesInvolved.add(concentList);
                    //End listOfSpeciesInvolved
                    
                    //Begin Stoichiometery
                    stochList = new ArrayList<Double>();
                    for (Double stoch:subsStoch.get(reac)){
                        stochList.add(stoch);
                    }
                    listOfStochsInvolved.add(stochList);
                    
                    stochList = new ArrayList<Double>();
                    for (Double stoch:prodsStoch.get(reac)){
                        stochList.add(stoch);
                    }
                    listOfStochsInvolved.add(stochList);
                    //End Stoichiometery
                }
                else{
                    ratesList.add(rates.get(reac).get(0));
                    signList.add(-1.0);
                    //Begin listOfSpeciesInvolved
                    concentList = new ArrayList<String>();
                    for (String sub:subs.get(reac)){
                        concentList.add(sub);
                    }
                    listOfSpeciesInvolved.add(concentList);
                    //End listOfSpeciesInvolved
                    
                    //Begin Stoichiometery
                    stochList = new ArrayList<Double>();
                    for (Double stoch:subsStoch.get(reac)){
                        stochList.add(stoch);
                    }
                    listOfStochsInvolved.add(stochList);
                    //End Stoichiometery
                }
            }
            else if (prods.get(reac).contains(specie)){
                reactionList.add(reac); //add reaction names to the data
                if (reverse.get(reac) == true){
                    reactionList.add(reac); //one more time
                    ratesList.add(rates.get(reac).get(0));
                    ratesList.add(rates.get(reac).get(1));
                    signList.add(1.0);
                    signList.add(-1.0);
                    
                    //Begin listOfSpeciesInvolved
                    concentList = new ArrayList<String>();
                    for (String sub:subs.get(reac)){
                        concentList.add(sub);
                    }
                    listOfSpeciesInvolved.add(concentList);
                    
                    concentList = new ArrayList<String>();
                    for (String pro:prods.get(reac)){
                        concentList.add(pro);
                    }
                    listOfSpeciesInvolved.add(concentList);
                    //End listOfSpeciesInvolved
                    
                    //Begin Stoichiometery
                    stochList = new ArrayList<Double>();
                    for (Double stoch:subsStoch.get(reac)){
                        stochList.add(stoch);
                    }
                    listOfStochsInvolved.add(stochList);
                    
                    stochList = new ArrayList<Double>();
                    for (Double stoch:prodsStoch.get(reac)){
                        stochList.add(stoch);
                    }
                    listOfStochsInvolved.add(stochList);
                    //End Stoichiometery
                }
                else{
                    ratesList.add(rates.get(reac).get(0));
                    signList.add(1.0);
                    //Begin listOfSpeciesInvolved
                    concentList = new ArrayList<String>();
                    for (String sub:subs.get(reac)){
                        concentList.add(sub);
                    }
                    listOfSpeciesInvolved.add(concentList);
                    //End listOfSpeciesInvolved
                    
                    //Begin Stoichiometery
                    stochList = new ArrayList<Double>();
                    for (Double stoch:subsStoch.get(reac)){
                        stochList.add(stoch);
                    }
                    listOfStochsInvolved.add(stochList);
                    //End Stoichiometery
                }
            }
        }
        /*System.out.println(specie);
        System.out.println("The signs are: " + signList);
        System.out.println("rates associated are: " + ratesList);
        System.out.println("ODE is: " + listOfSpeciesInvolved);*/
        ode = printODE(specie,signList,ratesList,listOfStochsInvolved,listOfSpeciesInvolved);
        //System.out.println("-----------");
        
        ArrayList<String> temp = new ArrayList<String>();
        ArrayList<ArrayList<String>> temptemp = new ArrayList<ArrayList<String>>();
        
        for (Double sig:signList){
            temp.add(Double.toString(sig));
        }
        temptemp.add(temp);
        
        temp = new ArrayList<String>();
        for (Double rat:ratesList){
            temp.add(Double.toString(rat));
        }
        temptemp.add(temp);
        temptemp.add(reactionList);
        dataOfODE.add(temptemp);
        dataOfODE.add(listOfSpeciesInvolved);
        //Converting Stochs into string to be saved
        
        temptemp = new ArrayList<ArrayList<String>>();
        
        for (ArrayList<Double> stoch1:listOfStochsInvolved){
            temp = new ArrayList<String>();
            for (Double stoch2:stoch1){
                temp.add(Double.toString(stoch2));
            }
            temptemp.add(temp);
        }
        dataOfODE.add(temptemp);
        //End of Convert
        return dataOfODE;
    }
    
    public String printODE(String spc, ArrayList<Double> sign, ArrayList<Double> rate, ArrayList<ArrayList<Double>> stochList, ArrayList<ArrayList<String>> species){
        String ode = "";
        
        for (int i = 0 ; i < sign.size() ; i++){
            String specielist = "";
                
                for (int j = 0 ; j < species.get(i).size(); j++){
                    specielist = specielist + "[" + species.get(i).get(j) + "]^" + Double.toString(stochList.get(i).get(j)) + "";
                }
                
            ode = ode + " + (" + Double.toString(sign.get(i)) + " * " + Double.toString(rate.get(i)) + " * " + specielist + ")";
        }
        
        return ode;
    }
    
    public ArrayList<Double> toDouble (ArrayList<String> conv){
        ArrayList<Double> converted = new ArrayList<Double>();
        
        for (String con:conv){
            converted.add(Double.parseDouble(con));
        }
        
        return converted;
    }
    public Hashtable<String,ArrayList<Double>> readExpression(String filePath) throws FileNotFoundException, IOException{
        Hashtable<String,ArrayList<Double>> expOut = new Hashtable<String,ArrayList<Double>>();
        ArrayList<String> txtData = new ArrayList<String>();
        
        //Read file and store in txtData as an ArrayList
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = "";
            //int numLines = 0;
            
            while ((line = br.readLine()) != null) {
               //numLines ++;
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
            //System.out.println(temp);
            //Put data in the hashtable
            if (!expOut.containsKey(temp.get(0))){
                ArrayList<Double> expOutVal = new ArrayList<Double>(2);
                expOutVal.add(0.0);
                expOutVal.add(0.0);
                expOut.put(temp.get(0),expOutVal);
            }
            if (temp.get(1).equals("1")){
                expOut.get(temp.get(0)).set(0, Double.parseDouble(temp.get(2)));
            }
            else{
                expOut.get(temp.get(0)).set(1, Double.parseDouble(temp.get(2)));
            }
            //System.out.println(expOut);
        }
        //System.out.println(expOut);
        return expOut;
    }
    
    public void writeOnCSV(Hashtable<String,ArrayList<Double>> totalOut, Double dure, int steps) throws IOException{
        ArrayList<String> keys = new ArrayList<String>();
        //File name
        String address = "totalResult.csv";
        
        //Create file
        File file = new File(address);
        
        //Check if the file exists
        if(file.exists()&&!file.isDirectory()){
            file.delete();
        }
        
        //Initiations
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        //Create the headers
        bw.write("Time,");
        for (String specie:species){
            keys.add(specie);
            bw.write(specie + ",");
        }
        bw.write("\n");
        //System.out.println(keys);
        
        for (int i = 0 ; i <= steps ; i++){
            bw.write(Double.toString(i*dure/steps) + ",");
            for (String spc:species){
                bw.write(Double.toString(totalOut.get(spc).get(i)) + ",");
            }
            bw.write("\n");
        }
        
        //Close file
        bw.close();
    }
    
    public void randomGeneCreator(String fileName,ArrayList<String> reactions,Hashtable<String,Boolean> reactionReversible) throws IOException{
        Random rand = new Random();
        
        try{
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            
            //Create the Headers
            writer.println("Reaction\tDirection\tExpression");
            //Start Generating expressions and writing them on file
            for (String reac:reactions){
                writer.println(reac + "\t" + "1" + "\t" + rand.nextDouble());
                if (reactionReversible.get(reac) == true){
                    writer.println(reac + "\t" + "-1" + "\t" + rand.nextDouble());
                }
            }
            
            writer.close();
        } catch (IOException e) {
           // do something
        }
    }
    
    public Hashtable<String,Double> assignInitialConcent(ArrayList<String> species){
        Hashtable<String,Double> assignedInitials = new Hashtable<String,Double>();
        
        for (String spc:species){
            assignedInitials.put(spc, 0.001);
        }
        
        return assignedInitials;
    }
    
    public Hashtable<String,Double> modifyInitiation(String modifiedConcentrations) throws FileNotFoundException, IOException{
        Hashtable<String,Double> modifiedInitials = new Hashtable<String,Double>();
        
        Double mouseWeight = 22.0;  //In grams
        Double mouseBlood = 58.5;   //In mililiters/kg of mouse body
        
        double totalBlood = mouseWeight*mouseBlood/1000000; //In liters
        //The concentrations should be mole/litre
        
        ArrayList<String> txtData = new ArrayList<String>();
        
        
        //System.out.println(hashOut);
        //Read file and store in txtData as an ArrayList
        try (BufferedReader br = new BufferedReader(new FileReader(modifiedConcentrations))) {
            String line = "";
            
            while ((line = br.readLine()) != null) {
               txtData.add(line);
            }
        }
        txtData.remove(0);
        //System.out.println(txtData);
        
        for (String txtLine:txtData){
            ArrayList<String> temp = new ArrayList<String>();
            for (String splited:txtLine.split("\t")){
                temp.add(splited);
            }
            
            //Check the size of temp. If more than 2, then it has value
            if (temp.size() == 4){
                double addUpConcentrationValue = Double.parseDouble(temp.get(2))/Double.parseDouble(temp.get(3))/1000/totalBlood;
                //System.out.println(temp.get(1));
                //System.out.println(addUpConcentrationValue);
                
                double currentValue = hashOut.get(temp.get(1)).get(0);
                hashOut.get(temp.get(1)).set(hashOut.get(temp.get(1)).size()-1, currentValue + addUpConcentrationValue);
            }
        }
        
        //System.out.println(hashOut);
        return modifiedInitials;
    }
    
    public ArrayList<Double> integratedModelWithFile(Double dure, Integer steps, String method, String whatToWatch, 
            String fileName, String reactionsData, String geneExpressions, String feedingInput) throws XMLStreamException, IOException{
        Hashtable<String,ArrayList<Double>> totalOut = new Hashtable<String,ArrayList<Double>>();
        ArrayList<Double> out = new ArrayList<Double>();
        
        
        totalOut = readAndSimulateFile(dure,steps,method,whatToWatch,fileName,reactionsData,geneExpressions,feedingInput);
        out = totalOut.get(whatToWatch);
        //System.out.println("The result with the " + method + " method for " + whatToWatch + " is: \n" + out);
        writeOnCSV(totalOut,dure,steps);
        return out;
    }
}
