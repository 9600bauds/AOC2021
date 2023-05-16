package org.aoc2021;

import org.aoc2021.Day17.Day17;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class Day17Test {

    @Test
    void run() {
        Object[] answers = Day17.Run("Day17_Example", true);
        assertEquals(answers[0].toString(), "45");
        assertEquals(answers[1].toString(), "112");

        Long successes = Day17.CountSuccesses("Day17_Example", "Day17_Part2Test");
        assertEquals(successes.toString(), "112"); //one false example is included, if it returns 113 that's no good
    }
}