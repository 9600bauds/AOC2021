package org.aoc2021.Day16;

import org.aoc2021.Utils.Utils;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;

//https://adventofcode.com/2021/day/16
public class Day16 {

    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        answers[0] = 0L;
        answers[1] = 0L;
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByLinebreaks(filename);


        for(String hexLine : input){
            String binLine = Utils.Hex2Bin(hexLine);
            BITSScanner scanner = new BITSScanner(binLine);
            BITSPacket root = new BITSPacket(scanner);
            root.RecursivelyCalculateValue();
            int sum = root.RecursivelyGetPacketVersionSum();
            answers[0] = (long) answers[0] + sum;
            answers[1] = (long) answers[1] + root.value;
            if(!silent){
                System.out.println("Packet " + hexLine + " has a sum of versions of " + sum + " and a value of " + root.value);
                System.out.println("As a formula: " + root);
            }
        }

        return answers;

    }


}
