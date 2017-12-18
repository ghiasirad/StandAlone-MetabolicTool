/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package standAlone05_4.controller;

import java.util.*;

/**
 *
 * @author miladrad
 */
public class ControllerMetabolic {
    
    public double checkBoundryCons (double x, double low, double high){
        if (x < low){
            x = low + 0.000005;
        }
        else if (x > high){
            x = high - 0.000003;
        }
        return x;
    }
    
    public double checkBoundry (double x, double low, double high){
        if (x < low){
            x = low + 0.00009;
        }
        else if (x > high){
            x = high - 0.00009;
        }
        return x;
    }
    
    public double[][] taylorFunc(ArrayList<Double> con, Integer step, Double size,
            Double M1, Double M2, Double M3, Double M4, Double weight){
        
        //Output which is a matrix
        double[][] a = new double[step][116];
        
        //step size and number of steps
        double dt = size;
        int steps = step;
        
        //Max Concentration
        double cHigh = 1.0;
        
        //Max and Min for ODE
        double rLow = -0.005;
        double rHigh = 0.005;
        
        //Molar Calculation (7% of boly is blood)
        double blood = weight*0.07/2;          // Liters
        
        // Carb (Glucose is 180.16 g/mole and we affect alpha-D-glucose C00267)
        // Protein (Devided between 5 amino acids of Serine 105.09 and C00065, 
        // Valine 117.15 and C00183, Isolucine 131.18 and C00407, Lysine146.19
        // and C00047, Tyrosine 181.19 and C00082)
        
        // Added Proteins in Molar added to the concentration
        double P1 = M1/(5*105.09*blood);
        double P2 = M1/(5*117.15*blood);
        double P3 = M1/(5*131.18*blood);
        double P4 = M1/(5*146.19*blood);
        double P5 = M1/(5*181.19*blood);
        
        // Added Carbs in Molar added to the concentration
        double C1 = M3/(180.16*blood);
        
        // Initialization
        // Reactions and Original Rates with min and max as Comments
        double R01786 = 5;         // 5
        double R02740 = 1;         // 1
        double R04779 = 2;         // 2
        double R01070 = 10;        // 45,0.0002,64.5
        double R01015 = 70;        // 500,63,1080
        double R01061 = 5;         // 110,0.002,234
        double R07159 = 10;        // 500,N/A
        double R01512 = 10;        // 1500,0.78,2633
        double R01518 = 10;        // 1500,0.0009,3200
        double R00658 = 10;        // 110,0.018,230
        double R00200 = 20;        // 2000,0.38,3204
        double R00351 = 10;        // 100,0.275,167
        double R01325 = 5.3;       // 5.3
        double R01900 = 4;         // 4
        double R01899 = 12;        // 120,4.5,255
        double R00268 = 1;         // 150,N/A
        double R01197 = 5;         // 17,1.7,19
        double R02570 = 3;         // 30,N/A
        double R07618 = 40;        // 400,14.4,649
        double R00405 = 201;       // 201,Const.
        double R02164 = 10;        // 100,0.1,260
        double R01082 = 5;         // 500,1.9,1150
        double R00342 = 0.5;       // 2000,0.269,0.74 or 250,4729
        double R00209 = 3;         // 300,0.077,486
        double R10619 = 12000;     // 12000,Const. ?
        double R01092 = 0.5;       // 0.5
        double R00955 = 5;         // 500,0.0533,987
        double R00291 = 15;        // 45,2,73.95
        double R00959 = 10;        // 200,0.03,398
        double R00289 = 5;         // 50,0.12,191
        double R01513 = 10;        // 10,3.2,18.7
        double R04173 = 1.5;       // 1.5
        double R00582 = 24;        // 400,1.508,804
        double R00226 = 3;         // 3
        double R05071 = 3.511;     // 3.511
        double R04440 = 0.00417;   // 0.00417
        double R04441 = 1;         // 1
        double R01214 = 5;         // 500,N/A
        double R08648 = 1;         // 1
        double R05069 = 1;         // 1
        double R05068 = 1;         // 1
        double R05070 = 1;         // 1
        double R02199 = 15;        // 500,7.6,1075
        double R07399 = 1;         // 1
        double R03896 = 1;         // 1
        double R03898 = 1;         // 1
        double R00994 = 1;         // 1
        double R00271 = 1;         // 1
        double R03444 = 1;         // 1
        double R04371 = 1;         // 1
        double R01934 = 1;         // 1
        double R01939 = 1;         // 1
        double R03098 = 0.001;     // 1
        double R04863 = 1;         // 1
        double R04390 = 1;         // 1
        double R02315 = 1;         // 1
        double R00715 = 1;         // 1
        double R09720 = 1;         // 1
        double R00149 = 1.8;       // 1.8
        double R01398 = 82;        // 82,Const.
        double R01954 = 2;         // 2
        double R01086 = 15;        // 1500,0.12,2930
        double R00551 = 5;         // 500,0.013,1132
        double R01826 = 1;         // 1
        double R03083 = 1;         // 1
        double R03084 = 1;         // 1
        double R02413 = 1;         // 1
        double R02412 = 1;         // 1
        double R03460 = 1;         // 1
        double R01714 = 1;         // 1
        double R00729 = 8.42;      // 8.42,Const.
        double R02521 = 5;         // 5,0.03,9.9
        double R02519 = 20;        // 50,10.1,79.5
        double R03181 = 25;        // 250,2.1,464
        double R01364 = 10;        // 10,N/A
        double R00261 = 20;        // 50,0.286,75.41
        double R01648 = 10;        // 20,0.00023,47.4
        double R10178 = 8;         // 8,6.9,10.6
        double R00713 = 10;        // 100,0.3,164
        double R00714 = 10;        // 100,0.3,164
        double R90001 = 200;       // 200,N/A
        double R90002 = 200;       // 200,N/A
        
        // Metabolites
            // Normal
        double C00002 = con.get(0);	
        double C00001 = con.get(1);	
        double C00080 = con.get(2);	
        double C00026 = con.get(3);	
        double C00022 = con.get(4);	
        double C00009 = con.get(5);	
        double C00011 = con.get(6);	
        double C00003 = con.get(7);	
        double C00010 = con.get(8);	
        double C00122 = con.get(9);	
        double C00025 = con.get(10);	
        double C00197 = con.get(11);	
        double C00232 = con.get(12);	
        double C00118 = con.get(13);	
        double C00074 = con.get(14);	
        double C00103 = con.get(15);	
        double C00006 = con.get(16);	
        double C00007 = con.get(17);	
        double C05662 = con.get(18);	
        double C00668 = con.get(19);	
        double C00013 = con.get(20);	
        double C00042 = con.get(21);	
        double C01251 = con.get(22);	
        double C00111 = con.get(23);	
        double C00984 = con.get(24);	
        double C05535 = con.get(25);	
        double C04181 = con.get(26);	
        double C06006 = con.get(27);	
        double C00944 = con.get(28);	
        double C00544 = con.get(29);	
        double C00493 = con.get(30);	
        double C00671 = con.get(31);	
        double C00077 = con.get(32);	
        double C00141 = con.get(33);	
        double C00311 = con.get(34);	
        double C01269 = con.get(35);	
        double C02226 = con.get(36);	
        double C01061 = con.get(37);	
        double C00036 = con.get(38);	
        double C05345 = con.get(39);	
        double C01005 = con.get(40);	
        double C00052 = con.get(41);	
        double C15973 = con.get(42);	
        double C00956 = con.get(43);	
        double C00014 = con.get(44);	
        double C00158 = con.get(45);	
        double C00631 = con.get(46);	
        double C00109 = con.get(47);	
        double C04076 = con.get(48);	
        double C03406 = con.get(49);	
        double C02612 = con.get(50);	
        double C06007 = con.get(51);	
        double C00236 = con.get(52);	
        double C04691 = con.get(53);	
        double C00334 = con.get(54);	
        double C00446 = con.get(55);	
        double C05379 = con.get(56);	
        double C06010 = con.get(57);	
        double C04002 = con.get(58);	
        double C01352 = con.get(59);	
        double C00327 = con.get(60);	
        double C00091 = con.get(61);	
        double C03175 = con.get(62);	
        double C03232 = con.get(63);	
        double C02637 = con.get(64);	
        double C14463 = con.get(65);	
        double C01179 = con.get(66);	
        double C00267 = con.get(67) + C1;	
        double C00449 = con.get(68);	
        double C04272 = con.get(69);	
        double C05560 = con.get(70);	
        double C00417 = con.get(71);	
        double C15602 = con.get(72);	
        double C00041 = con.get(73);	
        double C05378 = con.get(74);	
        double C00149 = con.get(75);	
        double C00124 = con.get(76);	
        double C16254 = con.get(77);	
        double C00183 = con.get(78) + P2;	
        double C06032 = con.get(79);	
        double C01036 = con.get(80);	
        double C05381 = con.get(81);	
        double C00033 = con.get(82);	
        double C00035 = con.get(83);	
        double C00044 = con.get(84);	
        double C00530 = con.get(85);	
        double C00104 = con.get(86);	
        double C00081 = con.get(87);	
        double C00404 = con.get(88);	
        double C00068 = con.get(89);
            // Abnormal
        double C00322 = con.get(90);
        double C00065 = con.get(91) + P1;
        double C00005 = con.get(92);
        double C00047 = con.get(93) + P4;
        double C15972 = con.get(94);
        double C00008 = con.get(95);
        double C00020 = con.get(96);
        double C00139 = con.get(97);
        double C00164 = con.get(98);
        double C00062 = con.get(99);
        double C00279 = con.get(100);
        double C00407 = con.get(101) + P3;
        double C00138 = con.get(102);
        double C00024 = con.get(103);
        double C00027 = con.get(104);
        double C00251 = con.get(105);
        double C00049 = con.get(106);
        double C00082 = con.get(107) + P5;
        double C00169 = con.get(108);
        double C00029 = con.get(109);
        double C00086 = con.get(110);
        double C00075 = con.get(111);
        double C15603 = con.get(112);
        double C00004 = con.get(113);
        double C11482 = con.get(114);
        double C00016 = con.get(115);
        
        //Simulation (Euler Method)
        //Metabolites
            //Normal
        ArrayList<Double> vC00002 = new ArrayList<Double>();
        ArrayList<Double> vC00001 = new ArrayList<Double>();
        ArrayList<Double> vC00080 = new ArrayList<Double>();
        ArrayList<Double> vC00026 = new ArrayList<Double>();
        ArrayList<Double> vC00022 = new ArrayList<Double>();
        ArrayList<Double> vC00009 = new ArrayList<Double>();
        ArrayList<Double> vC00011 = new ArrayList<Double>();
        ArrayList<Double> vC00003 = new ArrayList<Double>();
        ArrayList<Double> vC00010 = new ArrayList<Double>();
        ArrayList<Double> vC00122 = new ArrayList<Double>();
        ArrayList<Double> vC00025 = new ArrayList<Double>();
        ArrayList<Double> vC00197 = new ArrayList<Double>();
        ArrayList<Double> vC00232 = new ArrayList<Double>();
        ArrayList<Double> vC00118 = new ArrayList<Double>();
        ArrayList<Double> vC00074 = new ArrayList<Double>();
        ArrayList<Double> vC00103 = new ArrayList<Double>();
        ArrayList<Double> vC00006 = new ArrayList<Double>();
        ArrayList<Double> vC00007 = new ArrayList<Double>();
        ArrayList<Double> vC05662 = new ArrayList<Double>();
        ArrayList<Double> vC00668 = new ArrayList<Double>();
        ArrayList<Double> vC00013 = new ArrayList<Double>();
        ArrayList<Double> vC00042 = new ArrayList<Double>();
        ArrayList<Double> vC01251 = new ArrayList<Double>();
        ArrayList<Double> vC00111 = new ArrayList<Double>();
        ArrayList<Double> vC00984 = new ArrayList<Double>();
        ArrayList<Double> vC05535 = new ArrayList<Double>();
        ArrayList<Double> vC04181 = new ArrayList<Double>();
        ArrayList<Double> vC06006 = new ArrayList<Double>();
        ArrayList<Double> vC00944 = new ArrayList<Double>();
        ArrayList<Double> vC00544 = new ArrayList<Double>();
        ArrayList<Double> vC00493 = new ArrayList<Double>();
        ArrayList<Double> vC00671 = new ArrayList<Double>();
        ArrayList<Double> vC00077 = new ArrayList<Double>();
        ArrayList<Double> vC00141 = new ArrayList<Double>();
        ArrayList<Double> vC00311 = new ArrayList<Double>();
        ArrayList<Double> vC01269 = new ArrayList<Double>();
        ArrayList<Double> vC02226 = new ArrayList<Double>();
        ArrayList<Double> vC01061 = new ArrayList<Double>();
        ArrayList<Double> vC00036 = new ArrayList<Double>();
        ArrayList<Double> vC05345 = new ArrayList<Double>();
        ArrayList<Double> vC01005 = new ArrayList<Double>();
        ArrayList<Double> vC00052 = new ArrayList<Double>();
        ArrayList<Double> vC15973 = new ArrayList<Double>();
        ArrayList<Double> vC00956 = new ArrayList<Double>();
        ArrayList<Double> vC00014 = new ArrayList<Double>();
        ArrayList<Double> vC00158 = new ArrayList<Double>();
        ArrayList<Double> vC00631 = new ArrayList<Double>();
        ArrayList<Double> vC00109 = new ArrayList<Double>();
        ArrayList<Double> vC04076 = new ArrayList<Double>();
        ArrayList<Double> vC03406 = new ArrayList<Double>();
        ArrayList<Double> vC02612 = new ArrayList<Double>();
        ArrayList<Double> vC06007 = new ArrayList<Double>();
        ArrayList<Double> vC00236 = new ArrayList<Double>();
        ArrayList<Double> vC04691 = new ArrayList<Double>();
        ArrayList<Double> vC00334 = new ArrayList<Double>();
        ArrayList<Double> vC00446 = new ArrayList<Double>();
        ArrayList<Double> vC05379 = new ArrayList<Double>();
        ArrayList<Double> vC06010 = new ArrayList<Double>();
        ArrayList<Double> vC04002 = new ArrayList<Double>();
        ArrayList<Double> vC01352 = new ArrayList<Double>();
        ArrayList<Double> vC00327 = new ArrayList<Double>();
        ArrayList<Double> vC00091 = new ArrayList<Double>();
        ArrayList<Double> vC03175 = new ArrayList<Double>();
        ArrayList<Double> vC03232 = new ArrayList<Double>();
        ArrayList<Double> vC02637 = new ArrayList<Double>();
        ArrayList<Double> vC14463 = new ArrayList<Double>();
        ArrayList<Double> vC01179 = new ArrayList<Double>();
        ArrayList<Double> vC00267 = new ArrayList<Double>();
        ArrayList<Double> vC00449 = new ArrayList<Double>();
        ArrayList<Double> vC04272 = new ArrayList<Double>();
        ArrayList<Double> vC05560 = new ArrayList<Double>();
        ArrayList<Double> vC00417 = new ArrayList<Double>();
        ArrayList<Double> vC15602 = new ArrayList<Double>();
        ArrayList<Double> vC00041 = new ArrayList<Double>();
        ArrayList<Double> vC05378 = new ArrayList<Double>();
        ArrayList<Double> vC00149 = new ArrayList<Double>();
        ArrayList<Double> vC00124 = new ArrayList<Double>();
        ArrayList<Double> vC16254 = new ArrayList<Double>();
        ArrayList<Double> vC00183 = new ArrayList<Double>();
        ArrayList<Double> vC06032 = new ArrayList<Double>();
        ArrayList<Double> vC01036 = new ArrayList<Double>();
        ArrayList<Double> vC05381 = new ArrayList<Double>();
        ArrayList<Double> vC00033 = new ArrayList<Double>();
        ArrayList<Double> vC00035 = new ArrayList<Double>();
        ArrayList<Double> vC00044 = new ArrayList<Double>();
        ArrayList<Double> vC00530 = new ArrayList<Double>();
        ArrayList<Double> vC00104 = new ArrayList<Double>();
        ArrayList<Double> vC00081 = new ArrayList<Double>();
        ArrayList<Double> vC00404 = new ArrayList<Double>();
        ArrayList<Double> vC00068 = new ArrayList<Double>();
            //Abnormal
        ArrayList<Double> vC00322 = new ArrayList<Double>();
        ArrayList<Double> vC00065 = new ArrayList<Double>();
        ArrayList<Double> vC00005 = new ArrayList<Double>();
        ArrayList<Double> vC00047 = new ArrayList<Double>();
        ArrayList<Double> vC15972 = new ArrayList<Double>();
        ArrayList<Double> vC00008 = new ArrayList<Double>();
        ArrayList<Double> vC00020 = new ArrayList<Double>();
        ArrayList<Double> vC00139 = new ArrayList<Double>();
        ArrayList<Double> vC00164 = new ArrayList<Double>();
        ArrayList<Double> vC00062 = new ArrayList<Double>();
        ArrayList<Double> vC00279 = new ArrayList<Double>();
        ArrayList<Double> vC00407 = new ArrayList<Double>();
        ArrayList<Double> vC00138 = new ArrayList<Double>();
        ArrayList<Double> vC00024 = new ArrayList<Double>();
        ArrayList<Double> vC00027 = new ArrayList<Double>();
        ArrayList<Double> vC00251 = new ArrayList<Double>();
        ArrayList<Double> vC00049 = new ArrayList<Double>();
        ArrayList<Double> vC00082 = new ArrayList<Double>();
        ArrayList<Double> vC00169 = new ArrayList<Double>();
        ArrayList<Double> vC00029 = new ArrayList<Double>();
        ArrayList<Double> vC00086 = new ArrayList<Double>();
        ArrayList<Double> vC00075 = new ArrayList<Double>();
        ArrayList<Double> vC15603 = new ArrayList<Double>();
        ArrayList<Double> vC00004 = new ArrayList<Double>();
        ArrayList<Double> vC11482 = new ArrayList<Double>();
        ArrayList<Double> vC00016 = new ArrayList<Double>();
        
        //Initialize
            //Normal
        vC00002.add(C00002);	
        vC00001.add(C00001);	
        vC00080.add(C00080);	
        vC00026.add(C00026);	
        vC00022.add(C00022);	
        vC00009.add(C00009);	
        vC00011.add(C00011);	
        vC00003.add(C00003);	
        vC00010.add(C00010);	
        vC00122.add(C00122);	
        vC00025.add(C00025);	
        vC00197.add(C00197);	
        vC00232.add(C00232);	
        vC00118.add(C00118);	
        vC00074.add(C00074);	
        vC00103.add(C00103);	
        vC00006.add(C00006);	
        vC00007.add(C00007);	
        vC05662.add(C05662);	
        vC00668.add(C00668);	
        vC00013.add(C00013);	
        vC00042.add(C00042);	
        vC01251.add(C01251);	
        vC00111.add(C00111);	
        vC00984.add(C00984);	
        vC05535.add(C05535);	
        vC04181.add(C04181);	
        vC06006.add(C06006);	
        vC00944.add(C00944);	
        vC00544.add(C00544);	
        vC00493.add(C00493);	
        vC00671.add(C00671);	
        vC00077.add(C00077);	
        vC00141.add(C00141);	
        vC00311.add(C00311);	
        vC01269.add(C01269);	
        vC02226.add(C02226);	
        vC01061.add(C01061);	
        vC00036.add(C00036);	
        vC05345.add(C05345);	
        vC01005.add(C01005);	
        vC00052.add(C00052);	
        vC15973.add(C15973);	
        vC00956.add(C00956);	
        vC00014.add(C00014);	
        vC00158.add(C00158);	
        vC00631.add(C00631);	
        vC00109.add(C00109);	
        vC04076.add(C04076);	
        vC03406.add(C03406);	
        vC02612.add(C02612);	
        vC06007.add(C06007);	
        vC00236.add(C00236);	
        vC04691.add(C04691);	
        vC00334.add(C00334);	
        vC00446.add(C00446);	
        vC05379.add(C05379);	
        vC06010.add(C06010);	
        vC04002.add(C04002);	
        vC01352.add(C01352);	
        vC00327.add(C00327);	
        vC00091.add(C00091);	
        vC03175.add(C03175);	
        vC03232.add(C03232);	
        vC02637.add(C02637);	
        vC14463.add(C14463);	
        vC01179.add(C01179);	
        vC00267.add(C00267);	
        vC00449.add(C00449);	
        vC04272.add(C04272);	
        vC05560.add(C05560);	
        vC00417.add(C00417);	
        vC15602.add(C15602);	
        vC00041.add(C00041);	
        vC05378.add(C05378);	
        vC00149.add(C00149);	
        vC00124.add(C00124);	
        vC16254.add(C16254);	
        vC00183.add(C00183);	
        vC06032.add(C06032);	
        vC01036.add(C01036);	
        vC05381.add(C05381);	
        vC00033.add(C00033);	
        vC00035.add(C00035);	
        vC00044.add(C00044);	
        vC00530.add(C00530);	
        vC00104.add(C00104);	
        vC00081.add(C00081);	
        vC00404.add(C00404);	
        vC00068.add(C00068);
            //Abnormal
        vC00322.add(C00322);
        vC00065.add(C00065);
        vC00005.add(C00005);
        vC00047.add(C00047);
        vC15972.add(C15972);
        vC00008.add(C00008);
        vC00020.add(C00020);
        vC00139.add(C00139);
        vC00164.add(C00164);
        vC00062.add(C00062);
        vC00279.add(C00279);
        vC00407.add(C00407);
        vC00138.add(C00138);
        vC00024.add(C00024);
        vC00027.add(C00027);
        vC00251.add(C00251);
        vC00049.add(C00049);
        vC00082.add(C00082);
        vC00169.add(C00169);
        vC00029.add(C00029);
        vC00086.add(C00086);
        vC00075.add(C00075);
        vC15603.add(C15603);
        vC00004.add(C00004);
        vC11482.add(C11482);
        vC00016.add(C00016);
        
        for (int i = 0 ; i < steps ; i++){
            //initial ODEs
            double dC00002 = 0;
            double dC00001 = 0;
            double dC00080 = 0;
            double dC00026 = 0;
            double dC00022 = 0;
            double dC00009 = 0;
            double dC00011 = 0;
            double dC00003 = 0;
            double dC00010 = 0;
            double dC00122 = 0;
            double dC00025 = 0;
            double dC00197 = 0;
            double dC00232 = 0;
            double dC00118 = 0;
            double dC00074 = 0;
            double dC00103 = 0;
            double dC00006 = 0;
            double dC00007 = 0;
            double dC05662 = 0;
            double dC00668 = 0;
            double dC00013 = 0;
            double dC00042 = 0;
            double dC01251 = 0;
            double dC00111 = 0;
            double dC00984 = 0;
            double dC05535 = 0;
            double dC04181 = 0;
            double dC06006 = 0;
            double dC00944 = 0;
            double dC00544 = 0;
            double dC00493 = 0;
            double dC00671 = 0;
            double dC00077 = 0;
            double dC00141 = 0;
            double dC00311 = 0;
            double dC01269 = 0;
            double dC02226 = 0;
            double dC01061 = 0;
            double dC00036 = 0;
            double dC05345 = 0;
            double dC01005 = 0;
            double dC00052 = 0;
            double dC15973 = 0;
            double dC00956 = 0;
            double dC00014 = 0;
            double dC00158 = 0;
            double dC00631 = 0;
            double dC00109 = 0;
            double dC04076 = 0;
            double dC03406 = 0;
            double dC02612 = 0;
            double dC06007 = 0;
            double dC00236 = 0;
            double dC04691 = 0;
            double dC00334 = 0;
            double dC00446 = 0;
            double dC05379 = 0;
            double dC06010 = 0;
            double dC04002 = 0;
            double dC01352 = 0;
            double dC00327 = 0;
            double dC00091 = 0;
            double dC03175 = 0;
            double dC03232 = 0;
            double dC02637 = 0;
            double dC14463 = 0;
            double dC01179 = 0;
            double dC00267 = 0;
            double dC00449 = 0;
            double dC04272 = 0;
            double dC05560 = 0;
            double dC00417 = 0;
            double dC15602 = 0;
            double dC00041 = 0;
            double dC05378 = 0;
            double dC00149 = 0;
            double dC00124 = 0;
            double dC16254 = 0;
            double dC00183 = 0;
            double dC06032 = 0;
            double dC01036 = 0;
            double dC05381 = 0;
            double dC00033 = 0;
            double dC00035 = 0;
            double dC00044 = 0;
            double dC00530 = 0;
            double dC00104 = 0;
            double dC00081 = 0;
            double dC00404 = 0;
            double dC00068 = 0;

            double dC15972 = 0;
            double dC00004 = 0;
            double dC00322 = 0;
            double dC00065 = 0;
            double dC00005 = 0;
            double dC00047 = 0;
            double dC00008 = 0;
            double dC00020 = 0;
            double dC00139 = 0;
            double dC00164 = 0;
            double dC00062 = 0;
            double dC00279 = 0;
            double dC00407 = 0;
            double dC00138 = 0;
            double dC00024 = 0;
            double dC00027 = 0;
            double dC00251 = 0;
            double dC00049 = 0;
            double dC00082 = 0;
            double dC00169 = 0;
            double dC00029 = 0;
            double dC00086 = 0;
            double dC00075 = 0;
            double dC15603 = 0;
            double dC11482 = 0;
            double dC00016 = 0;

            dC00002 = checkBoundryCons(-(R01786 * C00002 * C00267) *1-(R04779 * C00002 * C05345) *1+(R01512 * C00008 * C00236) *1+(R00200 * C00008 * C00074) *1+(R00405 * C00008 * C00009 * C00091) *1-(R01092 * C00002 * C00984) *1-(R03098 * C00956 * C00002) *1-2*(R00149 * C00002 * C00002 * C00014 * C00011 * C00001) *1-(R01954 * C00002 * C00327 * C00049) *1-(R02412 * C00002 * C00493) *1+3*(R90001 * C00004 * C00008 * C00008 * C00008) *1+2*(R90002 * C01352 * C00008 * C00008) *1, rLow, rHigh);  //-0.0002,0.008  -0.00025, 0.004 -0.0001, 0.0001
            dC00001 = checkBoundryCons(-(R07159 * C00118 * C00001 * C00139 * C00139) *1+(R00658 * C00631) *1-(R00351 * C00024 * C00001 * C00036) *1+(R01325 * C00158) *1-(R01900 * C00417 * C00001) *1-(R01082 * C00122 * C00001) *1-(R00582 * C01005 * C00001) *1+(R04441 * C04272) *1+(R05070 * C06007) *1-(R07399 * C00024 * C00022 * C00001) *1+(R03896 * C02612) *1-(R03898 * C02226 * C00001) *1-(R00271 * C00024 * C00001 * C00026) *1+(R03444 * C01251) *1-(R04371 * C04002 * C00001) *1+(R02315 * C00025 * C04076 * C00005 * C00080) *1-(R00715 * C00449 * C00003 * C00001) *1-(R00149 * C00002 * C00002 * C00014 * C00011 * C00001) *1-(R00551 * C00062 * C00001) *1-(R01826 * C00074 * C00279 * C00001) *1+(R03084 * C00944) *1-(R00729 * C00082 * C00001 * C00007) *1-(R01364 * C01061 * C00001) *1-(R00713 * C00232 * C00003 * C00001) *1-(R00714 * C00232 * C00006 * C00001) *1, rLow, rHigh);
            dC00080 = checkBoundryCons((R01061 * C00118 * C00009 * C00003) *1+2*(R07159 * C00118 * C00001 * C00139 * C00139) *1+(R01899 * C00311 * C00006) *1+2*(R01197 * C00139 * C00139 * C00026 * C00010) *1+(R07618 * C15973 * C00003) *1+(R00342 * C00149 * C00003) *1+(R00209 * C00022 * C00010 * C00003) *1+(R01513 * C00197 * C00003) *1-(R04440 * C04181 * C00005 * C00080) *1-(R05068 * C14463 * C00005 * C00080) *1+(R00994 * C06032 * C00003) *1+(R01934 * C05662 * C00003) *1-(R04390 * C05535 * C00005 * C00080) *1-(R02315 * C00025 * C04076 * C00005 * C00080) *1+(R00715 * C00449 * C00003 * C00001) *1-(R02413 * C02637 * C00005 * C00080) *1+(R00713 * C00232 * C00003 * C00001) *1+(R00714 * C00232 * C00006 * C00001) *1, rLow, rHigh);
            dC00026 = checkBoundryCons((R00268 * C05379) *1-(R01197 * C00139 * C00139 * C00026 * C00010) *1+(R04173 * C03232 * C00025) *1+(R01214 * C00141 * C00025) *1+(R02199 * C00671 * C00025) *1-(R00271 * C00024 * C00001 * C00026) *1+(R01939 * C00322 * C00025) *1+(R00715 * C00449 * C00003 * C00001) *1-(R01648 * C00334 * C00026) *1, rLow, rHigh);
            dC00022 = checkBoundryCons((R00200 * C00008 * C00074) *1-(R00209 * C00022 * C00010 * C00003) *1-2*(R00226 * C00022 * C00022) *1-(R08648 * C00022 * C00109) *1-(R07399 * C00024 * C00022 * C00001) *1-(R10178 * C00334 * C00022) *1, rLow, rHigh);
            dC00009 = checkBoundryCons(-(R01061 * C00118 * C00009 * C00003) *1-(R00405 * C00008 * C00009 * C00091) *1+(R00582 * C01005 * C00001) *1+(R00149 * C00002 * C00002 * C00014 * C00011 * C00001) *1+(R01398 * C00169 * C00077) *1+(R01826 * C00074 * C00279 * C00001) *1+(R03083 * C04691) *1+(R03460 * C00074 * C03175) *1+(R01714 * C01269) *1, rLow, rHigh);
            dC00011 = checkBoundryCons((R00268 * C05379) *1+(R01197 * C00139 * C00139 * C00026 * C00010) *1+(R00209 * C00022 * C00010 * C00003) *1+(R00226 * C00022 * C00022) *1+(R08648 * C00022 * C00109) *1+(R00994 * C06032 * C00003) *1+(R01934 * C05662 * C00003) *1-(R00149 * C00002 * C00002 * C00014 * C00011 * C00001) *1+(R02521 * C01179 * C00007) *1+(R00261 * C00025) *1, rLow, rHigh);
            dC00003 = checkBoundryCons(-(R01061 * C00118 * C00009 * C00003) *1-(R07618 * C15973 * C00003) *1-(R00342 * C00149 * C00003) *1-(R00209 * C00022 * C00010 * C00003) *1-(R01513 * C00197 * C00003) *1-(R00994 * C06032 * C00003) *1-(R01934 * C05662 * C00003) *1-(R00715 * C00449 * C00003 * C00001) *1-(R00713 * C00232 * C00003 * C00001) *1+(R90001 * C00004 * C00008 * C00008 * C00008) *1, rLow, rHigh);
            dC00010 = checkBoundryCons((R00351 * C00024 * C00001 * C00036) *1-(R01197 * C00139 * C00139 * C00026 * C00010) *1-(R02570 * C00010 * C16254) *1+(R00405 * C00008 * C00009 * C00091) *1-(R00209 * C00022 * C00010 * C00003) *1+(R07399 * C00024 * C00022 * C00001) *1+(R00271 * C00024 * C00001 * C00026) *1, rLow, rHigh);
            dC00122 = checkBoundryCons((R02164 * C15602 * C00042) *1-(R01082 * C00122 * C00001) *1+(R01086 * C03406) *1+(R01364 * C01061 * C00001) *1, rLow, rHigh);
            dC00025 = checkBoundryCons(-(R04173 * C03232 * C00025) *1-(R01214 * C00141 * C00025) *1-(R02199 * C00671 * C00025) *1-(R01939 * C00322 * C00025) *1-(R02315 * C00025 * C04076 * C00005 * C00080) *1-(R00261 * C00025) *1+(R01648 * C00334 * C00026) *1, rLow, rHigh);
            dC00197 = checkBoundryCons((R07159 * C00118 * C00001 * C00139 * C00139) *1+(R01512 * C00008 * C00236) *1-(R01518 * C00197) *1-(R01513 * C00197 * C00003) *1, rLow, rHigh);
            dC00232 = checkBoundryCons((R01648 * C00334 * C00026) *1+(R10178 * C00334 * C00022) *1-(R00713 * C00232 * C00003 * C00001) *1-(R00714 * C00232 * C00006 * C00001) *1, rLow, rHigh);
            dC00118 = checkBoundryCons((R01070 * C05378) *1+(R01015 * C00111) *1-(R01061 * C00118 * C00009 * C00003) *1-(R07159 * C00118 * C00001 * C00139 * C00139) *1, rLow, rHigh);
            dC00074 = checkBoundryCons((R00658 * C00631) *1-(R00200 * C00008 * C00074) *1-(R01826 * C00074 * C00279 * C00001) *1-(R03460 * C00074 * C03175) *1, rLow, rHigh);
            dC00103 = checkBoundryCons((R00955 * C00029 * C00446) *1+(R00959 * C00668) *1-(R00289 * C00075 * C00103) *1, rLow, rHigh);
            dC00006 = checkBoundryCons(-(R01899 * C00311 * C00006) *1+(R04440 * C04181 * C00005 * C00080) *1+(R05068 * C14463 * C00005 * C00080) *1+(R04390 * C05535 * C00005 * C00080) *1+(R02315 * C00025 * C04076 * C00005 * C00080) *1+(R02413 * C02637 * C00005 * C00080) *1-(R00714 * C00232 * C00006 * C00001) *1, rLow, rHigh);
            dC00007 = checkBoundryCons(-(R00729 * C00082 * C00001 * C00007) *1-(R02521 * C01179 * C00007) *1-(R02519 * C00544 * C00007) *1, rLow, rHigh);
            dC05662 = checkBoundryCons((R04371 * C04002 * C00001) *1-(R01934 * C05662 * C00003) *1+(R09720 * C01251) *1, rLow, rHigh);
            dC00668 = checkBoundryCons((R01786 * C00002 * C00267) *1-(R02740 * C00668) *1-(R00959 * C00668) *1, rLow, rHigh);
            dC00013 = checkBoundryCons((R00289 * C00075 * C00103) *1+(R03098 * C00956 * C00002) *1+(R01954 * C00002 * C00327 * C00049) *1, rLow, rHigh);
            dC00042 = checkBoundryCons((R00405 * C00008 * C00009 * C00091) *1-(R02164 * C15602 * C00042) *1+(R00713 * C00232 * C00003 * C00001) *1+(R00714 * C00232 * C00006 * C00001) *1, rLow, rHigh);
            dC01251 = checkBoundryCons((R00271 * C00024 * C00001 * C00026) *1-(R03444 * C01251) *1-(R09720 * C01251) *1, rLow, rHigh);
            dC00111 = checkBoundryCons((R01070 * C05378) *1-(R01015 * C00111) *1, rLow, rHigh);
            dC00984 = checkBoundryCons((R10619 * C00124) *1-(R01092 * C00002 * C00984) *1, rLow, rHigh);
            dC05535 = checkBoundryCons((R04863 * C05560 * C11482) *1-(R04390 * C05535 * C00005 * C00080) *1, rLow, rHigh);
            dC04181 = checkBoundryCons((R05071 * C06010) *1-(R04440 * C04181 * C00005 * C00080) *1, rLow, rHigh);
            dC06006 = checkBoundryCons((R08648 * C00022 * C00109) *1-(R05069 * C06006) *1, rLow, rHigh);
            dC00944 = checkBoundryCons((R03083 * C04691) *1-(R03084 * C00944) *1, rLow, rHigh);
            dC00544 = checkBoundryCons((R02521 * C01179 * C00007) *1-(R02519 * C00544 * C00007) *1, rLow, rHigh);
            dC00493 = checkBoundryCons((R02413 * C02637 * C00005 * C00080) *1-(R02412 * C00002 * C00493) *1, rLow, rHigh);
            dC00671 = checkBoundryCons((R05070 * C06007) *1-(R02199 * C00671 * C00025) *1, rLow, rHigh);
            dC00077 = checkBoundryCons(-(R01398 * C00169 * C00077) *1+(R00551 * C00062 * C00001) *1, rLow, rHigh);
            dC00141 = checkBoundryCons((R04441 * C04272) *1-(R01214 * C00141 * C00025) *1, rLow, rHigh);
            dC00311 = checkBoundryCons((R01900 * C00417 * C00001) *1-(R01899 * C00311 * C00006) *1, rLow, rHigh);
            dC01269 = checkBoundryCons((R03460 * C00074 * C03175) *1-(R01714 * C01269) *1, rLow, rHigh);
            dC02226 = checkBoundryCons((R03896 * C02612) *1-(R03898 * C02226 * C00001) *1, rLow, rHigh);
            dC01061 = checkBoundryCons((R03181 * C01036) *1-(R01364 * C01061 * C00001) *1, rLow, rHigh);
            dC00036 = checkBoundryCons(-(R00351 * C00024 * C00001 * C00036) *1+(R00342 * C00149 * C00003) *1, rLow, rHigh);
            dC05345 = checkBoundryCons((R02740 * C00668) *1-(R04779 * C00002 * C05345) *1, rLow, rHigh);
            dC01005 = checkBoundryCons((R04173 * C03232 * C00025) *1-(R00582 * C01005 * C00001) *1, rLow, rHigh);
            dC00052 = checkBoundryCons((R00955 * C00029 * C00446) *1-(R00291 * C00052) *1, rLow, rHigh);
            dC15973 = checkBoundryCons((R02570 * C00010 * C16254) *1-(R07618 * C15973 * C00003) *1, rLow, rHigh);
            dC00956 = checkBoundryCons((R01939 * C00322 * C00025) *1-(R03098 * C00956 * C00002) *1, rLow, rHigh);
            dC00014 = checkBoundryCons(-(R00149 * C00002 * C00002 * C00014 * C00011 * C00001) *1+(R00729 * C00082 * C00001 * C00007) *1, rLow, rHigh);
            dC00158 = checkBoundryCons((R00351 * C00024 * C00001 * C00036) *1-(R01325 * C00158) *1, rLow, rHigh);
            dC00631 = checkBoundryCons((R01518 * C00197) *1-(R00658 * C00631) *1, rLow, rHigh);
            dC00109 = checkBoundryCons(-(R08648 * C00022 * C00109) *1+(R00994 * C06032 * C00003) *1, rLow, rHigh);
            dC04076 = checkBoundryCons((R04390 * C05535 * C00005 * C00080) *1-(R02315 * C00025 * C04076 * C00005 * C00080) *1, rLow, rHigh);
            dC03406 = checkBoundryCons((R01954 * C00002 * C00327 * C00049) *1-(R01086 * C03406) *1, rLow, rHigh);
            dC02612 = checkBoundryCons((R07399 * C00024 * C00022 * C00001) *1-(R03896 * C02612) *1, rLow, rHigh);
            dC06007 = checkBoundryCons((R05068 * C14463 * C00005 * C00080) *1-(R05070 * C06007) *1, rLow, rHigh);
            dC00236 = checkBoundryCons((R01061 * C00118 * C00009 * C00003) *1-(R01512 * C00008 * C00236) *1, rLow, rHigh);
            dC04691 = checkBoundryCons((R01826 * C00074 * C00279 * C00001) *1-(R03083 * C04691) *1, rLow, rHigh);
            dC00334 = checkBoundryCons((R00261 * C00025) *1-(R01648 * C00334 * C00026) *1-(R10178 * C00334 * C00022) *1, rLow, rHigh);
            dC00446 = checkBoundryCons((R01092 * C00002 * C00984) *1-(R00955 * C00029 * C00446) *1, rLow, rHigh);
            dC05379 = checkBoundryCons((R01899 * C00311 * C00006) *1-(R00268 * C05379) *1, rLow, rHigh);
            dC06010 = checkBoundryCons((R00226 * C00022 * C00022) *1-(R05071 * C06010) *1, rLow, rHigh);
            dC04002 = checkBoundryCons((R03444 * C01251) *1-(R04371 * C04002 * C00001) *1, rLow, rHigh);
            dC01352 = checkBoundryCons(-(R90002 * C01352 * C00008 * C00008) *1, rLow, rHigh);
            dC00327 = checkBoundryCons((R01398 * C00169 * C00077) *1-(R01954 * C00002 * C00327 * C00049) *1, rLow, rHigh);
            dC00091 = checkBoundryCons((R01197 * C00139 * C00139 * C00026 * C00010) *1+(R02570 * C00010 * C16254) *1-(R00405 * C00008 * C00009 * C00091) *1, rLow, rHigh);
            dC03175 = checkBoundryCons((R02412 * C00002 * C00493) *1-(R03460 * C00074 * C03175) *1, rLow, rHigh);
            dC03232 = checkBoundryCons((R01513 * C00197 * C00003) *1-(R04173 * C03232 * C00025) *1, rLow, rHigh);
            dC02637 = checkBoundryCons((R03084 * C00944) *1-(R02413 * C02637 * C00005 * C00080) *1, rLow, rHigh);
            dC14463 = checkBoundryCons((R05069 * C06006) *1-(R05068 * C14463 * C00005 * C00080) *1, rLow, rHigh);
            dC01179 = checkBoundryCons((R00729 * C00082 * C00001 * C00007) *1-(R02521 * C01179 * C00007) *1, rLow, rHigh);
            dC00267 = checkBoundryCons(-(R01786 * C00002 * C00267) *1, rLow, rHigh);  //-0.0002,0.008
            dC00449 = checkBoundryCons((R02315 * C00025 * C04076 * C00005 * C00080) *1-(R00715 * C00449 * C00003 * C00001) *1, rLow, rHigh);
            dC04272 = checkBoundryCons((R04440 * C04181 * C00005 * C00080) *1-(R04441 * C04272) *1, rLow, rHigh);
            dC05560 = checkBoundryCons((R03098 * C00956 * C00002) *1-(R04863 * C05560 * C11482) *1, rLow, rHigh);
            dC00417 = checkBoundryCons((R01325 * C00158) *1-(R01900 * C00417 * C00001) *1, rLow, rHigh);
            dC15602 = checkBoundryCons(-(R02164 * C15602 * C00042) *1, rLow, rHigh);
            dC00041 = checkBoundryCons((R10178 * C00334 * C00022) *1, rLow, rHigh);
            dC05378 = checkBoundryCons((R04779 * C00002 * C05345) *1-(R01070 * C05378) *1, rLow, rHigh);
            dC00149 = checkBoundryCons((R01082 * C00122 * C00001) *1-(R00342 * C00149 * C00003) *1, rLow, rHigh);
            dC00124 = checkBoundryCons(-(R10619 * C00124) *1, rLow, rHigh);
            dC16254 = checkBoundryCons(-(R02570 * C00010 * C16254) *1, rLow, rHigh);
            dC00183 = checkBoundryCons((R01214 * C00141 * C00025) *1, rLow, rHigh);
            dC06032 = checkBoundryCons((R03898 * C02226 * C00001) *1-(R00994 * C06032 * C00003) *1, rLow, rHigh);
            dC01036 = checkBoundryCons((R02519 * C00544 * C00007) *1-(R03181 * C01036) *1, rLow, rHigh);
            dC05381 = checkBoundryCons(0, rLow, rHigh);
            dC00033 = checkBoundryCons(0, rLow, rHigh);
            dC00035 = checkBoundryCons(0, rLow, rHigh);
            dC00044 = checkBoundryCons(0, rLow, rHigh);
            dC00530 = checkBoundryCons(0, rLow, rHigh);
            dC00104 = checkBoundryCons(0, rLow, rHigh);
            dC00081 = checkBoundryCons(0, rLow, rHigh);
            dC00404 = checkBoundryCons(0, rLow, rHigh);
            dC00068 = checkBoundryCons(0, rLow, rHigh);

            dC15972 = (dC15973 + dC00003 - dC00004 - dC00080);
            dC00004 = (dC15973 + dC00003 - dC15972 - dC00080);
            dC00322 = (dC00956 + dC00026 - dC00025);
            dC00065 = (dC01005 + dC00001 - dC00009);
            dC00005 = (dC04272 + dC00006 - dC04181 - dC00080);
            dC00047 = (dC00449 + dC00003 + dC00001 - dC00026 - dC00004 - dC00080);
            dC00008 = (dC00002 + dC00197 - dC00236);
            dC00020 = (dC05560 + dC11482 - dC05535);
            dC00139 = (dC00197 + 2 * dC00080 + 2 * dC00138 - dC00118 - dC00001)/2;
            dC00164 = (dC01061 + dC00001 - dC00122);
            dC00062 = (dC00077 + dC00086 - dC00001);
            dC00279 = (dC04691 + dC00009 - dC00074 - dC00001);
            dC00407 = (dC00671 + dC00025 - dC00026);
            dC00138 = (dC00118 + dC00001 + 2 * dC00139 - dC00197 - 2 * dC00080)/2;
            dC00024 = (dC00158 + dC00010 - dC00001 - dC00036);
            dC00027 = (dC00082 + dC00001 + dC00007 - dC01179 - dC00014);
            dC00251 = (dC01269 - dC00009);
            dC00049 = (dC00020 + dC00013 + dC03406 - dC00002 - dC00327);
            dC00082 = (dC01179 + dC00014 + dC00027 - dC00001 - dC00007);
            dC00169 = (dC00009 + dC00327 - dC00077);
            dC00029 = (dC00103 + dC00052 - dC00446);
            dC00086 = (dC00062 + dC00001 - dC00077);
            dC00075 = (dC00013 + dC00029 - dC00103);
            dC15603 = (dC15602 + dC00042 - dC00122);
            dC11482 = (dC05535 + dC00020 - dC05560);
            dC00016 = (dC01352 + 2 * dC00008 - 2 * dC00002);

            //Values
            C00002 = checkBoundry(C00002 + (dC00002)*dt, 0.0005, 0.4); // 0.0009, cHigh
            C00001 = checkBoundry(C00001 + (dC00001)*dt, 0, cHigh);
            C00080 = checkBoundry(C00080 + (dC00080)*dt, 0, cHigh);
            C00026 = checkBoundry(C00026 + (dC00026)*dt, 0, cHigh);
            C00022 = checkBoundry(C00022 + (dC00022)*dt, 0, cHigh);
            C00009 = checkBoundry(C00009 + (dC00009)*dt, 0, cHigh);
            C00011 = checkBoundry(C00011 + (dC00011)*dt, 0, cHigh);
            C00003 = checkBoundry(C00003 + (dC00003)*dt, 0, cHigh);
            C00010 = checkBoundry(C00010 + (dC00010)*dt, 0, cHigh);
            C00122 = checkBoundry(C00122 + (dC00122)*dt, 0, cHigh);
            C00025 = checkBoundry(C00025 + (dC00025)*dt, 0, cHigh);
            C00197 = checkBoundry(C00197 + (dC00197)*dt, 0, cHigh);
            C00232 = checkBoundry(C00232 + (dC00232)*dt, 0, cHigh);
            C00118 = checkBoundry(C00118 + (dC00118)*dt, 0, cHigh);
            C00074 = checkBoundry(C00074 + (dC00074)*dt, 0, cHigh);
            C00103 = checkBoundry(C00103 + (dC00103)*dt, 0, cHigh);
            C00006 = checkBoundry(C00006 + (dC00006)*dt, 0, cHigh);
            C00007 = checkBoundry(C00007 + (dC00007)*dt, 0, cHigh);
            C05662 = checkBoundry(C05662 + (dC05662)*dt, 0, cHigh);
            C00668 = checkBoundry(C00668 + (dC00668)*dt, 0, cHigh);
            C00013 = checkBoundry(C00013 + (dC00013)*dt, 0, cHigh);
            C00042 = checkBoundry(C00042 + (dC00042)*dt, 0, cHigh);
            C01251 = checkBoundry(C01251 + (dC01251)*dt, 0, cHigh);
            C00111 = checkBoundry(C00111 + (dC00111)*dt, 0, cHigh);
            C00984 = checkBoundry(C00984 + (dC00984)*dt, 0, cHigh);
            C05535 = checkBoundry(C05535 + (dC05535)*dt, 0, cHigh);
            C04181 = checkBoundry(C04181 + (dC04181)*dt, 0, cHigh);
            C06006 = checkBoundry(C06006 + (dC06006)*dt, 0, cHigh);
            C00944 = checkBoundry(C00944 + (dC00944)*dt, 0, cHigh);
            C00544 = checkBoundry(C00544 + (dC00544)*dt, 0, cHigh);
            C00493 = checkBoundry(C00493 + (dC00493)*dt, 0, cHigh);
            C00671 = checkBoundry(C00671 + (dC00671)*dt, 0, cHigh);
            C00077 = checkBoundry(C00077 + (dC00077)*dt, 0, cHigh);
            C00141 = checkBoundry(C00141 + (dC00141)*dt, 0, cHigh);
            C00311 = checkBoundry(C00311 + (dC00311)*dt, 0, cHigh);
            C01269 = checkBoundry(C01269 + (dC01269)*dt, 0, cHigh);
            C02226 = checkBoundry(C02226 + (dC02226)*dt, 0, cHigh);
            C01061 = checkBoundry(C01061 + (dC01061)*dt, 0, cHigh);
            C00036 = checkBoundry(C00036 + (dC00036)*dt, 0, cHigh);
            C05345 = checkBoundry(C05345 + (dC05345)*dt, 0, cHigh);
            C01005 = checkBoundry(C01005 + (dC01005)*dt, 0, cHigh);
            C00052 = checkBoundry(C00052 + (dC00052)*dt, 0, cHigh);
            C15973 = checkBoundry(C15973 + (dC15973)*dt, 0, cHigh);
            C00956 = checkBoundry(C00956 + (dC00956)*dt, 0, cHigh);
            C00014 = checkBoundry(C00014 + (dC00014)*dt, 0, cHigh);
            C00158 = checkBoundry(C00158 + (dC00158)*dt, 0, cHigh);
            C00631 = checkBoundry(C00631 + (dC00631)*dt, 0, cHigh);
            C00109 = checkBoundry(C00109 + (dC00109)*dt, 0, cHigh);
            C04076 = checkBoundry(C04076 + (dC04076)*dt, 0, cHigh);
            C03406 = checkBoundry(C03406 + (dC03406)*dt, 0, cHigh);
            C02612 = checkBoundry(C02612 + (dC02612)*dt, 0, cHigh);
            C06007 = checkBoundry(C06007 + (dC06007)*dt, 0, cHigh);
            C00236 = checkBoundry(C00236 + (dC00236)*dt, 0, cHigh);
            C04691 = checkBoundry(C04691 + (dC04691)*dt, 0, cHigh);
            C00334 = checkBoundry(C00334 + (dC00334)*dt, 0, cHigh);
            C00446 = checkBoundry(C00446 + (dC00446)*dt, 0, cHigh);
            C05379 = checkBoundry(C05379 + (dC05379)*dt, 0, cHigh);
            C06010 = checkBoundry(C06010 + (dC06010)*dt, 0, cHigh);
            C04002 = checkBoundry(C04002 + (dC04002)*dt, 0, cHigh);
            C01352 = checkBoundry(C01352 + (dC01352)*dt, 0, cHigh);
            C00327 = checkBoundry(C00327 + (dC00327)*dt, 0, cHigh);
            C00091 = checkBoundry(C00091 + (dC00091)*dt, 0, cHigh);
            C03175 = checkBoundry(C03175 + (dC03175)*dt, 0, cHigh);
            C03232 = checkBoundry(C03232 + (dC03232)*dt, 0, cHigh);
            C02637 = checkBoundry(C02637 + (dC02637)*dt, 0, cHigh);
            C14463 = checkBoundry(C14463 + (dC14463)*dt, 0, cHigh);
            C01179 = checkBoundry(C01179 + (dC01179)*dt, 0, cHigh);
            C00267 = checkBoundry(C00267 + (dC00267)*dt, 0.002, cHigh); // 0.005
            C00449 = checkBoundry(C00449 + (dC00449)*dt, 0, cHigh);
            C04272 = checkBoundry(C04272 + (dC04272)*dt, 0, cHigh);
            C05560 = checkBoundry(C05560 + (dC05560)*dt, 0, cHigh);
            C00417 = checkBoundry(C00417 + (dC00417)*dt, 0, cHigh);
            C15602 = checkBoundry(C15602 + (dC15602)*dt, 0, cHigh);
            C00041 = checkBoundry(C00041 + (dC00041)*dt, 0, cHigh);
            C05378 = checkBoundry(C05378 + (dC05378)*dt, 0, cHigh);
            C00149 = checkBoundry(C00149 + (dC00149)*dt, 0, cHigh);
            C00124 = checkBoundry(C00124 + (dC00124)*dt, 0, cHigh);
            C16254 = checkBoundry(C16254 + (dC16254)*dt, 0, cHigh);
            C00183 = checkBoundry(C00183 + (dC00183)*dt, 0, cHigh);
            C06032 = checkBoundry(C06032 + (dC06032)*dt, 0, cHigh);
            C01036 = checkBoundry(C01036 + (dC01036)*dt, 0, cHigh);
            C05381 = checkBoundry(C05381 + (dC05381)*dt, 0, cHigh);
            C00033 = checkBoundry(C00033 + (dC00033)*dt, 0, cHigh);
            C00035 = checkBoundry(C00035 + (dC00035)*dt, 0, cHigh);
            C00044 = checkBoundry(C00044 + (dC00044)*dt, 0, cHigh);
            C00530 = checkBoundry(C00530 + (dC00530)*dt, 0, cHigh);
            C00104 = checkBoundry(C00104 + (dC00104)*dt, 0, cHigh);
            C00081 = checkBoundry(C00081 + (dC00081)*dt, 0, cHigh);
            C00404 = checkBoundry(C00404 + (dC00404)*dt, 0, cHigh);
            C00068 = checkBoundry(C00068 + (dC00068)*dt, 0, cHigh);

            //Abnormals Estimated by Algebra
            C00322 = checkBoundry(C00322 + (dC00322)*dt, 0, cHigh);
            C00065 = checkBoundry(C00065 + (dC00065)*dt, 0, cHigh);
            C00005 = checkBoundry(C00005 + (dC00005)*dt, 0, cHigh);
            C00047 = checkBoundry(C00047 + (dC00047)*dt, 0, cHigh);
            C15972 = checkBoundry(C15972 + (dC15972)*dt, 0, cHigh);
            C00008 = checkBoundry(C00008 + (dC00008)*dt, 0, cHigh);
            C00020 = checkBoundry(C00020 + (dC00020)*dt, 0, cHigh);
            C00139 = checkBoundry(C00139 + (dC00139)*dt, 0, cHigh);
            C00164 = checkBoundry(C00164 + (dC00164)*dt, 0, cHigh);
            C00062 = checkBoundry(C00062 + (dC00062)*dt, 0, cHigh);
            C00279 = checkBoundry(C00279 + (dC00279)*dt, 0, cHigh);
            C00407 = checkBoundry(C00407 + (dC00407)*dt, 0, cHigh);
            C00138 = checkBoundry(C00138 + (dC00138)*dt, 0, cHigh);
            C00024 = checkBoundry(C00024 + (dC00024)*dt, 0, cHigh);
            C00027 = checkBoundry(C00027 + (dC00027)*dt, 0, cHigh);
            C00251 = checkBoundry(C00251 + (dC00251)*dt, 0, cHigh);
            C00049 = checkBoundry(C00049 + (dC00049)*dt, 0, cHigh);
            C00082 = checkBoundry(C00082 + (dC00082)*dt, 0, cHigh);
            C00169 = checkBoundry(C00169 + (dC00169)*dt, 0, cHigh);
            C00029 = checkBoundry(C00029 + (dC00029)*dt, 0, cHigh);
            C00086 = checkBoundry(C00086 + (dC00086)*dt, 0, cHigh);
            C00075 = checkBoundry(C00075 + (dC00075)*dt, 0, cHigh);
            C15603 = checkBoundry(C15603 + (dC15603)*dt, 0, cHigh);
            C00004 = checkBoundry(C00004 + (dC00004)*dt, 0, cHigh);
            C11482 = checkBoundry(C11482 + (dC11482)*dt, 0, cHigh);
            C00016 = checkBoundry(C00016 + (dC00016)*dt, 0, cHigh);

            //Update the Vectors
            vC00002.add(C00002);
            a[i][0] = C00002;
            vC00001.add(C00001);
            a[i][1] = C00001;
            vC00080.add(C00080);
            a[i][2] = C00080;
            vC00026.add(C00026);
            a[i][3] = C00026;
            vC00022.add(C00022);
            a[i][4] = C00022;
            vC00009.add(C00009);
            a[i][5] = C00009;
            vC00011.add(C00011);
            a[i][6] = C00011;
            vC00003.add(C00003);
            a[i][7] = C00003;
            vC00010.add(C00010);
            a[i][8] = C00010;
            vC00122.add(C00122);
            a[i][9] = C00122;
            vC00025.add(C00025);
            a[i][10] = C00025;
            vC00197.add(C00197);
            a[i][11] = C00197;
            vC00232.add(C00232);
            a[i][12] = C00232;
            vC00118.add(C00118);
            a[i][13] = C00118;
            vC00074.add(C00074);
            a[i][14] = C00074;
            vC00103.add(C00103);
            a[i][15] = C00103;
            vC00006.add(C00006);
            a[i][16] = C00006;
            vC00007.add(C00007);
            a[i][17] = C00007;
            vC05662.add(C05662);
            a[i][18] = C05662;
            vC00668.add(C00668);
            a[i][19] = C00668;
            vC00013.add(C00013);
            a[i][20] = C00013;
            vC00042.add(C00042);
            a[i][21] = C00042;
            vC01251.add(C01251);
            a[i][22] = C01251;
            vC00111.add(C00111);
            a[i][23] = C00111;
            vC00984.add(C00984);
            a[i][24] = C00984;
            vC05535.add(C05535);
            a[i][25] = C05535;
            vC04181.add(C04181);
            a[i][26] = C04181;
            vC06006.add(C06006);
            a[i][27] = C06006;
            vC00944.add(C00944);
            a[i][28] = C00944;
            vC00544.add(C00544);
            a[i][29] = C00544;
            vC00493.add(C00493);
            a[i][30] = C00493;
            vC00671.add(C00671);
            a[i][31] = C00671;
            vC00077.add(C00077);
            a[i][32] = C00077;
            vC00141.add(C00141);
            a[i][33] = C00141;
            vC00311.add(C00311);
            a[i][34] = C00311;
            vC01269.add(C01269);
            a[i][35] = C01269;
            vC02226.add(C02226);
            a[i][36] = C02226;
            vC01061.add(C01061);
            a[i][37] = C01061;
            vC00036.add(C00036);
            a[i][38] = C00036;
            vC05345.add(C05345);
            a[i][39] = C05345;
            vC01005.add(C01005);
            a[i][40] = C01005;
            vC00052.add(C00052);
            a[i][41] = C00052;
            vC15973.add(C15973);
            a[i][42] = C15973;
            vC00956.add(C00956);
            a[i][43] = C00956;
            vC00014.add(C00014);
            a[i][44] = C00014;
            vC00158.add(C00158);
            a[i][45] = C00158;
            vC00631.add(C00631);
            a[i][46] = C00631;
            vC00109.add(C00109);
            a[i][47] = C00109;
            vC04076.add(C04076);
            a[i][48] = C04076;
            vC03406.add(C03406);
            a[i][49] = C03406;
            vC02612.add(C02612);
            a[i][50] = C02612;
            vC06007.add(C06007);
            a[i][51] = C06007;
            vC00236.add(C00236);
            a[i][52] = C00236;
            vC04691.add(C04691);
            a[i][53] = C04691;
            vC00334.add(C00334);
            a[i][54] = C00334;
            vC00446.add(C00446);
            a[i][55] = C00446;
            vC05379.add(C05379);
            a[i][56] = C05379;
            vC06010.add(C06010);
            a[i][57] = C06010;
            vC04002.add(C04002);
            a[i][58] = C04002;
            vC01352.add(C01352);
            a[i][59] = C01352;
            vC00327.add(C00327);
            a[i][60] = C00327;
            vC00091.add(C00091);
            a[i][61] = C00091;
            vC03175.add(C03175);
            a[i][62] = C03175;
            vC03232.add(C03232);
            a[i][63] = C03232;
            vC02637.add(C02637);
            a[i][64] = C02637;
            vC14463.add(C14463);
            a[i][65] = C14463;
            vC01179.add(C01179);
            a[i][66] = C01179;
            vC00267.add(C00267);
            a[i][67] = C00267;
            vC00449.add(C00449);
            a[i][68] = C00449;
            vC04272.add(C04272);
            a[i][69] = C04272;
            vC05560.add(C05560);
            a[i][70] = C05560;
            vC00417.add(C00417);
            a[i][71] = C00417;
            vC15602.add(C15602);
            a[i][72] = C15602;
            vC00041.add(C00041);
            a[i][73] = C00041;
            vC05378.add(C05378);
            a[i][74] = C05378;
            vC00149.add(C00149);
            a[i][75] = C00149;
            vC00124.add(C00124);
            a[i][76] = C00124;
            vC16254.add(C16254);
            a[i][77] = C16254;
            vC00183.add(C00183);
            a[i][78] = C00183;
            vC06032.add(C06032);
            a[i][79] = C06032;
            vC01036.add(C01036);
            a[i][80] = C01036;
            vC05381.add(C05381);
            a[i][81] = C05381;
            vC00033.add(C00033);
            a[i][82] = C00033;
            vC00035.add(C00035);
            a[i][83] = C00035;
            vC00044.add(C00044);
            a[i][84] = C00044;
            vC00530.add(C00530);
            a[i][85] = C00530;
            vC00104.add(C00104);
            a[i][86] = C00104;
            vC00081.add(C00081);
            a[i][87] = C00081;
            vC00404.add(C00404);
            a[i][88] = C00404;
            vC00068.add(C00068);
            a[i][89] = C00068;

            //Abnormals Estimated by Algebra
            vC00322.add(C00322);
            a[i][90] = C00322;
            vC00065.add(C00065);
            a[i][91] = C00065;
            vC00005.add(C00005);
            a[i][92] = C00005;
            vC00047.add(C00047);
            a[i][93] = C00047;
            vC15972.add(C15972);
            a[i][94] = C15972;
            vC00008.add(C00008);
            a[i][95] = C00008;
            vC00020.add(C00020);
            a[i][96] = C00020;
            vC00139.add(C00139);
            a[i][97] = C00139;
            vC00164.add(C00164);
            a[i][98] = C00164;
            vC00062.add(C00062);
            a[i][99] = C00062;
            vC00279.add(C00279);
            a[i][100] = C00279;
            vC00407.add(C00407);
            a[i][101] = C00407;
            vC00138.add(C00138);
            a[i][102] = C00138;
            vC00024.add(C00024);
            a[i][103] = C00024;
            vC00027.add(C00027);
            a[i][104] = C00027;
            vC00251.add(C00251);
            a[i][105] = C00251;
            vC00049.add(C00049);
            a[i][106] = C00049;
            vC00082.add(C00082);
            a[i][107] = C00082;
            vC00169.add(C00169);
            a[i][108] = C00169;
            vC00029.add(C00029);
            a[i][109] = C00029;
            vC00086.add(C00086);
            a[i][110] = C00086;
            vC00075.add(C00075);
            a[i][111] = C00075;
            vC15603.add(C15603);
            a[i][112] = C15603;
            vC00004.add(C00004);
            a[i][113] = C00004;
            vC11482.add(C11482);
            a[i][114] = C11482;
            vC00016.add(C00016);
            a[i][115] = C00016;
        }
        return a;
    }
}
