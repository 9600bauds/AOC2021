package org.aoc2021;

import org.aoc2021.Day9.Day9;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day9Test {

    @Test
    void run() {
        Object[] answers  = Day9.Run("Day9_Example", true);
        assertEquals(answers[0].toString(), "15");
        assertEquals(answers[1].toString(), "1134");
    }
}