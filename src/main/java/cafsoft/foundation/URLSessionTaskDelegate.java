/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

/**
 *
 * @author ceaufres
 */
public interface URLSessionTaskDelegate
        extends URLSessionDelegate{
    default void urlSession(URLSession session,
                    URLSessionTask task,
                    long bytesSent, long totalBytesSent,
                    long totalBytesExpectedToSend){
    }
}
