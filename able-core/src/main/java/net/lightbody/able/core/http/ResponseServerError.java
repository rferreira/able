package net.lightbody.able.core.http;

/**
 * Date: 5/21/12
 * Time: 9:05 PM
 */
public class ResponseServerError extends Response {
    public ResponseServerError(String content) {
        super(500,content , "text/html");
    }

    public ResponseServerError() {
        super(500,"Internal Server Error" , "text/html");
    }
}
