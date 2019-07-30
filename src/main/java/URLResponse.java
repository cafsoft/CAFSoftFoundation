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
public class URLResponse {
    private URL _URL = null;

    public URLResponse(URL newURL) {
        _URL = newURL;
    }

    public URL getURL() {
        return _URL;
    }   
}
