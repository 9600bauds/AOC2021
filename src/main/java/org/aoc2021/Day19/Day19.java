package org.aoc2021.Day19;

import javafx.geometry.Point3D;
import org.aoc2021.Utils.CollectionUtils;
import org.aoc2021.Utils.Geometry;
import org.aoc2021.Utils.Utils;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.List;

//https://adventofcode.com/2021/day/19
public class Day19 {
    public static Object[] Run(String filename, boolean silent) {
        Object[] answers = new Object[2];
        if (filename == null) filename = MethodHandles.lookup().lookupClass().getSimpleName();
        List<String> input = Utils.SplitInputByEmptyLines(filename);

        List<Scanner> scanners = ProcessInput(input);
        Scanner centerOfUniverse = scanners.get(0);
        centerOfUniverse.loc = new Point3D(0, 0, 0);

        List<Scanner> queue = new ArrayList<>(scanners);
        queue.remove(centerOfUniverse);
        while(queue.size() > 0){
            Scanner currScanner = queue.get(0);
            List<Point3D> commonPoints = centerOfUniverse.FindCommonPoints(currScanner, 1, 3);
            if(commonPoints.size() < 2){
                Collections.rotate(queue, -1); //Send back to end of queue
                continue;
            }
            Point3D pointA = commonPoints.get(0);
            Point3D pointB = commonPoints.get(1);

            if (FindScannerFromPoints(centerOfUniverse, currScanner, pointA, pointB)){
                //We have a match!!
                if(!silent) System.out.println("Found the location for scanner " + currScanner.id + ": " + currScanner.loc);
                for(Point3D p : currScanner.beaconsRelativeToOrigin){
                    if(!centerOfUniverse.detectedBeacons.contains(p)){
                        //System.out.println("New point thanks to scanner " + currScanner.id + ": " + p);
                        centerOfUniverse.detectedBeacons.add(p);
                    }
                }
                queue.remove(currScanner);
            }
            else{
                Collections.rotate(queue, -1); //Send back to end of queue
            }
        }

        int largestDist = 0;
        String pt2fluff = "";
        for(int i = 0; i < scanners.size(); i++){
            Scanner s1 = scanners.get(i);
            for(int j = i; j < scanners.size(); j++){
                Scanner s2 = scanners.get(j);
                double distance = Geometry.ManhattanDistance3D(s1.loc, s2.loc);
                if(distance > largestDist){
                    largestDist = (int) distance;
                    pt2fluff = "Scanner " + s1.id + " (" + s1.loc.getX() + "," + s1.loc.getY() + "," + s1.loc.getZ() + ")" +
                            " to Scanner " + s2.id + " (" + s2.loc.getX() + "," + s2.loc.getY() + "," + s2.loc.getZ() + ")";
                }
            }
        }

        answers[0] = centerOfUniverse.detectedBeacons.size();
        answers[1] = largestDist;
        if(!silent){
            System.out.println("Total number of beacons: " + answers[0]);
            System.out.println("Largest distance between any two beacons: " + answers[1]);
            System.out.println(pt2fluff);
        }

        return answers;
    }

    /**
     * Attempts to find the location and orientation of Scanner 2, using the scanner at 0,0,0 and one point shared in common as reference.
     * @param centerOfUniverse The scanner at 0,0,0
     * @param toFind The scanner whose location and orientation we're trying to find
     * @param pointA The position of a beacon, relative to centerOfUniverse
     * @param pointB The position of the same beacon, relative to Scanner 2
     * @return True if we could locate the scanner, False if not
     */
    private static boolean FindScannerFromPoints(Scanner centerOfUniverse, Scanner toFind, Point3D pointA, Point3D pointB) {
        //We don't know what orientation the second scanner has. So we'll just try all of them.
        for(int x = 0; x < 360; x = x + 90){
            for(int y = 0; y < 360; y = y + 90){
                for(int z = 0; z < 360; z = z + 90){
                    Point3D rotateMe = new Point3D(pointB.getX(), pointB.getY(), pointB.getZ());
                    rotateMe = Geometry.RotateX(rotateMe, x);
                    rotateMe = Geometry.RotateY(rotateMe, y);
                    rotateMe = Geometry.RotateZ(rotateMe, z);

                    double deltaX = pointA.getX() - rotateMe.getX();
                    double deltaY = pointA.getY() - rotateMe.getY();
                    double deltaZ = pointA.getZ() - rotateMe.getZ();
                    Point3D offset = new Point3D(deltaX, deltaY, deltaZ);

                    if(centerOfUniverse.VerifyRotationAndOffset(x, y, z, offset, toFind.detectedBeacons)){
                        toFind.LocationFound(x, y, z, offset);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Parses the input into a list of scanners and their list of beacons, relative to themselves.
     * Note that their locations or orientations are not yet known.
     */
    private static List<Scanner> ProcessInput(List<String> input) {
        List<Scanner> scanners = new ArrayList<>();
        for(String block : input) {
            List<Point3D> points = new ArrayList<>();

            String[] lines = block.split("\\r\\n");
            int id = Integer.parseInt(lines[0].replaceAll("\\D+", "")); //Get the number from the header
            for (int i = 1; i < lines.length; i++) { //Starts at 1 to skip the header
                String[] coordsSplit = lines[i].split(",");
                int[] coordsInt = CollectionUtils.StringArr2IntArr(coordsSplit);
                Point3D p = new Point3D(coordsInt[0], coordsInt[1], coordsInt[2]);
                points.add(p);
            }
            Scanner s = new Scanner(id, points);
            scanners.add(s);
        }
        return scanners;
    }

}
