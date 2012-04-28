package net.lightbody.able.core.middleware;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import net.lightbody.able.core.Request;
import net.lightbody.able.core.Response;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: raferrei
 * Date: 4/23/12
 * Time: 9:39 AM
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class MiddlewareManager {

    private List<Class> handlers = Lists.newArrayList();


    public List<Middleware> getMiddlewareList() {
        List<Middleware> r = Lists.newArrayList();
        for (Class z: handlers) {
            try {
                r.add( (Middleware) z.newInstance());
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
