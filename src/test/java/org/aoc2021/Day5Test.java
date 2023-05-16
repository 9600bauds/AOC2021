package org.aoc2021;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day5Test {

    @Test
    void run() {
        Object[] answers  = Day5.Run("Day5_Example", true);
        assertEquals(answers[0], 5);
        assertEquals(answers[1], 12);
    }
}