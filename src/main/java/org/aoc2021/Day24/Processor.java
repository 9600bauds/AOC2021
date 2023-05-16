package org.aoc2021.Day24;

import org.aoc2021.Utils.Utils;

import java.util.*;

public class Processor {
    public long[] vars = new long[]{0,0,0,0};
    Stack<Integer> fillerDigits;
    InstructionSet instructionSet;
    long constructedSerial = 0L;
    boolean silent = true;

    public Processor(InstructionSet instructionSet, Long digitsLong) {
        this.instructionSet = instructionSet;
        fillerDigits = Utils.longToStack(digitsLong);
    }

    public boolean resultIsValid(){
        return vars[3] == 0;
    }

    public void run(){
        int currentDigitIndex = 0;
        for(Instruction instruction : instructionSet.instructionList){
            switch(instruction.operation){
                case "inp":
                    boolean success = inp(instruction.indexA, currentDigitIndex);
                    if(!success){
                        return;
                    }
                    currentDigitIndex++;
                    break;
                // There's probably a better way to do this, but it apparently requires interfaces and a lot more complications than just this.
                case "set":
                    set(instruction.indexA, instruction.getRightValue(this));
                    break;
                case "add":
                    add(instruction.indexA, instruction.getRightValue(this));
                    break;
                case "mod":
                    mod(instruction.indexA, instruction.getRightValue(this));
                    break;
                case "div":
                    div(instruction.indexA, instruction.getRightValue(this));
                    break;
                case "mul":
                    mul(instruction.indexA, instruction.getRightValue(this));
                    break;
                case "eql":
                    eql(instruction.indexA, instruction.getRightValue(this));
                    break;
                default:
                    throw new IllegalArgumentException("Could not find a function for operation " + instruction.operation);
            }
        }
    }

    public boolean inp(int indexA, int index){
        Integer digit;
        if(!instructionSet.significantDigits.contains(index)){
            digit = fillerDigits.pop();
        }
        else{
            //At each "significant digit", we can work out what the current digit should be:
            //It should always be z%26 plus the next value that gets added to x.
            //If our serial is invalid, then at some point of the process we will be unable to satisfy this condition
            //since a digit would need to be smaller than 1 or bigger than 9.
            int mod26 = (int) (vars[3] % instructionSet.moduloInteger);
            long nextX = instructionSet.xDeltas.get(index);
            digit = Math.toIntExact(mod26 + nextX);

            if (digit < 1 || digit > 9) {
                return false;
            }
        }
        vars[indexA] = digit;
        constructedSerial *= 10;
        constructedSerial += digit;
        return true;
    }

    public void set(int indexA, long valueB){
        vars[indexA] = valueB;
    }

    public void add(int indexA, long valueB){
        long varA = vars[indexA];
        vars[indexA] = varA + valueB;
    }

    public void mul(int indexA, long valueB){
        long varA = vars[indexA];
        vars[indexA] = varA * valueB;
    }

    public void div(int indexA, long valueB){
        if(valueB == 0L){
            throw new IllegalArgumentException("Tried to divide by zero!");
        }
        long varA = vars[indexA];
        vars[indexA] = varA / valueB;
    }

    public void mod(int indexA, long valueB){
        if(valueB <= 0L){
            throw new IllegalArgumentException("Tried to modulo by " + valueB + "!");
        }
        long varA = vars[indexA];
        if(varA < 0L){
            throw new IllegalArgumentException("Tried to modulo with A being less than zero!");
        }
        vars[indexA] = varA % valueB;
    }

    public void eql(int indexA, long valueB){
        long varA = vars[indexA];
        if(varA == valueB){
            vars[indexA] = 1L;
        }
        else{
            vars[indexA] = 0L;
        }
    }
}
