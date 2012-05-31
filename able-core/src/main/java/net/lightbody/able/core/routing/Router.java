package net.lightbody.able.core.routing;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import net.lightbody.able.core.http.Request;
import net.lightbody.able.core.http.Response;
import net.lightbody.able.core.middleware.Middleware;
import net.lightbody.able.core.util.Log;
import net.lightbody.able.core.views.Default;
import net.lightbody.able.core.views.View;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Date: Apr 28, 2012
 * Time: 2:13:44 PM
 */
@Singleton
public class Router {

    public List<Route> routes = new CopyOnWriteArrayList<Route>();
    public List<Class> middleware = new CopyOnWriteArrayList<Class>();

    @Inject
    Injector injector;

    private static Log LOG = new Log();

    public Route route(String regex, Class clazz) {
        Route r = new Route(regex, clazz);
        routes.add(
                r
        );

        return r;
    }

    private View resolve(Request request) {
        NamedMatcher m;
        NamedPattern p;
        for (Route route : routes) {
            p = route.getPattern();
            m = p.matcher(request.getPath());
            if (m.matches()) {

                // parsing groups:
                if (m.groupCount() > 0) {
                    request.VARS.putAll(m.namedGroups());
                    LOG.fine(request.toString());
                }

                return (View) injector.getInstance(route.getClazz());
            }
        }

        return null;
    }

    public Response fire(Request req) throws IOException {

        Preconditions.checkNotNull(req);
        Response res;

        LOG.fine("routing: " + req.getPath());

        View view = resolve(req);

        // Handle middelware
        List<Middleware> middlewares = getMiddlewareClasses();

        for (Middleware m : middlewares) {
            m.process(req);
        }

        if (view != null) {
            res = view.dispatch(req);
        } else {
            LOG.fine("could not find a proper view to render this request - returning 404");
            res = injector.getInstance(Default.class).dispatch(req);

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
        for (Class z : middleware) {
            try {
                r.add((Middleware) injector.getInstance(z));
            } catch (Exception e) {
                LOG.severe("error while instanciating middleware [%s]", z.toString(), e);
                throw new RuntimeException(e);
            }
        }
        return r;
    }
}
