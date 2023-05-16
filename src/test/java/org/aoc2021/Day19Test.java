package org.aoc2021;

import org.aoc2021.Day19.Day19;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day19Test {

    @Test
    void run() {
        Object[] answers = Day19.Run("Day19_Example", true);
        assertEquals(answers[0].toString(), "79");
        assertEquals(answers[1].toString(), "3621");
    }
}