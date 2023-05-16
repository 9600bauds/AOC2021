package org.aoc2021;

import org.aoc2021.Utils.CollectionUtils;
import org.aoc2021.Utils.Utils;

import java.lang.invoke.MethodHandles;
import java.util.*;

//https://adventofcode.com/2021/day/1
public class Day1 {
    static final int WINDOW_SIZE = 3;

    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByLinebreaks(filename);

        //Convert the input to integers
        List<Integer> initialMeasurements = CollectionUtils.StringList2IntList(input);

        //Part 1 is handled by the AnalyzeMeasurements function on the initial input.
        answers[0] = (Object) AnalyzeMeasurements(initialMeasurements, silent);

        //Part 2 makes use of the same function, except we need to create a new input using a sliding-window technique.
        List<Integer> slidingWindowMeasurements = new ArrayList<>();
        int currMeasurement = 0; //Current sliding window value
        for (int i = initialMeasurements.size() - 1; i >= 0; i--) {
            //For each movement of the sliding window, add the new val and subtract the previous one
            currMeasurement += initialMeasurements.get(i);
            if (i < initialMeasurements.size() + 1 - WINDOW_SIZE) {
                int windowDelta = initialMeasurements.get(i + WINDOW_SIZE - 1);
                slidingWindowMeasurements.add(0, currMeasurement);
                currMeasurement -= windowDelta;
            }

        }
        answers[1] = AnalyzeMeasurements(slidingWindowMeasurements, silent);

        return answers;
    }

    public static int AnalyzeMeasurements(List<Integer> measurements, boolean silent) {
        int depthIncreases = 0;
        int previousDepth = measurements.get(0);
        //We start with i = 1 in order to skip the first measurement, since there's nothing to compare it to
        for (int i = 1; i < measurements.size(); i++) {
            int currDepth = measurements.get(i);
            if (currDepth > previousDepth) {
                //System.out.println("Depth of " + currDepth + " at index " + i + " is larger than " + previousDepth);
                depthIncreases++;
            }
            previousDepth = currDepth;
        }
        if (!silent) System.out.println("Total number of depth increases: " + depthIncreases);
        return depthIncreases;
    }
}
