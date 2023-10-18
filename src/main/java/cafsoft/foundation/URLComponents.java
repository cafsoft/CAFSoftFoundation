/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafsoft.foundation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ceaufres
 */
public class URLComponents {

    // A basic URL syntax can be generalized as:
    // scheme:[//[user:password@]host[:port]][/]path[?query][#fragment]
    private final String SCHEME = "(http|https|file)";
    private final String USER = "([^:@\\/\\?#]+)";
    private final String PASSWORD = "([^@\\/\\?#]+)?";
    private final String HOST = "([^:\\/?#]+)?";
    private final String PORT = "(?::(\\d+))?";
    private final String PATH = "(\\/[^?#]*)?";
    private final String QUERY = "(?:\\?([^#]*))?";
    private final String FRAGMENT = "(?:#(.*))?";

    private String scheme;
    private String host;
    private int port = -1;
    private String user;
    private String password;
    private String path;
    //private String query;
    private ArrayList<URLQueryItem> queryItems = new ArrayList<>();
    private String fragment;

    private final String REGEXP = "^" + SCHEME + ":\\/\\/" + "(?:" + USER + ":" + PASSWORD + "@)?" + HOST + PORT + PATH + QUERY + FRAGMENT + "$";


    public URLComponents() {
        super();
    }

    public URLComponents(String stringURL) {
        Pattern pattern = Pattern.compile(REGEXP);
        Matcher matcher = pattern.matcher(stringURL);
        String query = "";

        if (matcher.matches()) {
            scheme = matcher.group(1);
            user = matcher.group(2);
            password = matcher.group(3);
            host = matcher.group(4);
            if (host == null)
                host = "";
            try{
                String strPort = matcher.group(5);
                port = Integer.parseInt(strPort);
            }catch (NumberFormatException e){
                port = -1;
            }
            path = matcher.group(6);
            query = matcher.group(7);
            fragment = matcher.group(8);

            if (query != null) {
                String[] params = query.split("&");
                queryItems = new ArrayList<>();
                for (int k = 0; k < params.length; k++) {
                    String[] values = params[k].split("=");
                    queryItems.add(new URLQueryItem(values[0], values[1]));
                }
            }else
                query = "";
        } else {
            throw new IllegalArgumentException("Invalid URL format");
        }
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
        if ((user != null) && !user.isEmpty()) {
            sb.append(user);
            if ((password != null) && (password.isEmpty())) {
                sb.append(":");
                sb.append(password);
            }
            sb.append("@");
        }

        // host:port
        if (host != null) {
            sb.append(host);
            if (port != -1) {
                sb.append(":");
                sb.append(port);
            }
        }

        // path
        if ((path !=null) && !path.isEmpty()) {
            sb.append(path);
        }

        // query
        if (queryItems != null & queryItems.size() > 0) {
            for (URLQueryItem queryItem : queryItems) {
                sj.add(queryItem.toString());
            }
            sb.append("?");
            sb.append(sj.toString());
        }

        // fragment
        if ((fragment != null) && !fragment.isEmpty()) {
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
    public ArrayList<URLQueryItem> getQueryItems() {
        return queryItems;
    }

    /**
     * @param queryItems the queryItems to set
     */
    public void setQueryItems(URLQueryItem[] queryItems) {
        this.queryItems = new ArrayList<>();
        this.queryItems.addAll(Arrays.asList(queryItems));
    }

    public String getQuery(){
        if ((queryItems != null) && !queryItems.isEmpty()) {
            StringJoiner sj = new StringJoiner("&");
            for (URLQueryItem queryItem : queryItems) {
                sj.add(queryItem.toString());
            }

            return sj.toString();
        }

        return null;
    }

    public void setQuery(String newQuery){
        if (newQuery != null) {
            String[] params = newQuery.split("&");
            queryItems = new ArrayList<>();
            for (int k = 0; k < params.length; k++) {
                String[] values = params[k].split("=");
                queryItems.add(new URLQueryItem(values[0], values[1]));
            }
        }
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
        return (path != null) ? path : "";
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

}
