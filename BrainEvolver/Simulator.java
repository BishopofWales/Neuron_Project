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
    private static final int BRAININDEX = 0;
    private static final int SCORE = 1;
    private final BlockingQueue _displayQueue;
    private BoolWrapper _paused;
    @Override
    public synchronized void run(){
        runLizardPen();
    }
    public Simulator(BlockingQueue displayQueue, BoolWrapper paused){
        String fileName = "temp.txt";
        _paused = paused;
        _displayQueue = displayQueue;
        try {
            // Assume default encoding.
            FileWriter fileWriter =
                new FileWriter(fileName);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            // Note that write() does not automatically
            // append a newline character.
            bufferedWriter.write("Hello now,");
            bufferedWriter.write(" here is some new text.");
            bufferedWriter.newLine();
            bufferedWriter.write("We are writing");
            bufferedWriter.write(" the text to the file.");

            // Always close files.
            bufferedWriter.close();
        }
        catch(IOException f) {
            System.out.println(
                "Error writing to file '"
                + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
        BufferedReader testReader;
        try{
            FileReader testFileReader = new FileReader("text.txt");
            testReader =  new BufferedReader(testFileReader);
            while(testReader.ready()){
                System.out.println(testReader.readLine());
            }
            testReader.close();
        }
        catch(FileNotFoundException e){
            System.out.println("File is missing, yo.");
        }
        catch(IOException e){
            
        }
        scoreComparator = new ScoreComparator();
    }
    private void runLizardPen(){
        ArrayList<Lizard> lizards;
        Shape[] worldGeom;
        Point2D.Double[][] objectsToRender = new Point2D.Double[10][];
        objectsToRender[C.LIZARD_RENDER_INDEX] = new Point2D.Double[50000];
        objectsToRender[C.FOOD_RENDER_INDEX] = new Point2D.Double[C.NUMBER_OF_FOODS_IN_PEN];
        Map<Shape,Food> foodShapeFoodMap = new HashMap();
        lizards = new ArrayList();
        worldGeom = new Shape[C.NUMBER_OF_FOODS_IN_PEN];
        for(int i = 0; i < C.MINIMUM_NUMBER_OF_LIZARDS; i++){
            Brain newBrain =  new Brain();
            newBrain.randomizeSynapses();
            lizards.add(new Lizard(Math.random()*C.PEN_WIDTH, Math.random()*C.PEN_HEIGHT,newBrain));
        }
        for(int i = 0; i < worldGeom.length; i++){
            Point2D.Double foodPos = new Point2D.Double(Math.random()*C.PEN_WIDTH, Math.random()*C.PEN_HEIGHT);
            worldGeom[i] = new Shape( new Point2D.Double[]{new Point2D.Double(-C.FOOD_WIDTH/2,C.FOOD_WIDTH/2), new Point2D.Double(C.FOOD_WIDTH/2,C.FOOD_WIDTH/2),
            new Point2D.Double(C.FOOD_WIDTH/2,-C.FOOD_WIDTH/2),new Point2D.Double(-C.FOOD_WIDTH/2,-C.FOOD_WIDTH/2)},true, foodPos);
            foodShapeFoodMap.put(worldGeom[i], new Food());
        }
        int frameCount = 0;
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
                if(lizards.get(i).hasSplit()){ 
                    lizards.add(new Lizard(Math.random()*C.PEN_WIDTH, Math.random()*C.PEN_HEIGHT,mutateBrain(lizards.get(i).getBrain(),new Brain())));
                    lizards.add(new Lizard(Math.random()*C.PEN_WIDTH, Math.random()*C.PEN_HEIGHT,mutateBrain(lizards.get(i).getBrain(),new Brain())));
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
            if(lizards.size() < C.MINIMUM_NUMBER_OF_LIZARDS){
                Brain[] lizardBrains = new Brain[C.MINIMUM_NUMBER_OF_LIZARDS];
                
                for(int i = 0; i < lizards.size(); i++){
                    lizardBrains[i] = lizards.get(i).getBrain();
                }
                for(int i = lizards.size(); i < C.MINIMUM_NUMBER_OF_LIZARDS; i++){
                    lizardBrains[i] = lizards.get(ThreadLocalRandom.current().nextInt(0,lizards.size())).getBrain();
                }
                assessForProximityAndSpeed(lizardBrains);
                lizards.clear();
                for(int i = 0; i < C.MINIMUM_NUMBER_OF_LIZARDS; i++){
                    lizards.add(new Lizard(Math.random()*C.PEN_WIDTH, Math.random()*C.PEN_HEIGHT, lizardBrains[i]));
                }
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
    private void assessForProximityAndSpeed(Brain[] brains){
        int count = 0;
        while(count < 1){
            System.out.println("____________________________");
            for(int i = 0; i < brains.length; i++){
                brains[i].setScore(assessBrain3(brains[i]));
            }
             //Sort score here
            Arrays.sort(brains, scoreComparator);
            for(int i = 0; i < 10;i++){
                System.out.println(brains[i].getScore());
            }
            evolveBrains(brains);
            count ++;
        }
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
    
    public double assessBrain3(Brain rBrain){
        int distanceScore = 0;
        int timeScore = 0;
        double[] scores = new double[2];
        
        Point2D.Double foodPos = new Point2D.Double();
        Lizard lizard = new Lizard(0,0,rBrain);
        while(lizard.getPosition().distance(foodPos) < C.MIN_FOOD_DISTANCE || lizard.getPosition().distance(foodPos) > C.MIN_FOOD_DISTANCE+1){
            foodPos.x = C.ROOM_WIDTH * Math.random();
            foodPos.y = C.ROOM_HEIGHT * Math.random();
        }
        Shape food = new Shape( new Point2D.Double[]{new Point2D.Double(-C.FOOD_WIDTH/2,C.FOOD_WIDTH/2), new Point2D.Double(C.FOOD_WIDTH/2,C.FOOD_WIDTH/2),
        new Point2D.Double(C.FOOD_WIDTH/2,-C.FOOD_WIDTH/2),new Point2D.Double(-C.FOOD_WIDTH/2,-C.FOOD_WIDTH/2)},true, foodPos);
        Shape [] worldGeom = new Shape[] {food};
        for (int i = 0; i < C.NUMBER_OF_CYCLES_PER_TEST; i++){
            lizard.acceptStumuli(worldGeom);
            lizard.actOnStumuli();
            if(lizard.getPosition().distance(food.getLocation())<5){
                return C.NUMBER_OF_CYCLES_PER_TEST - i;
            }
        }
        return -lizard.getPosition().distance(food.getLocation());
    }
}