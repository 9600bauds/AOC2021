package org.aoc2021;

import org.aoc2021.Utils.Utils;

import java.lang.invoke.MethodHandles;
import java.util.*;

//https://adventofcode.com/2021/day/8
public class Day8 {
    static final int BASE = 10; //in case some maniac is using base 16 or something
    static final int DIGITS = 4; //how many digits in each display?
    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByLinebreaks(filename);

        int maxIndex = input.size();
        String[][] signalPatterns = new String[maxIndex][BASE];
        String[][] digitValues = new String[maxIndex][DIGITS];

        for(int i = 0; i < maxIndex; i++){
            String[] split = input.get(i).split(" \\| "); //Split by the character '|'
            signalPatterns[i] = split[0].split( " ");
            digitValues[i] = split[1].split(" ");
        }

        int part1Sum = 0;
        List<Integer> part1ValuesToCheck = Arrays.asList(1, 7, 4, 8);
        int part2Sum = 0;

        for(int i = 0; i < maxIndex; i++){
            String[] signalPatternArr = signalPatterns[i];
            //Luckily, the 3 numbers that we need to know in order to deduce the rest also happen to be the 3
            //with the least segments. So we can just sort the signal patterns based on length, and it will just work.
            Arrays.sort(signalPatternArr, Comparator.comparing(String::length));

            Map<Integer, char[]> signalDictionary = new HashMap<>();

            //Step 1: We use deduction to find out which pattern corresponds to each number.
            for(int j = 0; j < signalPatternArr.length; j++){
                //We represent each signal pattern as a sorted array of the characters that make it up.
                //That way we can compare 2 patterns and see if they're identical, regardless of the order of characters.
                char[] pattern = signalPatternArr[j].toCharArray();
                Arrays.sort(pattern);

                switch(pattern.length){ //How many segments does this number have?
                    case 2:
                        signalDictionary.put(1, pattern); //1 is the only number with 2 segments
                        break;
                    case 3:
                        signalDictionary.put(7, pattern); //7 is the only number with 3 segments
                        break;
                    case 4:
                        signalDictionary.put(4, pattern); //4 is the only number with 4 segments
                        break;
                    case 5: //2, 3 or 5
                        if(SegmentsInCommon(pattern, signalDictionary.get(4)) == 2){
                            signalDictionary.put(2, pattern); //2 is the only 5-segment that's got 2 segments in common with 4
                        }
                        else if(SegmentsInCommon(pattern, signalDictionary.get(1)) == 2){
                            signalDictionary.put(3, pattern); //3 is the only 5-segment that's got 2 segments in common with 1, etc
                        }
                        else{
                            signalDictionary.put(5, pattern);
                        }
                        break;
                    case 6: //0, 6 or 9
                        if(SegmentsInCommon(pattern, signalDictionary.get(4)) == 4){
                            signalDictionary.put(9, pattern);
                        }
                        else if(SegmentsInCommon(pattern, signalDictionary.get(1)) == 1){
                            signalDictionary.put(6, pattern);
                        }
                        else{
                            signalDictionary.put(0, pattern);
                        }
                        break;
                    case 7:
                        signalDictionary.put(8, pattern); //8 is the only number with 7 segments
                        break;
                }
            }

            //Step 2: Decoding the 4-digit number at the right
            int fourDigitNumber = 0; //What does the current display read?
            String[] digitValue = digitValues[i];
            for(int j = 0; j < digitValue.length; j++){
                //Again, we represent each digit as a sorted array of the characters that make it up.
                char[] digitArr = digitValue[j].toCharArray();
                Arrays.sort(digitArr);

                int digit = -1; //What's the current digit? -1 used as placeholder
                //Because you can't have a map with an array as key (thanks Java) we do it manually.
                for(Map.Entry<Integer, char[]> entry : signalDictionary.entrySet()) {
                    if(Arrays.equals(entry.getValue(), digitArr)){
                        digit = entry.getKey();
                        break;
                    }
                }
                if(digit < 0){
                    throw new RuntimeException("Unrecognized pattern! " + Arrays.toString(digitArr));
                }

                if (part1ValuesToCheck.contains(digit)) {
                    part1Sum++;
                }
                //"digitMult" will be 1 if this is te last digit, 10 if this is the second-last digit, etc.
                double digitMult = Math.pow(10, digitValue.length - j - 1);
                fourDigitNumber += digit * digitMult;
            }
            if(!silent) System.out.println(String.join(" ", digitValue) + ": " +  fourDigitNumber);
            part2Sum += fourDigitNumber;
        }
        answers[0] = part1Sum;
        answers[1] = part2Sum;
        if(!silent){
            System.out.println("PART 1: " + part1Sum);
            System.out.println("PART 2: " + part2Sum);
        }
        return answers;
    }

    /**
     * How many segments do these two patterns have in common? i.e. ['a', 'b', 'c'] & ['a', 'f'] would result in 1.
     * @return Integer counting the segments in common. Will count repeats twice, but the input has no repeats.
     */
    public static int SegmentsInCommon(char[] signal1, char[] signal2){
        int out = 0;
        for(int i = 0; i < signal1.length; i++){
            char c = signal1[i];
            for(int j = 0; j < signal2.length; j++){
                if(signal2[j] == c){
                    out++;
                    break;
                }
            }
        }
        return out;
    }
}
