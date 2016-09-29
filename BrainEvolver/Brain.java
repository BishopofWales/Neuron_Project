/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BrainEvolver;

import java.util.concurrent.ThreadLocalRandom;
import java.lang.Integer;


/**
 *
 * @author Michael
 */
public class Brain{
    private final Neuron[] _neurons;
    private double _score;
    private final String _name;
    public double getScore(){
        return _score;
    }
    public void setScore(double score){
        _score = score;
    }
    public Brain(){
        _name = Names.names[ThreadLocalRandom.current().nextInt(0,Names.names.length)] +Integer.toString(ThreadLocalRandom.current().nextInt(0,1000));
        _neurons = new Neuron[C.numberOfNeurons];
        for (int i = 0; i < _neurons.length; i++) {
            
            _neurons[i] = new Neuron();
            Synapse[] outgoingConnections = new Synapse[C.numberOfSynapses];
            for (int l = 0; l < outgoingConnections.length; l++) {
                outgoingConnections[l] = new Synapse();
                outgoingConnections[l].setNeuronID(ThreadLocalRandom.current().nextInt(0, _neurons.length));
                outgoingConnections[l].setConnectionStrength(20);
            }
            _neurons[i].setSynapses(outgoingConnections);
        }   
    }
    public String getName(){
        return _name;
    }
    public double getNeuronPolarization(int neuronIndex){
        return _neurons[neuronIndex].getPolarization();
    }
    public void addToNeuronPolarization(int neuronIndex, double polarization){
        _neurons[neuronIndex].addToPolarization(polarization);
    }
   
    public int getNeuronSyanapseLength(int neuronIndex){
        return _neurons[neuronIndex].getSynapses().length;
    }
    public Neuron getNeuron(int neuronIndex){
        return _neurons[neuronIndex];
    }
    public void setNeuron(int neuronIndex, Neuron neuron){
       _neurons[neuronIndex] = neuron;
    }
    public void updateBrain(){
        //update brain loop, checks each of the  neurons, sees if their action potential is above the threshold, if it is, raise the action potential of its subsidiaries
        //Then put the polariazaotin back at zero
        for (int i  = 0; i < _neurons.length; i++) {
            if (_neurons[i].getPolarization() > C.threshold) {
                _neurons[i].setActionPotentialReached(true);
                _neurons[i].dePolarize();
            }
            //move polarization towards zero
            else if(_neurons[i].getPolarization()  > C.polarizationLossPerCycle && _neurons[i].getPolarization() < -C.polarizationLossPerCycle) _neurons[i].dePolarize();
            else if (_neurons[i].getPolarization() < 0) _neurons[i].addToPolarization(C.polarizationLossPerCycle);
            else if (_neurons[i].getPolarization() > 0) _neurons[i].addToPolarization(-C.polarizationLossPerCycle);
        }
        for (Neuron neuron : _neurons){
            if(neuron.getActionPotentialReached() == true){
                for (Synapse synapse : neuron.getSynapses()){
                    if(_neurons[synapse.getNeuronID()].getActionPotentialReached() == true){
                        synapse.setConnectionStrength(synapse.getConnectionStrength()*C.firedTogetherMultiplier);
                    }
                    else{
                        synapse.setConnectionStrength(synapse.getConnectionStrength()*C.firedApartMultiplier);
                    }
                    _neurons[synapse.getNeuronID()].addToPolarization(synapse.getConnectionStrength());
                    
                }
            }
        }
        for (Neuron neuron : _neurons){
            neuron.setActionPotentialReached(false);
        }
    }
}
