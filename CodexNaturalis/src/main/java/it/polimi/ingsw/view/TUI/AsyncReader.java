package it.polimi.ingsw.view.TUI;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class AsyncReader implements Runnable{


    Object lock;
    private BlockingQueue<String> blockingQueue;

    AsyncReader(Object lock, BlockingQueue<String> blockingQueue)
    {
        this.blockingQueue = blockingQueue;
        this.lock = lock;
    }

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
                    throw new RuntimeException(e);
                }
            }
        }
    }
}