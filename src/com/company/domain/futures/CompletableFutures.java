package com.company.domain.futures;

import com.company.Main;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * from http://www.deadcoderising.com/java8-writing-asynchronous-code-with-completablefuture/
 *
 * also samples: http://www.programcreek.com/java-api-examples/index.php?class=java.util.concurrent.CompletableFuture&method=exceptionally
 */

public class CompletableFutures {

    public CompletableFutures() throws Exception {
        test9();
    }

    public void test1() throws Exception {
        CompletableFuture.supplyAsync(() -> "Hello")
                .thenAccept(Main::log);
    }

    public void test2() throws Exception {
        CompletableFuture.supplyAsync(() -> "Hello")
                .thenApply(String::toUpperCase)
                .thenApply(String::toLowerCase)
                .thenAccept(Main::log);
    }

    CompletionStage<String> sendMsgAsync(String msg) {
        return CompletableFuture.supplyAsync(() -> "Message \"" + msg + "\" sent OK");
    }

    public void test3() throws Exception {
        CompletableFuture.supplyAsync(() -> "Hello")
                //.thenApply(this::sendMsgAsync) // Returns type CompletionStage<CompletionStage<String>>
                .thenCompose(this::sendMsgAsync) // Returns type CompletionStage<String>
                .thenApply(String::toLowerCase)
                .thenAccept(Main::log);
    }

    public void test4() throws Exception {
        CompletableFuture<String> flow = CompletableFuture.supplyAsync(() -> "Hello");
        //flow.thenAccept(Main::log); // 2 one-by-one callbacks after supplier (in one thread)
        //flow.thenAccept(Main::log);
        flow.thenAcceptAsync(Main::log); // 2 simultaneous callbacks after supplier (in different threads)
        flow.thenAcceptAsync(Main::log);
        // also can be thenApply/thenApplyAsync
        // The key is — the asynchronous version can be convenient when you have several callbacks dependent on the same computation.
    }

    String failedSupplier() {
        throw new RuntimeException("Something went wrong");
    }

    public void test5() throws Exception {
        CompletableFuture.supplyAsync(this::failedSupplier)
                .exceptionally((ex) -> "Handling: " + ex.getMessage())
                .thenAccept(Main::log);
    }

    public void test6() throws Exception {
        CompletableFuture<String> first = CompletableFuture.supplyAsync(() -> "Hello ");
        CompletableFuture<String> second = CompletableFuture.supplyAsync(() -> "World!");
        first.thenCombine(second, (res1, res2) -> { Main.log(res1 + res2); return null; }); // executes when both parallel done
    }

    public void test7() throws Exception {
        CompletableFuture<String> first = CompletableFuture.supplyAsync(() -> "Hello ");
        CompletableFuture<String> second = CompletableFuture.supplyAsync(() -> "World!");
        first.runAfterBoth(second, () -> Main.log("Both done")); // executes when both parallel done
    }

    public void test8() throws Exception {
        CompletableFuture<String> first = CompletableFuture.supplyAsync(() -> "Hello ");
        CompletableFuture<String> second = CompletableFuture.supplyAsync(() -> "World!");
        first.acceptEither(second, Main::log); // executes when any first done
    }



    public void test9() throws Exception {
        CompletableFuture<String> first = CompletableFuture.supplyAsync(() -> "Hello ");
        CompletableFuture<String> second = CompletableFuture.supplyAsync(() -> "World!");
        first.thenCombine(second, (res1, res2) -> { Main.log(res1 + res2); return null; }); // executes when both parallel done
    }
}
