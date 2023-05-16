package org.aoc2021.Day4;

import org.aoc2021.Utils.Utils;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

//https://adventofcode.com/2021/day/4
public class Day4 {

    static final int BINGO_CARD_SIZE = 5;

    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByEmptyLines(filename);

        //The first line is only the bingo numbers
        String[] bingoNumbers = input.get(0).split(",");
        //Subsequent lines are the bingo cards
        List<String> bingoCardStrings = input.subList(1, input.size());
        List<BingoCard> bingoCards = new ArrayList<>();

        for(int i = 0; i < bingoCardStrings.size(); i++){
            BingoCard bingoCard = new BingoCard(new String[BINGO_CARD_SIZE][BINGO_CARD_SIZE],
                    new boolean[BINGO_CARD_SIZE][BINGO_CARD_SIZE]);
            bingoCards.add(bingoCard);

            String bingoCardString = bingoCardStrings.get(i);
            String[] lines = bingoCardString.split("\\r\\n"); //Split by linebreaks
            for(int lineIndex = 0; lineIndex < lines.length; lineIndex++){
                String line = lines[lineIndex];
                String[] numbers = Utils.GetAllRegexMatches(line, "\\d+"); //Regex matching whole numbers
                bingoCard.numbers[lineIndex] = numbers;
            }
        }

        boolean foundWinner = false;
        for(String number : bingoNumbers){
            for(int i = 0; i < bingoCards.size(); i++){
                BingoCard card = bingoCards.get(i);
                Boolean success = card.TickNumber(number);
                if(success && card.IsWinner()){
                    if (!foundWinner) {
                        foundWinner = true;
                        int sum = card.SumOfUnmarkedNumbers();
                        int finalScore = sum * Integer.parseInt(number);
                        answers[0] = finalScore;
                        if(!silent){
                            System.out.println("Bingo with number " + number + "!");
                            System.out.println(card.toString());
                            System.out.println("Sum of unmarked numbers: " + sum);
                            System.out.println("Final score: " + finalScore);
                        }
                    }
                    bingoCards.remove(card);
                    i--;
                    if(bingoCards.size() == 0){
                        int sum = card.SumOfUnmarkedNumbers();
                        int finalScore = sum * Integer.parseInt(number);
                        answers[1] = finalScore;
                        if(!silent){
                            System.out.println("Found the last winning card with number " + number + "!");
                            System.out.println(card.toString());
                            System.out.println("Sum of unmarked numbers: " + sum);
                            System.out.println("Final score: " + finalScore);
                        }
                        return answers;
                    }
                }
            }
        }
        //We return early if we find the answers, so we should never get to this point
        throw new RuntimeException("Could not compute answers!");
    }

}
