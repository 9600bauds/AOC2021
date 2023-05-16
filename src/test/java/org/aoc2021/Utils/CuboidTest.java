package org.aoc2021.Utils;

import javafx.geometry.Point3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CuboidTest {
    @Test
    void run(){
        Cuboid a1 = new Cuboid(new Point3D(0, 0, 0), 5, 5, 5);
        assertTrue(a1.Contains(a1));
        Cuboid a2 = new Cuboid(new Point3D(1, 1, 1), 3, 3, 3);
        assertTrue(a1.Contains(a2));
        assertEquals(a2, a1.Intersection(a2));
        Cuboid a3 = new Cuboid(new Point3D(1, 1, 1), 5, 5, 5);
        assertFalse(a1.Contains(a3));

        Cuboid a4 = new Cuboid(new Point3D(3, 3, 3), 5, 5, 5);
        assertTrue(a1.Intersects(a4));
        Cuboid a5 = new Cuboid(new Point3D(0, 6, 0), 5, 5, 5);
        assertFalse(a1.Intersects(a5));

        Cuboid supposedIntersection = new Cuboid(new Point3D(3, 3, 3), 2, 2, 2);
        Cuboid realIntersection = a1.Intersection(a4);
        assertEquals(supposedIntersection, realIntersection);
    }
}