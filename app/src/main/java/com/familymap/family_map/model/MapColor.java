package com.familymap.family_map.model;

public class MapColor {
    private final float color;

    public MapColor(float color) {
        this.color = color;
    }

    public static MapColor[] values() {
        float[] possibles = new float[]{(float) 0.0, (float) 240.0, (float) 120.0, (float) 60.0, (float) 300.0, (float) 30.0, (float) 210.0, (float) 330.0, (float) 270.0, (float) 180.0};
        MapColor[] values = new MapColor[10];
        for (int i = 0; i < 10; ++i) {
            values[i] = new MapColor(possibles[i]);
        }
        return values;
    }

    public float getColor() {
        return color;
    }
}
