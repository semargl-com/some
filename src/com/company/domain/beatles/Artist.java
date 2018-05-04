package com.company.domain.beatles;

public class Artist {

    private String name;
    private String from;

    public Artist() {}

    public Artist(String name, String from) {
        this.name = name;
        this.from = from;
    }

    public String getName() {
        return name;
    }

    public String getFrom() {
        return from;
    }

    public boolean isFrom(String from) {
        //System.out.println(this.from + " vs " + from + ": " + this.from.equals(from));
        return this.from.equals(from);
    }
}
