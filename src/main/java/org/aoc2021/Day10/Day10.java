package org.aoc2021.Day10;

import org.aoc2021.Utils.Utils;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//https://adventofcode.com/2021/day/10
public class Day10 {
    static final int INCOMPLETE_SCORE_MULT = 5;

    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByLinebreaks(filename);

        List<BracketPair> pairs = new ArrayList<>();
        pairs.add(new BracketPair('(', ')', 3, 1));
        pairs.add(new BracketPair('[', ']', 57, 2));
        pairs.add(new BracketPair('{', '}', 1197, 3));
        pairs.add(new BracketPair('<', '>', 25137, 4));

        int part1Score = 0;
        List<Long> part2ScoreList = new ArrayList<>();
        for(int li = 0; li < input.size(); li++){
            String line = input.get(li);
            if(!silent) System.out.println("Checking " + line);

            char[] lineArr = line.toCharArray();
            List<Character> pending = new ArrayList<>(); //Holds the open brackets that are still pending.

            //Part 1
            boolean lineIsIllegal = false;
            for(int i = 0; i < lineArr.length; i++){
                char currChar = lineArr[i];
                BracketPair pair = BracketPair.GetPair(currChar, pairs);
                if(currChar == pair.openChar) {
                    pending.add(currChar);
                }
                else if(currChar == pair.closeChar){
                    char lastPending = pending.get(pending.size()-1);
                    if(lastPending != pair.openChar){
                        if(!silent) System.out.println("Bad closing bracket " + currChar + " at index " + i);
                        part1Score += pair.invalidScore;
                        lineIsIllegal = true;
                        break;
                    }
                    pending.remove(pending.size()-1);
                }
            }
            if(lineIsIllegal){
                continue;
            }

            long part2Score = 0;
            //Part 2: If the line is not illegal, it must be incomplete.
            //We're supposed to calculate the score based on the brackets that we'd need to finish the expression.
            //But since both brackets in each pair are worth the same... we can also just calculate the score based
            //on the reversed order of pending brackets...
            Collections.reverse(pending);
            for(int i = 0; i < pending.size(); i++){
                part2Score *= INCOMPLETE_SCORE_MULT;
                char pendingBracket = pending.get(i);
                BracketPair pair = BracketPair.GetPair(pendingBracket, pairs);
                part2Score += pair.incompleteScore;
                //System.out.println("Score is now " + part2Score);
            }
            part2ScoreList.add(part2Score);
        }
        Collections.sort(part2ScoreList, Collections.reverseOrder());
        long middleScore = part2ScoreList.get(part2ScoreList.size()/2);
        if(!silent){
            System.out.println("Total error score: " + part1Score);
            System.out.println("Middle-ranking completion score: " + middleScore);
        }

        answers[0] = part1Score;
        answers[1] = middleScore;
        return answers;
    }


}
