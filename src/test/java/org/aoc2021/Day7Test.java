package org.aoc2021;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day7Test {

    @Test
    void run() {
        Object[] answers  = Day7.Run("Day7_Example", true);
        assertEquals(answers[0].toString(), "37");
        assertEquals(answers[1].toString(), "168");
    }
}