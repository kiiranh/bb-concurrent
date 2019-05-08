package synwait;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class QueueRL<E> {
    private E[] data = (E[]) new Object[10];
    private int count = 0;
    private ReentrantLock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    public void put(E e) throws InterruptedException {
        lock.lock();
        try {
            while (count >= data.length) notFull.await();
            data[count++] = e;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public E get() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) notEmpty.await();
            E rv = data[0];
            System.arraycopy(data, 1, data, 0, --count);
            notFull.signal();
            return rv;
        } finally {
            lock.unlock();
        }
    }
}
