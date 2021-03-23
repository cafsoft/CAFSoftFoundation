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
public class URLSessionUploadTask extends URLSessionDataTask {
    
    public URLSessionUploadTask(OperationQueue newWorkQueue, Operation newOperation) {
        super(newWorkQueue, newOperation);
    }
    
}
