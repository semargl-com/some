package com.company;

public class Main {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        Point p1 = new Point(10, 20);
        Point p2 = new Point(15, 2);
        Point p1x = new Point(10, 30);
        Point p1y = new Point(30, 20);
        log("distance p1 p2: " + p1.distance(p2));
        log("angle p1 p2: " + p1.angle(p2));
        log("angle p2 p1: " + p2.angle(p1));
        log("angle p1 p1x: " + p1.angle(p1x));
        log("angle p1x p1: " + p1x.angle(p1));
        log("angle p1 p1y: " + p1.angle(p1y));
        log("angle p1y p1: " + p1y.angle(p1));
    }

    public static void log(String s) {
        System.out.println(s);
    }
}
