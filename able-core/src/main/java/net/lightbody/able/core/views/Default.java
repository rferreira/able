package net.lightbody.able.core.views;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.lightbody.able.core.Able;
import net.lightbody.able.core.http.Request;
import net.lightbody.able.core.http.Response;
import net.lightbody.able.core.http.ResponseNotFound;
import net.lightbody.able.core.routing.Route;
import net.lightbody.able.core.routing.Router;
import net.lightbody.able.core.templates.Templates;
import net.lightbody.able.core.util.Log;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Date: May 3, 2012
 * Time: 11:08:59 PM
 */
@Singleton
public class Default implements View {

    private static Log LOG = new Log();
    boolean isDebug = false;

    @Inject
    Router router;

    @Inject
    public Default(@Named("debug") boolean debug) {
        isDebug = debug;
        LOG.info("running in debug mode:" + debug);
    }

    @Override
    public Response dispatch(Request req) {

        if (!isDebug) {
             return new ResponseNotFound();
        }
        LOG.fine("response not found, returning debug page");

        List<String> routeInfo = Lists.newArrayList();

        for (Route route: router.routes) {
            routeInfo.add(route.getRegex());
        }

        Map context = ImmutableMap.of(
                "error" , "page not found :(",
                "errormsg", "we could not find a view for path: " + req.getPath(),
                "version" , Able.version,
                "routes", routeInfo,
                "environment", System.getenv()

        );

        return Templates.render("able.debug.index", context);

    }
}
