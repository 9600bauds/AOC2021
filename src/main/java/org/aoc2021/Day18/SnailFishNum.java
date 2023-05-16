package org.aoc2021.Day18;

import org.aoc2021.Utils.Utils;

public class SnailFishNum {
    Object leftTerm;
    Object rightTerm;
    SnailFishNum parent;

    public SnailFishNum(String input, SnailFishNum parent) {
        this.parent = parent;
        int commaIndex = FindComma(input);
        if(commaIndex < 0){
            throw new IllegalArgumentException("Could not split string into 2 pairs! " + input);
        }
        String leftString = input.substring(1, commaIndex); //Start at 1 to cut out the starting bracket
        String rightString = input.substring(commaIndex+1, input.length() - 1); //End at length()-1 to cut out the closing bracket
        if(leftString.contains("[")){
            leftTerm = new SnailFishNum(leftString, this);
        }
        else{
            leftTerm = Integer.valueOf(leftString);
        }
        if(rightString.contains("[")){
            rightTerm = new SnailFishNum(rightString, this);
        }
        else{
            rightTerm = Integer.valueOf(rightString);
        }
    }

    public SnailFishNum(Integer leftInt, Integer rightInt, SnailFishNum parent) {
        this.leftTerm = leftInt;
        this.rightTerm = rightInt;
        this.parent = parent;
    }

    public SnailFishNum(SnailFishNum leftPair, SnailFishNum rightPair) {
        this.leftTerm = leftPair;
        leftPair.parent = this;
        this.rightTerm = rightPair;
        rightPair.parent = this;
    }

    /**
     * The magnitude of a pair is 3 times the magnitude of its left element plus 2 times the magnitude of its right element.
     * The magnitude of a regular number is just that number.
     * @return The magnitude of this SnailFishNum, which is calculated recursively if need be.
     */
    public Long GetMagnitude(){
        long out = 0L;
        if(leftTerm instanceof Integer){
            out += 3L * (Integer)leftTerm;
        }
        else if(leftTerm instanceof SnailFishNum){
            out += 3L * ((SnailFishNum) leftTerm).GetMagnitude();
        }
        if(rightTerm instanceof Integer){
            out += 2L * (Integer)rightTerm;
        }
        else if(rightTerm instanceof SnailFishNum){
            out += 2L * ((SnailFishNum) rightTerm).GetMagnitude();
        }
        return out;
    }

    public static int FindComma(String input){
        int depth = 0;
        int index = 0;
        for(char c : input.toCharArray()){
            if(c == '['){
                depth++;
            }
            else if(c == ']'){
                depth--;
            }
            else if(c == ','){
                if(depth == 1){
                    return index;
                }
            }
            index++;
        }
        return -1;
    }

    private SnailFishNum GetRoot(){
        if(parent == null){
            return this;
        }
        return parent.GetRoot();
    }

    /**
     * Keep checking for explosions or splits until no more actions are required and the number is fully reduced.
     */
    public void Reduce(){
        boolean working = true;
        while(working){
            working = CheckExplosion(0) || CheckSplit();
        }
    }

    public boolean CheckSplit(){
        if(leftTerm instanceof SnailFishNum){
            SnailFishNum left = (SnailFishNum) leftTerm;
            if(left.CheckSplit()){
                return true;
            }
        }
        else if(leftTerm instanceof Integer){
            if((Integer)leftTerm > Day18.MAX_NUMBER){
                Split(true);
                return true;
            }
        }

        if(rightTerm instanceof SnailFishNum){
            SnailFishNum right = (SnailFishNum) rightTerm;
            return right.CheckSplit();
        }
        else if(rightTerm instanceof Integer){
            if((Integer)rightTerm > Day18.MAX_NUMBER){
                Split(false);
                return true;
            }
        }
        return false;
    }

    private void Split(boolean isLeft) {
        if(isLeft){
            int val = (Integer)leftTerm;
            int roundedDown = val / 2;
            int roundedUp = Utils.DivideAndRoundUp(val, 2);
            leftTerm = new SnailFishNum(roundedDown, roundedUp, this);
        } else {
            int val = (Integer)rightTerm;
            int roundedDown = val / 2;
            int roundedUp = Utils.DivideAndRoundUp(val, 2);
            rightTerm = new SnailFishNum(roundedDown, roundedUp, this);
        }
    }

    public boolean CheckExplosion(int depth){
        if(depth > Day18.MAX_DEPTH){
            Explode();
            return true;
        }
        if(leftTerm instanceof SnailFishNum){
            SnailFishNum left = (SnailFishNum) leftTerm;
            if(left.CheckExplosion(depth + 1)){
                return true;
            }
        }
        if(rightTerm instanceof SnailFishNum){
            SnailFishNum right = (SnailFishNum) rightTerm;
            if(right.CheckExplosion(depth + 1)){
                return true;
            }
        }
        return false;
    }

    private void Explode() {
        parent.ReceiveLTRExplosion((Integer)rightTerm, this);
        parent.ReceiveRTLExplosion((Integer)leftTerm, this);
        parent.DestroyChild(this);
    }

    private void DestroyChild(SnailFishNum child) {
        if(child == leftTerm){
            leftTerm = 0;
        }
        else if(child == rightTerm){
            rightTerm = 0;
        }
        else{
            throw new RuntimeException("Could not find child to destroy!");
        }
    }

    //RTL -> right to left, meaning, the explosion is heading towards the left edge
    private void ReceiveRTLExplosion(int val, SnailFishNum incoming){
        if(incoming == leftTerm){
            if(parent != null) parent.ReceiveRTLExplosion(val, this);
        }
        else if(incoming == rightTerm){
            if(leftTerm instanceof Integer){
                leftTerm = (Integer)leftTerm + val;
            } else {
                SnailFishNum left = (SnailFishNum) leftTerm;
                left.ReceiveRTLExplosion(val, this);
            }
        }
        else{
            if(rightTerm instanceof Integer){
                rightTerm = (Integer)rightTerm + val;
            }
            else {
                SnailFishNum right = (SnailFishNum) rightTerm;
                right.ReceiveRTLExplosion(val, this);
            }
        }
    }

    //LTR = left-to-right, meaning, the explosion is heading towards the right edge
    private void ReceiveLTRExplosion(int val, SnailFishNum incoming){
        if(incoming == rightTerm){
            if(parent != null) parent.ReceiveLTRExplosion(val, this);
        }
        else if(incoming == leftTerm){
            if(rightTerm instanceof Integer){
                rightTerm = (Integer)rightTerm + val;
            } else {
                SnailFishNum right = (SnailFishNum) rightTerm;
                right.ReceiveLTRExplosion(val, this);
            }
        }
        else{
            if(leftTerm instanceof Integer){
                leftTerm = (Integer) leftTerm + val;
            }
            else {
                SnailFishNum left = (SnailFishNum) leftTerm;
                left.ReceiveLTRExplosion(val, this);
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("[");
        sb.append(leftTerm);
        sb.append(",");
        sb.append(rightTerm);
        sb.append("]");
        return sb.toString();
    }
}
