package net.lightbody.able.core;

/**
 * Date: Apr 29, 2012
 * Time: 7:24:25 PM
 */
public class ResponseNotFound extends Response {
    public ResponseNotFound() {
        super(404,"# 404 not found\n" , "text/html");
    }
}
