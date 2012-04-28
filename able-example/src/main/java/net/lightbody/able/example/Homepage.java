package net.lightbody.able.example;

import net.lightbody.able.core.Request;
import net.lightbody.able.core.Response;
import net.lightbody.able.core.View;

/**
 * Date: Apr 28, 2012
 * Time: 2:29:28 PM
 */
public class Homepage implements View {
    @Override
    public Response dispatch(Request req) {
        return new Response("hello world!", 200, "text/html");
    }
}
