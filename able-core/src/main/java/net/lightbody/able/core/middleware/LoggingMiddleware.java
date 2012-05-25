package net.lightbody.able.core.middleware;

import net.lightbody.able.core.http.Request;
import net.lightbody.able.core.http.Response;
import net.lightbody.able.core.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: raferrei
 * Date: 4/21/12
 * Time: 12:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class LoggingMiddleware extends Middleware {
    private static final Log LOG = new Log();

    @Override
    public void process(Request req) {
        System.out.println(req.toString());
    }

    @Override
    public void process(Response resp) {
        System.out.println(resp.toString());
    }
}
