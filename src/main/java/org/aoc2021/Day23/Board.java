package org.aoc2021.Day23;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Helper object used to store non-changing info about the board in the Amphipod Sorting game.
 */
public class Board {
    char SPRITE_WALL = '#';
    char SPRITE_EMPTY = '.';
    char SPRITE_INACCESSIBLE = '░';
    char SPRITE_BLANK = ' ';
    int hallwayY;
    int backrankY;
    List<Character> orderedChars;
    List<Integer> xRooms;
    List<Integer> xHallwaySpaces;
    int hallwayStartX;
    int hallwayEndX;
    int minx;
    int maxx;
    int miny;
    int maxy;
    List<Point> wallPoints;

    public Board(List<String> input) {
        minx = 0;
        miny = 0;
        maxx = input.get(0).length() - 1;
        maxy = input.size() - 1;

        hallwayStartX = minx + 1;
        hallwayEndX = maxx - 1;
        hallwayY = maxy - 1;
        backrankY = miny + 1;

        orderedChars = new ArrayList<>();
        xRooms = new ArrayList<>();
        xHallwaySpaces = new ArrayList<>();
        String backrankLine = input.get(maxy - backrankY);
        for(int x = hallwayStartX; x <= hallwayEndX; x++){
            if(x >= backrankLine.length()){
                xHallwaySpaces.add(x);
                continue;
            }
            char c = backrankLine.charAt(x);
            if(Character.isLetter(c) || c == SPRITE_EMPTY){
                orderedChars.add((char) ('A' + orderedChars.size()));
                xRooms.add(x);
            }
            else{
                xHallwaySpaces.add(x);
            }
        }

        wallPoints = new ArrayList<>();
        for (int y = 0; y <= maxy; y++) {
            String s = input.get(maxy - y);
            for (int x = 0; x <= s.length()-1; x++) {
                char c = s.charAt(x);
                if (c == SPRITE_WALL){
                    wallPoints.add(new Point(x, y));
                }
            }
        }
    }

    public String toString(Gamestate gamestate) {
        final StringBuilder sb = new StringBuilder();
        for(int y = maxy; y >= miny ; y--){
            for(int x = minx; x <= maxx; x++){
                Point p = new Point(x, y);
                sb.append(GetCharForPoint(p, gamestate));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public char GetCharForPoint(Point p, Gamestate gamestate) {
        if(p.y == hallwayY && p.x >= hallwayStartX && p.x <= hallwayEndX){
            if(xRooms.contains(p.x)){
                return SPRITE_INACCESSIBLE;
            }
            int index = gamestate.xToHallwayIndex(p.x);
            char c = gamestate.hallway[index];
            if(c == ' '){
                return SPRITE_EMPTY;
            }
            return c;
        }
        else if(p.y < hallwayY && p.y >= backrankY && xRooms.contains(p.x)){
            Room r = gamestate.xToRoom(p.x);
            Optional<Character> oc;
            int index = r.yToIndex(p.y);
            if(index >= r.elementsHeld){
                oc = Optional.empty();
            } else {
                oc = Optional.ofNullable(r.chars.get(index));
            }
            return oc.orElseGet(() -> SPRITE_EMPTY);
        }
        else if(wallPoints.contains(p)){
            return SPRITE_WALL;
        }
        else{
            return SPRITE_BLANK;
        }
    }

    public int GetMoveCost(char c) {
        return (int) Math.pow(10, c - 'A');
    }
}
