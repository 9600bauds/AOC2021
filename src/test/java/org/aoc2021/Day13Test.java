package org.aoc2021;

import org.aoc2021.Utils.Utils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day13Test {

    @Test
    void run() {
        Object[] answers  = Day13.Run("Day13_Example", true);
        assertEquals(answers[0].toString(), "17");
        assertEquals(Utils.NormalizeLineSeparators(answers[1].toString()),
                "#####\n" +
                "#···#\n" +
                "#···#\n" +
                "#···#\n" +
                "#####\n");
    }
}