/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

import cafsoft.foundation.URLSession.DownloadTaskCompletion;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author ceaufres
 */
public class URLSessionDownloadTask
        extends URLSessionTask {

    private DownloadTaskCompletion completionHandler = null;
    private OperationQueue queue = null;

    /*
    public URLSessionDownloadTask(OperationQueue newWQ, Operation newOperation) {
        super(newWQ, newOperation);
    }
     */
    public URLSessionDownloadTask(URLSession session, URLRequest request,
            int taskIdentifier, OperationQueue newWQ,
            DownloadTaskCompletion completionHandler) {

        super(session, request, taskIdentifier);

        this.completionHandler = completionHandler;
        this.queue = newWQ;
    }

    //public URLSessionDownloadTask(URLSession session, URLRequest request, 
    //int taskIdentifier) {
    /*
        Operation operation = new BlockOperation(() -> {
                            sendHttpRequest(request, null, completionHandler, null);

        });
        
        super(session, request, taskIdentifier, operation);*/
    //}
    @Override
    protected void download(InputStream source,
                            OutputStream destination,
                            long contentSize)
            throws IOException {

        final int BUFFER_SIZE = 100 * 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytes = 0;
        int totalBytes = 0;

        if (this.getSession().getDelegate() instanceof URLSessionDownloadDelegate) {
            URLSessionDownloadDelegate delegate = null;

            delegate = (URLSessionDownloadDelegate) getSession().getDelegate();

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

    private File downloadStreamInFile(InputStream inStream,
            long contentLength)
            throws IOException {

        FileOutputStream outStream = null;
        File tempFile = null;

        tempFile = File.createTempFile("tmp", ".tmp");
        outStream = new FileOutputStream(tempFile);
        download(inStream, outStream, contentLength);
        outStream.close();

        return tempFile;
    }

    private void sendHttpRequest(DownloadTaskCompletion completionHandler) {
        HttpURLConnection urlConnection = null;
        File tempFile = null;
        InputStream inStream = null;
        URL newURL = null;
        int respCode = -1;
        Error error = null;
        URLRequest request = null;
        URLResponse resp = null;
        long contentLength = -1;

        request = getRequest();
        try {
            urlConnection = (HttpURLConnection) request.getUrl().openConnection();

            // Set configuration
            configureHTTPURLConnection(urlConnection);

            sendBody(urlConnection);

            respCode = urlConnection.getResponseCode();
            if (respCode == HttpURLConnection.HTTP_OK) {
                inStream = urlConnection.getInputStream();
                try {
                    contentLength = urlConnection.getContentLengthLong();
                } catch (NoSuchMethodError e) {
                    contentLength = urlConnection.getContentLength();
                }
                tempFile = downloadStreamInFile(inStream, contentLength);
                inStream.close();
                newURL = tempFile.toURI().toURL();
            }/*else{
                inStream = urlConnection.getErrorStream();
                try {
                    contentLength = urlConnection.getContentLengthLong();
                } catch (NoSuchMethodError e) {
                    contentLength = urlConnection.getContentLength();
                }
                data = downloadStreamInData(inStream);
                inStream.close();
            }*/

            resp = new HTTPURLResponse(request.getUrl(), respCode);

        } catch (IOException ex) {
            error = new Error();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        
        if (completionHandler != null) {
            completionHandler.run(newURL, resp, error);
        }

        /*
        // Try remove temporary file before call the completion handler 
        if (tempFile != null) {
            tempFile.delete();
        }
        */
        
    }

    /*
    public URLSessionDownloadTask(OperationQueue newWQ, Operation newOperation) {
        super(newWQ, newOperation);
    }
     */
    @Override
    public void resume() {
        Operation operation = new BlockOperation(() -> {
            sendHttpRequest(completionHandler);
        });

        queue.addOperation(operation);
    }
}
