package net.lightbody.able.core.routes;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import net.lightbody.able.core.Request;
import net.lightbody.able.core.Response;
import net.lightbody.able.core.middleware.Middleware;
import net.lightbody.able.core.middleware.MiddlewareManager;
import net.lightbody.able.core.util.Log;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: raferrei
 * Date: 4/23/12
 * Time: 8:48 AM
 * To change this template use File | Settings | File Templates.
 */

@Singleton
public class Router {

    @Inject
    MiddlewareManager mm;

    private static Log LOG = new Log();

    public void fire(Request req, Response res) throws IOException {

        Preconditions.checkNotNull(req);
        Preconditions.checkNotNull(res);

        // Handle middelware
        List<Middleware> middlewares = mm.getMiddlewareList();

        for(Middleware m: middlewares)  {
            m.process(req);
        }


        LOG.info("routing: " + req.getPath());
        LOG.info(req.getBody());

        res.code = 404;
        //res.setContent("WAT?".getBytes());

        for(Middleware m: middlewares)  {
            m.process(res);
        }

    }
}
