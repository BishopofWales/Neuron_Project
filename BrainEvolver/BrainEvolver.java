//Bug: Dead lizard's pos can hang around if the population doesn't reach the same height
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BrainEvolver;

import java.awt.geom.Point2D;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.shape.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import java.util.ArrayList;
public class BrainEvolver extends Application {
     static Simulator mainSimulator;  
    /**
     * @param args the command line arguments
     */
    private static BlockingQueue posData;
    private static BoolWrapper paused;
    public static void main(String[] args) {
        posData = new ArrayBlockingQueue(1);
        paused = new BoolWrapper();
        mainSimulator =  new Simulator(posData,paused);
        mainSimulator.start();
        launch();
    }
    @Override
    public void start(Stage theStage){
        theStage.setTitle("Hello, World!");
        
        Group root = new Group();
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );
        Canvas canvas = new Canvas( 2000, 1000 );
        root.getChildren().add( canvas );
        GraphicsContext gc = canvas.getGraphicsContext2D();
        final long startNanoTime = System.nanoTime();
        ArrayList<String> input = new ArrayList();
        theScene.setOnKeyPressed(
        new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent e)
            {
                System.out.println("pressed");
                String code = e.getCode().toString();
                if ( !input.contains(code) )
                    input.add( code );
                if(code.equals("P")){
                    //if(paused.getBoolean())
                        //mainSimulator.notify();
                    
                    //else 
                    paused.setBoolean(!paused.getBoolean());
                    //System.out.println(paused.toString());
                }
            }
        });
        theScene.setOnKeyReleased(
        new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent e)
            {
                System.out.println("released");
                String code = e.getCode().toString();
                input.remove( code );
            }
        });
        new AnimationTimer()
        {
            @Override
            public void handle(long currentNanoTime)
            {
                if(!posData.isEmpty()){
                    Point2D.Double[][] objectsToRender;
                    try{
                        objectsToRender = (Point2D.Double[][])posData.take();
                    }
                    catch(InterruptedException e){
                        objectsToRender = null;
                    }
                    gc.setFill(Color.WHITE);
                    gc.fillRect(0, 0, 10000, 10000);
                    gc.setFill(Color.RED);
                    for(Point2D.Double lizard:objectsToRender[C.LIZARD_RENDER_INDEX]){
                          if(lizard!= null)gc.fillOval(lizard.x, lizard.y, 2.5, 2.5);
                          else break;
                    }
                    gc.setFill(Color.GREEN);
                    for(Point2D.Double food:objectsToRender[C.FOOD_RENDER_INDEX]){
                        if(food!= null)gc.fillOval(food.x, food.y, 2.5, 2.5);
                        else break;
                    }
                }
            }
        }.start();
        theStage.show();
    }
}