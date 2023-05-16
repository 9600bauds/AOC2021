package org.aoc2021.Day22;

public class Instruction {
    public int minx;
    public int maxx;
    public int miny;
    public int maxy;
    public int minz;
    public int maxz;
    public boolean isOn;

    public Instruction(int minx, int maxx, int miny, int maxy, int minz, int maxz, boolean isOn) {
        this.minx = minx;
        this.maxx = maxx;
        this.miny = miny;
        this.maxy = maxy;
        this.minz = minz;
        this.maxz = maxz;
        this.isOn = isOn;
    }

    @Override
    public String toString() {
        return "Instruction{" + "minx=" + minx +
                ", maxx=" + maxx +
                ", miny=" + miny +
                ", maxy=" + maxy +
                ", minz=" + minz +
                ", maxz=" + maxz +
                ", isOn=" + isOn +
                '}';
    }
}
