package org.aoc2021;

import org.aoc2021.Utils.CollectionUtils;
import org.aoc2021.Utils.Geometry;
import org.aoc2021.Utils.Utils;

import java.awt.*;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.List;

//https://adventofcode.com/2021/day/13
public class Day13 {
    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByEmptyLines(filename);

        String[] pointsInput = input.get(0).split("\r\n");
        String[] instructionsInput = input.get(1).split("\r\n");

        List<Point> points = new ArrayList<>();
        for(String line : pointsInput){
            String[] lineSplit = line.split(",");
            int x = Integer.parseInt(lineSplit[0]);
            int y = Integer.parseInt(lineSplit[1]);
            Point p = new Point(x, y);
            points.add(p);
        }

        for (String line : instructionsInput){
            line = line.replace("fold along ", "");
            String[] lineSplit = line.split("=");
            String operation = lineSplit[0];
            int delta = Integer.parseInt(lineSplit[1]);
            if(Objects.equals(operation, "x")){
                HorizontalFold(points, delta);
            }
            else if(Objects.equals(operation, "y")){
                VerticalFold(points, delta);
            }
            else{
                throw new RuntimeException("Unknown operation! " + line);
            }
            if(answers[0] == null){
                points = CollectionUtils.ListWithoutDuplicates(points);
                answers[0] = points.size();
            }
        }

        answers[1] = Board2String(points);
        if(!silent) {
            System.out.println("Unique points after the first operation: " + answers[0]);
            System.out.println("Final board:");
            DrawBoard(points);
        }
        return answers;
    }

    public static void VerticalFold(Collection<Point> points, int y){
        for(Point p : points){
            if(p.y < y){
                continue;
            }
            int deltaY = (y - p.y) * 2;
            p.translate(0, deltaY);
        }
    }

    public static void HorizontalFold(Collection<Point> points, int x){
        for(Point p : points){
            if(p.x < x){
                continue;
            }
            int deltaX = (x - p.x) * 2;
            p.translate(deltaX, 0);
        }
    }

    public static void DrawBoard(Collection<Point> points) {
        DrawBoard(points, null);
    }
    public static void DrawBoard(Collection<Point> points, Rectangle bounds){
        System.out.println(Board2String(points, bounds));
    }

    public static String Board2String(Collection<Point> points){
        return Board2String(points, null);
    }
    public static String Board2String(Collection<Point> points, Rectangle bounds){
        if(bounds == null) bounds = Geometry.GetBounds(points);
        int minx = bounds.x;
        int miny = bounds.y;
        int maxx = bounds.x + bounds.width;
        int maxy = bounds.y + bounds.height;
        StringBuilder out = new StringBuilder();
        for(int y = miny; y <= maxy; y++){ //y = 0 corresponds to the top of the rectangle as per the assignment

            for(int x = minx; x <= maxx; x++){
                Point currPoint = new Point(x, y);
                if(points.contains(currPoint)){
                    out.append("#");
                }
                else{
                    out.append("·");
                }
            }
            out.append("\n");
        }
        return out.toString();
    }
}
