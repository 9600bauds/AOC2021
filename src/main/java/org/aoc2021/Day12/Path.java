package org.aoc2021.Day12;

import java.util.*;

public class Path {
    List<Cave> trajectory;
    boolean canVisitAgain = false;

    public Path() {
        this.trajectory = new ArrayList<>();
    }
    public Path(Path parent) {
        trajectory = new ArrayList<>(parent.trajectory);
        canVisitAgain = parent.canVisitAgain;
    }

    public void BreadthFirstSearch(Cave loc, Set<Path> completedPaths){
        trajectory.add(loc);
        if(loc.isEndCave) {
            completedPaths.add(this);
            return;
        }
        for(Cave neighbor : loc.neighbors){
            if(neighbor.isStartCave){
                continue;
            }
            boolean revisiting = (!neighbor.isBigCave && trajectory.contains(neighbor));
            if(revisiting && !canVisitAgain) continue;

            Path continuation = new Path(this);
            if(revisiting && !neighbor.isBigCave){
                continuation.canVisitAgain = false;
            }
            continuation.BreadthFirstSearch(neighbor, completedPaths);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if(canVisitAgain) sb.append('*');
        for(Cave cave : trajectory){
            sb.append(cave.name + ",");
        }
        return sb.toString();
    }
}
