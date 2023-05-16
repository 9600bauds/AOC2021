package org.aoc2021.Day24;

import java.util.ArrayList;
import java.util.List;

public class InstructionSet {
    List<Instruction> instructionList;
    List<Long> xDeltas;
    List<Integer> significantDigits;
    Long moduloInteger;

    public InstructionSet(List<String> commands){
        instructionList = new ArrayList<>();
        for(String line : commands){
            instructionList.add(new Instruction(line));
        }

        /*for(int i = 1; i < instructionList.size(); i++){
            Instruction currInstruction = instructionList.get(i);
            Instruction prevInstruction = instructionList.get(i - 1);

            if(Objects.equals(prevInstruction.indexA, currInstruction.indexA)){
                if(prevInstruction.operation.equals("set") && prevInstruction.valueB != null && prevInstruction.valueB.equals(0L)){
                    if(currInstruction.operation.equals("add")){
                        instructionList.remove(currInstruction);
                        prevInstruction.valueB = currInstruction.valueB;
                        prevInstruction.indexB = currInstruction.indexB;
                        i--;
                    }
                }
            }
            if(currInstruction.operation.equals("mul") && currInstruction.valueB != null && currInstruction.valueB.equals(0L)){
                instructionList.remove(currInstruction);
                instructionList.add(i, new Instruction("set " + Instruction.int2char(currInstruction.indexA) + " 0"));
            }
        }*/

        int currentDigit = -1; //The first digit will always be the first instruction, and the counter updates before the instruction fires, so we start at -1
        xDeltas = new ArrayList<>();
        significantDigits = new ArrayList<>();
        for(Instruction instruction : instructionList){
            switch(Instruction.int2char(instruction.indexA)){
                case 'w':
                    if(instruction.operation.equals("inp")){
                        currentDigit++;
                    }
                    break;
                case 'x':
                    if(instruction.valueB != null && instruction.operation.equals("add")){
                        xDeltas.add(instruction.valueB);
                    }
                    break;
                case 'z':
                    if(instruction.operation.equals("div")){
                        long divisor = instruction.valueB;
                        if(divisor == 1) continue;
                        significantDigits.add(currentDigit);
                        moduloInteger = divisor;
                    }
                    break;
            }
        }
    }
}
