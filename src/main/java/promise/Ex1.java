package promise;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class Ex1 {

    public static CompletableFuture<String> readAFile(String filename) {
        CompletableFuture<String> cf = new CompletableFuture<>();
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Math.random() > 0.5) {
                cf.complete("The file called " + filename + " contains this text");
            } else {
                cf.completeExceptionally(new IOException("File " + filename + " failed to open"));
            }
        }).start();
        return cf;
    }


    public static void main(String[] args) {
        CompletableFuture<Void> cfv = CompletableFuture.supplyAsync(() -> {
            System.out.println("supplyAsync + " + Thread.currentThread().getName());
            return "Hello";
        })
                .thenCompose(s -> readAFile(s))
                .thenApplyAsync(s -> {
                    System.out.println("supplyAsync + " + Thread.currentThread().getName());
                    return "The word is " + s;
                })
                .handle((v, e) -> {
                    if (e != null) {
                        System.out.println("Handling exception " + e.getMessage());
                        return "RECOVERED";
                    }
                    return v;
                })
                .thenAcceptAsync(s -> {
                    System.out.println("supplyAsync + " + Thread.currentThread().getName());
                    System.out.println("The message is: " + s);
                })
                .thenAccept(v -> System.out.println("the void is " + v))
                .thenRun(() -> System.out.println("And we finished..."));

        System.out.println("The pipeline is built...");
                cfv.join();
    }
}
