package synwait;

class Prod implements Runnable {
    private QueueIsh<Integer> q;

    public Prod(QueueIsh<Integer> q) {
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

class Cons implements Runnable {
    private QueueIsh<Integer> q;

    public Cons(QueueIsh<Integer> q) {
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

public class TryQueue {
    public static void main(String[] args) {
        QueueIsh<Integer> qi = new QueueIsh<>();
        new Thread(new Prod(qi)).start();
        new Thread(new Cons(qi)).start();
        System.out.println("Kicked off...");
    }
}
