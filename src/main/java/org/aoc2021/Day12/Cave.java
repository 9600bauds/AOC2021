package org.aoc2021.Day12;

import org.aoc2021.Utils.Utils;

import java.util.HashSet;
import java.util.Set;

public class Cave {
    String name;
    boolean isBigCave;
    boolean isStartCave = false;
    boolean isEndCave = false;
    Set<Cave> neighbors;

    public Cave(String name) {
        this.name = name;
        if(Utils.isFullyUppercase(name)){
            isBigCave = true;
        }
        else if(Utils.isFullyLowercase(name)){
            isBigCave = false;
        }
        else{
            throw new RuntimeException("Mixed case on given cave name! " + name);
        }
        neighbors = new HashSet<>();
    }

    @Override
    public String toString() {
        return name;
    }
}
