package org.aoc2021.Day23;

import java.awt.*;
import java.util.*;

/**
 * Object that represents one "room" in the Amphipod Sorting game.
 */
public class Room implements Cloneable {
    public int x;
    int miny;
    int elementsHeld;
    int maxElements;
    char targetChar;
    Stack<Character> chars;

    public Room(int x, int miny, int maxElements, char targetChar) {
        this.x = x;
        this.miny = miny;
        this.targetChar = targetChar;
        this.maxElements = maxElements;
        chars = new Stack<>();
        elementsHeld = 0;
    }

    /**
     * Takes a y coordinate relative to the board and converts it into an index for this room's stack.
     * For example, y = 1 might correspond to index 0.
     */
    public int yToIndex(int y){
        return y - miny;
    }

    /**
     * Returns a 2D point relative to the board corresponding to the topmost character in this room.
     */
    public Point activePoint(){
        return new Point(x, miny + elementsHeld - 1);
    }

    /**
     * Returns a 2D point relative to the board corresponding to one tile above the topmost character in this room.
     */
    public Point emptyPoint(){
        return new Point(x, miny + elementsHeld);
    }

    /**
     * Will this room accept the given character? Returns false if the room is already full, the room contains improper characters that must be emptied first,
     * or if the character is not of the right type.
     */
    public boolean accepts(char c){
        boolean full = isFull();
        boolean charsAreValid = allCharsAreValid();
        boolean rightCharacter = (c == targetChar);
        return charsAreValid && !full && rightCharacter;
    }

    /**
     * Adds the given character to this room. Does not do any sort of validation.
     */
    public void forceAdd(char c){
        chars.add(c);
        elementsHeld++;
    }

    /**
     * Gets the frontmost character of this room and removes it.
     */
    public char popFrontChar(){
        char c = chars.pop();
        elementsHeld--;
        return c;
    }

    /**
     * Gets the frontmost character of this room without removing it.
     */
    public Character peekFrontChar(){
        return chars.peek();
    }

    public boolean isFull(){
        return elementsHeld == maxElements;
    }

    /**
     * Returns false if any characters inside this room don't belong inside. For example, if room 1 contains any characters besides A.
     */
    public boolean allCharsAreValid(){
        for(char c : chars){
            if(c != targetChar){
                return false;
            }
        }
        return true;
    }

    /**
     * If true, this room must be fully emptied in order to make it valid.
     */
    public boolean needsToBeEmptied(){
        return !allCharsAreValid();
    }

    /**
     * A room is "done" if all characters inside are valid, and it is full.
     */
    public boolean isDone(){
        return allCharsAreValid() && isFull();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return x == room.x && miny == room.miny && maxElements == room.maxElements && targetChar == room.targetChar && Objects.equals(chars, room.chars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, miny, maxElements, targetChar, chars);
    }

    @Override
    public Object clone() {
        try {
            Room cloned = (Room) super.clone();
            cloned.chars = new Stack<>();
            cloned.chars.addAll(this.chars);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "" + chars;
    }
}

