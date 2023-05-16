package org.aoc2021.Day24;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Instruction used for Day 24. Please note that indexB and valueB are mutually exclusive, an instruction will refer to either
 * a set value, or the index of a variable, not both. To check which one it refers to, you can check whether the corresponding var is null.
 */
public class Instruction {
    String operation;
    Integer indexA;
    Integer indexB; //Index for the variable that this instruction refers to (0 for w, 1 for x, 2 for y, 3 for z)
    Long valueB; //Long value that this instruction uses.

    public static int char2index(char c){
        return c - 'w';
    }

    public static char int2char(int i){
        return (char) ((char) i + 'w');
    }

    public Instruction(String line){
        String[] lineSplit = line.split(" ");

        operation = lineSplit[0];

        String stringA = lineSplit[1];
        indexA = char2index(stringA.charAt(0));

        if(lineSplit.length < 3){
            return;
        }
        String stringB = lineSplit[2];
        try {
            valueB = Long.parseLong(stringB);
        } catch (NumberFormatException e) {
            indexB = char2index(stringB.charAt(0));
        }
    }

    /**
     * Gets the long value of whatever this instruction refers to. If it refers to a static value, returns that. If it refers
     * to a variable index (e.g. 'x', 'y') it returns the variable directly from the given processor's memory.
     */
    public long getRightValue(Processor p){
        if(indexB != null){
            return p.vars[indexB];
        }
        else if(valueB != null){
            return valueB;
        }
        else{
            throw new RuntimeException("Instruction " + this + " had neither index nor value for right term!");
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append(operation);
        sb.append(" ").append(int2char(indexA));
        if(indexB != null){
            sb.append(" ").append(int2char(indexB));
        }
        else{
            sb.append(" ").append(valueB);
        }
        return sb.toString();
    }
}
