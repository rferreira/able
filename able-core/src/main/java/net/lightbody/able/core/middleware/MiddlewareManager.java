package net.lightbody.able.core.middleware;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import net.lightbody.able.core.config.JsonProperties;
import net.lightbody.able.core.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: raferrei
 * Date: 4/23/12
 * Time: 9:39 AM
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class MiddlewareManager {

    private List<Class> handlers = new CopyOnWriteArrayList<Class>();
    private static Log LOG = new Log();

    @Inject
    private Injector injector;

    @Inject
    JsonProperties props;

    @Inject
    public MiddlewareManager() {
        // this should load middleware from the config file
        //TODO: clean this hack
        add(0, XRuntimeMiddleware.class);
        add(1, LoggingMiddleware.class);

    }

    public List<Middleware> getMiddlewareList() {
        List<Middleware> r = Lists.newArrayList();
        for (Class z : handlers) {
            try {
                r.add((Middleware) injector.getInstance(z));
            } catch (Exception e) {
                new RuntimeException(e);
            }
        }
        return r;
    }


    public void add(int position, Class clazz) {
        handlers.add(position, clazz);
    }

}
