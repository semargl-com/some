package com.company.domain.beatles;

import java.util.Arrays;
import java.util.List;

public class BeatlesTests {
    public static void log(String s) {
        System.out.println(s);
    }

    private List<Artist> allArtists = Arrays.asList(
            new Artist("John Lennon", "London"),
            new Artist("Paul McCartney", "London"),
            new Artist("George Harrison", "London"),
            new Artist("Ringo Starr", "London")
    );

    public BeatlesTests() {
        count1();
    }

    private void count1() {
        long count = allArtists.stream()
                .filter(artist -> {
                    System.out.println(artist.getName());
                    return artist.isFrom("London");
                })
                .count();
        System.out.println("count: " + count);
    }
}
