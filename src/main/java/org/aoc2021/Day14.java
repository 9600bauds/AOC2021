package org.aoc2021;

import org.aoc2021.Utils.Utils;

import java.lang.invoke.MethodHandles;
import java.util.*;

//https://adventofcode.com/2021/day/14
public class Day14 {

    static final int PART_1_STEPS = 10;
    static final int PART_2_STEPS = 40;

    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();

        Map<String, Character> insertionRules = new HashMap<>();
        Map<Character, Long> letterUsages = new HashMap<>();
        Map<String, Long> blockUsages = new HashMap<>();
        ProcessInput(filename, insertionRules, letterUsages, blockUsages);

        for(int i = 0; i < PART_2_STEPS; i++){
            DoInsertionStep(insertionRules, letterUsages, blockUsages);
            if(i == PART_1_STEPS-1){
                answers[0] = LetterScore(letterUsages, silent);
            } else if (i == PART_2_STEPS-1) {
                answers[1] = LetterScore(letterUsages, silent);
            }
        }

        return answers;
    }

    /**
     * Alright, here's how this works: Rather than keep track of the whole polymer, we simply keep track of how many instances of each 2-letter block it has.
     * For each step, each block turns into 2 other blocks. Essentially -1 to a given block and +1 to two other blocks.
     * We don't need to keep track of where in the polymer each block is, so this works a lot faster.
     * We also keep track of how many letters are in the polymer by simply adding +1 to the corresponding letter in each operation.
     */
    private static void DoInsertionStep(Map<String, Character> insertionRules, Map<Character, Long> letterUsages, Map<String, Long> blockUsages) {
        Map<String, Long> tempUsages = new HashMap<>(); //Why a second map? We do our math on this one, so we don't change the counts of the real map mid-step.
        for(Map.Entry<String, Long> entry : blockUsages.entrySet()){
            String currBlock = entry.getKey();
            Long times = entry.getValue();
            char toAdd = insertionRules.get(currBlock);

            AddToMap(letterUsages, toAdd, times); //Update the letter count, since we keep track of that separately from the blocks

            String newBlock1 = "" + currBlock.charAt(0) + toAdd;
            String newBlock2 = "" + toAdd + currBlock.charAt(1);
            AddToMap(tempUsages, currBlock, -times); //Negative times: This is the block that's being "split" into two
            AddToMap(tempUsages, newBlock1, times);
            AddToMap(tempUsages, newBlock2, times);
        }
        for(Map.Entry<String, Long> entry : tempUsages.entrySet()){
            AddToMap(blockUsages, entry.getKey(), entry.getValue()); //Now that the step is finished, we update the real map with the temporary one.
        }
    }

    /**
     * Used to calculate the score asked for the assignment.
     */
    public static Long LetterScore(Map<Character, Long> usages, boolean silent){
        long max = Long.MIN_VALUE;
        long min = Long.MAX_VALUE;
        for(Long i : usages.values()){
            max = Math.max(i, max);
            min = Math.min(i, min);
        }
        if(!silent){
            System.out.println("Min: " + min + " - Max: " + max + " - Diff: " + (max - min));
        }
        return max - min;
    }

    /**
     * Just a helper function to make this more readable. Adds to the value of a String-Long map, safely,
     * even if the key does not yet exist (populating it to 0 in that case).
     * @param map The String-Long map to use.
     * @param key The key to add to.
     * @param amt The amount.
     */
    private static void AddToMap(Map<String, Long> map, String key, Long amt){
        map.put(key, map.getOrDefault(key, 0L) + amt);
    }

    //I couldn't figure out how to make this work with CharSequence.
    private static void AddToMap(Map<Character, Long> map, Character key, Long amt){
        map.put(key, map.getOrDefault(key, 0L) + amt);
    }

    /**
     * Populates insertionRules, letterUsages and blockUsages from the input.
     */
    private static void ProcessInput(String filename, Map<String, Character> insertionRules, Map<Character, Long> letterUsages, Map<String, Long> blockUsages){
        List<String> input = Utils.SplitInputByEmptyLines(filename);

        //Assembling the insertion rules
        String[] insertionRulesInput = input.get(1).split("\r\n");
        for(String line : insertionRulesInput){
            String[] lineSplit = line.split(" -> ");
            String pair = lineSplit[0];
            char insertion = lineSplit[1].charAt(0);
            insertionRules.put(pair, insertion);
        }

        //Decoding the initial state of the polymer, and creating the letterUsages and blockUsages maps
        String initialState = input.get(0); //The initial polymer. e.g. NNCB
        for(int i = 0; i < initialState.length(); i++){
            char c = initialState.charAt(i);
            //We count the letters in it.
            letterUsages.put(c, letterUsages.getOrDefault(c, 0L) + 1);
            //We also count the pairs of letters that exist in it.
            //Note that this assumes all combinations of letters have a corresponding insertion rule, which is true for the input.
            if(i < initialState.length() - 1){ //don't do this if we're at the last index or we'll overflow!
                String currPair = initialState.substring(i, i+2);
                AddToMap(blockUsages, currPair, 1L);
            }
        }
    }
}
