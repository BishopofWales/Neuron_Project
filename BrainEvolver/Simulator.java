//TO DO
//-Finish lizard pen
//-
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
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public final class Simulator extends Thread{
    //private final Brain[] brains;
    private final ScoreComparator scoreComparator;
    private final BlockingQueue _displayQueue;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private final BoolWrapper _paused;
    private ArrayList<Lizard> lizards;
    @Override
    public synchronized void run(){
        runLizardPen();
    }
    public Simulator(BlockingQueue displayQueue, BoolWrapper paused){
        String fileName = "temp.txt";
        _paused = paused;
        _displayQueue = displayQueue;
        
        scoreComparator = new ScoreComparator();
    }
    public void loadLizards(String fileName){
        try{
            FileReader testFileReader = new FileReader("text.txt");
            bufferedReader =  new BufferedReader(testFileReader);
            while(bufferedReader.ready()){
                System.out.println(bufferedReader.readLine());
            }
            bufferedReader.close();
        }
        catch(FileNotFoundException e){
            System.out.println("File is missing, yo.");
        }
        catch(IOException e){
            
        }
    }
    public void saveLizards(String fileName){
        try {
               // Assume default encoding.
               fileWriter = new FileWriter(fileName);
               
               // Always wrap FileWriter in BufferedWriter.
               bufferedWriter = new BufferedWriter(fileWriter);
               for(Lizard lizard:lizards){
                   bufferedWriter.write("*");
                   Neuron[] neuronsToWrite = lizard.getBrain().getNeurons();
                   bufferedWriter.newLine();
                   bufferedWriter.write(neuronsToWrite.length);
                   for(int i = 0; i < neuronsToWrite.length; i++){
                       bufferedWriter.write("**");
                       bufferedWriter.newLine();
                       Synapse[] neuronSynapses = neuronsToWrite[i].getSynapses();
                       bufferedWriter.write(neuronSynapses.length);
                       for(int k = 0; k < neuronSynapses.length; k++){
                           bufferedWriter.write(neuronSynapses[k].getNeuronID());
                           bufferedWriter.newLine();
                       }
                       bufferedWriter.newLine();
                   }
                   bufferedWriter.newLine();
               }
               // Always close files.
               bufferedWriter.close();
           }
           catch(IOException f) {
               System.out.println("Error writing to file '"+ fileName + "'");
               // Or we could just do this:
               // ex.printStackTrace();
           }

    }
    private void runLizardPen(){
        Shape[] worldGeom;
        Point2D.Double[][] objectsToRender = new Point2D.Double[10][];
        objectsToRender[C.LIZARD_RENDER_INDEX] = new Point2D.Double[50000];
        objectsToRender[C.FOOD_RENDER_INDEX] = new Point2D.Double[C.NUMBER_OF_FOODS_IN_PEN];
        Map<Shape,Food> foodShapeFoodMap = new HashMap();
        lizards = new ArrayList();
        int frameCount = 0;
        worldGeom = new Shape[C.NUMBER_OF_FOODS_IN_PEN];
        for(int i = 0; i < C.MINIMUM_NUMBER_OF_LIZARDS; i++){
            Brain newBrain =  new Brain();
            newBrain.randomizeSynapses();
            lizards.add(new Lizard(Math.random()*C.PEN_WIDTH, Math.random()*C.PEN_HEIGHT,newBrain,frameCount));
        }
        for(int i = 0; i < worldGeom.length; i++){
            Point2D.Double foodPos = new Point2D.Double(Math.random()*C.PEN_WIDTH, Math.random()*C.PEN_HEIGHT);
            worldGeom[i] = new Shape( new Point2D.Double[]{new Point2D.Double(-C.FOOD_WIDTH/2,C.FOOD_WIDTH/2), new Point2D.Double(C.FOOD_WIDTH/2,C.FOOD_WIDTH/2),
            new Point2D.Double(C.FOOD_WIDTH/2,-C.FOOD_WIDTH/2),new Point2D.Double(-C.FOOD_WIDTH/2,-C.FOOD_WIDTH/2)},true, foodPos);
            foodShapeFoodMap.put(worldGeom[i], new Food());
        }
        
        while(true){
            for(int i = 0; i < lizards.size(); i++){
                lizards.get(i).acceptStumuli(worldGeom);
            }
            for(int i = 0; i < lizards.size();i++){
                lizards.get(i).actOnStumuli();
                for(Shape food:worldGeom){
                    Food refFood = (Food)foodShapeFoodMap.get(food);
                    if(refFood.readyToGrowAgain(frameCount)){
                        food.visible = true;
                        food.changePosition(Math.random()*C.PEN_WIDTH,Math.random()*C.PEN_HEIGHT);
                    }
                    if(lizards.get(i).getPosition().distance(food.getLocation()) < 5 && food.visible){
                        food.visible = false;
                        refFood.eaten(frameCount);
                        lizards.get(i).setEnergy(lizards.get(i).getEnergy()+C.ENERGY_IN_FOOD);
                        //System.out.println("ate food");
                    }
                }
                if(lizards.get(i).hasSplit(frameCount)){ 
                    lizards.add(new Lizard(Math.random()*C.PEN_WIDTH, Math.random()*C.PEN_HEIGHT,mutateBrain(lizards.get(i).getBrain(),new Brain()),frameCount));
                    lizards.add(new Lizard(Math.random()*C.PEN_WIDTH, Math.random()*C.PEN_HEIGHT,mutateBrain(lizards.get(i).getBrain(),new Brain()),frameCount));
                    lizards.remove(lizards.get(i));
                    i--;
                }
                else if(lizards.get(i).hasDied()){
                    lizards.remove(lizards.get(i));
                    i--;
                }
            }
            
            //If the number of lizards falls below a certain amount
            //The program puts the lizard into another type of assesment, which selects for lizards that move towards food
            //The hope being that this will provide a baseline lizard which will be able to produce some offspring in the real lizardpen
            while(lizards.size() < C.MINIMUM_NUMBER_OF_LIZARDS){
                
                Brain newBrain = new Brain();
                newBrain.randomizeSynapses();
                lizards.add(new Lizard(Math.random()*C.PEN_WIDTH, Math.random()*C.PEN_HEIGHT, newBrain,frameCount));
            }
            frameCount ++;
            System.out.println(lizards.size());
            {
                int count = 0;
                for (Shape shape : worldGeom) {
                    if (shape.visible) {
                        objectsToRender[C.FOOD_RENDER_INDEX][count] = shape.getLocation();
                        count++;
                    }
                }
            }
            for(int i = 0; i < lizards.size();i++){
                objectsToRender[C.LIZARD_RENDER_INDEX][i] = lizards.get(i).getPosition();
            }
            _displayQueue.offer(objectsToRender);
            while(_paused.getBoolean()){
                try{
                    this.sleep(1000);
                }
                catch(InterruptedException e){
                    
                }
            }
            
        }
    }
   
    private void evolveBrains(Brain[] brains){
        
        int numberOfBrainsInNextGen = (int)(brains.length/C.RATIO_OF_BRAINS_IN_NEXT_GEN);
        ArrayList<Brain> brainList = new ArrayList(Arrays.asList(brains));
        for(int i = 0; i < numberOfBrainsInNextGen;i++){
            for(int k = 0; k < brainList.size();k++){
                if(Math.random()< C.CHANCE_BRAIN_SELECTION){
                    brains[i] = brainList.get(k);
                    brainList.remove(k);
                    break;
                }
                else if(k == brainList.size()-1){
                    brains[i] = brainList.get(k);
                    brainList.remove(k);
                    break;
                }
            }
        }
        for(int i = numberOfBrainsInNextGen; i < brains.length; i++){
            for(int k  = 0; k < numberOfBrainsInNextGen; k++){
                if(Math.random() < C.CHANCE_OF_BRAIN_COPY){
                    brains[i] = mutateBrain(brains[k],brains[i]);
                    break;
                }
                else if(k == numberOfBrainsInNextGen){
                    brains[i] = mutateBrain(brains[k],brains[i]);
                    break;
                }
            }
        }
        //This function should be called when the brain array is orgainized form best to worst, it will reward the best brains, turn the lesser brains into the offspring of the better brains.
        
            //The brains that don't pass become a mutated version of one of the brains that did.
        
    }
    private Brain resetBrain(Brain brain){
        Brain brainToReset = brain;
        for(int i = 0; i < C.NUMBER_OF_NEURONS; i++){
            brainToReset.getNeuron(i).dePolarize();
            
        }
        brain.setScore(0);
        return brainToReset;
    }
    private Brain mutateBrain(Brain parentBrain,Brain childBrain){
        
        for(int i = 0; i < C.NUMBER_OF_NEURONS; i++){
            Synapse[] mutatedNeuronSynapses = new Synapse[C.NUMBER_OF_SYNAPSES];
            
            for (int j = 0; j < C.NUMBER_OF_SYNAPSES; j++) {
                mutatedNeuronSynapses[j] = new Synapse();
                //if(C.percentChanceOfSynapseMutation >= ThreadLocalRandom.current().nextInt(0, 101)) {
                if(C.CHANCE_OF_SYNAPSE_MUTATION >= Math.random()){
                    mutatedNeuronSynapses[j].setNeuronID(ThreadLocalRandom.current().nextInt(0, C.NUMBER_OF_NEURONS));
                }
                else {
                    mutatedNeuronSynapses[j].setNeuronID(parentBrain.getNeuron(i).getSynapses()[j].getNeuronID());
                }
                //Remeber to copy connection strength here whenever you get around to implementing that...
                mutatedNeuronSynapses[j].setConnectionStrength(20);
            }
            childBrain.getNeuron(i).setSynapses(mutatedNeuronSynapses);
            resetBrain(childBrain);
        }
        return childBrain;
    }
}