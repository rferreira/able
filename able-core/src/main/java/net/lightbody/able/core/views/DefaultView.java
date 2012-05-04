package net.lightbody.able.core.views;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.lightbody.able.core.Request;
import net.lightbody.able.core.Response;
import net.lightbody.able.core.ResponseNotFound;
import net.lightbody.able.core.View;
import net.lightbody.able.core.util.Log;

/**
 * Date: May 3, 2012
 * Time: 11:08:59 PM
 */
public class DefaultView implements View {

    private static Log LOG = new Log();
    boolean isDebug = false;

    @Inject
    public DefaultView(@Named("debug") boolean debug) {
        isDebug = debug;
        LOG.info("running in debug mode:" + debug);
    }

    @Override
    public Response dispatch(Request req) {
        if (!isDebug) {
             return new ResponseNotFound();
        }

        return new Response(200,"DEBUG PAGE!", "text/html");
    }
}
