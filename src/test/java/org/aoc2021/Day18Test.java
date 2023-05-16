package org.aoc2021;

import org.aoc2021.Day18.Day18;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day18Test {

    @Test
    void run() {
        Object[] answers = Day18.Run("Day18_Example", true);
        assertEquals(answers[0].toString(), "4140");
        assertEquals(answers[1].toString(), "3993");
    }
}