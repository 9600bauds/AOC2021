package org.aoc2021.Day18;

import org.aoc2021.Utils.Utils;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;

//https://adventofcode.com/2021/day/18
public class Day18 {
    static final int MAX_DEPTH = 3;
    static final int MAX_NUMBER = 9;
    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByLinebreaks(filename);

        SnailFishNum root = null;

        for(String line : input){
            SnailFishNum newNum = new SnailFishNum(line, null);
            newNum.Reduce();
            if(root == null){
                root = newNum;
            }
            else{
                root = new SnailFishNum(root, newNum);
                root.Reduce();
            }
        }
        assert root != null;
        answers[0] = root.GetMagnitude();

        long largestMagnitude = 0L;
        for(int i = 0; i < input.size(); i++){
            for(int j = 0; j < input.size(); j++){
                String line1 = input.get(i);
                String line2 = input.get(j);
                if(Objects.equals(line1, line2)) continue; //Different numbers only please
                SnailFishNum num1 = new SnailFishNum(line1, null);
                num1.Reduce();
                SnailFishNum num2 = new SnailFishNum(line2, null);
                num2.Reduce();
                SnailFishNum sum = new SnailFishNum(num1, num2);
                sum.Reduce();

                long sumMagnitude = sum.GetMagnitude();
                if(sumMagnitude > largestMagnitude){
                    //System.out.println("Magnitude " + num1.GetMagnitude() + " + Magnitude " + num2.GetMagnitude() + " = " + sum.GetMagnitude());
                    largestMagnitude = sumMagnitude;
                }
            }
        }
        answers[1] = largestMagnitude;

        if(!silent){
            System.out.println(answers[0]);
            System.out.println(answers[1]);
        }

        return answers;
    }
}
