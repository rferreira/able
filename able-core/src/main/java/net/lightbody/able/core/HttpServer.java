package net.lightbody.able.core;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.lightbody.able.core.internal.NettyHandler;
import net.lightbody.able.core.util.Log;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: raferrei
 * Date: 4/20/12
 * Time: 11:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpServer {

    @Inject
    private NettyHandler handler;
    private int port;

    private static Log LOG = new Log();

    @Inject
    public HttpServer(@Named("port") int port) {
        this.port = port;
    }

    public void start() {
        ServerBootstrap bs = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool())
        );

        bs.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", new HttpRequestDecoder());
                pipeline.addLast("aggregator", new HttpChunkAggregator(65536));
                pipeline.addLast("encoder", new HttpResponseEncoder());
                pipeline.addLast("handler", handler);

                return pipeline;
            }
        });
        bs.bind(new InetSocketAddress(port));

    }

}
