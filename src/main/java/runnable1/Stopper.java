package runnable1;

public class Stopper {
    private static volatile boolean stop = false;

    public static void main(String[] args) throws Throwable {
//        final boolean[] stop = { false };
        new Thread(() -> {
            System.out.println("Starting...");
//            while (! stop[0])
            while (!stop)
                ;
            System.out.println("Stopped...");
        }).start();

        System.out.println("Job launched...");
        Thread.sleep(2_000);
//        stop[0] = true;
        stop = true;
        System.out.println("Stop message sent...");
    }
}
