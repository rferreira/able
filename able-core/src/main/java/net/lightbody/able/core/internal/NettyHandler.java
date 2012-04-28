package net.lightbody.able.core.internal;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import net.lightbody.able.core.Request;
import net.lightbody.able.core.Response;
import net.lightbody.able.core.middleware.*;
import net.lightbody.able.core.routes.Router;
import net.lightbody.able.core.util.Log;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.codec.http.HttpRequest;

import java.util.Map;

public class NettyHandler extends SimpleChannelHandler {
    private static final Log LOG = new Log();

//    @Inject
//    Configuration config;

    @Inject
    Router router;

    @Inject MiddlewareManager mm;

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

        HttpRequest internalReq = (HttpRequest) e.getMessage();

        Map<String, String> headers = Maps.newHashMap();

        for ( Map.Entry<String,String> entry: internalReq.getHeaders()) {
            headers.put(entry.getKey(),entry.getValue());
        }

        Request req = new Request(
                internalReq.getMethod().toString(),
                internalReq.getUri(),
                headers,
                new ChannelBufferInputStream(internalReq.getContent())
        );
        Response res = new Response(new ChannelBufferOutputStream(internalReq.getContent()));

        //TODO hack
        mm.add(0, XRuntimeMiddleware.class);
        mm.add(1,LoggingMiddleware.class);


        // processing request:
        router.fire(req, res);

        for ( Map.Entry<String,String> entry: res.HEADERS.entrySet()) {
            internalReq.setHeader(entry.getKey(),entry.getValue());
        }



    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        super.exceptionCaught(ctx, e);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
