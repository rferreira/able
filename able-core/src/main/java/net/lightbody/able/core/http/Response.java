package net.lightbody.able.core.http;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import net.lightbody.able.core.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: raferrei
 * Date: 4/20/12
 * Time: 10:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Response {
    private static Log LOG = new Log();

    public static String DEFAULT_CONTENT_TYPE = "text/html; charset=utf-8";
    public int status = 200;

    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    public final Map<String, String> HEADERS = Maps.newConcurrentMap();
    private final List<Cookie> cookies = new CopyOnWriteArrayList<Cookie>();

    public void setCookie(String name, String value) {
        this.setCookie(name, value, 0, "/", null, false, true );
    }

    public void setCookie(String name, String value, int maxAge, String path, String domain, boolean isSecure, boolean isHttpOnly) {
        Preconditions.checkNotNull(value);
        Preconditions.checkNotNull(name);
        Cookie c = new Cookie(name, value);

        if (maxAge != 0 ) {
            c.setMaxAge(maxAge);
        }

        if (path != null) {
            c.setPath(path);
        }

        if (domain != null) {
            c.setDomain(domain);
        }

        c.setSecure(isSecure);
        c.setHttpOnly(isHttpOnly);

        cookies.add(c);

    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setContent(byte[] content) {
        try {
            buffer.write(content);
        } catch (IOException e) {
            LOG.warn("oops", e);
        }
    }

    public Response(int status, String content, String contentType) {
        setContent(content.getBytes());
        this.status = status;
        HEADERS.put(HttpHeaders.CONTENT_TYPE, contentType);
    }


    public Response(int status, byte[] content, String contentType) {
        setContent(content);
        this.status = status;
        HEADERS.put(HttpHeaders.CONTENT_TYPE, contentType);

    }

    public Response() {
        status = 200;
        HEADERS.put(HttpHeaders.CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
    }

    public synchronized ByteArrayOutputStream flush() throws IOException {
        buffer.flush();

        HEADERS.put(HttpHeaders.CONTENT_LENGTH, String.valueOf(buffer.size()));

        return buffer;
    }

    @Override
    public String toString() {
        return "Response: \n{" +
                "status=" + status +
                ", HEADERS=" + HEADERS +
                ", cookies=" + cookies +
                '}';
    }
}
