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
public class PenLizard {
    private double _energy;
    private double _linearSpeed;
    private double _angularSpeed;
    private final Point2D.Double _position;
    private final Brain _brain;
    private double _rotation = Math.PI/2;
    public PenLizard(double startingX,double startingY, Brain startingBrain){
        _position = new Point2D.Double(startingX,startingY);
        _brain = startingBrain;
    }
    public void acceptStumuli(Shape[] worldGeom){
        final double increment = C.fieldOfVision/C.numberOfRods;
        
        final double offset = -C.fieldOfVision/2;
        for(int k = 0; k < C.numberOfRods; k++){
            double sightLine = _rotation + offset + k*increment;
            Point2D.Double sight = new Point2D.Double(Math.cos(sightLine) + _position.x, Math.sin(sightLine) + _position.y);
            for(int i = 0; i < worldGeom.length; i++){
                if(worldGeom[i].rayCollidesWith(_position.x, _position.y, sight.x, sight.y)){
                    _brain.addToNeuronPolarization(k, C.rodDetectPolAdd);
                    break;
                }
            }
        }
    }
    public void actOnStumuli(){
        _brain.updateBrain();
        if(_brain.getNeuronPolarization(50) > C.threshold){
            _rotation += C.lizardAngularSpeed;
        }
        if(_brain.getNeuronPolarization(51) > C.threshold){
            _rotation -= C.lizardAngularSpeed;
        }
        if(_brain.getNeuronPolarization(52) > C.threshold){
            _position.x += C.lizardLinearSpeed * Math.cos(_rotation);
            _position.y += C.lizardLinearSpeed * Math.sin(_rotation);
        }
    }
    public void updatePosition(double newX,double newY){
        _position.x = newX;
        _position.y = newY;
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
