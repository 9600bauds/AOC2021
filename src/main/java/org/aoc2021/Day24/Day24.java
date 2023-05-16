package org.aoc2021.Day24;

import org.aoc2021.Utils.Utils;

import java.lang.invoke.MethodHandles;
import java.util.*;

public class Day24 {

    public static void main(String[] args) {
        Run("Day24", false);
    }
    public static Object[] Run(String filename, boolean silent) {
        /*
        I am unashamed to admit that I hated this puzzle and was unable to "properly" work out what the MONAD function does.
        Basically: At each digit of the 14-digit serial, one of two things will happen:
        EITHER: z increases, nothing we can do about it
            (this is treated as an 'insignificant digit', we don't really know what should go here)
        OR: z decreases, but only if z % 26 + (the next value that will be added to x) == the current digit being inputted.
            (this is treated as a 'significant digit', we can work out what should go here with the above formula)
        Since I couldn't work out what the proper logic is for which 'insignificant' digits you should input to always pass the z%26+x==w check,
        I simply bruteforce them. If at any point of a serial, a 'significant digit' z%26+x==w check fails, we just try another serial.
        This is not the "proper" solution, but this bruteforce is optimized enough to take less than 2 seconds in my PC, so I consider it acceptable.
         */
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> textInput = Utils.SplitInputByLinebreaks(filename);

        InstructionSet instructionSet = new InstructionSet(textInput);

        long highestSerial = (long) Math.pow(10, instructionSet.significantDigits.size()) - 1;
        long lowestSerial = highestSerial / 9;
        for(long currSerial = highestSerial; currSerial >= lowestSerial; currSerial--){
            if(Utils.containsZero(currSerial)) continue;

            Processor p = new Processor(instructionSet, currSerial);
            p.run();
            if(p.resultIsValid()){
                answers[0] = p.constructedSerial;
                if(!silent){
                    System.out.println("Largest valid serial: " + p.constructedSerial + " (Filler: " + currSerial + ")");
                }
                break;
            }
        }
        for(long currSerial = lowestSerial; currSerial <= highestSerial; currSerial++){
            if(Utils.containsZero(currSerial)) continue;

            Processor p = new Processor(instructionSet, currSerial);
            p.run();
            if(p.resultIsValid()){
                answers[1] = p.constructedSerial;
                if(!silent){
                    System.out.println("Smallest valid serial: " + p.constructedSerial + " (Filler: " + currSerial + ")");
                }
                break;
            }
        }

        return answers;
    }
}
