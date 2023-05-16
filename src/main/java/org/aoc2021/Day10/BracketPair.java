package org.aoc2021.Day10;

import java.util.List;

public class BracketPair {
    char openChar;
    char closeChar;
    int invalidScore;
    int incompleteScore;

    public BracketPair(char openChar, char closeChar, int score, int incompleteScore) {
        this.openChar = openChar;
        this.closeChar = closeChar;
        this.invalidScore = score;
        this.incompleteScore = incompleteScore;
    }

    public static BracketPair GetPair(char c, List<BracketPair> pairs){
        for(BracketPair pair : pairs){
            if(pair.openChar == c || pair.closeChar == c){
                return pair;
            }
        }
        throw new RuntimeException("Could not find a pair for char " + c + "!");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append(openChar);
        sb.append(closeChar);
        return sb.toString();
    }
}