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
public class URLSessionTaskQueue implements Runnable {

    /*
    private final Queue<URLSessionTask> queue = new LinkedList<>();
    private Thread queueThread = null;

    public synchronized void add(URLSessionTask task) {
        queue.add(task);
        if (queueThread == null || !queueThread.isAlive()) {
            queueThread = new Thread(this);
            queueThread.start();
        }
    }
    */

    @Override
    public void run() {
        /*
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
                    try {
                        curThread.join();
                    } catch (InterruptedException ex) {
                        //Logger.getLogger(URLSessionTaskQueue.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
    */    
    }

}
