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
public class BlockOperation extends Operation {

    private Queue<Runnable> queue = null;

    public BlockOperation() {
        queue = new LinkedList<>();
    }

    public BlockOperation(Runnable newRunnable) {
        this();
        
        queue.add(newRunnable);
    }

    public void addExecutionBlock(Runnable newRunnable)
        throws IllegalArgumentException{

        if (isExecuting() || isFinished()) {
            throw new IllegalArgumentException("the receiver is executing or has already finished");
        } else {
            queue.add(newRunnable);
        }
    }

    public Queue<Runnable> getOperationsBlock() {

        return queue;
    }

    @Override
    public void main() {
        super.main();

        Thread[] threads = null;
        Thread thread = null;
        int k = 0;

        threads = new Thread[queue.size()];
        for (Runnable runnableBlock : queue) {
            threads[k] = new Thread(runnableBlock);
            k++;
        }

        // execute the first runnable block in main thread (or current thread)
        threads[0].run();
        
        // start the concurrent execution of the others runnable blocks
        for (int i = 1; i < threads.length; i++) {
            threads[i].start();
        }

        // Wait for all executable blocks to finish (Barrier) 
        for (int i = 1; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                //Logger.getLogger(BlockOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /*
        if (completionBlock != null) {
            thread = new Thread(completionBlock);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException ex) {
                //Logger.getLogger(BlockOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
    }

}
