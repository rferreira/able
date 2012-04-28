package net.lightbody.able.core.routing;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import jregex.Matcher;
import jregex.Pattern;
import net.lightbody.able.core.Request;
import net.lightbody.able.core.Response;
import net.lightbody.able.core.View;
import net.lightbody.able.core.middleware.Middleware;
import net.lightbody.able.core.middleware.MiddlewareManager;
import net.lightbody.able.core.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Date: Apr 28, 2012
 * Time: 2:13:44 PM
 */
@Singleton
public class Router {

    CopyOnWriteArrayList<Route> routes = new CopyOnWriteArrayList<Route>();

    @Inject
    Injector injector;
    @Inject
    MiddlewareManager mm;


    private static Log LOG = new Log();

    public void route(String regex, Class clazz) {
        routes.add(
                new Route(new Pattern(regex), clazz)
        );
    }

    private View resolve(Request request) {
        Matcher m;
        for (Route route: routes) {
            m = route.getPattern().matcher(request.getPath());
            if (m.matches()) {
                return (View) injector.getInstance(route.getClazz());
            }
        }

        return null;
    }

    public Response fire(Request req) throws IOException {

        Preconditions.checkNotNull(req);
        Response res;

        // Handle middelware
        List<Middleware> middlewares = mm.getMiddlewareList();

        for(Middleware m: middlewares)  {
            m.process(req);
        }

        LOG.info("routing: " + req.getPath());

        View view = resolve(req);


        if (view == null) {
            LOG.info("could not find a proper view to render this request");
            res = new Response("Page not found\n", 404, "text/html");
        } else {
            res = view.dispatch(req);
        }

        
        for(Middleware m: middlewares)  {
            m.process(res);
        }

        return res;

    }
}
