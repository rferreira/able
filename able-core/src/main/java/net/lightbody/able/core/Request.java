package net.lightbody.able.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.google.common.net.HttpHeaders;
import com.sun.xml.internal.bind.v2.runtime.output.Encoded;

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

    private InputStream is;
    private String method = "GET";
    private String httpVersion = "1.1";
    private String path;

    public final Map<String, String> GET = Maps.newConcurrentMap();
    public final Map<String, String> POST = Maps.newConcurrentMap();
    public final Map<String, String> HEADERS = Maps.newConcurrentMap();

    public final Map<String,String> SESSION = Maps.newConcurrentMap();
    public final Map<String, String> COOKIES = Maps.newConcurrentMap();

    public String getHttpVersion() {
        return httpVersion;
    }

    public Request(String method, String path, Map<String,String> headers, InputStream is) {
        this.method = method;
        this.path = path;
        this.is = is;

        /* Processing headers */

        HEADERS.putAll(headers);

    }

    public InputStream getInputStream() {
        Preconditions.checkNotNull(is);
        return is;
    }

    public String getBody() throws IOException {
        return new String(ByteStreams.toByteArray(is));

    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Request() {
    }

    public static void main(String[] args) {
        Request r = new Request();

        if (r.getMethod().equals("GET")) {
            System.out.println("hello world");
        }

        if (r.getHttpVersion().equals("1.1")) {
            System.out.println("bar");
        }

        String username = r.COOKIES.get("cart");
        String password = r.GET.get("password");

        r.SESSION.put("hello" ,"world");

    }

    @Override
    public String toString() {
        return "Request{" +
                "is=" + is +
                ", method='" + method + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                ", path='" + path + '\'' +
                ", GET=" + GET +
                ", POST=" + POST +
                ", HEADERS=" + HEADERS +
                ", SESSION=" + SESSION +
                ", COOKIES=" + COOKIES +
                '}';
    }
}
