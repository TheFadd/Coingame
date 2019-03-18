package com.example.stargame;

public class SquareProperty {

    private final int imageRes;
    private final int points;

    public SquareProperty(int imageRes, int points) {
        this.imageRes = imageRes;
        this.points = points;
    }

    public int getImageRes() {
        return imageRes;
    }

    public int getPoints() {
        return points;
    }
}
