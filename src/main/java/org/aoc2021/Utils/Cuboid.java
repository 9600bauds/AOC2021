package org.aoc2021.Utils;

import javafx.geometry.Point3D;

import java.util.*;

public class Cuboid {
    public Point3D origin;
    public double deltaX;
    public double deltaY;
    public double deltaZ;

    public Cuboid(Point3D origin, double deltaX, double deltaY, double deltaZ) {
        this.origin = origin;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
    }

    public Cuboid(double minx, double maxx, double miny, double maxy, double minz, double maxz) {
        origin = new Point3D(minx, miny, minz);
        deltaX = maxx - minx;
        deltaY = maxy - miny;
        deltaZ = maxz - minz;
    }

    public double MinX(){
        return origin.getX();
    }
    public double MinY(){
        return origin.getY();
    }
    public double MinZ(){
        return origin.getZ();
    }
    public double MaxX(){
        return origin.getX() + deltaX;
    }
    public double MaxY(){
        return origin.getY() + deltaY;
    }
    public double MaxZ(){
        return origin.getZ() + deltaZ;
    }
    public double GetVolume() {
        return deltaX * deltaY * deltaZ;
    }

    public boolean Contains(Cuboid other){
        if(other.origin.getX() < origin.getX() || other.MaxX() > MaxX()){
            return false;
        }
        if(other.origin.getY() < origin.getY() || other.MaxY() > MaxY()) {
            return false;
        }
        if(other.origin.getZ() < origin.getZ() || other.MaxZ() > MaxZ()) {
            return false;
        }
        return true;
    }
    public boolean Intersects(Cuboid other){
        if(other.MaxX() < origin.getX() || MaxX() < other.origin.getX()){
            return false;
        }
        if(other.MaxY() < origin.getY() || MaxY() < other.origin.getY()){
            return false;
        }
        if(other.MaxZ() < origin.getZ() || MaxZ() < other.origin.getZ()){
            return false;
        }
        return true;
    }
    public Cuboid Intersection(Cuboid other){
        double minX = Math.max(origin.getX(), other.origin.getX());
        double maxX = Math.min(MaxX(), other.MaxX());
        double minY = Math.max(origin.getY(), other.origin.getY());
        double maxY = Math.min(MaxY(), other.MaxY());
        double minZ = Math.max(origin.getZ(), other.origin.getZ());
        double maxZ = Math.min(MaxZ(), other.MaxZ());

        Point3D origin = new Point3D(minX, minY, minZ);
        double deltaX = maxX - minX;
        double deltaY = maxY - minY;
        double deltaZ = maxZ - minZ;
        return new Cuboid(origin, deltaX, deltaY, deltaZ);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cuboid cuboid = (Cuboid) o;
        return Double.compare(cuboid.deltaX, deltaX) == 0 && Double.compare(cuboid.deltaY, deltaY) == 0 && Double.compare(cuboid.deltaZ, deltaZ) == 0 && Objects.equals(origin, cuboid.origin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, deltaX, deltaY, deltaZ);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Q:");
        sb.append("{").append(origin).append("} / ");
        sb.append(deltaX).append("x").append(deltaY).append("x").append(deltaZ);
        return sb.toString();
    }

    public static boolean CuboidInCollectionContains(Cuboid q, Collection<Cuboid> collection){
        for(Cuboid other : collection){
            if(other.Contains(q)){
                return true;
            }
        }
        return false;
    }

    /**
     * Iterates the given list from end to start. For each cuboid that intersects q, the intersection between this cuboid and q is added to the output.
     * Stops prematurely if q is in the given list, that is, this will only use the cuboids in the list that come after q.
     */
    public static List<Cuboid> GetIntersectionsOnTopOfCuboid(Cuboid q, List<? extends Cuboid> allCuboids){
        List<Cuboid> out = new ArrayList<>();
        for(int i = allCuboids.size()-1; i >= 0; i--){
            Cuboid other = allCuboids.get(i);
            if(other == q) break;
            if(!other.Intersects(q)) continue;
            Cuboid intersection = q.Intersection(other);
            out.add(intersection);
        }
        return out;
    }
}

