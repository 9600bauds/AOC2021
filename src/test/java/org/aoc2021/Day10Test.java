package org.aoc2021;

import org.aoc2021.Day10.Day10;
import org.aoc2021.Day9.Day9;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day10Test {

    @Test
    void run() {
        Object[] answers  = Day10.Run("Day10_Example", true);
        assertEquals(answers[0].toString(), "26397");
        assertEquals(answers[1].toString(), "288957");
    }
}