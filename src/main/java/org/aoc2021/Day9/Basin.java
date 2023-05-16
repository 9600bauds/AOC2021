package org.aoc2021.Day9;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.aoc2021.Utils.Geometry.GetOrthogonalNeighbors;

public class Basin {
    List<Point> tiles;
    int[][] map;
    int maxHeight;
    String color;

    public Basin(int[][] map, int maxHeight) {
        this.map = map;
        this.maxHeight = maxHeight;
        tiles = new ArrayList<>();
    }

    public int GetSize(){
        return tiles.size();
    }
    public void BreadthFirstSearch(Point p){
        tiles.add(p);
        for(Point neighbor : GetOrthogonalNeighbors(p, map)){
            if(map[neighbor.x][neighbor.y] >= maxHeight){
                continue;
            }
            if(tiles.contains(neighbor)){
                continue;
            }
            BreadthFirstSearch(neighbor);
        }
    }

}
