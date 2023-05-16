package org.aoc2021.Day17;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProbeLaunch {
    int initX;
    int initY;
    int xvel;
    int yvel;
    boolean success = false;
    int highestY = Integer.MIN_VALUE;
    Point loc;
    Rectangle target;
    List<Point> positions = new ArrayList<>();

    public ProbeLaunch(int initX, int initY, Point loc, Rectangle target) {
        this.initX = initX;
        xvel = initX;
        this.initY = initY;
        yvel = initY;
        this.loc = new Point(loc);
        this.target = target;
    }

    public boolean Simulate(){
        while(loc.x < target.x + target.width && loc.y > target.y){
            loc.translate(xvel, yvel);
            positions.add(new Point(loc));

            if(loc.y > highestY){
                highestY = loc.y;
            }

            if(target.contains(loc)){
                success = true;
            }

            if(xvel > 0){
                xvel--;
            }
            else if (xvel < 0){
                xvel++;
            }
            yvel--;
        }
        return success;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append(initX);
        sb.append(", ").append(initY);
        return sb.toString();
    }
}
