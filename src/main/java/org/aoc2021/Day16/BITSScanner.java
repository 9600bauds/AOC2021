package org.aoc2021.Day16;

import org.aoc2021.Utils.Utils;

//This class mostly exists because you can't byref primitives in Java, so I need to make a whole dang class
//just so other scopes can modify int i.
public class BITSScanner {
    int i;
    String fullBinary;

    public BITSScanner(String fullBinary) {
        this.fullBinary = fullBinary; //Utils.Hex2Bin(hex);
        i = 0;
    }

    public String getBits(int amt){
        String out = fullBinary.substring(i, i+amt);
        i += amt;
        return out;
    }

    public boolean Done(){
        if (i >= fullBinary.length() - 1){
            return true;
        }
        String remaining = fullBinary.substring(i);
        if(remaining.matches("0*")){ //Only thing left is trailing zeroes
            return true;
        }
        return false;
    }
}
