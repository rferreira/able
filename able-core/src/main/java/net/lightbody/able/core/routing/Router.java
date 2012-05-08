package net.lightbody.able.core.routing;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import jregex.Matcher;
import net.lightbody.able.core.Request;
import net.lightbody.able.core.Response;
import net.lightbody.able.core.View;
import net.lightbody.able.core.middleware.Middleware;
import net.lightbody.able.core.util.Log;
import net.lightbody.able.core.views.DefaultView;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Date: Apr 28, 2012
 * Time: 2:13:44 PM
 */
@Singleton
public class Router {

    private List<Route> routes = new CopyOnWriteArrayList<Route>();
    public List<Class> middlewares = new CopyOnWriteArrayList<Class>();

    @Inject
    Injector injector;

    @Inject
    DefaultView defaultView;

    private static Log LOG = new Log();

    public void route(String regex, Class clazz) {
        routes.add(
                new Route(regex, clazz)
        );
    }

    private View resolve(Request request) {
        Matcher m;
        for (Route route : routes) {
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
        List<Middleware> middlewares = getMiddlewareClasses();

        for (Middleware m : middlewares) {
            m.process(req);
        }

        LOG.fine("routing: " + req.getPath());

        View view = resolve(req);

        if (view != null) {
            res = view.dispatch(req);
        } else {
            LOG.fine("could not find a proper view to render this request - returning 404");
            res = defaultView.dispatch(req);

        }

       // post middleware processing 
        for (Middleware m : middlewares) {
            m.process(res);
        }

        return res;

    }

    /**
     * returns the reverse of an route - prob could use some speed up.
     *
     * @param clazz
     * @return
     */
    public String reverse(Class clazz) {
        for (Route r : routes) {
            if (r.getClazz().equals(clazz)) {
                return r.getRegex();
            }

        }

        return null;
    }

    private List<Middleware> getMiddlewareClasses() {
        List<Middleware> r = Lists.newArrayList();
        for (Class z : middlewares) {
            try {
                r.add((Middleware) injector.getInstance(z));
            } catch (Exception e) {
                new RuntimeException(e);
            }
        }
        return r;
    }
}
