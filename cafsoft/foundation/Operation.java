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
public class Operation {

    public Runnable completionBlock = null;
    private boolean ready = true;
    private boolean cancelled = false;
    private boolean executing = false;
    private boolean finished = false;
    //private State state = State.READY;

    /*
    enum State {
        READY,
        FINISHED,
        EXECUTING,
        CANCELED
    }
     */
    public void start() {
        executing = true;
        main();
        executing = false;
        finished = true;
        if (completionBlock != null) {
            Thread thread = new Thread(completionBlock);
            thread.start();
            /*try {
                thread.join();
            } catch (InterruptedException ex) {
                //Logger.getLogger(BlockOperation.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
    }

    public void main() {
    }

    public boolean isReady() {
        return ready;
    }

    public boolean isExecuting() {
        return executing;
    }

    public boolean isCancelled() {
        
        return this.cancelled;
    }

    public boolean isFinished() {
        
        return this.finished;
    }
    
    public boolean isAsync(){
        
        return false;
    }
    
    public void cancel() {
        this.cancelled = true;
    }
     
}
