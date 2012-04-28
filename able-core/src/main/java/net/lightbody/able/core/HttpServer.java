package net.lightbody.able.core;

import com.google.inject.ImplementedBy;
import net.lightbody.able.core.internal.NettyHttpServer;

/**
 * Created with IntelliJ IDEA.
 * User: raferrei
 * Date: 4/23/12
 * Time: 9:34 AM
 * To change this template use File | Settings | File Templates.
 */
@ImplementedBy(NettyHttpServer.class)
public interface HttpServer {
    void start();
}
