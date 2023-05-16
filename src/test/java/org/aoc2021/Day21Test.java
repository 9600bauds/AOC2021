package org.aoc2021;

import org.aoc2021.Day21.Day21;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day21Test {
    @Test
    void run() {
        Object[] answers = Day21.Run("Day21_Example", true);
        assertEquals(answers[0].toString(), "739785");
        assertEquals(answers[1].toString(), "444356092776315");
    }
}