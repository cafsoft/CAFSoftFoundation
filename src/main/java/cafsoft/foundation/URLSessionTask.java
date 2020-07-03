/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 *
 * @author ceaufres
 */
public class URLSessionTask {

    private ExecutorService workQueue = null;
    private Runnable runnable = null;
    private Future<String> future = null; 
    //private Thread thread = null;

    public URLSessionTask(ExecutorService newWQ, Runnable newR){
        workQueue = newWQ;
        runnable = newR;
    }
    
    public void resume(){
         future = (Future<String>) workQueue.submit(runnable);
    }
    
    public boolean cancel(){
        return future.cancel(true);
    }
    
    /*
    public URLSessionTask(Runnable r) {
        thread = new Thread(r);
    }

    public void resume() {
        if (!thread.isAlive()) {
            thread.start();
        }
    }
    
    public void join() 
            throws InterruptedException{
        
        thread.join();
    }
    */
    /*
    public void join(long millis) 
            throws InterruptedException{
        
        thread.join(millis);
    }
    
    
    public void cancel(){
        thread.stop();
    }
    
    public void suspend(){
        thread.suspend();
    }
     */
}
