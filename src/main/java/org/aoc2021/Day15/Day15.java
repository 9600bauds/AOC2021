package org.aoc2021.Day15;

import org.aoc2021.Utils.Geometry;
import org.aoc2021.Utils.TerminalColors;
import org.aoc2021.Utils.Utils;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.lang.invoke.MethodHandles;


//https://adventofcode.com/2021/day/15
public class Day15 {
    static final int PART_2_SIZE_MULT = 5;
    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByLinebreaks(filename);

        int[][] board = ProcessInput(input);
        Point startingPoint = new Point(0,0);
        Point finishPoint = new Point(board[0].length-1, board.length-1);

        SearchState winner = PathFind(board, startingPoint, finishPoint);
        answers[0] = winner.score;
        if(!silent){
            DrawMap(board, winner.path);
            System.out.println("Score of solution 1: " + winner.score);
        }

        int[][] bigBoard = BigBoardify(board, PART_2_SIZE_MULT);
        Point startingPoint2 = new Point(0,0);
        Point finishPoint2 = new Point(bigBoard[0].length-1, bigBoard.length-1);

        SearchState winner2 = PathFind(bigBoard, startingPoint2, finishPoint2);
        answers[1] = winner2.score;
        if(!silent){
            DrawMap(bigBoard, winner2.path);
            System.out.println("Score of solution 2: " + winner2.score);
        }
        return answers;
    }

    private static int[][] BigBoardify(int[][] board, int times) {
        int width = board[0].length;
        int height = board.length;
        int[][] bigBoard = new int[width * times][height * times];
        for(int i = 0; i < times; i++){
            for(int j = 0; j < times; j++){
                for(int x = 0; x < width; x++){
                    for(int y = 0; y < height; y++){
                        int num = board[x][y];
                        num += i;
                        num += j;
                        if(num > 9) num = num % 9;
                        bigBoard[x + i* height][y + j* height] = num;
                    }
                }
            }
        }
        return bigBoard;
    }

    /**
     * Processing the input to get a 2d int array of the board.
     * We just iterate through the input and populate the array 1 char at a time.
     * Note that [0][0] corresponds to the board's upper-left corner in this case.
     */
    private static int[][] ProcessInput(List<String> input) {
        int height = input.size();
        int width = input.get(0).length();
        int[][] board = new int[width][height];
        for(int y = 0; y < height; y++) {
            String line = input.get(y);
            for (int x = 0; x < width; x++) {
                board[x][y] = Character.getNumericValue(line.charAt(x));
            }
        }
        return board;
    }

    private static SearchState PathFind(int[][] board, Point startingPoint, Point finishPoint) {
        int width = board[0].length;
        int height = board.length;
        int[][] bestScore = new int[width][height];
        Arrays.stream(bestScore).forEach(a -> Arrays.fill(a, Integer.MAX_VALUE)); //More score = more bad
        bestScore[startingPoint.x][startingPoint.y] = 0;

        SearchState startingState = new SearchState(startingPoint);
        List<SearchState> pendingSearches = new ArrayList<>();
        pendingSearches.add(startingState);

        SearchState winner = null;
        while(pendingSearches.size() > 0){
            pendingSearches.sort(Comparator.comparing(searchState -> searchState.score)); //todo use an ordered set
            SearchState currState = pendingSearches.get(0);
            pendingSearches.remove(currState);
            if(currState.pos.equals(finishPoint)){
                winner = currState;
                break;
            }
            List<Point> neighbors = Geometry.GetOrthogonalNeighbors(currState.pos, board);
            for(Point neighbor : neighbors){
                SearchState newState = new SearchState(neighbor, currState, board);
                int toBeat = bestScore[neighbor.x][neighbor.y];
                if(newState.score < toBeat){
                    bestScore[neighbor.x][neighbor.y] = newState.score;
                    pendingSearches.add(newState);
                }
            }
        }
        return winner;
    }


    public static void DrawMap(int[][] board, List<Point> highlights){
        for(int y = 0; y < board.length; y++){
            StringBuilder line = new StringBuilder();
            for(int x = 0; x < board[0].length; x++){
                int num = board[x][y];
                if(highlights.contains(new Point(x, y))){
                    line.append(TerminalColors.ANSI_BG_GREEN).append(num).append(TerminalColors.ANSI_RESET);
                }
                else{
                    line.append(num);
                }

            }
            System.out.println(line);
        }
    }
}
