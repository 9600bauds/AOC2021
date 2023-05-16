package org.aoc2021;

import org.aoc2021.Day23.Day23;
import org.aoc2021.Day23.Board;
import org.aoc2021.Day23.Gamestate;
import org.aoc2021.Utils.Utils;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class Day23Test {

    @Test
    void testClasses() {
        List<String> input = Utils.SplitInputByLinebreaks("Day23_Testing");

        Board starterBoard = new Board(input);
        Gamestate testGamestate = new Gamestate(input, starterBoard);

        assertEquals(3, testGamestate.Move(new Point(4,3), new Point(3, 1)));
        assertEquals(3, testGamestate.Move(new Point(3,1), new Point(4, 3)));
        assertEquals(3000,testGamestate.Move(new Point(8,3), new Point(9, 1)));
        assertEquals(3000,testGamestate.Move(new Point(9,1), new Point(8, 3)));
        assertEquals(3003, testGamestate.MovePlayersHome());

        assertFalse(testGamestate.rooms.get(0).needsToBeEmptied());
        assertTrue(testGamestate.rooms.get(1).needsToBeEmptied());
        assertTrue(testGamestate.rooms.get(2).needsToBeEmptied());
        assertFalse(testGamestate.rooms.get(3).needsToBeEmptied());

        Set<Point> points = testGamestate.GetPossibleHallwayDestinations(5);
        assertEquals(points.size(), 7);
        assertEquals(300, testGamestate.Move(new Point(5, 1), new Point(4, 3)));
        Set<Point> points2 = testGamestate.GetPossibleHallwayDestinations(7);
        assertEquals(points2.size(), 4);

        assertEquals(560, testGamestate.MovePlayersHome());

        Gamestate clone = (Gamestate) testGamestate.clone();
        assertEquals(testGamestate, clone);

        assertEquals(testGamestate.toString(),
                "#############\n" +
                "#..░.░.░.░..#\n" +
                "###.#.#.#.###\n" +
                "  #A#B#C#D#  \n" +
                "  #########  \n");

    }
    @Test
    void run() {
        Object[] answers = Day23.Run("Day23_Example", true);
        assertEquals(answers[0].toString(), "12521");
        assertEquals(answers[1].toString(), "44169");
    }
}