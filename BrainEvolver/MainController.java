//TO DO
//-Implement ScoreCard interface
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
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTextField;

public final class MainController {
    private final Brain[] brains;
    private final ScoreComparator scoreComparator;
    private static final int BRAININDEX = 0;
    private static final int SCORE = 1;
    private JTextField commandField;
    private JFrame commandFieldFrame;
    private Keychecker keychecker;
    
    public MainController(){
        commandField = new JTextField();

        commandField.addKeyListener(keychecker =new Keychecker());
        
        commandFieldFrame = new JFrame();

        commandFieldFrame.add(commandField);

        commandFieldFrame.setSize(400, 350);

        commandFieldFrame.setVisible(true);
        
        String fileName = "temp.txt";
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
        catch(FileNotFoundException ex){
            System.out.println("File is missing, yo.");
        }
        catch(IOException Ex){
            
        }
        scoreComparator = new ScoreComparator();
        //Controls cycle of assesment and evolution.
        brains = new Brain[C.numberOfBrains];
        
        for(int i = 0; i < C.numberOfBrains; i++){
            brains[i] = new Brain();
        }
        
        assessForProximityAndSpeed();
        assessForConsistency();
        runBrains();
    }
    private void executeCommand(String command){
        System.out.print(command);
    }
    private void runBrains(){
        commandField.getText();
        commandField.setText("");
        keychecker.enterPressed();
        int count = 0;
        while(count < 300){
            System.out.println("______________________________");
            for(int i = 0; i < C.numberOfBrains; i++){
                brains[i] = resetBrain(brains[i]);
                brains[i].setScore(assessBrain3(brains[i]));
            }
            Arrays.sort(brains, scoreComparator);
            for(int i = 0; i < C.numberOfPassingBrains; i++){
                //score display here
                System.out.print("Index:" + i + "Score:");
                System.out.print(brains[i].getScore());
                System.out.println(brains[i].getName() +"*");               
            }
            count ++;
        }
    }
    private void assessForConsistency(){
        //Instead of testing purely for speed to the food, this assesment looks for consistent results, rewarding brains that get to the food reliably.
        final int HISTORY_LENGTH = 20;
        int count = 0;
        int booleanArrayCount = 0;
        Map brainScoreMap = new HashMap<>();
        for(int i = 0; i < C.numberOfBrains; i++){
            brainScoreMap.put(brains[i], new boolean[HISTORY_LENGTH]);
        }
        while(true){
            System.out.println("_______________________________");
            if(booleanArrayCount >= HISTORY_LENGTH){
                booleanArrayCount = 0;
            }
            for(int i = 0; i < C.numberOfBrains; i++){
                boolean[] successTracker = (boolean[])brainScoreMap.get(brains[i]);
                //A value greater than zero means the brain made it to the end
                if(assessBrain3(brains[i])>0){
                    successTracker[booleanArrayCount] = true;
                }
                else{
                    successTracker[booleanArrayCount] = false;
                }
                int successes = 0;
                for(Boolean success:successTracker){
                    if(success){
                       successes ++;
                    }
                }
                brains[i].setScore(successes);
            }
            Arrays.sort(brains, scoreComparator);
            evolveBrains();
            booleanArrayCount ++;
            count ++;
            
        }
    }
    private void runLizardPen(){
        PenLizard[] lizards = new PenLizard[C.minimumNumberOfLizards];
        for(int i = 0; i < C.minimumNumberOfLizards; i++){
            //lizards[i] = new PenLizard();
        }
        while(true){
        
        }
    }
    private void evolveBrains(){
        //This function should be called when the brain array is orgainized form best to worst, it will reward the best brains, turn the lesser brains into the offspring of the better brains.
        for(int i = 0; i < C.numberOfPassingBrains; i++){
            //score display here
            System.out.print("Index:" + i + "Score:");
            System.out.print(brains[i].getScore());
            System.out.println(brains[i].getName());
            //Clears score and polarization, so that brains don't start the next round charged.
            brains[i] = resetBrain(brains[i]);
        }
        int breedingBrainIndex = 0;
        int indexToChangeAt = C.rewardArray[0];
        for(int j = C.numberOfPassingBrains; j < C.numberOfBrains; j++){
            brains[j] = mutateBrain(brains[breedingBrainIndex],brains[j]);
            //if(breedingBrainIndex == 0) System.out.println("!");
            
            if(j-C.numberOfPassingBrains == indexToChangeAt){
                
                breedingBrainIndex ++;
                indexToChangeAt +=C.rewardArray[breedingBrainIndex];
            }
            //The brains that don't pass become a mutated version of one of the brains that did.
        }
    }
    private void assessForProximityAndSpeed(){
        int count = 0;
        while(count < 500)
        {
            System.out.println("____________________________");
            for(int i = 0; i < C.numberOfBrains; i++){
                brains[i].setScore(assessBrain3(brains[i]));
            }
             //Sort score here
            Arrays.sort(brains, scoreComparator);
            evolveBrains();
            count ++;
            if(keychecker.enterPressed())return;
        }
    }
    public Brain resetBrain(Brain brain){
        Brain brainToReset = brain;
        //brainToReset.setScore(0);
        for(int i = 0; i < C.numberOfNeurons; i++){
            brainToReset.getNeuron(i).dePolarize();
        }
        brain.setScore(0);
        return brainToReset;
    }
    public Brain mutateBrain(Brain parentBrain,Brain childBrain){
    
        for(int i = 0; i < C.numberOfNeurons; i++){
            Synapse[] mutatedNeuronSynapses = new Synapse[C.numberOfSynapses];
            
            for (int j = 0; j < C.numberOfSynapses; j++) {
                mutatedNeuronSynapses[j] = new Synapse();
                //if(C.percentChanceOfSynapseMutation >= ThreadLocalRandom.current().nextInt(0, 101)) {
                if(C.chanceOfSynapseMutation >= Math.random()){
                    mutatedNeuronSynapses[j].setNeuronID(ThreadLocalRandom.current().nextInt(0, C.numberOfNeurons));
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
    public int assessBrain(Brain rBrain){
        int score = 0;
        for (int i = 0; i < C.numberOfCyclesPerTest; i++){
            if(i % 20 == 0 && i < 201) rBrain.addToNeuronPolarization(1,50);
            rBrain.updateBrain();
            if(rBrain.getNeuronPolarization(2) > C.threshold)
            score ++;
        }
        return score;
    }
    public int assessBrain2(Brain rBrain){
        int score = 0;
        int mistakes = 0;
        int currentStimulatedNeuron = 0;
        //rBrain.addToNeuronPolarization(1,100);
        for (int i = 0; i < C.numberOfCyclesPerTest; i++){
            if(i%100 == 0){
                currentStimulatedNeuron  = ThreadLocalRandom.current().nextInt(0, 4);
            }
            if(i%4 == 0){
                rBrain.addToNeuronPolarization(currentStimulatedNeuron, 20);
            }
            switch(currentStimulatedNeuron){
                case 0:
                    if(rBrain.getNeuronPolarization(4) > 40) score ++;
                    if (rBrain.getNeuronPolarization(5) > 40 || rBrain.getNeuronPolarization(6) > 40 || rBrain.getNeuronPolarization(7) > 40) {
                        score -=40;
                        mistakes ++;
                    }
                    break;
                case 1:
                    if(rBrain.getNeuronPolarization(5) > 40) score ++;
                    if (rBrain.getNeuronPolarization(4) > 40 || rBrain.getNeuronPolarization(6) > 40 || rBrain.getNeuronPolarization(7) > 40) {
                        score -=40;
                        mistakes ++;
                    }
                    break;
                case 2:
                    if(rBrain.getNeuronPolarization(6) > 40) score ++;
                    if (rBrain.getNeuronPolarization(4) > 40 || rBrain.getNeuronPolarization(5) > 40 || rBrain.getNeuronPolarization(7) > 40){
                        score -=40;
                        mistakes ++;
                    }
                    break;
                case 3:
                    if(rBrain.getNeuronPolarization(7) > 40) score ++;
                    if (rBrain.getNeuronPolarization(4) > 40 || rBrain.getNeuronPolarization(5) > 40 || rBrain.getNeuronPolarization(6) > 40){
                        score -=40;
                        mistakes ++;
                    }
                    break;
            }
            rBrain.updateBrain();
        }
        //System.out.println("Mistakes:" + mistakes);
        return score;
       
    }
    
    public double assessBrain3(Brain rBrain){
        int distanceScore = 0;
        int timeScore = 0;
        double[] scores = new double[2];
        
        Point2D.Double foodPos = new Point2D.Double();
        PenLizard lizard = new PenLizard(0,0,rBrain);
        while(lizard.getPosition().distance(foodPos) < C.minFoodDistance || lizard.getPosition().distance(foodPos) > C.minFoodDistance+1){
            foodPos.x = C.roomWidth * Math.random();
            foodPos.y = C.roomHeight * Math.random();
        }
        Shape food = new Shape( new Point2D.Double[]{new Point2D.Double(-C.foodWidth/2,C.foodWidth/2), new Point2D.Double(C.foodWidth/2,C.foodWidth/2),
        new Point2D.Double(C.foodWidth/2,-C.foodWidth/2),new Point2D.Double(-C.foodWidth/2,-C.foodWidth/2)},true, foodPos);
        Shape [] worldGeom = new Shape[] {food};
        for (int i = 0; i < C.numberOfCyclesPerTest; i++){
            lizard.acceptStumuli(worldGeom);
            lizard.actOnStumuli();
            if(lizard.getPosition().distance(food.getLocation())<5){
                return C.numberOfCyclesPerTest - i;
            }
        }
        return -lizard.getPosition().distance(food.getLocation());
    }
}