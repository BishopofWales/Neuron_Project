/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//
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
public class BrainEvolver extends Application {
     static Simulator mainSimulator;  
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        mainSimulator =  new Simulator();
        mainSimulator.start();
        launch();
    }

    @Override
    public void start(Stage theStage){
        theStage.setTitle("Hello, World!");
        
        Group root = new Group();
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );
 
        Canvas canvas = new Canvas( 1000, 512 );
        root.getChildren().add( canvas );
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image earth = new Image( "earth.png" );
        Image sun   = new Image( "sun.png" );
        Image space = new Image( "space.png" );
        final long startNanoTime = System.nanoTime();
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, 10000, 10000);
                gc.setFill(Color.RED);
                
                Point2D.Double[] liveLizards = mainSimulator.getLizardPos();
                for(Point2D.Double lizardPos:liveLizards){
                     gc.fillOval(lizardPos.x, lizardPos.y, 30, 30);
                }
                gc.setFill(Color.GREEN);
                Point2D.Double[] foods = mainSimulator.getFoodPos();
                for(Point2D.Double food:foods){
                    gc.fillOval(food.x, food.y, 30, 30);
                }
            }
        }.start();

        theStage.show();
    }
 
}
