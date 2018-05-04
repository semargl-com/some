package com.company.domain.futures;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Samples from https://tproger.ru/translations/java8-concurrency-tutorial-1/
 */

public class Futures {

    public Futures() throws Exception {
        test2();
    }

    public void test1() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Integer> future = executor.submit(() -> 10);

        System.out.println("future done? " + future.isDone());

        Integer result = future.get();

        System.out.println("future done? " + future.isDone());
        System.out.print("result: " + result);
    }

    public void test2() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(1); // need to shutdown, otherwise app not finishes
        //ExecutorService executor = Executors.newWorkStealingPool(); // finishes after tasks done

        List<Callable<String>> callables = Arrays.asList(
                () -> "task1",
                () -> "task2",
                () -> "task3");

        executor.invokeAll(callables)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    }
                    catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .forEach(System.out::println);
    }

}
