package org.aoc2021;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day3Test {

    @Test
    void run() {
        Object[] answers  = Day3.Run("Day3_Example", true);
        assertEquals(answers[0], 198);
        assertEquals(answers[1], 230);
    }
}