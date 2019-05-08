package synwait;

class Cons3 implements Runnable {
    public Cons3(QueueRL<Integer> q) {
        this.q = q;
    }

    private QueueRL<Integer> q;
    int[] data = new int[10_000];

    @Override
    public void run() {
        while (true) {
            try {
                int x = q.get();
                data[x]++;
            } catch (InterruptedException e) {
                System.out.println("Consumer Interrupted");
                break;
            }
        }
    }
}

class Prod3 implements Runnable {
    private QueueRL<Integer> q;

    public Prod3(QueueRL<Integer> q) {
        this.q = q;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10_000; i++) {
            try {
                q.put(i);
                if (Math.random() < 0.001) q.put(i);
                if (i < 100) Thread.sleep(1);
            } catch (InterruptedException e) {
                System.err.println("Interrupted producer");
                break;
            }
        }
        System.out.println("All produced...");
    }
}

public class TryQueue3 {
    public static void main(String[] args) throws Throwable {
        QueueRL<Integer> q = new QueueRL<>();

        Thread t1 = new Thread(new Prod3(q));
        t1.start();
        Thread t2 = new Thread(new Prod3(q));
        t2.start();
        Thread t3 = new Thread(new Prod3(q));
        t3.start();
        Cons3 c1 = new Cons3(q);
        Thread t4 = new Thread(c1);
        t4.start();
        Cons3 c2 = new Cons3(q);
        Thread t5 = new Thread(c2);
        t5.start();
        t1.join();
        t2.join();
        t3.join();
        // all producers now stopped
        Thread.sleep(1_000);
        t4.interrupt();
        t5.interrupt();
        t4.join();
        t5.join();
        for (int i = 0; i < 10_000; i++) {
            c1.data[i] += c2.data[i];
            if (c1.data[i] != 3) {
                System.out.println("Error, value " + i + " found " + c1.data[i] + " times");
            }
        }
        System.out.println("Finished checking.");
    }
}
