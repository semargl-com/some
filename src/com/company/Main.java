package com.company;

public class Main {

    Point p1 = new Point(10, 20);
    Point p2 = new Point(15, 2);
    Point p1x = new Point(10, 30);
    Point p1y = new Point(30, 20);

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        log("\n");
        testDistance();
        testAngle();
        testDirection();
        log("");
    }

    public void testDistance() {
        log("distance p1 p2: " + p1.distance(p2));
        log("distance p1 p1x: " + p1.distance(p1x));
        log("distance p1 p1y: " + p1.distance(p1y));
        log("");
    }

    public void testAngle() {
        log("angle p1 p2: " + p1.angle(p2));
        log("angle p2 p1: " + p2.angle(p1));
        log("angle p1 p1x: " + p1.angle(p1x));
        log("angle p1x p1: " + p1x.angle(p1));
        log("angle p1 p1y: " + p1.angle(p1y));
        log("angle p1y p1: " + p1y.angle(p1));
        log("");
    }

    public void testDirection() {
        log("direction 0.23: " + new Direction(0.23).at(p1, p2));
        log("direction 0.252: " + new Direction(0.252).at(p1, p2));
        log("direction 0.27: " + new Direction(0.27).at(p1, p2));
        log("direction 0.29: " + new Direction(0.29).at(p1, p2));
        log("direction 0.31: " + new Direction(0.31).at(p1, p2));
        log("");
    }

    public void log(String s) {
        System.out.println(s);
    }
}
