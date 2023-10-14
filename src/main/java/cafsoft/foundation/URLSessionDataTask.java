/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

import cafsoft.foundation.URLSession.DataTaskCompletion;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.ByteBuffer;

/**
 *
 * @author ceaufres
 */
public class URLSessionDataTask extends URLSessionTask {

    private DataTaskCompletion completionHandler = null;
    private OperationQueue queue = null;

    public URLSessionDataTask(URLSession session, 
            URLRequest request,
            int taskIdentifier, OperationQueue newWQ,
            DataTaskCompletion completionHandler) {

        super(session, request, taskIdentifier);

        this.completionHandler = completionHandler;
        this.queue = newWQ;
    }

    // Read bytes from connection and save its in memory block
    private Data downloadStreamInData(InputStream inStream)
            throws IOException {

        ByteArrayOutputStream outStream = null;
        ByteBuffer byteData = null;

        outStream = new ByteArrayOutputStream(); // (buffer.length)
        transfer(inStream, outStream, -1, null);
        byteData = ByteBuffer.wrap(outStream.toByteArray());
        outStream.close();

        return new Data(byteData);
    }

    private void sendHttpRequest(DataTaskCompletion completionHandler) {

        HttpURLConnection urlConnection = null;
        Data data = null;
        InputStream inStream = null;
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
                data = downloadStreamInData(inStream);
                inStream.close();
            }else{
                inStream = urlConnection.getErrorStream();
                try {
                    contentLength = urlConnection.getContentLengthLong();
                } catch (NoSuchMethodError e) {
                    contentLength = urlConnection.getContentLength();
                }
                data = downloadStreamInData(inStream);
                inStream.close();
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
            completionHandler.run(data, resp, error);
        }
    }

    @Override
    public void resume() {
        Operation operation = new BlockOperation(() -> {
            sendHttpRequest(completionHandler);
        });

        queue.addOperation(operation);
    }

}
