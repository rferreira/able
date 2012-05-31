package net.lightbody.able.example.views;

import net.lightbody.able.core.http.Request;
import net.lightbody.able.core.http.Response;
import net.lightbody.able.core.views.View;

import java.util.Date;
import java.util.Map;

/**
 * Date: Apr 28, 2012
 * Time: 2:29:28 PM
 */
public class Homepage implements View {
    @Override
    public Response dispatch(Request req) {

        StringBuilder sb = new StringBuilder();
        sb.append("<h1>Test Homepage</h1>");

        if (req.COOKIES.size() > 0) {
            for (Map.Entry<String, String> e : req.COOKIES.entrySet()) {
                sb.append(String.format("cookie %s : %s <br>", e.getKey(), e.getValue()));
            }
        }

        // testing sessions
        req.SESSION.put("hello", "world");


        // storing arbitraty junk:
        req.SESSION.put("date", new Date());

        req.SESSION.remove("cookie");

        if (req.SESSION.size() > 0) {
            for (Map.Entry<String, Object> e : req.SESSION.entrySet()) {
                sb.append(String.format("session %s : %s <br>", e.getKey(), e.getValue()));
            }
        }

        Response r = new Response(200, sb.toString(), "text/html");
        r.setCookie("foo", "bar", 100, "/", "localhost", false, false);

        r.setCookie("batman", "robin", 0, "/", "localhost", false, false);
        r.setCookie("green", "lantern");


        return r;
    }
}
