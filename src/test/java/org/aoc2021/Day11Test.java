package org.aoc2021;

import org.aoc2021.Day10.Day10;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day11Test {

    @Test
    void run() {
        Object[] answers  = Day11.Run("Day11_Example", true);
        assertEquals(answers[0].toString(), "1656");
        assertEquals(answers[1].toString(), "195");
    }
}