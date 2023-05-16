package org.aoc2021.Day23;


import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Object that represents one possible state for the Amphipod Sorting game.
 */
public class Gamestate implements Cloneable {
    public List<Room> rooms;
    char[] hallway;
    Board board;
    Gamestate parent; //Intentionally not included in equals()

    /**
     * Creates the gamestate from a list of strings, representing the input as given from the website
     */
    public Gamestate(List<String> input, Board board){
        this.board = board;
        hallway = new char[board.hallwayEndX - board.hallwayStartX + 1];
        //Populating the hallway
        for(int x = board.hallwayStartX; x <= board.hallwayEndX; x++){
            char c = input.get(1).charAt(x);
            int index = xToHallwayIndex(x);
            if(!Character.isLetter(c)){
                hallway[index] = ' ';
            }
            else{
                hallway[index] = c;
            }
        }

        //Creating the rooms
        rooms = new ArrayList<>(board.orderedChars.size());
        int roomSize = board.hallwayY - board.backrankY;
        int currIndex = 0;
        for(int x : board.xRooms){
            Room currRoom = new Room(x, board.backrankY, roomSize, board.orderedChars.get(currIndex));
            rooms.add(currRoom);
            for(int y = board.backrankY; y < board.hallwayY; y++){
                String s = input.get(input.size() - 1 - y);
                char c = s.charAt(x);
                if (!Character.isLetter(c)) continue;
                currRoom.forceAdd(c);
            }
            currIndex++;
        }
    }

    /**
     * Takes an X coordinate relative to the board and transforms it into its corresponding index in the hallway array.
     * For example, X = 2 corresponds to index 1.
     */
    public int xToHallwayIndex(int x){
        return x - board.hallwayStartX;
    }

    /**
     * Takes an X coordinate and returns the room that corresponds to that column.
     * Note that this will throw an exception if no room exists.
     */
    public Room xToRoom(int x){
        int index = board.xRooms.indexOf(x);
        if(index < 0){
            throw new IllegalArgumentException("X coord did not correspond to a room!");
        }
        return rooms.get(index);
    }

    /**
     * Takes a character and returns the "home" for that character, that is, which room it wants to go to.
     * For example, an A wants to go to the leftmost room.
     * Will throw an exception if no such room exists.
     */
    public Room charToRoom(char c){
        for(Room r : rooms){
            if(r.targetChar == c){
                return r;
            }
        }
        throw new IllegalArgumentException("Could not find a room corresponding to " + c + "!");
    }

    /**
     * Returns true if every room is full and containing only its correct character.
     */
    public boolean IsDone(){
        for(Room r :rooms){
            if(!r.isDone()){
                return false;
            }
        }
        return true;
    }

    /**
     * Starting from the given X coordinate, returns all the points along the hallway that can be reached from there.
     * That is, it will go left until it hits a wall or another character, and then do the same for the right.
     * @param starterX The X to start the search at
     * @return A set of 2D points, representing the spaces that an amphipod could reach starting from there.
     */
    public Set<Point> GetPossibleHallwayDestinations(int starterX){
        Set<Point> out = new HashSet<>();

        for(int x = starterX - 1; x >= board.hallwayStartX; x--){
            int index = xToHallwayIndex(x);
            if(Character.isLetter(hallway[index])){
                break;
            }
            if(board.xHallwaySpaces.contains(x)){
                out.add(new Point(x, board.hallwayY));
            }
        }
        for(int x = starterX + 1; x <= board.hallwayEndX; x++){
            int index = xToHallwayIndex(x);
            if(Character.isLetter(hallway[index])){
                break;
            }
            if(board.xHallwaySpaces.contains(x)){
                out.add(new Point(x, board.hallwayY));
            }
        }
        return out;
    }

    /**
     * Moves an amphipod from point A to point B.
     * It will remove said amphipod from a room if need be, and put it in another room if necessary.
     * Note that this assumes the movement is possible and valid. It performs no verifications on its own.
     * @return The energy cost of this move.
     */
    public int Move(Point a, Point b){
        char c;
        if(a.y >= board.hallwayY){
            int index = xToHallwayIndex(a.x);
            c = hallway[index];
            hallway[index] = ' ';
        }
        else{
            Room r = xToRoom(a.x);
            c = r.popFrontChar();
        }

        if(b.y >= board.hallwayY){
            int index = xToHallwayIndex(b.x);
            hallway[index] = c;
        }
        else{
            Room r = xToRoom(b.x);
            r.forceAdd(c);
        }
        int dist = GetDist(a, b);
        return dist * board.GetMoveCost(c);
    }

