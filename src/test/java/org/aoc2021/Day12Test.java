package org.aoc2021;

import org.aoc2021.Day12.Day12;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day12Test {
    @Test
    void run() {
        Object[] answers  = Day12.Run("Day12_Example");
        assertEquals(answers[0].toString(), "10");
        assertEquals(answers[1].toString(), "36");
    }

}