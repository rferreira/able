package net.lightbody.able.core.internal;

import com.google.inject.Inject;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Date: May 3, 2012
 * Time: 7:22:36 PM
 */
public class HttpPipelineFactory implements ChannelPipelineFactory {

    @Inject
    private NettyHandler handler;
    @Inject
    private XSendFileHandler xsendfile;


    @Override
    public ChannelPipeline getPipeline() throws Exception {

        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpChunkAggregator(65536));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("framework", handler);
        pipeline.addLast("xsendfile", xsendfile);

        return pipeline;
    }
}
