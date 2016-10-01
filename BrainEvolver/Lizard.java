/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BrainEvolver;

import java.awt.geom.Point2D;

/**
 *
 * @author Michael
 */
public class Lizard {
    private double _energy;
    private double _linearSpeed;
    private double _angularSpeed;
    private final Point2D.Double _position;
    public Lizard(double startingX,double startingY){
        _position = new Point2D.Double(startingX,startingY);
    }
    public void acceptStumuli(Shape[] worldGeom){
        
    }
    public void updatePosition(double newX,double newY){
        _position.x = newX;
        _position.y = newY;
    }
    public void actOnStumuli(){
    
    }
    public Point2D.Double getPosition(){
        return  _position;
    }
    public boolean hasDied(){
        if(_energy < C.energyAtWhichALizardDies){
            return true;
        }
        else return false;
    }
    public boolean hasSplit(){
        if(_energy > C.energyRequiredToSplit){
            return true;
        }
        else return false;
    }
    
}
