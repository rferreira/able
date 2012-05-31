package net.lightbody.able.example.views;

import net.lightbody.able.core.http.Request;
import net.lightbody.able.core.http.Response;
import net.lightbody.able.core.views.View;

/**
 * Date: 5/18/12
 * Time: 7:21 AM
 */
public class API implements View {
    @Override
    public Response dispatch(Request req) {
        return new Response(200, "api response for model " + req.VARS.get("model"), Response.DEFAULT_CONTENT_TYPE);
    }
}
