package it.polimi.ingsw.view.TUI;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

/**
 *This class is responsible for asynchronously reading user input.
 *It implements the Runnable interface, allowing it to be used in a separate thread.
 */
public class AsyncReader implements Runnable{
    final Object lock;
    private final BlockingQueue<String> blockingQueue;

    /**
     *Constructor for the AsyncReader class.
     *@param lock The lock object used for synchronization.
     *@param blockingQueue The queue where the read strings are stored.
     */
    AsyncReader(Object lock, BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        this.lock = lock;
    }

    /**
     *The run method is called when the thread is started.
     *It continuously reads lines from the standard input and adds them to the blocking queue.
     *After adding a line to the queue, it waits until it is notified.
     */
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while(!Thread.interrupted()) {
            synchronized (lock) {
                String value = scanner.nextLine();
                this.blockingQueue.add(value);
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}
