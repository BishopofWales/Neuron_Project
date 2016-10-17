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
    public static final int MAXIMUM_NEURONS = 100000;
    public static final int MAXIMUM_SYNAPSES = 160;
   
    public static final int NUMBER_OF_CYCLES_PER_TEST = 80;
    public static final double THRESHOLD = 40;
    public static final double POLARIZATION_LOSS_PER_CYCLE = 1;
    public static final int numberOfSynapses = 6;
    public static final int numberOfNeurons = 200;//1000; 
    public static final float ROOM_WIDTH = 75;
    public static final float ROOM_HEIGHT = 75;
    public static final int NUMBER_OF_RODS = 9;
    public static final double FIELD_OF_VISION = (float)Math.PI/2f;
    public static final double FOOD_WIDTH = 5;
    public static final double ROD_DETECT_POLAR_ADD = 44;
    public static final double lizardLinearSpeed = .3;
    public static final double lizardAngularSpeed = 20*Math.PI/180;
    public static final double minFoodDistance = 20;
    
    public static final double firedTogetherMultiplier = 1.01;
    public static final double firedApartMultiplier = .99;
    //Mutation Constants
    public static final double chanceOfSynapseMutation = .02;
    //Evolution Constants
    public static final int numberOfBrains = 60;
    public static final int numberOfPassingBrains = 15;
    //Lizard Constants
    public static final double energyRequiredToSplit = 100;
    public static final double energyAtWhichALizardDies = 0;
    public static final int NUMBER_OF_CHILDREN_A_LIZARD_HAS = 2;
    public static final int MINIMUM_NUMBER_OF_LIZARDS = 60;
    public static final double STARTING_ENERGY = 20;
    public static final double ENERGY_LOSS_PER_ACTION = .12;
    
    //Pen Constants
    public static final double PEN_WIDTH = 1000;
    public static final double PEN_HEIGHT = 400;
    public static final int FRAMES_FOR_FOOD_TO_GROW = 40;
    public static final int NUMBER_OF_FOODS_IN_PEN = 1000;
    public static final double ENERGY_IN_FOOD = 30;
    
    //Render Constants
    public static final int LIZARD_RENDER_INDEX = 0;
    public static final int FOOD_RENDER_INDEX = 1;
}