package org.aoc2021.Day21;

import org.aoc2021.Utils.CollectionUtils;
import org.aoc2021.Utils.Utils;

import java.util.Map;
import java.util.Objects;

public class Diracverse {
    int player1pos;
    int player2pos;
    int player1score;
    int player2score;
    boolean isPlayer1Turn;
    int maxSpace;
    int scoreGoal;

    public Diracverse(int player1pos, int player2pos, int player1score, int player2score, int maxSpace, int scoreGoal) {
        this.player1pos = player1pos;
        this.player2pos = player2pos;
        this.player1score = player1score;
        this.player2score = player2score;
        this.maxSpace = maxSpace;
        this.scoreGoal = scoreGoal;
        isPlayer1Turn = true;
    }

    public Diracverse(Diracverse parent){
        player1pos = parent.player1pos;
        player2pos = parent.player2pos;
        player1score = parent.player1score;
        player2score = parent.player2score;
        maxSpace = parent.maxSpace;
        scoreGoal = parent.scoreGoal;
        isPlayer1Turn = parent.isPlayer1Turn;
    }

    public void DoMultiverseSplit(Map<Integer, Long> quantumDiceResults, Map<Diracverse, Long> newMultiverse, Long currInstances) {
        for(Map.Entry<Integer, Long> diceResult : quantumDiceResults.entrySet()){
            int val = diceResult.getKey(); //What's the combination of results that the dice landed on during the 3 throws?
            long weight = diceResult.getValue(); //How often does that result occur per 3 throws?
            long universalResults = weight * currInstances; //How many new universes will be created by this outcome?

            Diracverse result = new Diracverse(this);
            result.TakeTurn(val);

            CollectionUtils.addValueToMap(newMultiverse, result, universalResults);
        }
    }

    public boolean IsFinished(){
        return Player1Wins() || Player2Wins();
    }

    public boolean Player1Wins() {
        return player1score >= scoreGoal;
    }

    public boolean Player2Wins() {
        return player2score >= scoreGoal;
    }

    public void TakeTurn(int diceResult){
        if(isPlayer1Turn){
            player1pos = Utils.SumToLoopingValue(player1pos, maxSpace, diceResult);
            player1score += player1pos;
        }
        else{
            player2pos = Utils.SumToLoopingValue(player2pos, maxSpace, diceResult);
            player2score += player2pos;
        }
        isPlayer1Turn = !isPlayer1Turn;
    }

    @Override
    public String toString() {
        return "P1:" + player1score + "/" +
                "P2:" + player2score;
    }

    //Very important for the map
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Diracverse that = (Diracverse) o;
        return player1pos == that.player1pos && player2pos == that.player2pos && player1score == that.player1score && player2score == that.player2score && isPlayer1Turn == that.isPlayer1Turn && maxSpace == that.maxSpace && scoreGoal == that.scoreGoal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(player1pos, player2pos, player1score, player2score, isPlayer1Turn, maxSpace, scoreGoal);
    }
}
