package synwait;

class Prod2 implements Runnable {
    private QueueRL<Integer> q;

    public Prod2(QueueRL<Integer> q) {
        this.q = q;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10_000; i++) {
            try {
                if (i == 100) {
                    q.put(0);
                } else {
                    q.put(i);
                }
                if (i < 100) Thread.sleep(1);
            } catch (InterruptedException e) {
                System.err.println("Interrupted producer");
                break;
            }
        }
        System.out.println("All produced...");
    }
}

class Cons2 implements Runnable {
    private QueueRL<Integer> q;

    public Cons2(QueueRL<Integer> q) {
        this.q = q;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10_000; i++) {
            try {
                int x = q.get();
                if (x != i) {
                    System.err.println("Mismatch, expected " + i + " got " + x);
                }
                if (i > 9_900) {
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                System.err.println("Interrupted consumer");
                break;
            }
        }
        System.out.println("All consumed...");
    }
}

public class TryQueue2 {
    public static void main(String[] args) {
        QueueRL<Integer> qi = new QueueRL<>();
        new Thread(new Prod2(qi)).start();
        new Thread(new Cons2(qi)).start();
        System.out.println("Kicked off...");
    }
}
