package org.aoc2021.Day22;

import org.aoc2021.Utils.CollectionUtils;
import org.aoc2021.Utils.Utils;

import java.lang.invoke.MethodHandles;
import java.util.*;

//https://adventofcode.com/2021/day/22
public class Day22 {
    static List<Cuboid> cuboidStatic = new ArrayList<>(); //Only used for visualizations
    static final int SIZE_PT1 = 50;
    public static void main(String[] args) {
        Run("Day22_Example", false);
    }
    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByLinebreaks(filename);

        List<Instruction> instructions = ProcessInput(input);
        
        boolean[][][] cube1 = new boolean[SIZE_PT1*2+1][SIZE_PT1*2+1][SIZE_PT1*2+1]; //+1 because 0 exists, too
        for(Instruction instruction : instructions){
            if(instruction.minx > SIZE_PT1 || instruction.maxx < -SIZE_PT1 ||
            instruction.miny > SIZE_PT1 || instruction.maxy < -SIZE_PT1 ||
            instruction.minz > SIZE_PT1 || instruction.maxz < -SIZE_PT1){
                continue; //Ignore instructions fully outside the smaller area
            }
            for(int x = Math.max(instruction.minx, -SIZE_PT1); x <= Math.min(instruction.maxx, SIZE_PT1); x++){
                for(int y = Math.max(instruction.miny, -SIZE_PT1); y <= Math.min(instruction.maxy, SIZE_PT1); y++){
                    for(int z = Math.max(instruction.minz, -SIZE_PT1); z <= Math.min(instruction.maxz, SIZE_PT1); z++){
                        cube1[x + SIZE_PT1][y + SIZE_PT1][z + SIZE_PT1] = instruction.isOn;
                    }
                }
            }
        }
        answers[0] = CollectionUtils.countBooleanCube(cube1);

        // Part 2 is a doozy.
        // First we identify all "on" cuboids.
        // Then, for each of those (let's call each onQ), we find all other cuboids that intersect it, but only those that come later in the list.
        // (i.e. for the purposes of calculating the volume of Cuboid #2, we don't care about Cuboid #1, since #2 overwrites #1 anyways)
        // Then we use the vertices of all the cuboids that intersect onQ in order to create subdivisions of onQ,
        // small enough that no other cuboid could be half-intersecting it. In other words, each subdivision has to be intersecting or not, no inbetween.
        // Then we iterate through the subdivisions, and for each one that does not intersect with anything, we add its volume to the total.
        long pt2count = 0;
        List<Cuboid> allCuboids = new ArrayList<>();
        for(Instruction instruction : instructions){
            /*if(instruction.minx > SIZE_PT1 || instruction.maxx < -SIZE_PT1 ||
                    instruction.miny > SIZE_PT1 || instruction.maxy < -SIZE_PT1 ||
                    instruction.minz > SIZE_PT1 || instruction.maxz < -SIZE_PT1){
                continue; //Ignore instructions fully outside the smaller area
            }*/
            allCuboids.add(new Cuboid(instruction));
        }

        for(Cuboid onCu : allCuboids){
            if(!onCu.isOn) continue;

            List<org.aoc2021.Utils.Cuboid> intersections = org.aoc2021.Utils.Cuboid.GetIntersectionsOnTopOfCuboid(onCu, allCuboids);
            TreeSet<Double> xCoords = new TreeSet<>();
            TreeSet<Double> yCoords = new TreeSet<>();
            TreeSet<Double> zCoords = new TreeSet<>();
            AddCoordsToSets(onCu, xCoords, yCoords, zCoords);
            for(org.aoc2021.Utils.Cuboid intersection : intersections){
                AddCoordsToSets(intersection, xCoords, yCoords, zCoords);
            }
            if(!silent) System.out.printf("Simplified %s to a %sx%sx%s grid.%n", onCu, xCoords.size()-1, yCoords.size()-1, zCoords.size()-1);
            Set<org.aoc2021.Utils.Cuboid> subDivisions = GetAllSubdivisions(xCoords, yCoords, zCoords);
            for(org.aoc2021.Utils.Cuboid subDivision : subDivisions){
                if(!org.aoc2021.Utils.Cuboid.CuboidInCollectionContains(subDivision, intersections)){
                    pt2count += subDivision.GetVolume();
                }
            }
        }
        answers[1] = pt2count;

        if(!silent){
            System.out.println("Part 1 answer: " + answers[0]);
            System.out.println("Part 2 answer: " + answers[1]);

            cuboidStatic = allCuboids;
            CuboidVisualizer.main(new String[0]);
        }
        return answers;
    }

    /**
     * Given a list of X coordinates, Y coordinates and Z coordinates, it will return all possible subdivisions that can be created.
     * Basically, imagine you have a bunch of dots on a blank paper, and then you draw lines on top of those dots in order to turn
     * the paper into graphing paper. Like that, but in 3D, and it returns a set of the resulting cuboids.
     * Why do we want this? Well, if we take a bunch of cuboids, and make a list of all their vertices,
     * the subdivisions created with their vertices and this algorithm represents all the possible subdivisions where they COULD
     * possibly intersect.
     */
    private static Set<org.aoc2021.Utils.Cuboid> GetAllSubdivisions(TreeSet<Double> xCoords, TreeSet<Double> yCoords, TreeSet<Double> zCoords) {
        Set<org.aoc2021.Utils.Cuboid> out = new HashSet<>();
        for (Double minx : xCoords) {
            if(Objects.equals(minx, xCoords.last())) break;
            Double maxx = xCoords.higher(minx);
            assert maxx != null;
            for (Double miny : yCoords) {
                if(Objects.equals(miny, yCoords.last())) break;
                Double maxy = yCoords.higher(miny);
                assert maxy != null;
                for (Double minz : zCoords) {
                    if(Objects.equals(minz, zCoords.last())) break;
                    Double maxz = zCoords.higher(minz);
                    assert maxz != null;
                    org.aoc2021.Utils.Cuboid currentSubdivision = new org.aoc2021.Utils.Cuboid(minx, maxx, miny, maxy, minz, maxz);
                    out.add(currentSubdivision);
                }
            }
        }
        return out;
    }

    private static void AddCoordsToSets(org.aoc2021.Utils.Cuboid q, TreeSet<Double> xCoords, TreeSet<Double> yCoords, TreeSet<Double> zCoords) {
        xCoords.add(q.MinX());
        xCoords.add(q.MaxX());
        yCoords.add(q.MinY());
        yCoords.add(q.MaxY());
        zCoords.add(q.MinZ());
        zCoords.add(q.MaxZ());
    }

    private static List<Instruction> ProcessInput(List<String> input) {
        List<Instruction> instructions = new ArrayList<>();
        for(String line : input){
            boolean onoroff = Objects.equals(Utils.GetFirstRegexMatch(line, "\\w+"), "on");
            int[] digits = CollectionUtils.StringArr2IntArr(Utils.GetAllRegexMatches(line,"[-\\d]+"));
            int minx = Math.min(digits[0], digits[1]);
            int maxx = Math.max(digits[0], digits[1]);
            int miny = Math.min(digits[2], digits[3]);
            int maxy = Math.max(digits[2], digits[3]);
            int minz = Math.min(digits[4], digits[5]);
            int maxz = Math.max(digits[4], digits[5]);
            Instruction instruction = new Instruction(minx, maxx, miny, maxy, minz, maxz, onoroff);
            instructions.add(instruction);
        }
        return instructions;
    }


}
