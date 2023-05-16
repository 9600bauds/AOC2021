package org.aoc2021.Day19;

import javafx.geometry.Point3D;
import org.aoc2021.Utils.Geometry;

import java.util.*;

public class Scanner {
    int id;
    List<Point3D> detectedBeacons;
    Point3D loc;

    int xrotations;
    int yrotations;
    int zrotations;
    List<Point3D> beaconsRelativeToOrigin;

    /**
     * Attempts to find beacons that both scanners can see.
     * @param other Second scanner to check
     * @param limit Maximum number of beacons to find
     * @param distMatchesNeeded How many distance matches are required for a point to be determined as equal
     * @return A list of points that both scanners can see. Each point is listed twice, first relative to scanner A, then relative to scanner B
     * e.g. {[point #1 relative to scanner A], [point#1 relative to scanner B], [point#2 relative to scanner A], [point#2 relative to scanner B]...}
     */
    public List<Point3D> FindCommonPoints(Scanner other, int limit, int distMatchesNeeded){
        List<Point3D> out = new ArrayList<>();
        for(int i = 0; i < detectedBeacons.size(); i++) {
            Point3D point1 = detectedBeacons.get(i);
            List<Double> pointDists = new ArrayList<>();
            for(int j = 0; j < detectedBeacons.size(); j++){
                if(i == j) continue;
                Point3D point2 = detectedBeacons.get(j);
                double dist = point1.distance(point2);
                pointDists.add(dist);
            }
            Point3D matchingPoint = other.GetMatchingPoint(pointDists, distMatchesNeeded);
            if(matchingPoint != null){
                out.add(point1);
                out.add(matchingPoint);
                if(out.size() >= limit * 2){
                    break;
                }
            }
        }
        return out;
    }

    /**
     * The way we find beacons in other scanners' scans is by using the distance between beacons as a unique fingerprint.
     * No matter how offset or skewed a scan is, the distances between beacons always stays the same, and the distance
     * between any two beacons is pretty specific and unique.
     * For example, if we find a beacon with a neighbor 5.124m away and another neighbor at 17.632m away,
     * it's fairly safe to assume any other beacons reported by other scanners that also have a neighbor 5.124m away and
     * another neighbor 17.632m away... are in fact, the same beacon, just seen by 2 different scanners.
     * @param fingerprint A list of distances representing how far away pointA's neighbors are.
     * @param matchesNeeded The minimum number of exact matches required to conclude that two points are the same beacon
     * @return The position of that beacon as reported by this scanner, rotation and offset and all
     */
    public Point3D GetMatchingPoint(List<Double> fingerprint, int matchesNeeded){
        for(int i = 0; i < detectedBeacons.size(); i++) {
            int matches = 0;
            Point3D point1 = detectedBeacons.get(i);
            for(int j = 0; j < detectedBeacons.size(); j++){
                if(i == j) continue;
                Point3D point2 = detectedBeacons.get(j);
                double dist = point1.distance(point2);
                if(fingerprint.contains(dist)){
                    matches++;
                    if(matches >= matchesNeeded){
                        return point1;
                    }
                }
            }
        }
        return null;
    }

    public static Point3D ApplyRotationAndOffset(int x, int y, int z, Point3D offset, Point3D p){
        Point3D newPoint = new Point3D(p.getX(), p.getY(), p.getZ());
        newPoint = Geometry.RotateX(newPoint, x);
        newPoint = Geometry.RotateY(newPoint, y);
        newPoint = Geometry.RotateZ(newPoint, z);
        newPoint = newPoint.add(offset);
        return newPoint;
    }

    public boolean VerifyRotationAndOffset(int x, int y, int z, Point3D offset, List<Point3D> toCheck){
        int tally = 0;
        for(Point3D p : toCheck){
            Point3D processed = ApplyRotationAndOffset(x, y, z, offset, p);
            if(detectedBeacons.contains(processed)){
                tally++;
                if(tally > 2){
                    return true;
                }
            }
        }
        return false;
    }

    public void LocationFound(int x, int y, int z, Point3D offset){
        xrotations = x;
        yrotations = y;
        zrotations = z;
        loc = offset;
        beaconsRelativeToOrigin = new ArrayList<>();
        for(Point3D p : detectedBeacons){
            Point3D newPoint = ApplyRotationAndOffset(x, y, z, offset, p);
            beaconsRelativeToOrigin.add(newPoint);
        }
    }

    public Scanner(int id, List<Point3D> detectedBeacons) {
        this.id = id;
        this.detectedBeacons = detectedBeacons;
    }

    @Override
    public String toString() {
        return "Scanner{" + "id=" + id +
                '}';
    }
}
