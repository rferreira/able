package net.lightbody.able.core;

import com.google.common.net.HttpHeaders;

/**
 * Date: Apr 29, 2012
 * Time: 7:33:59 PM
 */
public class ResponseRedirect extends Response {
    public ResponseRedirect(String location) {
        super();
        this.status = 302;
        this.HEADERS.put(HttpHeaders.LOCATION, location );
    }
}
