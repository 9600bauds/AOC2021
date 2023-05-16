package org.aoc2021;

import org.aoc2021.Utils.Utils;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.regex.*;

//https://adventofcode.com/2021/day/2
public class Day2 {

    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByLinebreaks(filename);

        int fakedepth = 0;
        int realdepth = 0;
        int horizontalpos = 0;
        int aim = 0; //This is technically the same as "fakedepth" but I'll keep them separate for clarity.

        //Regex pattern that matches: 1 whole word (the command) and then 1 number (the amount)
        Pattern regex = Pattern.compile("(\\w+) (\\d+)");

        for (int i = 0; i < input.size(); i++) {
            String instruction = input.get(i);
            Matcher match = regex.matcher(instruction);
            if (!match.find() || match.groupCount() < 2) {
                throw new RuntimeException("Invalid instruction! " + instruction);
            }
            String command = match.group(1);
            int amount = Integer.parseInt(match.group(2));
            switch (command) {
                case "down":
                    fakedepth += amount;
                    aim += amount;
                    break;
                case "up":
                    fakedepth -= amount;
                    aim -= amount;
                    break;
                case "forward":
                    horizontalpos += amount;
                    realdepth += aim * amount;
                    break;
                default:
                    throw new RuntimeException("Invalid instruction! " + instruction);
            }
        }
        answers[0] = fakedepth * horizontalpos;
        if (!silent) {
            System.out.println(" --PART 1--");
            System.out.println("Final depth: " + fakedepth + " - Final horizontal pos: " + horizontalpos);
            System.out.println("Multiplied together: " + answers[0]);
        }

        answers[1] = realdepth * horizontalpos;
        if (!silent) {
            System.out.println(" --PART 2--");
            System.out.println("Final depth: " + realdepth + " - Final horizontal pos: " + horizontalpos);
            System.out.println("Multiplied together: " + answers[1]);
        }

        return answers;
    }
}
