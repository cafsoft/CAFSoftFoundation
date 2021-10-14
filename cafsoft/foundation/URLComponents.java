/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringJoiner;

/**
 *
 * @author ceaufres
 */
public class URLComponents {

    // A basic URL syntax can be generalized as:
    // scheme:[//[user:password@]host[:port]][/]path[?query][#fragment]
    private String scheme = "";
    private String user = "";
    private String password = "";
    private String host = "";
    private int port = -1;
    private String path = "";
    private URLQueryItem[] queryItems = {};
    private String fragment = "";

    public URLComponents() {
        super();
    }

    public URLComponents(String stringURL) {
        String[] parts = {};
        String _scheme = null;
        String _fragment = null;
        String[] userPassword = {};
        String[] hostPort = {};
        String host = "";
        String port = "";
        String user = null;
        String password = null;
        String path = "";
        String queryFragment = null;
        String query = null;

        parts = stringURL.split("://", 2);
        if (parts[0].equals("http") || parts[0].equals("https")) {
            _scheme = parts[0];

            if (parts[1].contains("@")) {
                parts = parts[1].split("@", 2);
                userPassword = parts[0].split(":");
                user = userPassword[0];
                password = userPassword[1];
            }

            if (parts[1].contains("#")) {
                parts = parts[1].split("#", 2);
                _fragment = parts[1];
                parts[1] = parts[0];
            }

            if (parts[1].contains("?")) {
                parts = parts[1].split("\\?", 2);
            }

            System.out.println("parts[1] = " + parts[1]);

            if (parts[0].contains("/")) {
                parts = parts[0].split("/", 2);
                path = "/" + parts[1];
            }

            if (parts[0].contains(":")) {
                hostPort = parts[0].split(":", 2);
                host = hostPort[0];
                port = hostPort[1];
            }

            /*
            parts = parts[1].split("\\?");

            queryFragment = parts[1];

            parts = queryFragment.split("#");
            query = parts[0];
            _fragment = parts[1];
             */
        }
        

        System.out.println("scheme = " + _scheme);
        System.out.println("user = " + user);
        System.out.println("password = " + password);
        System.out.println("host = " + host);
        System.out.println("port = " + port);
        System.out.println("path = " + path);

        System.out.println(query);

        System.out.println("fragment = " + _fragment);

    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String newScheme) {
        this.scheme = newScheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String newHost) {
        this.host = newHost;
    }

    public URL getURL() {
        URL url = null;
        StringBuilder sb = new StringBuilder();
        StringJoiner sj = new StringJoiner("&");

        // scheme
        sb.append(scheme);
        sb.append("://");

        // user:password
        if (!user.isEmpty()) {
            sb.append(user);
            if (!password.isEmpty()) {
                sb.append(":");
                sb.append(password);
            }
            sb.append("@");
        }

        // host:port
        sb.append(host);
        if (port != -1) {
            sb.append(":");
            sb.append(port);
        }

        // path
        if (!path.isEmpty()) {
            sb.append(path);
        }

        // query
        if (queryItems.length > 0) {
            for (URLQueryItem queryItem : queryItems) {
                sj.add(queryItem.toString());
            }
            sb.append("?");
            sb.append(sj.toString());
        }

        // fragment
        if (!fragment.isEmpty()) {
            sb.append("#");
            sb.append(fragment);
        }

        //System.out.println(sb.toString());
        try {
            url = new URL(sb.toString());
        } catch (MalformedURLException ex) {

        }

        return url;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param newUser the user to set
     */
    public void setUser(String newUser) {
        this.user = newUser;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param newPassword the password to set
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * @return the fragment
     */
    public String getFragment() {
        return fragment;
    }

    /**
     * @param fragment the fragment to set
     */
    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    /**
     * @return the queryItems
     */
    public URLQueryItem[] getQueryItems() {
        return queryItems;
    }

    /**
     * @param queryItems the queryItems to set
     */
    public void setQueryItems(URLQueryItem[] queryItems) {
        this.queryItems = queryItems;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

}