    /**
     * Checks every single amphipod on the board and sends them home, if possible.
     * It will call itself recursively after each amphipod moved, since the act of sending them home could unblock the way for more amphipods.
     * @return The total energy cost of all the amphipods going home, or 0 if none were moved.
     */
    public int MovePlayersHome(){
        int out = 0;
        for(int x = board.hallwayStartX; x <= board.hallwayEndX; x++){
            int index = xToHallwayIndex(x);
            char c = hallway[index];
            if(!Character.isLetter(c)) continue;
            Room r = charToRoom(c);
            if(!r.accepts(c)) continue;
            int newx = (int) (x + (1 * Math.signum(r.x - x)));
            if(HallwayIsBlocked(newx, r.x)) continue;

            Point a = new Point(x, board.hallwayY);
            Point b = r.emptyPoint();
            out += Move(a, b);
            out += MovePlayersHome(); //Do this recursively, since more players may be able to go home now
            break;
        }
        for(Room r : rooms){
            if(r.allCharsAreValid()) continue;
            char c = r.peekFrontChar();
            Room r2 = charToRoom(c);
            if(!r2.accepts(c)) continue;
            if(HallwayIsBlocked(r.x, r2.x)) continue;

            Point a = r.activePoint();
            Point b = r2.emptyPoint();
            out += Move(a, b);
            out += MovePlayersHome(); //Do this recursively, since more players may be able to go home now
            break;
        }
        return out;
    }

    /**
     * Are there any obstacles on the hallway between X1 and X2?
     */
    public boolean HallwayIsBlocked(int x1, int x2){
        int minx = Math.min(x1, x2);
        int maxx = Math.max(x1, x2);
        for(int x = minx; x <= maxx; x++){
            int index = xToHallwayIndex(x);
            if(Character.isLetter(hallway[index])){
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the distance between two points, corresponding to 1 distance per tile traveled.
     * Assumes that the amphipod will move up to the hallway first if needed, then move to the desired X coordinate,
     * and then go down again if needed.
     */
    public int GetDist (Point a, Point b){
        int out = 0;
        if(a.y < board.hallwayY){
            out += board.hallwayY - a.y;
        }
        out += Math.abs(b.x - a.x);
        if(b.y < board.hallwayY){
            out += board.hallwayY - b.y;
        }
        return out;
    }

    /**
     * Calculates every move possible from this gamestate, creates a clone for each, and adds them to the given queue and map.
     */
    public void SpawnChildren(Map<Gamestate, Integer> scores, PriorityQueue<Map.Entry<Gamestate, Integer>> queue) {
        int thisScore = scores.get(this);
        for(Room r : rooms){
            if(!r.needsToBeEmptied()) continue;
            Point start = r.activePoint();
            Set<Point> possibleDestinations = GetPossibleHallwayDestinations(r.x);
            for(Point end : possibleDestinations){
                Gamestate child = (Gamestate) this.clone();
                child.parent = this;
                int newScore = thisScore;
                newScore += child.Move(start, end);
                newScore += child.MovePlayersHome();
                if(!scores.containsKey(child) || scores.get(child) > newScore){
                    scores.put(child, newScore);
                    queue.add(Map.entry(child, newScore));
                }
            }
        }
    }

    @Override
    public Object clone() {
        try {
            Gamestate cloned = (Gamestate) super.clone();
            cloned.board = this.board;
            cloned.rooms = new ArrayList<>(this.rooms.size());
            for (Room room : this.rooms) {
                cloned.rooms.add((Room) room.clone());
            }
            cloned.hallway = this.hallway.clone();
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return board.toString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gamestate gamestate = (Gamestate) o;
        return Objects.equals(rooms, gamestate.rooms) && Arrays.equals(hallway, gamestate.hallway) && Objects.equals(board, gamestate.board);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rooms, board);
        result = 31 * result + Arrays.hashCode(hallway);
        return result;
    }
}
