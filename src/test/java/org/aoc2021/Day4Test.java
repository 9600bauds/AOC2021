package org.aoc2021;

import org.aoc2021.Day4.Day4;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day4Test {

    @Test
    void run() {
        Object[] answers  = Day4.Run("Day4_Example", true);
        assertEquals(answers[0], 4512);
        assertEquals(answers[1], 1924);
    }
}