/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

import java.net.URL;

/**
 *
 * @author ceaufres
 */
public class HTTPURLResponse extends URLResponse {
    private int _statusCode = 0;

    public HTTPURLResponse(URL newURL, int newStatusCode) {
        super(newURL);
        _statusCode = newStatusCode;
    }

    public int getStatusCode() {
        return _statusCode;
    }  
}
