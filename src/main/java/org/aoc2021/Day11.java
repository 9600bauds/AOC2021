package org.aoc2021;

import org.aoc2021.Utils.Geometry;
import org.aoc2021.Utils.TerminalColors;
import org.aoc2021.Utils.Utils;

import java.awt.*;
import java.lang.invoke.MethodHandles;
import java.util.List;

//https://adventofcode.com/2021/day/11
public class Day11 {
    static final int STEPS_TO_SIMULATE = 100;
    static final boolean WAIT_FOR_SYNCHRONIZATION = true;
    static final int MAX_ENERGY_LEVEL = 9;
    static final int MIN_ENERGY_LEVEL = 0;

    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByLinebreaks(filename);

        //Processing the input to get a 2d int array of the board
        //We just iterate through the input and populate the array 1 char at a time.
        //Notice that [0][0] corresponds to the board's lower-left corner, thus,
        //the first line corresponds to the max y value.
        int maxy = input.size() - 1;
        int maxx = input.get(0).length() - 1;
        int[][] board = new int[maxx + 1][maxy + 1];
        boolean[][] flashing = new boolean[maxx + 1][maxy + 1];
        for(int i = 0; i <= maxy; i++) {
            String line = input.get(i);
            for (int j = 0; j <= maxx; j++) {
                board[j][maxy - i] = Character.getNumericValue(line.charAt(j));
            }
        }

        int flashCounter = 0;
        int step = 1;
        boolean done = false;
        while(!done){
            //Pass 1: Initial energy
            for(int y= 0; y <= maxy; y++) {
                for (int x = 0; x <= maxx; x++) {
                    board[x][y]++;
                }
            }
            //Pass 2: Check for flashes
            for(int y= 0; y <= maxy; y++) {
                for (int x = 0; x <= maxx; x++) {
                    Point p = new Point(x, y);
                    CheckFlash(board, flashing, p);
                }
            }
            //Pass 3: Cleanup flashes
            int flashesThisStep = 0;
            for(int y= 0; y <= maxy; y++) {
                for (int x = 0; x <= maxx; x++) {
                    if(!flashing[x][y]){
                        continue;
                    }
                    flashing[x][y] = false;
                    board[x][y] = MIN_ENERGY_LEVEL;
                    flashesThisStep++;
                }
            }
            flashCounter += flashesThisStep;
            if(flashesThisStep >= (maxx + 1) * (maxy + 1)){
                if(!silent){
                    System.out.println("Synchronized at step: " + step);
                }
                answers[1] = step;
                done = true;
            }

            if(step == STEPS_TO_SIMULATE){
                if(!silent){
                    System.out.println("\r\nAfter step " + step + ":");
                    DrawMap(board);
                    System.out.println("Total flashes at this step: " + flashCounter);
                }
                answers[0] = flashCounter;
                if (!WAIT_FOR_SYNCHRONIZATION) {
                    done = true;
                }
            }
            else if(!silent && step % 10 == 0 && step < STEPS_TO_SIMULATE){
                System.out.println("\r\nAfter step " + step + ":");
                DrawMap(board);
            }
            step++;
        }

        return answers;
    }

    /**
     * Checks if a tile should flash, and if so, goes ahead and flashes.
     */
    public static void CheckFlash(int[][] board, boolean[][] flashing, Point p){
        if(flashing[p.x][p.y]){
            return;
        }
        if(board[p.x][p.y] > MAX_ENERGY_LEVEL){
            Flash(board, flashing, p);
        }
    }

    public static void Flash(int[][] board, boolean[][] flashing, Point p){
        flashing[p.x][p.y] = true;
        List<Point> neighbors = Geometry.Orange(p, 1, board);
        for(Point neighbor : neighbors){
            board[neighbor.x][neighbor.y]++;
            CheckFlash(board, flashing, neighbor);
        }
    }

    public static void DrawMap(int[][] board){
        for(int y = board.length-1; y >= 0; y--){
            String line = "";
            for(int x = 0; x < board[0].length-1; x++){
                int num = board[x][y];
                if(num == MIN_ENERGY_LEVEL){
                    line += TerminalColors.ANSI_BG_CYAN + num + TerminalColors.ANSI_RESET;
                }
                else line += num;
            }
            System.out.println(line);
        }
    }
}
