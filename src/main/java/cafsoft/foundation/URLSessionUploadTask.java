/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

import cafsoft.foundation.URLSession.DataTaskCompletion;
import cafsoft.foundation.URLSession.UploadTaskCompletion;
import static cafsoft.foundation.URLSession.addRequestProperties;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 *
 * @author ceaufres
 */
public class URLSessionUploadTask extends URLSessionTask {

    private DataTaskCompletion completionHandler = null;
    private OperationQueue queue = null;
    private Data bodyData = null;

    public URLSessionUploadTask(URLSession session, URLRequest request,
            int taskIdentifier, Data bodyData, OperationQueue newWQ,
            DataTaskCompletion completionHandler) {

        super(session, request, taskIdentifier);

        this.bodyData = bodyData;
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

    private void sendHttpRequest(URLRequest request, Data bodyData,
            DataTaskCompletion completionHandler) {

        HttpURLConnection urlConnection = null;
        Data data = null;
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

        try {
            url = request.getUrl();
            urlConnection = (HttpURLConnection) url.openConnection();

            // Set configuration
            urlConnection.setRequestMethod(request.getHttpMethod());
            addRequestProperties(urlConnection, request);
            
            session = getSession();
            configuration = session.getConfiguration();

            urlConnection.setConnectTimeout(configuration.getConnectTimeout());
            urlConnection.setReadTimeout(configuration.getReadTimeout());
            if (request.getHttpBody() != null) {
                urlConnection.setDoOutput(true);
                outStream = urlConnection.getOutputStream();
                uploadStream(outStream, request.getHttpBody().toBytes());

            } else if (bodyData != null) {
                urlConnection.setDoOutput(true);
                outStream = urlConnection.getOutputStream();
                uploadStream(outStream, bodyData.toBytes());
            }

            respCode = urlConnection.getResponseCode();
            if (respCode == HttpURLConnection.HTTP_OK) {
                inStream = urlConnection.getInputStream();
                contentLength = urlConnection.getContentLengthLong();
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
            sendHttpRequest(getRequest(), bodyData, completionHandler);
        });

        queue.addOperation(operation);
    }

}
