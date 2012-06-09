package net.lightbody.able.core.views;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.lightbody.able.core.http.*;
import net.lightbody.able.core.http.Methods;
import net.lightbody.able.core.util.Log;

import java.io.File;

/**
 * Date: Apr 29, 2012
 * Time: 7:04:36 PM
 */
public class ServeStatic implements View {

    private static Log LOG = new Log();
    private String STATIC_ROOT = null;
    private String STATIC_URL = null;


    @Inject
    public ServeStatic(@Named("static_root") String location, @Named("static_url") String url ) {
        if (location.length() == 0) {
            return;
        }

        STATIC_ROOT = location;
        STATIC_URL = url;

    }

    @Override
    public Response dispatch(Request req) {

        if (req.getMethod() != Methods.GET) {
            return new Response(405, "method not allowed", "text/html");
        }

        String path = req.getPath().replace(STATIC_URL, "").replace('/', File.separatorChar);

        try {
            File fd = new File(STATIC_ROOT, path);

            if (!fd.isFile() || !fd.exists()) {
                return new ResponseNotFound();
            }

            LOG.fine("returning file %s", fd);

            Response r = new Response();
            r.HEADERS.put(XHeaders.X_SENDFILE, fd.getCanonicalPath());

            return r;

        } catch (Exception e) {
            LOG.severe("error while attending to server a static file ", e);
        }

        return new ResponseNotFound();
    }

}
