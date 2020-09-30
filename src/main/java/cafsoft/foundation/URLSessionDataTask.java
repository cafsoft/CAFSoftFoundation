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
public class URLSessionDataTask extends URLSessionTask{
    
    public URLSessionDataTask(URLSessionTasksQueue newWorkQueue, Runnable r) {
        super(newWorkQueue, r);
    }
    
}
