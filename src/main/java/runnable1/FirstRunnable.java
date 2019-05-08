package runnable1;

//class MyJob implements Runnable {
//    int i = 0;
//    @Override
//    public void run() {
//
//        while (i < 10_000) {
//            System.out.println(Thread.currentThread().getName() + " i is " + i);
//            i++;
//        }
//    }
//}
class MyJob implements Runnable {
    int i = 0;
    @Override
    public void run() {

        for (int j = 0; j < 10_000; j++) {
            i++;
        }
        System.out.println("Finished");
    }
}
public class FirstRunnable {
    public static void main(String[] args) throws Throwable {
        MyJob mj = new MyJob();
        Thread t1 = new Thread(mj);
        Thread t2 = new Thread(mj);
        System.out.println(Thread.currentThread().getName() + " about to start workers");
        t1.start();
        t2.start();
        System.out.println(Thread.currentThread().getName() + " started workers");

        Thread.sleep(2_000);
        System.out.println("result is " + mj.i);

    }
}
