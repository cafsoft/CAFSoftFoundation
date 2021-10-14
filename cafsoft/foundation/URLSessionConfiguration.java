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
public class URLSessionConfiguration {

    private int connectTimeout = 0;
    private int readTimeout = 0;

    //private static URLSessionConfiguration _default = null;
    
    /**
     * @return the connectTimeout
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * @param connectTimeout the connectTimeout to set
     */
    public void setConnectTimeout(int connectTimeout) {
        if (connectTimeout >= 0) {
            this.connectTimeout = connectTimeout;
        }
    }

    /**
     * @return the readTimeout
     */
    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * @param readTimeout the readTimeout to set
     */
    public void setReadTimeout(int readTimeout) {
        if (readTimeout >= 0) {
            this.readTimeout = readTimeout;
        }
    }

    /*
    public static URLSessionConfiguration getDefault() {
        return _default;
    }
    */
}
