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
   
    public static final double THRESHOLD = 40;
    public static final double POLARIZATION_LOSS_PER_CYCLE = 1;
    public static final int NUMBER_OF_SYNAPSES = 6;
    public static final int NUMBER_OF_NEURONS = 200; 
    public static final int NUMBER_OF_RODS = 9;
    public static final double FIELD_OF_VISION = Math.PI/2;
    public static final double FOOD_WIDTH = 5;
    public static final double ROD_DETECT_POLAR_ADD = 44;
    public static final double LIZARD_LINEAR_SPEED = .3;
    public static final double LIZARD_ANGULAR_SPEED = 20*Math.PI/180;
    public static final double MIN_FOOD_DISTANCE = 20;
    
    public static final double FIRED_TOGETHER_MULTIPLIER = 1.01;
    public static final double FIRED_APART_MULITPLIER = .99;
    //Mutation Constants
    public static final double CHANCE_OF_SYNAPSE_MUTATION = .004;
    //Evolution Constants
    public static final int NUMBER_OF_PASSING_BRAINS = 15;
    public static final double RATIO_OF_BRAINS_IN_NEXT_GEN = .3;
    public static final double CHANCE_BRAIN_SELECTION = .5;
    public static final double CHANCE_OF_BRAIN_COPY = .2;
    //Lizard Constants
    public static double ENERGY_REQUIRED_TO_SPLIT = 100;
    public static final double ENERGY_AT_WHICH_LIZARD_DIES = 0;
    public static final int NUMBER_OF_CHILDREN_A_LIZARD_HAS = 2;
    public static final int MINIMUM_NUMBER_OF_LIZARDS = 60;
    public static final double STARTING_ENERGY = 20;
    public static final double ENERGY_LOSS_PER_ACTION = .06;
    
    //Pen Constants
    public static final double PEN_WIDTH = 1000;
    public static final double PEN_HEIGHT = 400;
    public static int FRAMES_FOR_FOOD_TO_GROW = 30;
    public static final int NUMBER_OF_FOODS_IN_PEN = 1500;
    public static final double ENERGY_IN_FOOD = 30;
    
    //Render Constants
    public static final int LIZARD_RENDER_INDEX = 0;
    public static final int FOOD_RENDER_INDEX = 1;
}