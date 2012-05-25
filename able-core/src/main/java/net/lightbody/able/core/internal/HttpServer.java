package net.lightbody.able.core.internal;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.lightbody.able.core.util.Log;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

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
    private HttpPipelineFactory pf;
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

        bs.setPipelineFactory(pf);
        bs.setOption("tcpNodelay", true);
        bs.bind(new InetSocketAddress(port));


    }

}
