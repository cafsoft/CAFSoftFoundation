/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

import java.util.concurrent.ExecutorService;

/**
 *
 * @author ceaufres
 */
public class URLSessionDataTask extends URLSessionTask{
    
    public URLSessionDataTask(ExecutorService newWorkQueue, Runnable r) {
        super(newWorkQueue, r);
    }
    
}
