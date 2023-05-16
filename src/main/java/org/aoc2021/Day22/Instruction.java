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
        final StringBuilder sb = new StringBuilder("Instruction{");
        sb.append("minx=").append(minx);
        sb.append(", maxx=").append(maxx);
        sb.append(", miny=").append(miny);
        sb.append(", maxy=").append(maxy);
        sb.append(", minz=").append(minz);
        sb.append(", maxz=").append(maxz);
        sb.append(", isOn=").append(isOn);
        sb.append('}');
        return sb.toString();
    }
}
