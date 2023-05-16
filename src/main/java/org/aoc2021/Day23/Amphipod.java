package org.aoc2021.Day23;

import java.util.Objects;

public class Amphipod implements Cloneable {
    char sprite;
    boolean isDone;
    public Amphipod(char c) {
        sprite = c;
        isDone = false;
    }

    public int GetIndex(){
        return sprite - 'A';
    }

    public int GetMoveCost() {
        return (int) Math.pow(10, sprite - 'A');
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append(sprite);
        sb.append(", ").append(isDone);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amphipod amphipod = (Amphipod) o;
        return sprite == amphipod.sprite && isDone == amphipod.isDone;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sprite, isDone);
    }
}
