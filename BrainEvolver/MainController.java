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
        while(count < 1000){
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
    public boolean findIntersect(Point2D.Double line1Point1, Point2D.Double line1Point2, Point2D.Double line2Point1, Point2D.Double line2Point2){
        double m1 = (line1Point1.y - line1Point2.y)/(line1Point1.x -line1Point2.x);
        double m2 = (line2Point1.y - line2Point2.y)/(line2Point1.x -line2Point2.x);
        if(m1-m2 == 0) return false;
        double b1 = line1Point1.y - m1*line1Point1.x;
        double b2 = line2Point1.y - m2*line2Point1.x;
        
        Point2D.Double intersectPoint = new Point2D.Double();
        intersectPoint.x = (b2 - b1)/(m1-m2);
        intersectPoint.y = m1*intersectPoint.x + b1;
        if(intersectPoint.x <= line2Point1.x != intersectPoint.x <= line2Point2.x && line1Point1.x > line1Point2.x == line1Point1.x > intersectPoint.x && line1Point1.y > line1Point2.y == line1Point1.y > intersectPoint.y){
            return true;
        }
        else return false;
        
    }
    public double assessBrain3(Brain rBrain){
        int distanceScore = 0;
        int timeScore = 0;
        double[] scores = new double[2];
        
        boolean positionChanged = true;
        double rotation = Math.PI/2;
        
        final double increment = C.fieldOfVision/C.numberOfRods;
        final double offset = -C.fieldOfVision/2;
        Point2D.Double food = new Point2D.Double(C.roomWidth * Math.random(), C.roomHeight * Math.random());
        Point2D.Double lizard = new Point2D.Double(0,0);
        while(lizard.distance(food) < C.minFoodDistance || lizard.distance(food) > C.minFoodDistance+1){
            food.x = C.roomWidth * Math.random();
            food.y = C.roomHeight * Math.random();
        }
        //System.out.println(lizard.distance(food));
        final Point2D.Double[] foodGeom = new Point2D.Double[4];
        foodGeom[0] = new Point2D.Double(-C.foodWidth/2+food.x,C.foodWidth/2+food.y);
        foodGeom[1] = new Point2D.Double(C.foodWidth/2+food.x,C.foodWidth/2+food.y);
        foodGeom[2] = new Point2D.Double(C.foodWidth/2+food.x,-C.foodWidth/2+food.y);
        foodGeom[3] = new Point2D.Double(-C.foodWidth/2+food.x,-C.foodWidth/2+food.y);
        
        for (int i = 0; i < C.numberOfCyclesPerTest; i++){
            if(positionChanged = true){
                //Code for updating what is "sees".
                //
                for(int k = 0; k < C.numberOfRods; k++){
                    double sightLine = rotation + offset + k*increment;
                    Point2D.Double sight = new Point2D.Double(Math.cos(sightLine) + lizard.x, Math.sin(sightLine) + lizard.y);
                    
                    for(int j = 0; j < foodGeom.length; j++){
                        if(findIntersect(lizard,sight,foodGeom[j],foodGeom[(j+1)%foodGeom.length])){
                            rBrain.addToNeuronPolarization(k, C.rodDetectPolAdd);
                            break;
                        }
                    }
                }
                positionChanged = false;
            }
            rBrain.updateBrain();
            if(rBrain.getNeuronPolarization(50) > C.threshold){
                rotation += C.lizardAngularSpeed;
            }
            if(rBrain.getNeuronPolarization(51) > C.threshold){
                rotation += C.lizardAngularSpeed;
            }
            if(rBrain.getNeuronPolarization(52) > C.threshold){
                lizard.x += C.lizardLinearSpeed * Math.cos(rotation);
                lizard.y += C.lizardLinearSpeed * Math.sin(rotation);
            }
            if(lizard.distance(food)<5){
                return C.numberOfCyclesPerTest - i;
            }
        }
        return -lizard.distance(food);
        
    }
}