package org.aoc2021;

import org.aoc2021.Day15.Day15;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day15Test {

    @Test
    void run() {
        Object[] answers  = Day15.Run("Day15_Example", true);
        assertEquals(answers[0].toString(), "40");
        assertEquals(answers[1].toString(), "315");
    }
}