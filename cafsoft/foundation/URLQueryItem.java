/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ceaufres
 */
public class URLQueryItem {

    private String name = "";
    private String value = "";

    public URLQueryItem(String newName, String newValue) {
        name = newName;
        value = newValue;
    }

    
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        String encValue = "";
        
        try {
            encValue = URLEncoder.encode(value, "utf-8");
            encValue = encValue.replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException ex) {
            encValue = value;
        }
        
        System.out.println(name + ":" + encValue);
        return name + "=" + encValue;
    }
    
    
    
}
