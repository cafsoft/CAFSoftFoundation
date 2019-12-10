/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

import java.net.URL;
import java.util.HashMap;

/**
 *
 * @author ceaufres
 */
public class URLRequest{
    private URL url = null;
    private String httpMethod = "GET";
    private String httpBody = "";
    private HashMap<String, String> httpHeaderFields = new HashMap<>();

    public URLRequest(URL url){
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }
    
    
    
    public void setHttpMethod(String newHttpMethod){
        httpMethod = newHttpMethod;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpBody(String httpBody) {
        this.httpBody = httpBody;
    }

    public String getHttpBody() {
        return httpBody;
    }

/*    
    public void setValue(String value, String field){
    }
*/
    
    public void addValue(String value, String field){
        httpHeaderFields.put(field, value);
    }
    
    public HashMap<String, String> getAllHttpHeaderFields(){
        return httpHeaderFields;
    }
    
}
