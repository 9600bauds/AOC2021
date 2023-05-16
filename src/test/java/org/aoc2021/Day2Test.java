package org.aoc2021;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day2Test {

    @Test
    void run() {
        Object[] answers  = Day2.Run("Day2_Example", true);
        assertEquals(answers[0], 150);
        assertEquals(answers[1], 900);
    }
}