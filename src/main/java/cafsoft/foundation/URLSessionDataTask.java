/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

import cafsoft.foundation.URLSession.DataTaskCompletion;
//import static cafsoft.foundation.URLSession.addRequestProperties;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
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

    private void sendHttpRequest(URLRequest request, 
            Data bodyData,
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
        // URLSession session = null;
        URLSessionConfiguration configuration = null;

        try {
            url = request.getUrl();
            urlConnection = (HttpURLConnection) url.openConnection();

            //session = getSession();
            configuration = getSession().getConfiguration();

            // Set configuration
            if (urlConnection instanceof HttpsURLConnection) {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) urlConnection;
                SSLContext context = configuration.getSSLContext();
                if (context != null){
                    httpsURLConnection.setSSLSocketFactory(context.getSocketFactory());
                }
            }
            urlConnection.setRequestMethod(request.getHttpMethod());
            URLSession.addRequestProperties(urlConnection, request);

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
            sendHttpRequest(getRequest(), null, completionHandler);
        });

        queue.addOperation(operation);
    }

}
