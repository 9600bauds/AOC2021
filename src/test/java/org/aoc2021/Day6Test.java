package org.aoc2021;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day6Test {

    @Test
    void run() {
        Object[] answers  = Day6.Run("Day6_Example", true);
        assertEquals(answers[0].toString(), "5934");
        assertEquals(answers[1].toString(), "26984457539");
    }
}