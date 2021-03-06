package net.lightbody.able.core.middleware;

import net.lightbody.able.core.http.Request;
import net.lightbody.able.core.http.Response;
import net.lightbody.able.core.views.View;

/**
 * Created with IntelliJ IDEA.
 * User: raferrei
 * Date: 4/21/12
 * Time: 12:28 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Middleware {

    public void process(Request req) {}
    public void process(Response resp) {}
    public void process(Exception e) {}
    public void process(View view) {}

}
