package org.aoc2021;

import org.aoc2021.Utils.CollectionUtils;
import org.aoc2021.Utils.Utils;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

//https://adventofcode.com/2021/day/7
public class Day7 {
    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByCommas(filename);

        int[] crabs = CollectionUtils.StringList2IntArr(input);
        Arrays.sort(crabs);
        if(!silent) System.out.println(Arrays.toString(crabs));

        //The optimal position for part 1 will always be the median, for mathematical reasons that I don't understand, but embrace.
        int medianIndex = crabs[crabs.length/2];
        answers[0] = LinearScore(crabs, medianIndex);
        if(!silent){
            System.out.println("PART 1:");
            System.out.println("For position " + medianIndex + " it would cost a total of " + answers[0] + " fuel.");
        }


        //I have no clue how to solve this mathematically so let's do binary search.
        int min = crabs[0];
        int max = crabs[crabs.length-1];
        int winner = -1;
        //Since the sum of fuel costs changes linearly with the chosen spot, we can just do a simple search.
        //We declare a min index and a max index for our search. For each step we check the spot in the middle.
        while(winner < 0){
            int med = (min+max)/2;
            int medScore = TriangularScore(crabs, med);
            if(TriangularScore(crabs, med-1) < medScore){
                max = med;
            }
            else if(TriangularScore(crabs, med+1) < medScore){
                min = med;
            }
            else{
                winner = med;
            }
        }
        answers[1] = TriangularScore(crabs, winner);
        if(!silent){
            System.out.println("\r\nPART 2:");
            System.out.println("For position " + winner + " it would cost a total of " + answers[1] + " fuel.");
        }

        return answers;
    }

    public static int TriangularScore(int[] crabs, int pos){
        int sum = 0;
        for(int crab : crabs){
            int delta = Math.abs(crab - pos);
            sum += Utils.TriangularNum(delta);
        }
        return sum;
    }

    public static int LinearScore(int[] crabs, int pos){
        int sum = 0;
        for(int crab : crabs){
            sum += Math.abs(crab - pos);
        }
        return sum;
    }
}
