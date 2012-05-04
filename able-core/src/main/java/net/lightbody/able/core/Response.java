package net.lightbody.able.core;

import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import net.lightbody.able.core.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: raferrei
 * Date: 4/20/12
 * Time: 10:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Response {
    private static Log LOG = new Log();

    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final String DEFAULT_CONTENT_TYPE = "text/html";

    public int status = 200;
    public final Map<String, String> HEADERS = Maps.newHashMap();


    public void setCookie(String key, String value) {
        throw new IllegalStateException("not implemented!");
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

    public ByteArrayOutputStream flush() throws IOException {
        buffer.flush();

        HEADERS.put(HttpHeaders.CONTENT_LENGTH, String.valueOf(buffer.size()));

        return buffer;
    }

    @Override
    public String toString() {
        return "Response{" +
                ", status=" + status +
                ", HEADERS=" + HEADERS +
                '}';
    }
}
