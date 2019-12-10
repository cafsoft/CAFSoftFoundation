/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

/**
 *
 * @author ceaufres
 */
public class URLSessionTask {

    private Thread thread = null;

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
