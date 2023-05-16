package org.aoc2021;

import org.aoc2021.Utils.Utils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day14Test {

    @Test
    void run() {
        Object[] answers  = Day14.Run("Day14_Example", true);
        assertEquals(answers[0].toString(), "1588");
        assertEquals(answers[1].toString(), "2188189693529");
    }
}