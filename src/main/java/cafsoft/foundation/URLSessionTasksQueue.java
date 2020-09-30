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
public class URLSessionTasksQueue implements Runnable {

    private final Queue<URLSessionTask> queue = new LinkedList<>();
    private Thread thread = null;

    public void add(URLSessionTask task) {
        synchronized (queue) {
            queue.add(task);
        }
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(this);
            thread.start();
            System.out.println("New queue thread");
        }
    }

    @Override
    public void run() {
        URLSessionTask task = null;
        Thread curThread = null;

        while (!queue.isEmpty()) {
            synchronized (queue) {
                task = queue.poll();
            }
            if (task != null) {
                task.getRunnable().run();

                
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
                        //Logger.getLogger(URLSessionTasksQueue.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }
    }
*/

}
