/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ceaufres
 */
public class BlockOperation extends Operation {

    private final Queue<Runnable> queue = new LinkedList<>();

    public BlockOperation() {
    }

    public BlockOperation(Runnable newRunnable) {

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

        Thread[] threads = new Thread[queue.size()];
        Thread thread = null;
        int k = 0;

        for (Runnable runnableBlock : queue) {
            threads[k] = new Thread(runnableBlock);
            k++;
        }

        for (int i = 1; i < threads.length; i++) {
            threads[i].start();
        }

        threads[0].run();

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
