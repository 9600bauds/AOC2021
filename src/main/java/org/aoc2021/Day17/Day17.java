package org.aoc2021.Day17;

import org.aoc2021.Utils.CollectionUtils;
import org.aoc2021.Utils.Geometry;
import org.aoc2021.Utils.Utils;

import java.awt.*;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.List;

//https://adventofcode.com/2021/day/17
public class Day17 {
    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();

        Rectangle targetZone = GetTargetZone(filename);
        Point start = new Point(0, 0);

        List<ProbeLaunch> successes = new ArrayList<>();
        //The horizontal deceleration of the probe follows a triangular number pattern.
        //So, the lowest amount of force needed will be the triangular root of the distance needed to cover, rounded up. Let's round down just to be sure.
        int minx = (int) Utils.TriangularRoot(targetZone.x - start.x);
        //The max X value that will work is simply the distance between the outermost edge of the target and the start,
        //such that the probe hits the edge of the target in one step.
        int maxx = targetZone.x + targetZone.width - start.x;
        //The min Y value that will work is simply the distance between the lowermost edge of the target and the start,
        //such that the probe hits the bottom of the target in one step.
        int miny = -1 * Math.max(start.y - targetZone.y, start.y - (targetZone.y + targetZone.height)); //Theoretically should work if the target is in positive Y?
        //The max Y seems to be just negative minY, but I don't know why. I don't understand the maths. So we'll cover twice as much ground just to be sure.
        int maxy = -2 * miny;

        for (int initY = miny; initY < maxy; initY++) {
            for (int initX = minx; initX < maxx; initX++) {
                ProbeLaunch attempt = new ProbeLaunch(initX, initY, start, targetZone);
                if (attempt.Simulate()) {
                    successes.add(attempt);
                }
            }
        }

        ProbeLaunch tallestLaunch = Collections.max(successes, Comparator.comparingInt(obj -> obj.highestY));
        answers[0] = tallestLaunch.highestY;
        answers[1] = successes.size();
        if (!silent) {
            DrawBoard(start, tallestLaunch.positions, targetZone, null);
            System.out.println("Highest Y: " + answers[0] + " with initial Y speed of " + tallestLaunch.initY);
            System.out.println("Total amount of viable launches: " + answers[1]);
        }
        return answers;
    }

    /**
     * Takes a target and a list of settings, and then simulates them to see how many of them are successful.
     * Input is in the form of raw filenames. Used for unit tests.
     *
     * @param targetFilename   Filename that describes the target in the same format as the main assignment.
     * @param settingsFilename Filename that contains the settings to try in the format: [initialX],[initialY], one per line.
     * @return The count of launches that were successful.
     */
    public static Long CountSuccesses(String targetFilename, String settingsFilename) {
        Long out = 0L;

        Rectangle targetZone = GetTargetZone(targetFilename);
        Point start = new Point(0, 0);

        List<String> correctSettings = Utils.SplitInputByLinebreaks(settingsFilename);
        for (String line : correctSettings) {
            String[] lineSplit = line.split(",");
            int[] settings = CollectionUtils.StringArr2IntArr(lineSplit);
            ProbeLaunch attempt = new ProbeLaunch(settings[0], settings[1], start, targetZone);
            attempt.Simulate();
            if (attempt.success) {
                out++;
            }
        }

        return out;
    }

    /**
     * Takes a filename containing the specifications for the target (example: "target area: x=192..251, y=-89..-59")
     * and returns a Rectangle corresponding to said target area.
     *
     * @param filename The filename of the input containing the specifications.
     * @return A rectangle spanning the target area.
     */
    public static Rectangle GetTargetZone(String filename) {
        String input = Utils.Filename2String(filename); //example input: target area: x=192..251, y=-89..-59

        String[] inputStrings = Utils.GetAllRegexMatches(input, "[-|\\d]+"); //Catch all 4 numbers, including negative sign
        int[] inputArr = CollectionUtils.StringArr2IntArr(inputStrings);
        int x1 = inputArr[0];
        int x2 = inputArr[1];
        int y1 = inputArr[2];
        int y2 = inputArr[3];
        int lowerLeftX = Math.min(x1, x2); //Fun fact: Java docs say the Rectangle class x/y represent its top-left coordinates.
        int lowerLeftY = Math.min(y1, y2); //It lies. It's actually the bottom-left.
        int width = Math.abs(x2 - x1) + 1;
        int height = Math.abs(y2 - y1) + 1;

        return new Rectangle(lowerLeftX, lowerLeftY, width, height);
    }

    /**
     * Crude console-based visualization.
     *
     * @param highlights Optional parameter, can pass a list of points that will be highlighted as #s in the output
     * @param bounds     Optional parameter, can pass a rectangle to define the bounds of the coordinate that will be rendered
     */
    public static void DrawBoard(Point start, Collection<Point> highlights, Rectangle target, Rectangle bounds) {
        if (bounds == null) {
            Collection<Point> importantStuff = new ArrayList<>(highlights);
            importantStuff.add(start);
            importantStuff.add(new Point(target.x, target.y + target.height));
            importantStuff.add(new Point(target.x + target.width, target.y));
            bounds = Geometry.GetBounds(importantStuff);
        }
        int minx = bounds.x;
        int miny = bounds.y;
        int maxx = bounds.x + bounds.width;
        int maxy = bounds.y + bounds.height;

        StringBuilder out = new StringBuilder();
        for (int y = maxy; y >= miny; y--) { //0,0 corresponds to bottom-left, therefore we start from the top
            for (int x = minx; x <= maxx; x++) {
                Point currPoint = new Point(x, y);
                if (currPoint.equals(start)) {
                    out.append("S");
                } else if (highlights.contains(currPoint)) {
                    out.append("#");
                } else if (target.contains(currPoint)) {
                    out.append("T");
                } else {
                    out.append("·");
                }
            }
            out.append("\n");
        }
        System.out.println(out);
    }
}
