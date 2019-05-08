package synwait;

public class QueueIsh<E> {
    private E[] data = (E[]) new Object[10];
    private int count = 0;

    public void put(E e) throws InterruptedException {
        synchronized (this) {
//            while (count >= data.length) {
////                try {
//                    this.wait();
////                } catch (InterruptedException ex) {
////                    ex.printStackTrace();
////                }
//            }
            while (count >= data.length) this.wait();
//            this.notify(); // !!! :)
            data[count++] = e;
            this.notifyAll();
        }
    }

    public/* synchronized */E get() throws InterruptedException {
        synchronized (this) {
            while (count == 0) this.wait();
            E rv = data[0];
            System.arraycopy(data, 1, data, 0, --count);
//            this.notify();
            this.notifyAll();
            return rv;
        }
    }
}
