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
public interface URLSessionDownloadDelegate
        extends URLSessionDelegate {

    public void urlSession(URLSession session,
            URLSessionDownloadTask downloadTask,
            long bytesWritten, long totalBytesWritten,
            long totalBytesExpectedToWrite);
    
}
