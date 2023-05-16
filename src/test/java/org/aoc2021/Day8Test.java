package org.aoc2021;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day8Test {

    @Test
    void run() {
        Object[] answers  = Day8.Run("Day8_Example", true);
        assertEquals(answers[0].toString(), "26");
        assertEquals(answers[1].toString(), "61229");
    }
}