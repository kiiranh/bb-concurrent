package promise;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class ManyParts {

    public static CompletableFuture<String> readAFile(String filename) {
        CompletableFuture<String> cf = new CompletableFuture<>();
        new Thread(() -> {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
                System.out.println(filename + " produced...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cf.complete("The file called " + filename + " contains this text");
        }).start();
        return cf;
    }

    public static void main(String[] args) {
        CompletableFuture<String> theRoot = readAFile("File number 0");

        for (int fileNo = 1; fileNo < 4; fileNo++) {
            CompletableFuture<String> nextFile = readAFile("File number " + fileNo);
            theRoot = theRoot.thenCombineAsync(nextFile, (t1, t2) -> t1 + "\n" + t2);
        }
        System.out.println("Pipeline built...");
        theRoot.thenAccept(s -> System.out.println("Files completely read:\n" + s)).join();
    }
}
