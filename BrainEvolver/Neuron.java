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
public class Neuron {
    private Synapse[] _sendingConnections;
    private double _polarization;
    private double _lastTimeActionPotential;
    private boolean _actionPotentialReached;
    public Neuron(){
        _polarization = 0;
    }
    public  void setSynapses(Synapse []rArray){
        _sendingConnections = rArray;
    }
    public void addToPolarization(double amountToAdd){
        _polarization += amountToAdd;
    }
    public double getPolarization(){
        return _polarization;
    }
    public Synapse[] getSynapses(){
        return _sendingConnections;
    }
    public void dePolarize(){
        _polarization = 0;
    }
    public void setActionPotentialReached(boolean reached){
        _actionPotentialReached = reached;
    }
    public boolean getActionPotentialReached(){
        return _actionPotentialReached;
    }
}
    
