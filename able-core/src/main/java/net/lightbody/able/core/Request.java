package net.lightbody.able.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.google.common.net.HttpHeaders;
import net.lightbody.able.core.http.HttpMethod;
import net.lightbody.able.core.http.XHeaders;
import net.lightbody.able.core.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: raferrei
 * Date: 4/20/12
 * Time: 10:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Request {

    private InputStream body;
    private HttpMethod method = null;
    private String version = "1.1";
    private String path;

    private static Log LOG = new Log();

    public final Map<String, String> GET = Maps.newConcurrentMap();
    public final Map<String, String> POST = Maps.newConcurrentMap();
    public final Map<String, String> HEADERS = Maps.newConcurrentMap();

    public final Map<String,String> SESSION = Maps.newConcurrentMap();
    public final Map<String, String> COOKIES = Maps.newConcurrentMap();

    public String getVersion() {
        return version;
    }

    public Request(HttpMethod method, String path, InputStream body) {
        this.method = method;
        this.path = path;
        this.body = body;

    }

    public InputStream getInputStream() {
        Preconditions.checkNotNull(body);
        return body;
    }

    public String getBody() throws IOException {
        return new String(ByteStreams.toByteArray(body));

    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Request() {
    }

    public boolean isSecure() { return false; }

    public boolean isAjax() {
        return HEADERS.get(XHeaders.HTTP_X_REQUESTED_WITH) != null && HEADERS.get(XHeaders.HTTP_X_REQUESTED_WITH).equalsIgnoreCase("XMLHttpRequest");
    }

    public boolean isKeepAlive() {
        return HEADERS.get(HttpHeaders.CONNECTION) != null && HEADERS.get(HttpHeaders.CONNECTION).equalsIgnoreCase("Keep-Alive");
    }
        
    @Override
    public String toString() {
        return "Request{" +
                ", method='" + method + '\'' +
                ", version='" + version + '\'' +
                ", path='" + path + '\'' +
                ", GET=" + GET +
                ", POST=" + POST +
                ", HEADERS=" + HEADERS +
                ", SESSION=" + SESSION +
                ", COOKIES=" + COOKIES +
                '}';
    }
}
