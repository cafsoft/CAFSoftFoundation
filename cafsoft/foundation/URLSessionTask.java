/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author ceaufres
 */
public abstract class URLSessionTask {
    
    private int taskIdentifier = 0;
    private URLSession session = null;
    private URLRequest request = null;
    
    public enum State {
        RUNNING,
        SUSPENDED,
        CANCELING,
        COMPLETED
    }

    //private OperationQueue workQueue = null;
    //private Operation operation = null;

    
    public URLSessionTask(URLSession session, URLRequest request, 
            int taskIdentifier){
        
        this.session = session;
        this.request = request;
        this.taskIdentifier = taskIdentifier;
        
        //workQueue = newWQ;
        //operation = newOperation;
    }
    
    /*
    public URLSessionTask(OperationQueue newWQ, Operation newOperation){
        workQueue = newWQ;
        operation = newOperation;
    }
    */
    
    
    protected void uploadStream(OutputStream outStream, byte[] bytesBuffer)
            throws IOException {

        outStream.write(bytesBuffer);
    }
    
    protected void transfer(InputStream inStream,
            OutputStream outStream, long contentSize,
            URLSessionDownloadTask downloadTask)
            throws IOException {

        final int BUFFER_SIZE = 100 * 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = 0;
        int total = 0;

        do {
            bytesRead = inStream.read(buffer);
            if (bytesRead > 0) {
                outStream.write(buffer, 0, bytesRead);
                total += bytesRead;

                if (getSession().getDelegate() != null) {
                    URLSessionDownloadDelegate del = null;

                    del = (URLSessionDownloadDelegate) getSession().getDelegate();
                    del.urlSession(getSession(), downloadTask, bytesRead, total, contentSize);
                }

            }
        } while (bytesRead != -1);
    }
    
    
    public abstract void resume();
    
    /*
    public void resume(){
         //future = (Future<String>) workQueue.submit(runnable);
         //thread = new Thread(operation.getcompletionBlock());
         workQueue.addOperation(operation);
    }*/
    
    
    
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

    public URLSession getSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(URLSession session) {
        this.session = session;
    }

    /**
     * @return the request
     */
    public URLRequest getRequest() {
        return request;
    }
    
}
