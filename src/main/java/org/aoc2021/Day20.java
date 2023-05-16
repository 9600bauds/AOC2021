package org.aoc2021;

import org.aoc2021.Utils.Geometry;
import org.aoc2021.Utils.Utils;

import java.awt.*;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.List;

//https://adventofcode.com/2021/day/20
public class Day20 {
    static final int STEPS_FOR_ANS1 = 2;
    static final int STEPS_FOR_ANS2 = 50;
    static final int STEPS_TO_SIMULATE = STEPS_FOR_ANS2;
    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByEmptyLines(filename);

        char[] enhancementDict = input.get(0).toCharArray();
        Set<Point> points = ProcessInput(input);
        Rectangle exampleCam = new Rectangle(-5, -6, 16, 16);
        Rectangle bounds = Geometry.GetBounds(points);
        bounds.grow(STEPS_TO_SIMULATE + 1, STEPS_TO_SIMULATE + 1); //Gotta be big enough that the borders are never reached

        if(!silent){
            System.out.println("Initial state:");
            PrintBoard(points, exampleCam);
        }

        for(int step = 1; step <= STEPS_TO_SIMULATE; step++){
            Set<Point> newPoints = new HashSet<>();
            for(int y = bounds.y; y <= bounds.y + bounds.height; y++){
                for(int x = bounds.x; x <= bounds.x + bounds.width; x++){
                    Point p = new Point(x, y);
                    if(PixelIsLit(p, points, enhancementDict, bounds)){
                        newPoints.add(p);
                    }
                }
            }
            points = newPoints;
            if(!silent && step <= STEPS_FOR_ANS1){
                System.out.println("Step " + step + ":");
                PrintBoard(points, exampleCam);
            }
            if(step == STEPS_FOR_ANS1){
                answers[0] = points.size();
            }
            else if(step == STEPS_FOR_ANS2){
                answers[1] = points.size();
            }
        }

        if(!silent){
            System.out.println("Final state:");
            PrintBoard(points, null);
            System.out.printf("Amount of points at step %s: %s%n", STEPS_FOR_ANS1, answers[0]);
            System.out.printf("Amount of points at step %s: %s%n", STEPS_FOR_ANS2, answers[1]);
        }
        return answers;
    }

    public static boolean PixelIsLit(Point p, Set<Point> points, char[] enhancementDict, Rectangle bounds){
        int index = 0; //How many pixels we have evaluated thus far
        int dictdec = 0; //Final index of the dictionary that we'll use
        for(int y = p.y + 1; y >= p.y - 1; y--){
            for(int x = p.x - 1; x <= p.x + 1; x++){
                Point neighbor = new Point(x, y);
                boolean foundLitPixel;
                if(bounds.contains(neighbor)){
                    foundLitPixel = points.contains(neighbor);
                }
                else{
                    //What is this?
                    //This is how we implement "infinite pixels".
                    //We initialize the board to be big enough that the borders will never be reached.
                    //So the borders are already a good representation for what out-of-bounds is like.
                    foundLitPixel = points.contains(p);
                }
                if(foundLitPixel){
                    double bin = Math.pow(2, 8 - index);
                    dictdec += bin; //This is effectively binary, except we don't have to muck with conversion
                }
                index++;
            }
        }
        char c = enhancementDict[dictdec];
        //System.out.printf("Point at %s,%s has index %s (%s, %s)%n", p.x, p.y, dictdec, Integer.toBinaryString(dictdec), c);
        return c == '#';
    }

    public static void PrintBoard(Set<Point> points, Rectangle bounds){
        if(bounds == null) bounds = Geometry.GetBounds(points);
        int minx = bounds.x;
        int miny = bounds.y;
        int maxx = bounds.x + bounds.width;
        int maxy = bounds.y + bounds.height;
        StringBuilder out = new StringBuilder();
        for(int y = maxy; y >= miny ; y--){
            for(int x = minx; x <= maxx; x++){
                Point currPoint = new Point(x, y);
                if(points.contains(currPoint)){
                    out.append("#");
                }
                else{
                    //out.append("·");
                    out.append(".");
                }
            }
            out.append("\n");
        }
        System.out.println(out);
    }

    private static Set<Point> ProcessInput(List<String> input) {
        Set<Point> out = new HashSet<>();
        String[] boardStringArr = input.get(1).split("\r\n");
        for(int y = 0; y < boardStringArr.length; y++){
            for(int x = 0; x < boardStringArr[0].length(); x++){
                char c = boardStringArr[y].charAt(x);
                if(c == '#'){
                    //y=0 corresponds to bottom of the graph, but line 0 is the top of the text input
                    out.add(new Point(x, boardStringArr.length - 1 - y));
                }
            }
        }
        return out;
    }
}
