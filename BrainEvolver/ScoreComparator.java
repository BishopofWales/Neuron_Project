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
import java.util.Comparator;
public class ScoreComparator implements Comparator<Brain>{
    
    @Override
    public int compare(Brain o1, Brain o2) {
        //return o2.getScore() - o1.getScore();
        double x = o1.getScore();
        double y = o2.getScore();
        if(x==y)return 0;
        if(x>y) return -1;
        return 1;
    }
}
