package org.aoc2021.Day21;

import org.aoc2021.Utils.CollectionUtils;
import org.aoc2021.Utils.Utils;

import java.lang.invoke.MethodHandles;
import java.util.*;

//https://adventofcode.com/2021/day/21
public class Day21 {
    static final int MAX_SPACE = 10;
    static final int DICE_FACES_PART1 = 100;
    static final int DICE_FACES_PART2 = 3;
    static final int SCORE_GOAL_PART1 = 1000;
    static final int SCORE_GOAL_PART2 = 21;
    static final int DICE_THROWS_PER_TURN = 3;

    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByLinebreaks(filename);

        int inputPos1 = Character.getNumericValue(input.get(0).charAt(input.get(0).length() - 1));
        int inputPos2 = Character.getNumericValue(input.get(1).charAt(input.get(1).length() - 1));

        //I got lazy, so we're doing Part 1 right here without any classes.
        int player1pos = inputPos1;
        int player2pos = inputPos2;
        long player1score = 0;
        long player2score = 0;
        long timesDiceRolled = 0;

        while(player1score < SCORE_GOAL_PART1 && player2score < SCORE_GOAL_PART1){
            boolean isPlayer1Turn = timesDiceRolled % 2 == 0;
            int squaresMoved = 0;
            for(int i = 0; i < DICE_THROWS_PER_TURN; i++){ //Could also do (timesDiceRolled+1)*2, but that's clevercode
                squaresMoved += (timesDiceRolled % DICE_FACES_PART1) + 1;
                timesDiceRolled++;
            }
            if(isPlayer1Turn){
                player1pos = Utils.SumToLoopingValue(player1pos, MAX_SPACE, squaresMoved);
                player1score += player1pos;
            }
            else{
                player2pos = Utils.SumToLoopingValue(player2pos, MAX_SPACE, squaresMoved);
                player2score += player2pos;
            }
        }
        long losingPlayerScore = Math.min(player1score, player2score);
        long finalAnswer = losingPlayerScore * timesDiceRolled;
        answers[0] = finalAnswer;

        //For efficiency, we'll calculate ahead of time the deterministic results of each possible quantum dice result.
        //Stores: The sum of the results, How many times the result happens in each set of 3 throws
        Map<Integer, Long> quantumDiceResults = new HashMap<>();
        for(int i = 1; i <= DICE_FACES_PART2; i++){
            for(int j = 1; j <= DICE_FACES_PART2; j++){
                for(int k = 1; k <= DICE_FACES_PART2; k++){
                    int result = i + j + k;
                    CollectionUtils.addValueToMap(quantumDiceResults, result,1L);
                }
            }
        }

        //How many wins has each player gotten?
        long[] wins = new long[2];
        //This is the multiverse folder. If two universes are functionally equivalent, we simplify them down to 1 copy,
        //and then just increase the count for that universe.
        //This map stores a universe "state" and then the count for how many of that universe exist.
        Map<Diracverse, Long> pendingUniverses = new HashMap<>();
        Diracverse starter = new Diracverse(inputPos1, inputPos2, 0, 0, MAX_SPACE, SCORE_GOAL_PART2);
        pendingUniverses.put(starter, 1L); //count of 1 starter universes

        while(!pendingUniverses.isEmpty()){
            Map<Diracverse, Long> newMultiverse = new HashMap<>();

            for(Map.Entry<Diracverse, Long> entry : pendingUniverses.entrySet()){
                Diracverse thisUniverse = entry.getKey();
                Long currInstances = entry.getValue();

                if(thisUniverse.IsFinished()){
                    if(thisUniverse.Player1Wins()){
                        wins[0] += currInstances;
                    }
                    else if(thisUniverse.Player2Wins()){
                        wins[1] += currInstances;
                    }
                    continue; //Do not split, do not pass go, do not collect $200
                }

                thisUniverse.DoMultiverseSplit(quantumDiceResults, newMultiverse, currInstances);
            }

            pendingUniverses = newMultiverse;
        }
        answers[1] = wins[0];

        if(!silent){
            System.out.println("Part 1: Score of losing player times dice rolls: " + answers[0]);
            System.out.printf("Part 2: %nUniverses where Player 1 wins: %s%nUniverses where Player 2 wins: %s", wins[0], wins[1]);
        }

        return answers;
    }

}
