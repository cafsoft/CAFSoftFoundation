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
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author ceaufres
 */
public class URLSession {

    private ExecutorService workQueue = null;

    private static URLSession shared = null;

    private URLSession() {
        workQueue = Executors.newSingleThreadExecutor();
    }

    public static URLSession getShared() {
        if (shared == null) {
            shared = new URLSession();
        }
        return shared;
    }

    public URLSessionDataTask dataTask(URLRequest request, 
            DataTaskCompletionHandler completionHandler) {
        
        Runnable r = () -> {
            Data data = null;
            URLConnection conn = null;
            InputStream inStream = null;
            int responseCode = -1;
            Error error = null; 

            // System.out.println("Start");
            
            try {
                conn = request.getUrl().openConnection();
                if (conn instanceof HttpURLConnection) {
                    HttpURLConnection httpConn = (HttpURLConnection) conn;
                    httpConn.setRequestMethod(request.getHttpMethod());
                    
                    request.getAllHttpHeaderFields().forEach((value, field) -> {
                        httpConn.addRequestProperty(field, value);
                    });
                                        
                    httpConn.setReadTimeout(30000);
                    httpConn.setConnectTimeout(15000);
                    httpConn.setAllowUserInteraction(false);
                    httpConn.setInstanceFollowRedirects(true);
                    //httpConn.setDoInput(true);
                    httpConn.setDoOutput(true);

                    OutputStream os = httpConn.getOutputStream();
                    os.write(request.getHttpBody().getBytes());
                    os.flush();

                    responseCode = httpConn.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                       
                        inStream = httpConn.getInputStream();

                        data = new Data(inStream);

                        inStream.close();
                    }else{
                        error = new Error();
                    }
                    httpConn.disconnect();
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            HTTPURLResponse response = new HTTPURLResponse(request.getUrl(), responseCode);

            
          
            completionHandler.run(data, response, error);            
        };

        return new URLSessionDataTask(r);
    }
}
