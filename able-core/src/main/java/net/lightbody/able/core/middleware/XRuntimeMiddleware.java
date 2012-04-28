package net.lightbody.able.core.middleware;

import com.google.inject.Singleton;
import net.lightbody.able.core.Request;
import net.lightbody.able.core.Response;

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
        resp.HEADERS.put("X-RUNTIME", String.valueOf(System.currentTimeMillis() - start));
    }
}
