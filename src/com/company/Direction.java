package com.company;

public class Direction {

    public static final double DEFAULT_THRESHOLD = 0.02;

    public double angle;

    public Direction(double angle) {
        this.angle = angle;
    }

    public Direction() {}

    public boolean at(Point from, Point to) {
        return at(from, to, DEFAULT_THRESHOLD);
    }

    public boolean at(Point from, Point to, double threshold) {
        double realAngle = from.angle(to);
        return Math.abs(angle - realAngle) < threshold; // TODO case when over Pi
    }
}
