package org.aoc2021;

import org.aoc2021.Utils.Utils;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

//https://adventofcode.com/2021/day/3
public class Day3 {
    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByLinebreaks(filename);

        int totalBits = input.get(0).length();

        StringBuilder epsilonRateBin = new StringBuilder();
        StringBuilder gammaRateBin = new StringBuilder();


        for(int bitIndex = 0; bitIndex < totalBits; bitIndex++){
            char commonBit = FindCommonBit(input, bitIndex, true);
            if(commonBit == '0'){
                gammaRateBin.append("0"); //Gamma rate uses the most common bit
                epsilonRateBin.append("1"); //Epsilon rate uses the least common bit
            }
            else if (commonBit == '1'){
                gammaRateBin.append("1"); //Gamma rate uses the most common bit
                epsilonRateBin.append("0"); //Epsilon rate uses the least common bit
            }
            else{
                throw new RuntimeException("Bits were equal for part 1 (should never happen)!");
            }
        }

        int epsilonRateDec = Integer.parseInt(epsilonRateBin.toString(), 2); //Convert to decimal
        int gammaRateDec = Integer.parseInt(gammaRateBin.toString(), 2); //Convert to decimal
        int powerConsumption = epsilonRateDec * gammaRateDec;
        answers[0] = powerConsumption;
        if(!silent){
            System.out.println("Gamma rate: " + gammaRateBin + ", which is " + epsilonRateDec + " in decimal.");
            System.out.println("Epsilon rate: " + epsilonRateBin + ", which is " + gammaRateDec + " in decimal.");
            System.out.println("Total power consumption: " + powerConsumption + "!");
        }

        //Now for part two. A bit repetitive
        String oxyRateBin = "";
        String co2RateBin = "";
        int oxyRateDec = 0;
        int co2RateDec = 0;

        List<String> oxyLines = new ArrayList<>(input);
        for(int bitIndex = 0; bitIndex < totalBits; bitIndex++) {
            char commonBit = FindCommonBit(oxyLines, bitIndex, true);
            if(commonBit == '='){
                commonBit = '1';
            }
            FilterLines(oxyLines, bitIndex, commonBit);
            if(oxyLines.size() <= 1){
                oxyRateBin = oxyLines.get(0);
                break;
            }
        }

        List<String> co2Lines = new ArrayList<>(input);
        for(int bitIndex = 0; bitIndex < totalBits; bitIndex++) {
            char commonBit = FindCommonBit(co2Lines, bitIndex, false);
            if(commonBit == '='){
                commonBit = '0';
            }
            FilterLines(co2Lines, bitIndex, commonBit);
            if(co2Lines.size() <= 1){
                co2RateBin = co2Lines.get(0);
                break;
            }
        }
        oxyRateDec = Integer.parseInt(oxyRateBin, 2); //Convert to decimal
        co2RateDec = Integer.parseInt(co2RateBin, 2); //Convert to decimal
        int lifeSupportRating = oxyRateDec * co2RateDec;
        answers[1] = lifeSupportRating;
        if(!silent){
            System.out.println("Oxygen generator rating: " + oxyRateBin + ", which is " + oxyRateDec + " in decimal.");
            System.out.println("CO2 scrubber rating: " + co2RateBin + ", which is " + co2RateDec + " in decimal.");
            System.out.println("Life support rating: " + lifeSupportRating + "!");
        }

        return answers;
    }

    /**
     * Removes the strings in a list that don't match a certain char at a certain index.
     * For example, use index 0 and char '1' to filter all strings that don't start with 1.
     * @param lines List of lines to input
     * @param bitIndex The index of the character to be filtered
     * @param correctBit Lines not matching this character will be removed
     * @return List of only lines that matched the filter
     */
    public static List<String> FilterLines(List<String> lines, int bitIndex, char correctBit){
        for(int i = 0; i < lines.size(); i++){
            String line = lines.get(i);
            if(line.charAt(bitIndex) != correctBit){
                lines.remove(i);
                i--; //We removed an item, so we have to update which index we'll check next, or we'll skip a line
            }
        }
        return lines;
    }

    /**
     * Finds the most/least common bit at a certain index of a list of strings. Only works for strings of ones and zeroes.
     * @param reportLines List of strings to process
     * @param bitIndex The index of the bit to scan
     * @param mostCommon Set to true to find the most common bit, set to false to find the least common
     * @return '1', '0', or '=' if they are equally common
     */
    public static char FindCommonBit(List<String> reportLines, int bitIndex, boolean mostCommon){
        int zeroes = 0;
        int ones = 0;
        for(int lineIndex = 0; lineIndex < reportLines.size(); lineIndex++){
            char currChar = reportLines.get(lineIndex).charAt(bitIndex);
            switch(currChar){
                case '0':
                    zeroes++;
                    break;
                case '1':
                    ones++;
                    break;
                default:
                    throw new RuntimeException("Unrecognized character! " + currChar);
            }
        }

        if(ones == zeroes){
            return '=';
        }
        if((mostCommon && (zeroes > ones)) || (!mostCommon && (ones > zeroes))){
            return '0';
        }
        else{
            return '1';
        }
    }
}
