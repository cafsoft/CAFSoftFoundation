/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author ceaufres
 */
public class URLSession {

    private ExecutorService workQueue = null;

    private static URLSession shared = null;

    private URLSessionConfiguration configuration = null;

    public URLSession(URLSessionConfiguration configuration) {

        this.configuration = configuration;
        workQueue = Executors.newSingleThreadExecutor();
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

    public URLSessionDataTask dataTask(URLRequest request,
            DataTaskCompletionHandler completionHandler) {

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                Data data = null;
                URLConnection conn = null;
                InputStream inStream = null;
                int responseCode = -1;
                Error error = null;

                try {
                    conn = request.getUrl().openConnection();
                    if (conn instanceof HttpURLConnection) {
                        HttpURLConnection httpConn = (HttpURLConnection) conn;
                        httpConn.setRequestMethod(request.getHttpMethod());

                        // Java 7 compatible code
                        HashMap<String, String> headerFields;
                        headerFields = request.getAllHttpHeaderFields();
                        for (Map.Entry<String, String> entry : headerFields.entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue();
                            httpConn.addRequestProperty(key, value);
                        }

                        /*  Java 8 compatible code (Lambda expressions)
                        request.getAllHttpHeaderFields().forEach((value, field) -> {
                            httpConn.addRequestProperty(field, value);
                        });
                         */
                        httpConn.setConnectTimeout(configuration.getConnectTimeout());
                        httpConn.setReadTimeout(configuration.getReadTimeout());
                        httpConn.setAllowUserInteraction(false);
                        httpConn.setInstanceFollowRedirects(true);
                        //httpConn.setDoInput(true);
                        httpConn.setDoOutput(true);

                        if (!request.getHttpBody().isEmpty()) {
                            OutputStream os = httpConn.getOutputStream();
                            os.write(request.getHttpBody().getBytes());
                            os.flush();
                        }

                        responseCode = httpConn.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_OK) {

                            inStream = httpConn.getInputStream();

                            data = new Data(inStream);

                            inStream.close();
                        } else {
                            error = new Error();
                        }
                        httpConn.disconnect();
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (completionHandler != null) {
                    HTTPURLResponse response = null;
                    response = new HTTPURLResponse(request.getUrl(), responseCode);

                    completionHandler.run(data, response, error);
                }
            }
        };

        return new URLSessionDataTask(runnable);
    }

    public URLSessionDataTask dataTask(URLRequest request) {

        return dataTask(request, null);
    }

    public URLSessionDataTask dataTask(URL url) {

        return dataTask(new URLRequest(url), null);
    }

    public URLSessionDataTask dataTask(URL url,
            DataTaskCompletionHandler completionHandler) {

        return dataTask(new URLRequest(url), completionHandler);
    }
}
