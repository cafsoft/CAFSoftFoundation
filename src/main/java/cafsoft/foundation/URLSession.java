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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author ceaufres
 */
public class URLSession {

    //private int nextTaskIdentifier = 1;
    private ExecutorService workQueue = null;

    private static URLSession shared = null;

    private URLSessionConfiguration configuration = null;

    public URLSession(URLSessionConfiguration configuration) {

        this.configuration = configuration;
        workQueue = Executors.newSingleThreadExecutor();
        //workQueue = Executors.newFixedThreadPool(1);
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
    private void sendGETRequest(URLRequest req,
            DataTaskCompletionHandler completionHandler) {

        HttpURLConnection httpURLConnection = null;
        int respCode = -1;
        InputStream inStream = null;
        Data data = null;
        Error error = null;

        try {
            httpURLConnection = (HttpURLConnection) req.getUrl().openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod(req.getHttpMethod());

            // Java 7 compatible code
            Set<Map.Entry<String, String>> allHeaders;
            allHeaders = req.getAllHttpHeaderFields().entrySet();
            for (Map.Entry<String, String> header : allHeaders) {
                String key = header.getKey();
                String value = header.getValue();
                httpURLConnection.addRequestProperty(key, value);
            }

            /*
        //Java 8 compatible code (Lambda expressions
        )
                        request.getAllHttpHeaderFields().forEach((value, field) -> {
            httpConn.addRequestProperty(field, value);
        });
             */
            httpURLConnection.setConnectTimeout(configuration.getConnectTimeout());
            httpURLConnection.setReadTimeout(configuration.getReadTimeout());
            //System.out.println(httpURLConnection.getConnectTimeout());
            //System.out.println(httpURLConnection.getReadTimeout());

            if (req.getHttpMethod().equals("POST")) {
                if (!req.getHttpBody().isEmpty()) {
                    OutputStream os = httpURLConnection.getOutputStream();
                    os.write(req.getHttpBody().getBytes());
                    os.flush();
                }
            }

            respCode = httpURLConnection.getResponseCode();
            if (respCode == HttpURLConnection.HTTP_OK) {
                inStream = httpURLConnection.getInputStream();
                data = new Data(inStream);
                inStream.close();
            } else {
                error = new Error();
            }

            httpURLConnection.disconnect();

        } catch (IOException e) {
        }

        if (completionHandler != null) {
            HTTPURLResponse resp = null;
            resp = new HTTPURLResponse(req.getUrl(), respCode);

            completionHandler.exec(data, resp, error);
        }
    }

    private void sendHttpRequest(URLRequest request,
            DataTaskCompletionHandler completionHandler) {

        HttpURLConnection conn = null;
        int respCode = -1;
        InputStream inStream = null;
        Data data = null;
        Error error = null;

        try {
            conn = (HttpURLConnection) request.getUrl().openConnection();
            if (request.getHttpMethod().equals("POST")) {
                conn.setDoOutput(true);
                conn.setRequestMethod(request.getHttpMethod());

                // Java 7 compatible code
                Set<Map.Entry<String, String>> allHeaders;
                allHeaders = request.getAllHttpHeaderFields().entrySet();
                for (Map.Entry<String, String> header : allHeaders) {
                    String key = header.getKey();
                    String value = header.getValue();
                    conn.addRequestProperty(key, value);
                }

                ////Java 8 compatible code (Lambda expressions
                //request.getAllHttpHeaderFields().forEach((value, field) -> {
                //    httpConn.addRequestProperty(field, value);
                //});
                conn.setConnectTimeout(configuration.getConnectTimeout());
                conn.setReadTimeout(configuration.getReadTimeout());

                if (!request.getHttpBody().isEmpty()) {
                    OutputStream os = conn.getOutputStream();
                    os.write(request.getHttpBody().getBytes());
                    os.flush();
                }

            } else { // Method GET
                conn.setRequestMethod(request.getHttpMethod());
                conn.setConnectTimeout(configuration.getConnectTimeout());
                conn.setReadTimeout(configuration.getReadTimeout());
            }

            respCode = conn.getResponseCode();
            if (respCode == HttpURLConnection.HTTP_OK) {
                inStream = conn.getInputStream();
                data = new Data(inStream);
                inStream.close();
            } else {
                error = new Error();
            }

        } catch (IOException e) {
        }

        if (conn != null) {
            conn.disconnect();
        }

        if (completionHandler != null) {
            HTTPURLResponse resp = null;
            resp = new HTTPURLResponse(request.getUrl(), respCode);

            completionHandler.exec(data, resp, error);
        }
    }

    public URLSessionDataTask dataTask(URLRequest request,
            DataTaskCompletionHandler completionHandler) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String method = request.getHttpMethod();

                if (method.equals("GET") || method.equals("POST")) {
                    sendHttpRequest(request, completionHandler);
                }
            }
        };

        return new URLSessionDataTask(workQueue, runnable);
    }

    /*
    public URLSessionDataTask dataTask(URLRequest req,
            DataTaskCompletionHandler completionHandler) {

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                Data data = null;
                URLConnection conn = null;
                InputStream inStream = null;
                int respCode = -1;
                Error error = null;

                try {
                    conn = req.getUrl().openConnection();
                    if (conn instanceof HttpURLConnection) {
                        HttpURLConnection httpConn = (HttpURLConnection) conn;

                        respCode = httpConn.getResponseCode();

                        httpConn.setRequestMethod(req.getHttpMethod());

                        // Java 7 compatible code
                        Set<Map.Entry<String, String>> allHeaders;
                        allHeaders = req.getAllHttpHeaderFields().entrySet();
                        for (Map.Entry<String, String> header : allHeaders) {
                            String key = header.getKey();
                            String value = header.getValue();
                            httpConn.addRequestProperty(key, value);
                        }

                        
                        //// Java 8 compatible code (Lambda expressions)
                        //request.getAllHttpHeaderFields().forEach((value, field) -> {
                        //    httpConn.addRequestProperty(field, value);
                        //});
                        
                        httpConn.setConnectTimeout(configuration.getConnectTimeout());
                        httpConn.setReadTimeout(configuration.getReadTimeout());
                        //httpConn.setAllowUserInteraction(false);
                        //httpConn.setInstanceFollowRedirects(true);
                        //httpConn.setDoInput(true);
                        //httpConn.setDoOutput(true);

                        if (req.getHttpMethod().equals("POST")) {
                            if (!req.getHttpBody().isEmpty()) {
                                httpConn.setDoOutput(true);
                                OutputStream os = httpConn.getOutputStream();
                                os.write(req.getHttpBody().getBytes());
                                os.flush();
                            }
                        }

                        if (respCode == HttpURLConnection.HTTP_OK) {
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
                    //e.printStackTrace();
                }

                if (completionHandler != null) {
                    HTTPURLResponse resp = null;
                    resp = new HTTPURLResponse(req.getUrl(), respCode);

                    completionHandler.run(data, resp, error);
                }
            }
        };

        return new URLSessionDataTask(workQueue, runnable);
    }
     */
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
