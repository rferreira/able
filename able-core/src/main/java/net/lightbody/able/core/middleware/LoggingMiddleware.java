package net.lightbody.able.core.middleware;

import net.lightbody.able.core.Request;
import net.lightbody.able.core.Response;
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
        LOG.info(req.toString());
    }

    @Override
    public void process(Response resp) {
        LOG.info(resp.toString());
    }
}
