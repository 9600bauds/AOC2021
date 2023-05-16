package org.aoc2021.Day9;

import org.aoc2021.Utils.Geometry;
import org.aoc2021.Utils.TerminalColors;
import org.aoc2021.Utils.Utils;

import java.awt.*;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//https://adventofcode.com/2021/day/9
public class Day9 {
    static final int TOP_BASINS = 3; //how many basins matter for part 2?
    static final int MAX_HEIGHT = 9;

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
        for(int i = 0; i <= maxy; i++){
            String line = input.get(i);
            for(int j = 0; j <= maxx ; j++){
                board[j][maxy - i] = Character.getNumericValue(line.charAt(j));
            }
        }

        //Part 1: Identifying the low points.
        List<Point> lowPoints = new ArrayList<>();
        for(int y = maxy; y >= 0; y--){
            for(int x = 0; x <= maxx; x++){
                Point p = new Point(x, y);
                if(IsLowPoint(board, p)){
                    lowPoints.add(p);
                }
            }
        }
        int part1Sum = 0;
        for(Point p : lowPoints){
            part1Sum += GetDangerLevel(board[p.x][p.y]);
        }

        //Part 2: Identifying the basins.
        //Luckily for us, each low point corresponds to exactly one basin.
        List<Basin> basins = new ArrayList<>();
        for(Point p : lowPoints){
            Basin candidate = new Basin(board, MAX_HEIGHT);
            basins.add(candidate);
            candidate.BreadthFirstSearch(p); //Actually populate the basin
        }
        //Sort basins by size
        Collections.sort(basins, Comparator.comparing(Basin::GetSize).reversed()); //reversed since we want the biggest at the top
        List<Basin> topBasins = new ArrayList<>();
        for(int i = 0; i < TOP_BASINS; i++){
            Basin basin = basins.get(i);
            basin.color = TerminalColors.BACKGROUNDS[i+3]; //Give them colors, just for fun
            topBasins.add(basin);

        }
        int part2Mult = 1; //1 is the identity for multiplication
        List<String> part2Fluff = new ArrayList<>(); //Just fluff, we store the size of the basins so we can show our work
        for(Basin b : topBasins){
            int size = b.GetSize();
            part2Mult *= size;
            part2Fluff.add(Integer.toString(size));
        }

        if(!silent){
            DrawBoard(board, topBasins, lowPoints);
            System.out.println("Sum of the risk levels of all low points: " + part1Sum);
            System.out.println("Multiplication of the sizes of the " + TOP_BASINS + " largest basins: " + part2Mult
                    + " (" + String.join("*", part2Fluff) + ")!");
        }

        answers[0] = part1Sum;
        answers[1] = part2Mult;
        return answers;
    }

    public static void DrawBoard(int[][] board, List<Basin> topBasins, List<Point> lowPoints){
        int maxx = board.length - 1;
        int maxy = board[0].length - 1;

        for(int y = maxy; y >= 0; y--){
            String line = "";
            for(int x = 0; x <= maxx; x++){
                Point p = new Point(x, y);
                int num = board[x][y];

                String color = GetColor(p, num, topBasins, lowPoints, MAX_HEIGHT);
                if(color != null){
                    line += color + num + TerminalColors.ANSI_RESET;
                }
                else{
                    line += num;
                }

            }
            System.out.println(line);
        }
    }

    public static String GetColor(Point p, int num, List<Basin> basins, List<Point> lowPoints, int maxHeight){
        if(lowPoints.contains(p)){
            return TerminalColors.ANSI_BG_GREEN;
        }
        if(num >= maxHeight){
            return TerminalColors.ANSI_BG_RED;
        }
        for(Basin basin : basins){
            if(basin.tiles.contains(p)){
                return basin.color;
            }
        }
        return null;
    }
    public static boolean IsLowPoint(int[][] board, Point p){
        List<Point> candidates = Geometry.GetOrthogonalNeighbors(p, board);

        int num = board[p.x][p.y];
        for(Point neighbor : candidates){
            int neighborInt = board[neighbor.x][neighbor.y];
            if(neighborInt <= num){
                return false;
            }
        }
        return true;
    }

    public static int GetDangerLevel(int num){
        return 1+num;
    }
}
