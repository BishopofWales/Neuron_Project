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
public class Food {
    private int _lastTimeEaten;
    private boolean _alive;
    
    public Food(){
        _alive = true;
    }
    public void eaten(int frameCount){
        _alive = false;
        _lastTimeEaten = frameCount;
    }
    public boolean readyToGrowAgain(int frameCount){
        if(_alive == false && frameCount - _lastTimeEaten > C.FRAMES_FOR_FOOD_TO_GROW){
            _alive = true;
            return true;
        }
        else return false;
    }
}
