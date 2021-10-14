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
    private File downloadStreamInFile(InputStream inStream,
            long contentLength, 
            URLSessionDownloadTask downloadTask)
            throws IOException {

        FileOutputStream outStream = null;
        File tempFile = null;

        tempFile = File.createTempFile("tmp", ".tmp");
        outStream = new FileOutputStream(tempFile);
        transfer(inStream, outStream, contentLength, downloadTask);
        outStream.close();

        return tempFile;
    }

    private void sendHttpRequest(Data bodyData,
            DownloadTaskCompletion completionHandler,
            URLSessionDownloadTask downloadTask) {

        HttpURLConnection urlConnection = null;
        File tempFile = null;
        InputStream inStream = null;
        OutputStream outStream = null;
        URL url = null;
        URL newURL = null;
        int respCode = -1;
        Error error = null;
        URLResponse resp = null;
        long contentLength = -1;
        URLSession session = null;
        URLSessionConfiguration configuration = null;
        URLRequest request = null;

        request = getRequest();
        try {
            url = request.getUrl();
            urlConnection = (HttpURLConnection) url.openConnection();

            // Set configuration
            //urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(request.getHttpMethod());
            URLSession.addRequestProperties(urlConnection, request);

            session = getSession();
            configuration = session.getConfiguration();

            urlConnection.setConnectTimeout(configuration.getConnectTimeout());
            urlConnection.setReadTimeout(configuration.getReadTimeout());

            if (!request.getHttpBody().isEmpty()) {
                urlConnection.setDoOutput(true);
                outStream = urlConnection.getOutputStream();
                uploadStream(outStream, request.getHttpBody().getBytes());

            } else if (bodyData != null) {
                urlConnection.setDoOutput(true);
                outStream = urlConnection.getOutputStream();
                uploadStream(outStream, bodyData.toBytes());
            }

            respCode = urlConnection.getResponseCode();
            if (respCode == HttpURLConnection.HTTP_OK) {
                inStream = urlConnection.getInputStream();
                try {
                    contentLength = urlConnection.getContentLengthLong();
                } catch (UnsupportedOperationException e) {
                    contentLength = urlConnection.getContentLength();
                }
                tempFile = downloadStreamInFile(inStream, contentLength, downloadTask);
                inStream.close();
                newURL = tempFile.toURI().toURL();
            }

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
            sendHttpRequest(null, completionHandler, this);
        });

        queue.addOperation(operation);

    }
}
