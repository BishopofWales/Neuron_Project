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
public class Synapse {
    //private String connectionType;
    private double _connectionStength;
    private int _neuronID;   
    public void setConnectionStrength(double rDouble){
        _connectionStength = rDouble;
    }
    
    public void setNeuronID(int rInt){
        _neuronID = rInt;
    }
    public int getNeuronID(){
        return _neuronID;
    }
    
    public double getConnectionStrength(){
        return _connectionStength;
    }
}
