package org.aoc2021.Day25;

import org.aoc2021.Utils.Utils;

import java.awt.*;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.List;

public class Day25 {
    static final char EMPTY_CHAR = '.';
    static final List<Character> HERD_PRIORITY = Arrays.asList('>', 'v');

    public static void main(String[] args) {
        Run("Day25", false);
    }
    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByLinebreaks(filename);
        int maxx = input.get(0).length() - 1;
        int maxy = input.size() - 1;

        Map<Point, Character> board = getBoard(input, maxx, maxy);
        List<Herd> herds = new ArrayList<>();
        herds.add(new Herd('>', new Point(1, 0)));
        herds.add(new Herd('v', new Point(0, -1)));
        for(Map.Entry<Point, Character> e : board.entrySet()){
            int index = HERD_PRIORITY.indexOf(e.getValue());
            Herd herd = herds.get(index);
            herd.integrants.add(e.getKey());
        }

        int timeElapsed = 0;
        boolean done = false;
        while(!done){
            done = true;
            for(Herd herd : herds){
                List<Map.Entry<Point, Point>> queuedMovements = new ArrayList<>();
                for(Point a : herd.integrants){
                    Point vector = herd.vector;
                    int newX = a.x + vector.x;
                    newX = Utils.RealModulo(newX, maxx + 1);
                    int newY = a.y + vector.y;
                    newY = Utils.RealModulo(newY, maxy + 1);
                    Point b = new Point(newX, newY);
                    if(board.containsKey(b)) continue;

                    done = false;
                    queuedMovements.add(new AbstractMap.SimpleEntry<>(a, b));
                }
                for(Map.Entry<Point, Point> movement : queuedMovements){
                    move(movement.getKey(), movement.getValue(), board, herd);
                }
            }
            timeElapsed++;
            if(!silent){
                System.out.println("After " + timeElapsed + " steps:");
                printBoard(board, maxx, maxy);
            }
        }
        answers[0] = timeElapsed;
        answers[1] = "Merry Christmas!";

        if(!silent){
            System.out.println("Time elapsed until all herds settle down: " + answers[0]);
            System.out.println(answers[1]);
        }

        return answers;
    }

    public static void move(Point a, Point b, Map<Point, Character> board, Herd herd){
        if(board.containsKey(b)){
            throw new IllegalArgumentException("Illegal movement: Point B is already taken! " + a + " -> " + b);
        }
        board.remove(a);
        board.put(b, herd.sprite);
        herd.integrants.remove(a);
        herd.integrants.add(b);
    }

    private static Map<Point, Character> getBoard(List<String> input, int maxx, int maxy) {
        Map<Point, Character> board = new HashMap<>();
        for(int y = 0; y <= maxy; y++){
            String line = input.get(maxy - y);
            for(int x = 0; x <= maxx; x++){
                char c = line.charAt(x);
                if(c == EMPTY_CHAR) continue;
                Point point = new Point(x, y);
                board.put(point, c);
            }
        }
        return board;
    }
    public static void printBoard(Map<Point, Character> board, int maxx, int maxy){
        StringBuilder out = new StringBuilder();
        for(int y = maxy; y >= 0; y--){
            for(int x = 0; x <= maxx; x++){
                Point point = new Point(x, y);
                if(!board.containsKey(point)){
                    out.append(EMPTY_CHAR);
                }
                else{
                    out.append(board.get(point));
                }
            }
            out.append("\n");
        }
        System.out.println(out);
    }

}
