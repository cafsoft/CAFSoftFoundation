/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;

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

    protected void upload(Data data,
                          OutputStream destination)
            throws IOException {

        byte[] bytes = data.toBytes();
        ByteArrayInputStream source = new ByteArrayInputStream(bytes);
        upload(source, destination, bytes.length);
        destination.close();
    }

    protected void upload(InputStream source,
                          OutputStream destination,
                          long contentSize)
            throws IOException {

        final int BUFFER_SIZE = 100 * 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytes = 0;
        int totalBytes = 0;

        if (this.getSession().getDelegate() instanceof URLSessionTaskDelegate) {
            URLSessionTaskDelegate delegate = null;

            delegate = (URLSessionTaskDelegate) getSession().getDelegate();

            bytes = source.read(buffer);
            while (bytes != -1) {
                destination.write(buffer, 0, bytes);
                totalBytes += bytes;
                delegate.urlSession(getSession(), this, bytes, totalBytes, contentSize);

                bytes = source.read(buffer);
            }
        }else{
            bytes = source.read(buffer);
            while (bytes != -1) {
                destination.write(buffer, 0, bytes);
                bytes = source.read(buffer);
            }
        }
    }

    protected void download(InputStream source,
                            OutputStream destination,
                            long contentSize)
            throws IOException {

        final int BUFFER_SIZE = 100 * 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytes = 0;

        bytes = source.read(buffer);
        while (bytes != -1) {
            destination.write(buffer, 0, bytes);
            bytes = source.read(buffer);
        }
    }

    public void addRequestProperties(HttpURLConnection connection) {

        URLRequest request = getRequest();

        request.getAllHttpHeaderFields().forEach((fieldKey, fieldValue) -> {
            connection.addRequestProperty(fieldKey, fieldValue);
        });
    }

    protected void configureHTTPURLConnection(HttpURLConnection connection)
            throws ProtocolException {

        URLSessionConfiguration configuration = getSession().getConfiguration();

        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) connection;
            SSLSocketFactory socketFactory = configuration.getSocketFactory();
            if (socketFactory != null){
                httpsURLConnection.setSSLSocketFactory(socketFactory);
            }
        }
        connection.setRequestMethod(request.getHttpMethod());
        addRequestProperties(connection);

        connection.setConnectTimeout(configuration.getConnectTimeout());
        connection.setReadTimeout(configuration.getReadTimeout());
    }

    protected void sendBody(HttpURLConnection connection)
            throws IOException {

        URLRequest request = getRequest();

        if (request.getHttpBody() != null) {
            connection.setDoOutput(true);
            OutputStream outStream = connection.getOutputStream();
            //uploadStream(outStream, request.getHttpBody().toBytes());

            upload(request.getHttpBody(), outStream);
        }
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
