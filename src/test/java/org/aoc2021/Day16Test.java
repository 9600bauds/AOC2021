package org.aoc2021;

import org.aoc2021.Day16.Day16;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day16Test {
    @Test
    void run() {
        Object[] answers  = Day16.Run("Day16_Example", true);
        assertEquals(answers[0].toString(), "82");
        assertEquals(answers[1].toString(), "161");

        Object[] answers_2  = Day16.Run("Day16_Example2", true);
        assertEquals(answers_2[0].toString(), "116");
        assertEquals(answers_2[1].toString(), "75");
    }
}