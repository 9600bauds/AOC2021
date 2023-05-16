package org.aoc2021.Day22;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;

public class Cuboid extends org.aoc2021.Utils.Cuboid {
    boolean isOn;
    public Cuboid(Instruction instruction) {
        //VERY IMPORTANT: We decrease the instruction's minimum coordinates by 1.
        //To understand why, imagine an instruction x1..1,y1..1,z1..1
        //What it's saying is "turn on the block 1,1,1". But a cuboid going from 1,1,1 to 1,1,1 has a volume of 0, not 1.
        //Therefore, we interpret "the block 1,1,1" as "the block starting at 0,0,0 and finishing at 1,1,1".
        super(new Point3D(instruction.minx - 1, instruction.miny - 1, instruction.minz - 1),
                instruction.maxx - (instruction.minx - 1),
                instruction.maxy - (instruction.miny - 1),
                instruction.maxz - (instruction.minz - 1)
        );
        isOn = instruction.isOn;
    }

    //Used for JavaFX visualizations
    public Material GetMaterial(){
        PhongMaterial material = new PhongMaterial();
        if(isOn){
            material.setDiffuseColor(new Color(0, 1, 0, 0.75)); // Set diffuse color with alpha value
            material.setSpecularColor(Color.GREEN);
        }
        else{
            material.setDiffuseColor(new Color(1, 0, 0, 0.75)); // Set diffuse color with alpha value
            material.setSpecularColor(Color.RED);
        }
        return material;
    }
}
