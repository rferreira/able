package net.lightbody.able.core.http;

/**
 * Date: 5/21/12
 * Time: 9:08 PM
 */
public class ResponseBadRequest extends Response {
    public ResponseBadRequest() {
        super(400,"Bad Request", DEFAULT_CONTENT_TYPE);
    }
}
