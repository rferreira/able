package net.lightbody.able.core.internal;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import net.lightbody.able.core.Request;
import net.lightbody.able.core.Response;
import net.lightbody.able.core.ResponseNotFound;
import net.lightbody.able.core.middleware.MiddlewareManager;
import net.lightbody.able.core.routing.Router;
import net.lightbody.able.core.util.Log;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;

import java.util.List;
import java.util.Map;

import static org.jboss.netty.util.CharsetUtil.UTF_8;

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

        /*
         * PARSING REQUEST VARIABLES:
         */

        Map<String, List<String>> params = null;

        if (req.getMethod() == net.lightbody.able.core.http.HttpMethod.GET) {
            params = new QueryStringDecoder(req.getPath()).getParameters();

            // i'm really not sure why these are lists.
            for (Map.Entry<String, List<String>> entry : params.entrySet()) {
                req.GET.put(entry.getKey(), entry.getValue().get(0));

            }
        } else if (req.getMethod() == net.lightbody.able.core.http.HttpMethod.POST) {
            params = new QueryStringDecoder("?" + internalReq.getContent().toString(UTF_8)).getParameters();

            // i'm really not sure why these are lists.
            for (Map.Entry<String, List<String>> entry : params.entrySet()) {
                req.POST.put(entry.getKey(), entry.getValue().get(0));

            }

        }


        /*
         * FIRING REQUEST:
         */

        // let's assume its a 404
        Response res = new ResponseNotFound();

        try {
             res = router.fire(req);
        } catch (Exception ex) {
            LOG.severe("error while processing request", ex);
        }


        for (Map.Entry<String, String> entry : res.HEADERS.entrySet()) {
            internalReq.setHeader(entry.getKey(), entry.getValue());
        }

        /**
         * WRITING RESPONSE:
        **/

        HttpResponse internalResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(res.status));
        internalResponse.setContent(ChannelBuffers.copiedBuffer(res.flush().toByteArray()));

        for (Map.Entry<String, String> entry : res.HEADERS.entrySet()) {
            internalResponse.setHeader(entry.getKey(), entry.getValue());
        }

        ChannelFuture future = e.getChannel().write(internalResponse);
        if (!req.isKeepAlive()) {
            future.addListener(ChannelFutureListener.CLOSE);
        }

        ctx.sendUpstream(e);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
