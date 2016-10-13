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
public class Shape {
    Point2D.Double[] _points;
    public boolean visible;
    public Point2D.Double _location;
    boolean _closed;
    public Shape(Point2D.Double[] points, boolean closed,Point2D.Double registrationPoint){
        _points = points;
        _closed = closed;
        _location = registrationPoint;
        //The points of the shape were created relative to the registration point, this updates them to consistent with the positon of the registration
        //in world space.
        for(Point2D.Double point:_points){
            point.x += _location.x;
            point.y += _location.y;
        }
        visible = true;
    }
    public void changePosition(Double newX, Double newY){
        _location.x = newX;
        _location.y = newY;
    }
    public Point2D.Double getLocation(){
        return _location;
    }
    public boolean rayCollidesWith(double rayStartX,double rayStartY,double rayEndX,double rayEndY){
        if(!visible) return false;
        for(int j = 0; j < _points.length; j++){
            if(findIntersect(new Point2D.Double(rayStartX,rayStartY),new Point2D.Double(rayEndX,rayEndY),_points[j],_points[(j+1)%_points.length])){
                return true;
            }
        }
        return false;
    }
    private boolean findIntersect(Point2D.Double line1Point1, Point2D.Double line1Point2, Point2D.Double line2Point1, Point2D.Double line2Point2){
        double m1 = (line1Point1.y - line1Point2.y)/(line1Point1.x -line1Point2.x);
        double m2 = (line2Point1.y - line2Point2.y)/(line2Point1.x -line2Point2.x);
        if(m1-m2 == 0) return false;
        double b1 = line1Point1.y - m1*line1Point1.x;
        double b2 = line2Point1.y - m2*line2Point1.x;
        
        Point2D.Double intersectPoint = new Point2D.Double();
        intersectPoint.x = (b2 - b1)/(m1-m2);
        intersectPoint.y = m1*intersectPoint.x + b1;
        if(intersectPoint.x <= line2Point1.x != intersectPoint.x <= line2Point2.x && line1Point1.x > line1Point2.x == line1Point1.x > intersectPoint.x && line1Point1.y > line1Point2.y == line1Point1.y > intersectPoint.y){
            return true;
        }
        else return false;   
    }
}
