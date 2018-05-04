package com.company;

import com.company.domain.futures.CompletableFutures;

public class Main {

    public static void log(String s) {
        System.out.println(s);
    }

    public static void main(String[] args) throws Exception {
        //new BeatlesTests();
        //new JmsOpts();
        //new Futures();
        new CompletableFutures();
    }

}
