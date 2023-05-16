package org.aoc2021;

import org.aoc2021.Day24.InstructionSet;
import org.aoc2021.Day24.Processor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class Day24Test {
    @Test
    void run() {

        InstructionSet instructions1 = new InstructionSet(Arrays.asList("inp x", "mul x -1"));
        Processor p1 = new Processor(instructions1, 1L);
        p1.run();
        assertEquals(p1.vars[1], -1L);

        InstructionSet instructions2 = new InstructionSet(Arrays.asList("inp z", "inp x", "mul z 3", "eql z x"));
        Processor p2 = new Processor(instructions2, 39L);
        p2.run();
        assertEquals(p2.vars[3], 1);

        InstructionSet instructions3 = new InstructionSet(Arrays.asList("inp w", "add z w", "mod z 2", "div w 2", "add y w", "mod y 2", "div w 2", "add x w", "mod x 2", "div w 2", "mod w 2"));
        Processor p3 = new Processor(instructions3, 7L);
        p3.run();
        assertEquals(p3.vars[0], 0);
        assertEquals(p3.vars[1], 1);
        assertEquals(p3.vars[2], 1);
        assertEquals(p3.vars[3], 1);
    }
}