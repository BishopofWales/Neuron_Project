/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package BrainEvolver;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author Michael
 */
public class Keychecker extends KeyAdapter{
    private boolean _enterPressed;
    @Override
    public void keyPressed(KeyEvent event) {
        
        if(event.getKeyCode() == KeyEvent.VK_ENTER){
            _enterPressed = true;
      };
      
    }
    public boolean enterPressed(){
        if(_enterPressed){
            _enterPressed = false;
            return true;
        }
        else return false;
    }
}
