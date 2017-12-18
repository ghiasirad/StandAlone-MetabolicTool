/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package standAlone05_4.controller;

import java.util.*;

/**
 *
 * @author MiRad
 */
public class ModelNoFile {
    
    public ArrayList<Double> initial (Integer W){
        int weight = W;   //weight
        
        ArrayList<Double> concent = new ArrayList<Double>();
        
        //Normal
        concent.add(0.001077);    // C00002 
        concent.add(0.001);       // C00001
        concent.add(0.001);       // C00080
        concent.add(0.00044);     // C00026
        concent.add(0.001);       // C00022
        concent.add(0.001);       // C00009
        concent.add(0.001);       // C00011
        concent.add(0.0026);      // C00003
        concent.add(0.0014);      // C00010
        concent.add(0.00012);    // C00122
        concent.add(0.096);      // C00025
        concent.add(0.0015);     // C00197
        concent.add(0.001);      // C00232
        concent.add(0.001);      // C00118
        concent.add(0.00018);    // C00074
        concent.add(0.001);     // C00103
        concent.add(2.1e-06);    // C00006
        concent.add(0.001);      // C00007
        concent.add(0.001);      // C05662
        concent.add(0.001);      // C00668
        concent.add(0.001);      // C00013
        concent.add(0.00057);    // C00042
        concent.add(0.001);      // C01251
        concent.add(0.00037);    // C00111
        concent.add(0.001);      // C00984
        concent.add(0.001);      // C05535
        concent.add(0.001);      // C04181
        concent.add(0.001);      // C06006
        concent.add(0.001);      // C00944
        concent.add(0.001);      // C00544
        concent.add(1.4e-05);    // C00493	
        concent.add(0.001);      // C00671
        concent.add(1e-05);      // C00077
        concent.add(0.001);      // C00141
        concent.add(0.001);      // C00311
        concent.add(0.001);      // C01269
        concent.add(0.001);      // C02226
        concent.add(0.001);      // C01061
        concent.add(0.001);      // C00036
        concent.add(0.001);      // C05345
        concent.add(0.001);      // C01005
        concent.add(0.001);      // C00052
        concent.add(0.001);      // C15973
        concent.add(0.001);      // C00956
        concent.add(0.001);      // C00014
        concent.add(0.002);      // C00158
        concent.add(0.001);      // C00631
        concent.add(0.001);      // C00109
        concent.add(0.001);      // C04076
        concent.add(0.001);      // C03406
        concent.add(0.001);      // C02612
        concent.add(0.001);      // C06007
        concent.add(0.001);      // C00236
        concent.add(0.001);      // C04691
        concent.add(0.001);      // C00334
        concent.add(0.001);      // C00446
        concent.add(0.001);      // C05379
        concent.add(0.001);      // C06010
        concent.add(0.001);      // C04002
        concent.add(0.001);      // C01352
        concent.add(0.0014);     // C00327
        concent.add(0.00023);    // C00091
        concent.add(0.001);      // C03175
        concent.add(0.001);      // C03232
        concent.add(0.001);      // C02637
        concent.add(0.001);      // C14463
        concent.add(0.001);      // C01179
        concent.add(0.006);      // C00267,0.001
        concent.add(0.001);      // C00449
        concent.add(0.001);      // C04272
        concent.add(0.001);      // C05560
        concent.add(1.6e-05);    // C00417
        concent.add(0.001);      // C15602
        concent.add(0.0026);     // C00041
        concent.add(0.015);      // C05378
        concent.add(0.0017);     // C00149
        concent.add(0.001);      // C00124
        concent.add(0.001);      // C16254
        concent.add(0.004);      // C00183
        concent.add(0.001);      // C06032
        concent.add(0.001);      // C01036
        concent.add(0.001);      // C05381
        concent.add(0.001);      // C00033
        concent.add(0.00068);    // C00035
        concent.add(0.0049);     // C00044
        concent.add(0.001);      // C00530
        concent.add(2.4e-05);    // C00104
        concent.add(0.00021);    // C00081
        concent.add(0.001);      // C00404
        concent.add(0.00038);    // C00068
        // Abnormal
        concent.add(0.001);      // C00322
        concent.add(6.8e-05);    // C00065
        concent.add(0.00012);    // C00005
        concent.add(0.00041);    // C00047
        concent.add(0.001);      // C15972
        concent.add(0.00056);    // C00008
        concent.add(0.00028);    // C00020
        concent.add(0.001);      // C00139
        concent.add(0.001);      // C00164
        concent.add(0.00057);   // C00062
        concent.add(0.001);     // C00279
        concent.add(0.001);     // C00407
        concent.add(0.001);     // C00138
        concent.add(0.00061);   // C00024
        concent.add(0.001);     // C00027
        concent.add(0.001);     // C00251
        concent.add(0.0042);    // C00049
        concent.add(2.9e-05);   // C00082
        concent.add(0.001);     // C00169
        concent.add(0.0025);    // C00029
        concent.add(0.001);     // C00086
        concent.add(0.0083);    // C00075
        concent.add(0.001);     // C15603
        concent.add(8.3e-05);   // C00004
        concent.add(0.001);     // C11482
        concent.add(0.001);     // C00016
        
        return concent;
        
    }
    
    public ArrayList<Double> integratedModel(ArrayList<Double> food1, ArrayList<Double> food2, ArrayList<Double> food3){
        
        ControllerMetabolic functions = new ControllerMetabolic();
        ArrayList<Double> out = new ArrayList<Double>();
        int Packages = 50;
        int step = 3600/Packages;
        //double dPackages = 50;
        ArrayList<Double> food = new ArrayList<Double>(7);
        ArrayList<Double> appliedConcent = new ArrayList<Double>();
        ArrayList<Double> totalOut = new ArrayList<Double>();
        double[][] taylorOut = new double[step][116];
        
        //Initialize the total ingreients
        for (int i = 0 ; i < 7 ; i++){
            food.add(food1.get(i)+food2.get(i)+food3.get(i));
        }
        
        //Initialize the concentrations
        for (int i = 0 ; i < initial(180).size() ; i++){
            appliedConcent.add(initial(180).get(i));
        }
        
        //Feeding
        for (int iter = 0 ; iter<1 ; iter++){
            taylorOut = functions.taylorFunc(appliedConcent,step,1.0,food.get(1)/Packages,food.get(2)/Packages,food.get(3)/Packages,food.get(4)/Packages,180.0);
        }
        
        for (int i = 0 ; i < step ; i++){
            out.add(taylorOut[i][1]);
        }
        return out;
    }
}
