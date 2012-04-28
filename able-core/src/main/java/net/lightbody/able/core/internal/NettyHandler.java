package net.lightbody.able.core.internal;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import net.lightbody.able.core.Request;
import net.lightbody.able.core.Response;
import net.lightbody.able.core.middleware.LoggingMiddleware;
import net.lightbody.able.core.middleware.MiddlewareManager;
import net.lightbody.able.core.middleware.XRuntimeMiddleware;
import net.lightbody.able.core.routing.Router;
import net.lightbody.able.core.util.Log;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;

import java.util.List;
import java.util.Map;

public class NettyHandler extends SimpleChannelHandler {
    private static final Log LOG = new Log();

    @Inject
    Router router;

    @Inject
    MiddlewareManager mm;

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

        HttpRequest internalReq = (HttpRequest) e.getMessage();

        Map<String, String> headers = Maps.newHashMap();

        for (Map.Entry<String, String> entry : internalReq.getHeaders()) {
            headers.put(entry.getKey(), entry.getValue());
        }

        Request req = new Request(
                net.lightbody.able.core.http.HttpMethod.valueOf(internalReq.getMethod().toString()),
                internalReq.getUri(),
                new ChannelBufferInputStream(internalReq.getContent())
        );

        req.HEADERS.putAll(headers);

        // parsing variables:

        Map<String, List<String>> params = null;

        if (req.getMethod() == net.lightbody.able.core.http.HttpMethod.GET) {
            params = new QueryStringDecoder(req.getPath()).getParameters();

            // i'm really not sure why these are lists. 
            for (Map.Entry<String, List<String>> entry : params.entrySet()) {
                req.GET.put(entry.getKey(), entry.getValue().get(0));

            }
        }


//        //TODO hack
        mm.add(0, XRuntimeMiddleware.class);
        mm.add(1, LoggingMiddleware.class);

        // processing request:
        Response res = router.fire(req);

        for (Map.Entry<String, String> entry : res.HEADERS.entrySet()) {
            internalReq.setHeader(entry.getKey(), entry.getValue());
        }


        // WRITING RESPONSE:

        HttpResponse internalResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(res.status));
        internalResponse.setContent(ChannelBuffers.copiedBuffer(res.flush().toByteArray()));

        for (Map.Entry<String, String> entry : res.HEADERS.entrySet()) {
            internalResponse.setHeader(entry.getKey(), entry.getValue());
        }

        ChannelFuture future = e.getChannel().write(internalResponse);
        if (!internalReq.isKeepAlive()) {
            future.addListener(ChannelFutureListener.CLOSE);
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
