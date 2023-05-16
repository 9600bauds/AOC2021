package org.aoc2021;

import org.aoc2021.Utils.CollectionUtils;
import org.aoc2021.Utils.Utils;

import java.lang.invoke.MethodHandles;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

//https://adventofcode.com/2021/day/5
public class Day5 {
    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByLinebreaks(filename);

        Map<String, Integer> ventsNoDiagonals = new Hashtable<String, Integer>();
        Map<String, Integer> ventsWithDiagonals = new Hashtable<String, Integer>();


        for(String line : input){
            String[] coords = line.split(" -> ");
            int[] start = CollectionUtils.StringArr2IntArr(coords[0].split(","));
            int[] end = CollectionUtils.StringArr2IntArr(coords[1].split(","));
            if(LineIsVerticalOrHorizontal(start, end)) {
                drawRectangle(ventsNoDiagonals, start, end);
                drawRectangle(ventsWithDiagonals, start, end);
            }
            else{ //We treat everything that's not vertical or horizontal as a 45 degree diagonal
                drawDiagonal(ventsWithDiagonals, start, end);
            }
        }

        ShowAnswer(silent, answers, 0, ventsNoDiagonals);
        ShowAnswer(silent, answers, 1, ventsWithDiagonals);

        return answers;
    }

    private static void ShowAnswer(boolean silent, Object[] answers, int answersIndex, Map<String, Integer> ventMap) {
        int multiVents = 0;
        for(String coord : ventMap.keySet()){
            if(ventMap.get(coord) > 1){
                multiVents++;
            }
        }
        answers[answersIndex] = multiVents;
        if(!silent) {
            drawBoard(ventMap);
            System.out.println("Part " + (answersIndex+1) + " answer: " + multiVents);
        }
    }

    private static boolean LineIsVerticalOrHorizontal(int[] start, int[] end) {
        return start[0] == end[0] || start[1] == end[1];
    }

    //What are lines if not thin rectangles?
    public static void drawRectangle(Map<String, Integer> vents, int[] start, int[] end){
        int minx = Math.min(start[0], end[0]);
        int miny = Math.min(start[1], end[1]);
        int maxx = Math.max(start[0], end[0]);
        int maxy = Math.max(start[1], end[1]);

        for(int x = minx; x <= maxx; x++){
            for(int y = miny; y <= maxy; y++){
                String thisCoord = x + "," + y;
                if(!vents.containsKey(thisCoord)){
                    vents.put(thisCoord, 0);
                }
                vents.put(thisCoord, vents.get(thisCoord) + 1);
            }
        }
    }

    //Only works for 45 degree diagonals
    public static void drawDiagonal(Map<String, Integer> vents, int[] start, int[] end){
        int deltaX = end[0] - start[0];
        int deltaY = end[1] - start[1];
        int slopeX = Integer.signum(deltaX);
        int slopeY = Integer.signum(deltaY);

        for(int i = 0; i <= Math.abs(deltaX); i++){
            int x = start[0] + (i * slopeX);
            int y = start[1] + (i * slopeY);
            String thisCoord = x + "," + y;
            if(!vents.containsKey(thisCoord)){
                vents.put(thisCoord, 0);
            }
            vents.put(thisCoord, vents.get(thisCoord) + 1);
        }
    }

    //Only from 0 to 10, for testing purposes
    public static void drawBoard(Map<String, Integer> vents){
        StringBuilder out = new StringBuilder();
        for(int y = 0; y < 10; y++){
            for(int x = 0; x < 10; x++){
                String thisCoord = x + "," + y;
                if(vents.containsKey(thisCoord)){
                    out.append(vents.get(thisCoord));
                }
                else{
                    out.append(".");
                }
            }
            out.append("\r\n");
        }
        System.out.println(out);
    }
}
