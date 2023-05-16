package org.aoc2021;

import org.aoc2021.Utils.Utils;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

//https://adventofcode.com/2021/day/6
public class Day6 {
    static final int FISH_SPAWN_CYCLE = 7;
    static final int FISH_INFANCY_MALUS = 2;
    static final int DAYS_TO_SIMULATE_PT1 = 80;
    static final int DAYS_TO_SIMULATE_PT2 = 256;

    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByCommas(filename);
        //We represent the fish as an array counting the number of fish in each stage of growth.
        //i.e. "there are 155 fish that are 1 day away from spawning, there are 166 fish that are 2 days away..." etc
        long[] fish = new long[FISH_SPAWN_CYCLE + FISH_INFANCY_MALUS];
        for(String line : input){
            int days = Integer.parseInt(line);
            fish[days]++;
        }
        //Cloning the array just so we can do part 2 on it later
        long[] fishCopy = Arrays.copyOf(fish, fish.length);

        if(!silent) System.out.println("Initial state: " + Arrays.toString(fish));
        long answerPart1 = simulate(fish, DAYS_TO_SIMULATE_PT1, silent);
        answers[0] = answerPart1;

        long answerPart2 = simulate(fishCopy, DAYS_TO_SIMULATE_PT2, silent);
        answers[1] = answerPart2;

        return answers;
    }

    private static long simulate(long[] fish, int days, boolean silent) {
        for(int day = 1; day <= days; day++){
            long temp_FishReadyToSpawn = fish[0];
            for(int i = 0; i < fish.length - 1; i++){
                fish[i] = fish[i+1];
            }
            fish[fish.length-1] = 0;

            fish[FISH_SPAWN_CYCLE-1] += temp_FishReadyToSpawn;
            fish[FISH_SPAWN_CYCLE-1 + FISH_INFANCY_MALUS] += temp_FishReadyToSpawn;
            //System.out.println("After " + day + " days: " + Arrays.toString(fish) + " Total: " + Arrays.stream(fish).sum());
        }
        long sum = Arrays.stream(fish).sum();
        if(!silent) System.out.println("Amount of fish after " + days + " days: " + sum);
        return sum;
    }
}
