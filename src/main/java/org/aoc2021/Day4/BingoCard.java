package org.aoc2021.Day4;

import org.aoc2021.Utils.TerminalColors;

public class BingoCard {
    String[][] numbers;
    boolean[][] numbersCalled;
    int rows;
    int cols;

    public BingoCard(String[][] numbers, boolean[][] numbersCalled) {
        this.numbers = numbers;
        this.numbersCalled = numbersCalled;
        this.rows = numbers.length;
        this.cols = numbers[0].length;
    }

    public boolean IsWinner(){
        return FindColWithBingo() > -1 || FindRowWithBingo() > -1;
    }

    /**
     * Checks if we have a given number in our card, and if so, marks it as called.
     * @param number The number being called.
     * @return True if we had that number, False if we didn't.
     */
    public Boolean TickNumber(String number){
        for(int rowIndex = 0; rowIndex < rows; rowIndex++) {
            for (int colIndex = 0; colIndex < cols; colIndex++) {
                String tempNum = numbers[rowIndex][colIndex];
                if(tempNum.equals(number)){ //because == doesn't actually work for strings in Java
                    numbersCalled[rowIndex][colIndex] = true;
                    return true;
                }
            }
        }
        return false;
    }

    public int SumOfUnmarkedNumbers(){
        int sum = 0;
        for(int rowIndex = 0; rowIndex < rows; rowIndex++) {
            for (int colIndex = 0; colIndex < cols; colIndex++) {
                if(!numbersCalled[rowIndex][colIndex]){
                    sum += Integer.parseInt(numbers[rowIndex][colIndex]);
                }

            }
        }
        return sum;
    }

    /**
     * Iterates through the card to see if we have a full row of called numbers.
     * @return -1 if no winning row was found, or the index of the winning row if found
     */
    public int FindRowWithBingo(){
        for(int rowIndex = 0; rowIndex < rows; rowIndex++){
            boolean fullRow = true;
            for(int colIndex = 0; colIndex < cols; colIndex++){
                boolean numberCalled = numbersCalled[rowIndex][colIndex];
                if(!numberCalled){
                    fullRow = false;
                    break;
                }
            }
            if(fullRow) {
                return rowIndex;
            }
        }
        return -1;
    }

    /**
     * Iterates through the card to see if we have a full column of called numbers.
     * @return -1 if no winning column was found, or the index of the winning column if found
     */
    public int FindColWithBingo(){
        for(int colIndex = 0; colIndex < cols; colIndex++){
            boolean fullCol = true;
            for(int rowIndex = 0; rowIndex < rows; rowIndex++){
                boolean numberCalled = numbersCalled[rowIndex][colIndex];
                if(!numberCalled){
                    fullCol = false;
                    break;
                }
            }
            if(fullCol) {
                return colIndex;
            }
        }
        return -1;
    }

    @Override
    //Multiline representation of the card. Marks the called numbers as green
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        for(int rowIndex = 0; rowIndex < rows; rowIndex++) {
            for (int colIndex = 0; colIndex < cols; colIndex++) {
                String color = TerminalColors.ANSI_RESET;
                if(numbersCalled[rowIndex][colIndex]){
                    color = TerminalColors.ANSI_GREEN;
                }
                String num = numbers[rowIndex][colIndex];
                if(num.length() < 2){
                    num = " " + num;
                }
                sb.append(color + num + " ");
            }
            sb.append("\r\n");
        }
        sb.append(TerminalColors.ANSI_RESET);
        return sb.toString();
    }
}
