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
    
    public enum State {
        RUNNING,
        SUSPENDED,
        CANCELING,
        COMPLETED
    }

    private OperationQueue workQueue = null;
    private Operation operation = null;
    //private Future<String> future = null; 
    //private Thread thread = null;

    public URLSessionTask(OperationQueue newWQ, Operation newOperation){
        workQueue = newWQ;
        operation = newOperation;
    }
    
    public void resume(){
         //future = (Future<String>) workQueue.submit(runnable);
         //thread = new Thread(operation.getcompletionBlock());
         workQueue.addOperation(operation);
    }
    
    /*
    public boolean cancel(){
        return future.cancel(true);
    }
    */
    
    /*
    public Thread getThread(){
        return thread;
    }
    */
    
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
    
    /*
    public Runnable getRunnable(){
        return runnable;
    }
    */
}
