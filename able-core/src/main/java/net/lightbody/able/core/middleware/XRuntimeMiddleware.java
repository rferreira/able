package net.lightbody.able.core.middleware;

import net.lightbody.able.core.http.Request;
import net.lightbody.able.core.http.Response;
import net.lightbody.able.core.http.XHeaders;

/**
 * Created with IntelliJ IDEA.
 * User: raferrei
 * Date: 4/24/12
 * Time: 7:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class XRuntimeMiddleware extends Middleware {

    // this works since Middleware is stateless
    long start;

    @Override
    public void process(Request req) {
        start = System.currentTimeMillis();
    }

    @Override
    public void process(Response resp) {
        resp.HEADERS.put(XHeaders.X_RUNTIME, String.valueOf(System.currentTimeMillis() - start));
    }
}
