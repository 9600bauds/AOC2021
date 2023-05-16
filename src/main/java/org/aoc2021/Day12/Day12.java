package org.aoc2021.Day12;

import org.aoc2021.Utils.Utils;

import java.lang.invoke.MethodHandles;
import java.util.*;

//https://adventofcode.com/2021/day/12
public class Day12 {
    static final String START_CAVE = "start";
    static final String END_CAVE = "end";

    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByLinebreaks(filename);

        Map<String, Cave> caves = new HashMap<>();
        for(String line : input){
            String[] inputSplit = line.split("-");
            String cave1Str = inputSplit[0];
            if(!caves.containsKey(cave1Str)){
                caves.put(cave1Str, new Cave(cave1Str));
            }
            String cave2Str = inputSplit[1];
            if(!caves.containsKey(cave2Str)){
                caves.put(cave2Str, new Cave(cave2Str));
            }

            Cave cave1Obj = caves.get(cave1Str);
            Cave cave2Obj = caves.get(cave2Str);
            cave1Obj.neighbors.add(cave2Obj);
            cave2Obj.neighbors.add(cave1Obj);
        }
        caves.get(END_CAVE).isEndCave = true;
        caves.get(START_CAVE).isStartCave = true;

        Cave start = caves.get(START_CAVE);
        Set<Path> completedPaths_Part1 = new HashSet<>();
        Path starterPath_Part1 = new Path();
        starterPath_Part1.BreadthFirstSearch(start, completedPaths_Part1);
        answers[0] = completedPaths_Part1.size();

        Set<Path> completedPaths_Part2 = new HashSet<>();
        Path starterPath_Part2 = new Path();
        starterPath_Part2.canVisitAgain = true;
        starterPath_Part2.BreadthFirstSearch(start, completedPaths_Part2);
        answers[1] = completedPaths_Part2.size();

        return answers;
    }
}
