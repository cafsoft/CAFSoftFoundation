/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author ceaufres
 */
public class OperationQueue {

    private Queue<Operation> executionBlocks = new LinkedList<>();
    private final Queue<Operation> blocks = new LinkedList<>();
    private int maxConcurrentOperationCount = 1;

    private Thread[] threads = new Thread[1];

    public void addOperation(Operation anOperation) {

        synchronized (executionBlocks) {
            executionBlocks.add(anOperation);

            startThreads();
        }
    }

    private void startThreads() {

        if (!executionBlocks.isEmpty()) {
            for (int k = 0; k < threads.length; k++) {
                if (threads[k] == null || !threads[k].isAlive()) {

                    int k2 = k;
                    threads[k] = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            System.out.println("New thread " + k2);
                            executeOperations(k2);
                        }
                    });
                    threads[k].start();
                }
            }
        }
    }

    public void setMaxConcurrentOperationCount(int newMaxConcurrentOperationCount) {
        Operation operation = null;
        Queue<Operation> newExecutionBlocks = null;

        if (newMaxConcurrentOperationCount > 0 && newMaxConcurrentOperationCount <= 4) {
            if (maxConcurrentOperationCount != newMaxConcurrentOperationCount) {
                this.maxConcurrentOperationCount = newMaxConcurrentOperationCount;
                newExecutionBlocks = new LinkedList<>();
                synchronized (executionBlocks) {
                    while (!executionBlocks.isEmpty()) {
                        newExecutionBlocks.add(executionBlocks.poll());
                    }
                    executionBlocks = newExecutionBlocks;
                    threads = new Thread[maxConcurrentOperationCount];
                    startThreads();
                }
            }
        }
    }

    public void addOperation(Runnable runnableBlock) {

        addOperation(new BlockOperation(runnableBlock));
    }

    public void executeOperations(int threadNumber) {
        Operation operation = null;
        //Thread curThread = null;
        Queue<Operation> curExecutionBlocks = this.executionBlocks;

        while (!curExecutionBlocks.isEmpty()) {
            synchronized (curExecutionBlocks) {
                operation = curExecutionBlocks.poll();
            }
            if (operation != null) {
                //operation.getcompletionBlock().run();
                System.out.println("Thread " + threadNumber);
                operation.start();

            }
        }
    }

    /* Foreach URLSessionTask async
    @Override
    public void run() {
        URLSessionTask task = null;
        Thread curThread = null;

        while (!queue.isEmpty()) {
            synchronized (queue) {
                task = queue.poll();
            }
            if (task != null) {
                curThread = task.getThread();
                if (curThread != null) {
                    curThread.start();
                    System.out.println("New Thread");

                    try {
                        curThread.join();
                    } catch (InterruptedException ex) {
                        //Logger.getLogger(OperationQueue.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }
    }
     */
}
