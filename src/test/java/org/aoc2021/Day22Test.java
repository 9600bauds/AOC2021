package org.aoc2021;

import org.aoc2021.Day22.Day22;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day22Test {

    @Test
    void run() {
        Object[] answers = Day22.Run("Day22_Example", true);
        assertEquals(answers[0].toString(), "474140");
        assertEquals(answers[1].toString(), "2758514936282235");
    }
}