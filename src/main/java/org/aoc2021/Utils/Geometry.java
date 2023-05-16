package org.aoc2021.Utils;

import javafx.geometry.Point3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Geometry {
    private static double round(double value, int decimalPlaces){
        double factor = Math.pow(10, decimalPlaces);
        return Math.round(value * factor) / factor;
    }
    public static java.util.List<Point> Orange(Point p, int range, int minx, int maxx, int miny, int maxy){
        List<Point> out = new ArrayList<>();

        int startx = Math.max(minx, p.x - range);
        int endx = Math.min(maxx, p.x + range);
        int starty = Math.max(miny, p.y - range);
        int endy = Math.min(maxy, p.y + range);

        for(int y = starty; y <= endy; y++){
            for(int x = startx; x <= endx; x++){
                out.add(new Point(x, y));
            }
        }
        out.remove(p); //The O in ORANGE
        return out;
    }

    public static List<Point> Orange(Point p, int range, int[][] arr){
        int minx = 0;
        int maxx = arr[0].length-1;
        int miny = 0;
        int maxy = arr.length-1;
        return Orange(p, range, minx, maxx, miny, maxy);
    }

    public static List<Point> GetOrthogonalNeighbors(Point p, int minx, int maxx, int miny, int maxy){
        List<Point> candidates = new ArrayList<>();

        if(p.x > minx){
            candidates.add(new Point(p.x-1, p.y));
        }
        if(p.x < maxx){
            candidates.add(new Point(p.x+1, p.y));
        }
        if(p.y > miny){
            candidates.add(new Point(p.x, p.y-1));
        }
        if(p.y < maxy){
            candidates.add(new Point(p.x, p.y+1));
        }

        return candidates;
    }

    public static List<Point> GetOrthogonalNeighbors(Point p, int[][] arr){
        int minx = 0;
        int maxx = arr.length-1;
        int miny = 0;
        int maxy = arr[0].length-1;
        return GetOrthogonalNeighbors(p, minx, maxx, miny, maxy);
    }

    public static Rectangle GetBounds(Collection<Point> points){
        int minx = Integer.MAX_VALUE;
        int miny = Integer.MAX_VALUE;
        int maxx = Integer.MIN_VALUE;
        int maxy = Integer.MIN_VALUE;
        for(Point p : points){
            minx = Math.min(minx, p.x);
            maxx = Math.max(maxx, p.x);
            miny = Math.min(miny, p.y);
            maxy = Math.max(maxy, p.y);
        }
        int height = maxy - miny;
        int width = maxx - minx;
        return new Rectangle(minx, miny, width, height);
    }

    public static int ManhattanDistance(Point p1, Point p2){
        return Math.abs(p2.x - p1.x) + Math.abs(p2.y - p1.y);
    }
    public static double ManhattanDistance3D(Point3D p1, Point3D p2){ //Now in 3D!
        return Math.abs(p2.getX() - p1.getX()) + Math.abs(p2.getY() - p1.getY()) + Math.abs(p2.getZ() - p1.getZ());
    }

    public static Point3D RotateX(Point3D p, double degrees){
        double radians = Math.toRadians(degrees);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);
        double x = p.getX();
        double y = (p.getY() * cosTheta) - (p.getZ() * sinTheta);
        double z = (p.getY() * sinTheta) + (p.getZ() * cosTheta);
        return new Point3D(round(x, 3), round(y, 3), round(z, 3));
    }
    public static Point3D RotateY(Point3D p, double degrees){
        double radians = Math.toRadians(degrees);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);
        double x = (p.getX() * cosTheta) + (p.getZ() * sinTheta);
        double y = p.getY();
        double z = (-p.getX() * sinTheta) + (p.getZ() * cosTheta);
        return new Point3D(round(x, 3), round(y, 3), round(z, 3));
    }
    public static Point3D RotateZ(Point3D p, double degrees){
        double radians = Math.toRadians(degrees);
        double cosTheta = Math.cos(radians);
        double sinTheta = Math.sin(radians);
        double x = (p.getX() * cosTheta) - (p.getY() * sinTheta);
        double y = (p.getX() * sinTheta) + (p.getY() * cosTheta);
        double z = p.getZ();
        return new Point3D(round(x, 3), round(y, 3), round(z, 3));
    }

}
