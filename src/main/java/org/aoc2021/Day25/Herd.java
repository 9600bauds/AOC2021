package org.aoc2021.Day25;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Herd {
    char sprite;
    Point vector;
    Set<Point> integrants;

    public Herd(char sprite, Point vector) {
        this.sprite = sprite;
        this.vector = vector;
        integrants = new HashSet<>();
    }
}
