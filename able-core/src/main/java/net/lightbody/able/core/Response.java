package net.lightbody.able.core;

import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.google.common.io.OutputSupplier;

import java.io.*;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: raferrei
 * Date: 4/20/12
 * Time: 10:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Response {

    public int code = 200;
    private OutputStream os;
    public final Map<String, String> HEADERS = Maps.newConcurrentMap();

    public Response(OutputStream os) {
        this.os = os;
    }

    public void setContent(byte[] content) throws IOException {
        os.write(content,0,content.length);

    }

    public OutputStream getOutputStream() {
        return os;
    }

    public void flush() {

    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", os=" + os +
                ", HEADERS=" + HEADERS +
                '}';
    }
}
