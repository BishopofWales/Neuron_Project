/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BrainEvolver;

/**
 *
 * @author Michael
 */
public class C {
    //It seems decreasing mutation change and increasing population size works best..(maybe)
    public static int maximumNeurons = 100000;
    public static int maximumSynapses = 160;
   
    public static int numberOfCyclesPerTest = 80;
    public static double threshold = 40;
    public static double polarizationLossPerCycle = 1;
    public static int numberOfSynapses = 6;
    public static int numberOfNeurons = 200;//1000; 
    public static float roomWidth = 75;
    public static float roomHeight = 75;
    public static int numberOfRods = 8;
    public static double fieldOfVision = (float)Math.PI/2f;
    public static double foodWidth = 5;
    public static double rodDetectPolAdd = 44;
    public static double lizardLinearSpeed = .3;
    public static double lizardAngularSpeed = 20*Math.PI/180;
    public static double minFoodDistance = 20;
    
    public static double firedTogetherMultiplier = 1.01;
    public static double firedApartMultiplier = .99;
    //Mutation Constants
    public static double chanceOfSynapseMutation = .02;
    //Evolution Constants
    public static int numberOfBrains = 110;
    public static int numberOfPassingBrains = 15;
    public static double portionOfBrainsForNumberOne = .4;
    public static double proportionReductionPerPlace = .4;
    public static int[] rewardArray = {40,20,10,5,5,3,2,2,2,1,1,1,1,1,1};
    public static double chanceOfLizardBeingFemale = .5;
    
    //Lizard Constants
    public static double energyRequiredToSplit = 270;
    public static double energyAtWhichALizardDies = 0;
    public static int numberOfChildrenALizardHas = 2;
    public static int minimumNumberOfLizards = 10;
    public static double startingEnergy = 20;
    public static double energyLossPerAction = .12;
    
    //Pen Constants
    public static double penWidth = 18000;
    public static double penHeight = 18000;
    public static int framesForFoodToGrow = 40;
    public static int numberOfFoodsInPen = 100;
    public static double energyInFood = 30;
}