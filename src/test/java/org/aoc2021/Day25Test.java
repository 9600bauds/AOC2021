package org.aoc2021;

import org.aoc2021.Day25.Day25;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day25Test {

    @Test
    void run() {
        Object[] answers = Day25.Run("Day25_Example", true);
        assertEquals(answers[0].toString(), "58");
    }
}