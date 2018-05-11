package com.company.domain.futures;

import com.company.Main;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * from http://www.deadcoderising.com/java8-writing-asynchronous-code-with-completablefuture/
 *
 * also samples: http://www.programcreek.com/java-api-examples/index.php?class=java.util.concurrent.CompletableFuture&method=exceptionally
 */

/*
    Input   Output  Sync        Async           flats inner future

    -       +       -           supplyAsync
    +       +       thenApply(map)   ...Async        thenCompose(flatMap)
    +       -       thenAccept  ...Async
    -       -       thenRun     runAsync

combining:
    +       +       thenCombine ...Async
    +       -       runAfterBoth ...Async
    -       -       allOf

quicker wins:
    +       +       applyToEither ...Async
    +       -       acceptEither ...Async
    -       -       anyOf

 */

public class CompletableFutures {

    private void test1() throws Exception {
        CompletableFuture.supplyAsync(() -> "Hello")
                .thenAccept(Main::log);
    }

    private void test2() throws Exception {
        CompletableFuture.supplyAsync(() -> "Hello")
                .thenApply(String::toUpperCase)
                .thenApply(String::toLowerCase)
                .thenAccept(Main::log);
    }

    private CompletionStage<String> sendMsgAsync(String msg) {
        return CompletableFuture.supplyAsync(() -> "Message \"" + msg + "\" sent OK");
    }

    private void test3() throws Exception {
        CompletableFuture.supplyAsync(() -> "Hello")
                //.thenApply(this::sendMsgAsync) // Returns type CompletionStage<CompletionStage<String>>
                .thenCompose(this::sendMsgAsync) // Returns type CompletionStage<String>
                .thenApply(String::toLowerCase)
                .thenAccept(Main::log);
    }

    private void test4() throws Exception {
        CompletableFuture<String> flow = CompletableFuture.supplyAsync(() -> "Hello");
        //flow.thenAccept(Main::log); // 2 one-by-one callbacks after supplier (in one thread)
        //flow.thenAccept(Main::log);
        flow.thenAcceptAsync(Main::log); // 2 simultaneous callbacks after supplier (in different threads)
        flow.thenAcceptAsync(Main::log);
        // also can be thenApply/thenApplyAsync
        // The key is — the asynchronous version can be convenient when you have several callbacks dependent on the same computation.
    }

    private String failedSupplier() {
        throw new RuntimeException("Something went wrong");
    }

    private void test5() throws Exception {
        CompletableFuture.supplyAsync(this::failedSupplier)
                .exceptionally((ex) -> "Handling: " + ex.getMessage())
                .thenAccept(Main::log);
    }

    private void test6() throws Exception {
        CompletableFuture<String> first = CompletableFuture.supplyAsync(() -> "Hello ");
        CompletableFuture<String> second = CompletableFuture.supplyAsync(() -> "World!");
        first.thenCombine(second, (res1, res2) -> { Main.log(res1 + res2); return null; }); // executes when both parallel done
    }

    private void test7() throws Exception {
        CompletableFuture<String> first = CompletableFuture.supplyAsync(() -> "Hello ");
        CompletableFuture<String> second = CompletableFuture.supplyAsync(() -> "World!");
        first.runAfterBoth(second, () -> Main.log("Both done")); // executes when both parallel done
    }

    private void test8() throws Exception {
        CompletableFuture<String> first = CompletableFuture.supplyAsync(() -> "Hello ");
        CompletableFuture<String> second = CompletableFuture.supplyAsync(() -> "World!");
        first.acceptEither(second, Main::log); // executes when any first done
    }

    private void test9() throws Exception {
        CompletableFuture<String> first = CompletableFuture.supplyAsync(() -> "Hello ");
        CompletableFuture<String> second = CompletableFuture.supplyAsync(() -> "World!");
        first.thenCombine(second, (res1, res2) -> { Main.log(res1 + res2); return null; }); // executes when both parallel done
    }

    // from https://www.youtube.com/watch?v=yBF9VRiGkik

    private int slowIncrement(int i) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1 + i;
    }

    private void test10() throws Exception {
        CompletableFuture<?> future = CompletableFuture
                .supplyAsync(() -> 1)
                .thenApply(this::slowIncrement)
                .thenAccept(res -> System.out.println("final res: " + res));
//        int arg1 = 1;
//        int arg2 = 2;
//        int arg3 = 3;
        future.get();
    }


    // test11 and test12 - are about difference between thenApply and thenApplyAsync
    // разница без Async и с - как если есть один подчиненный, или больше
    // (то есть в первом случае, задачи выполняются в другом (не основном) потоке, но только одном (последовательно))

    private void test11() throws Exception { // runs 2 seconds
        CompletableFuture<Integer> initial = CompletableFuture.supplyAsync(() -> 1);
        CompletableFuture<Integer> future11 = initial.thenApply(this::slowIncrement);
        CompletableFuture<Integer> future12 = initial.thenApply(this::slowIncrement);
        CompletableFuture<Void> finalFut = future11.thenCombine(future12, (a, b) -> a + b)
                .thenAccept(res -> System.out.println("final res: " + res));
        finalFut.get();
    }

    private void test12() throws Exception { // runs 1 second
        CompletableFuture<Integer> initial = CompletableFuture.supplyAsync(() -> 1);
        CompletableFuture<Integer> future11 = initial.thenApplyAsync(this::slowIncrement);
        CompletableFuture<Integer> future12 = initial.thenApplyAsync(this::slowIncrement);
        CompletableFuture<Void> finalFut = future11.thenCombine(future12, (a, b) -> a + b)
                .thenAccept(res -> System.out.println("final res: " + res));
        finalFut.get();
    }


    private void test13_anyOf() throws Exception {
        CompletableFuture<Integer> initial = CompletableFuture.supplyAsync(() -> 1);
        CompletableFuture<Integer> future11 = initial.thenApplyAsync(this::slowIncrement);
        CompletableFuture<Integer> future12 = initial.thenApplyAsync((i) -> i * 3);
        CompletableFuture<?> finalFut = CompletableFuture.anyOf(future11, future12); // gets first ready result
        System.out.println(finalFut.get());
    }

    private void test14_applyToEither() throws Exception {
        CompletableFuture<Integer> initial = CompletableFuture.supplyAsync(() -> 1);
        CompletableFuture<Integer> future11 = initial.thenApplyAsync(this::slowIncrement);
        CompletableFuture<Integer> future12 = initial.thenApplyAsync((i) -> i * 3);
        CompletableFuture<?> finalFut = future11.applyToEither(future12, (i) -> i * 4); // continue with first ready result
        System.out.println(finalFut.get());
    }

    public CompletableFutures() throws Exception {
        test14_applyToEither();
    }

}
