package org.aoc2021;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day1Test {

    @Test
    void run() {
        Object[] answers  = Day1.Run("Day1_Example", true);
        assertEquals(answers[0], 7);
        assertEquals(answers[1], 5);
    }
}