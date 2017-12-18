/*
 * This is a program to work as an independent stand alone program to get the 
 * metabolic information from the user and return its prediction.
 * The ability to read gene expression data has been added to the program.
 * In this version in May 8 2017, I added a more complete metabolic model of E-Coli, 
 * and I used the old method of random gene expression generator (Like standAlone02)
 */
package standAlone05_4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.util.*;
import java.io.File;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;

import standAlone05_4.controller.AffiliationFinder;
import standAlone05_4.controller.ControllerMetabolic;
import standAlone05_4.controller.ModelNoFile;
import standAlone05_4.controller.ModelWithFile;

import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.Model;

/*import org.jfree.chart.JFreeChart; 
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.ChartUtilities; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;*/

/**
 *
 * @author miladrad
 */
public class StandAlone05_4 {
    
    public static void main(String[] args) throws XMLStreamException, IOException {
        
        String duration = "";
        String steps = "";
        String method = "";
        String whatToWhatch = "";
        String file = "";
        String reactionsData = "";
        String geneExpressions = "";
        String feedingInput = "";
        String updateInitialConcentration = ""; //Added Oct. 13th for update of Initial Concentrations from Files
        
        for (int i = 0 ; i < args.length ; i++){
            if (args[i].equals("-d")){
                if (duration.equals("")){
                    i++;
                    duration = args[i];
                    
                }
                else{
                    System.out.println("Error Detected");
                    return;
                }
            }
            if (args[i].equals("-s")){
                if (steps.equals("")){
                    i++;
                    steps = args[i];
                    
                }
                else{
                    System.out.println("Error Detected");
                    return;
                }
            }
            if (args[i].equals("-m")){
                if (method.equals("")){
                    i++;
                    method = args[i];
                    
                }
                else{
                    System.out.println("Error Detected");
                    return;
                }
            }
            if (args[i].equals("-w")){
                if (whatToWhatch.equals("")){
                    i++;
                    whatToWhatch = args[i];
                    
                }
                else{
                    System.out.println("Error Detected");
                    return;
                }
            }
            if (args[i].equals("-f")){
                if (file.equals("")){
                    i++;
                    file = args[i];
                    
                }
                else{
                    System.out.println("Error Detected");
                    return;
                }
            }
            if (args[i].equals("-e")){
                if (reactionsData.equals("")){
                    i++;
                    reactionsData = args[i];
                    
                }
                else{
                    System.out.println("Error Detected");
                    return;
                }
            }
            if (args[i].equals("-g")){
                if (geneExpressions.equals("")){
                    i++;
                    geneExpressions = args[i];
                    
                }
                else{
                    System.out.println("Error Detected");
                    return;
                }
            }
            if (args[i].equals("-i")){
                if (feedingInput.equals("")){
                    i++;
                    feedingInput = args[i];
                    
                }
                else{
                    System.out.println("Error Detected");
                    return;
                }
            }
            if (args[i].equals("-c")){
                if (updateInitialConcentration.equals("")){
                    i++;
                    updateInitialConcentration = args[i];
                    
                }
                else{
                    System.out.println("Error Detected");
                    return;
                }
            }
        }
        

        //double durationDouble = Double.parseDouble(duration);   //Seconds
        int stepsInt = Integer.parseInt(steps);
        
        //Run the Simulation
        //StandAlone05_1 simulate = new StandAlone05_1(duration, stepsInt, method, whatToWhatch, file, reactionsData, geneExpressions, feedingInput);
        
        standAlone(duration, stepsInt, method, whatToWhatch, file, reactionsData, geneExpressions, feedingInput, updateInitialConcentration);
    }
    
    public static ArrayList<Double> standAlone(String dure, Integer steps, String method, String whatToWhatch, String fileName, 
    		String reactionsData, String geneExpressions, String feedingInput, String updateInitialConcentration) 
            throws XMLStreamException, IOException{
        
    	//return arraylist from here
    	ArrayList<Double> plot = new ArrayList<Double>();
    	
        if (fileName.isEmpty()){
            plot = noFile(dure,steps);
        }
        else{
            plot = withFile(dure,steps, method, whatToWhatch, fileName, reactionsData, geneExpressions, feedingInput, updateInitialConcentration);
        }
        
        return plot;
    }
    
    public static ArrayList<Double> noFile(String dure, Integer steps){
        
        ArrayList<String> durations = new ArrayList<String>();
        
        for (String dur:dure.split(",")){
            durations.add(dur);
        }
        
        //Create the food arrays
        ArrayList<Double> plot = new ArrayList<Double>();
        ArrayList<Double> f1 = new ArrayList<Double>(7);
        ArrayList<Double> f2 = new ArrayList<Double>(7);
        ArrayList<Double> f3 = new ArrayList<Double>(7);
        
        //Initialize the arrays
        for (int i = 0 ; i < 7 ; i++){
            f1.add(1.0);
            f2.add(0.5);
            f3.add(0.3);
        }
        
        //Run the program
        ModelNoFile algorithm = new ModelNoFile();
        
        plot = algorithm.integratedModel(f1, f2, f3);
        System.out.println(plot);
        
        return plot;
    }
    public static ArrayList<Double> withFile(String dure, Integer steps, String method, String whatToWhatch, String fileName, String reactionsData, 
    		String geneExpressions, String feedingInput, String updateInitialConcentration) 
            throws XMLStreamException, IOException{
        ArrayList<Double> plot = new ArrayList<Double>();
        
        ModelWithFile algorithm = new ModelWithFile();
        AffiliationFinder affAlgorithm = new AffiliationFinder();
        
        //In this part of the program, we should seperate the differet phases of the simulation.
        //ٍEach one is for each phase of the program
        //The number of phases is measured by the number of the durations 
        ArrayList<String> durations = new ArrayList<String>();
        
        for (String dur:dure.split(",")){
            durations.add(dur);
        }
        
        
        //This is the output after the simulation
        //plot = algorithm.integratedModelWithFile(Double.parseDouble(durations.get(0)), steps, method,whatToWhatch,fileName,reactionsData, geneExpressions);
        
        //This part of the program tries to find the Affiliations for various rates using the core of the program
        plot = affAlgorithm.affiliationAnalysis(Double.parseDouble(durations.get(0)), steps, method,whatToWhatch,fileName,reactionsData, 
        		geneExpressions, feedingInput, updateInitialConcentration);
        
        plotOutput chart = new plotOutput();
        
        chart.lineChart(plot,whatToWhatch,Double.parseDouble(durations.get(0))/steps);
        
        return plot;
        
    }
    
}