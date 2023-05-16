package org.aoc2021.Day15;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class SearchState {
    Point pos;
    Integer score;
    List<Point> path;

    @Override
    public String toString() {
        return pos +
                ", score=" + score;
    }

    public SearchState(Point pos) {
        this.pos = pos;
        score = 0;
        path = new ArrayList<>();
        path.add(pos);
    }

    public SearchState(Point pos, SearchState parent, int[][] board) {
        this.pos = pos;
        score = parent.score + board[pos.x][pos.y];
        path = new ArrayList<>(parent.path);
        path.add(pos);
    }

}
