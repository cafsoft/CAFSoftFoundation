/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ceaufres
 */
public class URLSession {

    //private int nextTaskIdentifier = 1;
    private OperationQueue workQueue = null;

    private static URLSession shared = null;

    private URLSessionConfiguration configuration = null;

    private URLSessionDelegate delegate = null;

    public URLSession(URLSessionConfiguration configuration,
            URLSessionDelegate delegate, OperationQueue queue) {

        this.configuration = configuration;
        this.delegate = delegate;
        //... = queue;

        workQueue = new OperationQueue();
    }

    public URLSession(URLSessionConfiguration configuration) {

        this(configuration, null, null);
    }

    public URLSession() {

        this(new URLSessionConfiguration());
    }

    public static URLSession getShared() {

        if (shared == null) {
            shared = new URLSession();
        }

        return shared;
    }

 /*
    private int createNextTaskIdentifier() {

        int i = nextTaskIdentifier;

        nextTaskIdentifier = nextTaskIdentifier + 1;

        return i;
    }
     */

 /*
    public URLSessionUploadTask uploadTask(URLRequest request, Data bodyData,
            DataTaskCompletion completionHandler) {

        Operation operation = new BlockOperation(new Runnable() {
            @Override
            public void run() {
                String method = request.getHttpMethod();

                if (method.equals("PUT")) {
                    sendHttpRequest(request, bodyData, completionHandler);
                }
            }
        });

        return new URLSessionUploadTask(workQueue, operation);
    }
     */

    /*
    public URLSessionUploadTask uploadTask(URLRequest request, Data bodyData,
            DataTaskCompletion completionHandler) {

        String protocol = request.getUrl().getProtocol();
        String method = request.getHttpMethod();
        URLSessionUploadTask uploadTask = null;

        if (protocol.equals("http") || protocol.equals("https")) {

            if (method.equals("PUT")) {
                //sendHttpRequest(request, null, completionHandler);

                uploadTask = new URLSessionUploadTask(this, request, 0, 
                        bodyData, workQueue, completionHandler);
            }
        } else {
            return null;
        }

        return uploadTask;
    }
    */

    public URLSessionDownloadTask downloadTask(URLRequest request,
            DownloadTaskCompletion completionHandler) {

        String protocol = request.getUrl().getProtocol();
        URLSessionDownloadTask downloadTask = null;

        if (protocol.equals("http") || protocol.equals("https")) {

            downloadTask = new URLSessionDownloadTask(this, request, 0,
                    workQueue, completionHandler);

        } else {
            return null;
        }

        return downloadTask;
    }

    public URLSessionDataTask dataTask(URLRequest request,
            DataTaskCompletion completionHandler) {

        String protocol = request.getUrl().getProtocol();
        URLSessionDataTask dataTask = null;

        if (protocol.equals("http") || protocol.equals("https")) {
            String[] methods = {"GET", "POST", "PUT", "DELETE"};

            for (String method : methods){
                if (request.getHttpMethod().equals(method)) {
                    dataTask = new URLSessionDataTask(this, request, 0,
                            workQueue, completionHandler);
                    break;
                }
            }

        }

        return dataTask;
    }


    public URLSessionDownloadTask downloadTask(URL url) {

        return downloadTask(new URLRequest(url), null);
    }

    public URLSessionDownloadTask downloadTask(URL url,
            DownloadTaskCompletion completionHandler) {

        return downloadTask(new URLRequest(url), completionHandler);
    }

    public URLSessionDataTask dataTask(URLRequest request) {

        return dataTask(request, null);
    }

    public URLSessionDataTask dataTask(URL url) {

        return dataTask(new URLRequest(url), null);
    }

    public URLSessionDataTask dataTask(URL url,
            DataTaskCompletion completionHandler) {

        return dataTask(new URLRequest(url), completionHandler);
    }

    public interface TaskCompletion {
    }

    public interface DataTaskCompletion extends TaskCompletion {
        void run(Data data, URLResponse response, Error error);
    }

    public interface DownloadTaskCompletion extends TaskCompletion {
        void run(URL url, URLResponse response, Error error);
    }

    public interface UploadTaskCompletion extends DataTaskCompletion {

        //public abstract void run(URL url, URLResponse response, Error error);
    }

    private class _TaskRegistry {

        private Map<Integer, URLSessionTask> tasks = new HashMap<>();

        public void add(URLSessionTask task) {

        }

    }

    /**
     * @return the configuration
     */
    public URLSessionConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * @return the delegate
     */
    public URLSessionDelegate getDelegate() {
        return delegate;
    }

}
