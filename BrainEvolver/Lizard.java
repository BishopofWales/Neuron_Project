
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
    private final Brain _brain;
    private double _rotation = Math.PI/2;
    public Lizard(double startingX,double startingY, Brain startingBrain){
        _position = new Point2D.Double(startingX,startingY);
        _brain = startingBrain;
        _energy = C.STARTING_ENERGY;
    }
    public Brain getBrain(){
        return _brain;
    }
    public void acceptStumuli(Shape[] worldGeom){
        final double increment = C.FIELD_OF_VISION/C.NUMBER_OF_RODS;
        
        final double offset = -C.FIELD_OF_VISION/2;
        for(int k = 0; k < C.NUMBER_OF_RODS; k++){
            double sightLine = _rotation + offset + k*increment;
            Point2D.Double sight = new Point2D.Double(Math.cos(sightLine) + _position.x, Math.sin(sightLine) + _position.y);
            for(int i = 0; i < worldGeom.length; i++){
                if(worldGeom[i].rayCollidesWith(_position.x, _position.y, sight.x, sight.y)){
                    _brain.addToNeuronPolarization(k, C.ROD_DETECT_POLAR_ADD);
                    //System.out.println("sight");
                    break;
                }
            }
        }
    }
    public void actOnStumuli(){
        _brain.updateBrain();
        _energy -= C.ENERGY_LOSS_PER_ACTION;
        if(_brain.getNeuronPolarization(50) > C.THRESHOLD){
            _rotation += C.LIZARD_ANGULAR_SPEED;
            //System.out.println("moved left");
        }
        if(_brain.getNeuronPolarization(51) > C.THRESHOLD){
            _rotation -= C.LIZARD_ANGULAR_SPEED;
            //System.out.println("moved right");
        }
        if(_brain.getNeuronPolarization(52) > C.THRESHOLD){
            _position.x += C.LIZARD_LINEAR_SPEED * Math.cos(_rotation);
            _position.y += C.LIZARD_LINEAR_SPEED * Math.sin(_rotation);
            //System.out.println("moved forward");
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
        if(_energy < C.ENERGY_AT_WHICH_LIZARD_DIES){
            return true;
        }
        else return false;
    }
    public boolean hasSplit(){
        if(_energy > C.ENERGY_REQUIRED_TO_SPLIT){
            return true;
        }
        else return false;
    }
    public void setEnergy(double newEnergy){
        _energy = newEnergy;
    }
    public double getEnergy(){
        return _energy;
    }
}
