package net.lightbody.able.core.views;

import net.lightbody.able.core.http.Request;
import net.lightbody.able.core.http.Response;

/**
 * Created with IntelliJ IDEA.
 * User: raferrei
 * Date: 4/24/12
 * Time: 7:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface View {

    public Response dispatch(Request req);

}
