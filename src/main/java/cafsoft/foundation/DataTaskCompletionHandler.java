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
public interface DataTaskCompletionHandler 
        extends Behavior {
    
    public abstract void run(Data data, URLResponse response, Error error);
    
}
