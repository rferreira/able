package net.lightbody.able.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.lightbody.able.core.config.ConfigurationModule;
import net.lightbody.able.core.internal.NettyHttpServer;
import net.lightbody.able.core.util.Log;

public class Main {
    private static final Log LOG = new Log();

    public static void main(String[] args) throws Exception {
        LOG.info("Starting Able...");

        Injector injector = Guice.createInjector(new ConfigurationModule("example"));
        net.lightbody.able.core.HttpServer server = injector.getInstance(NettyHttpServer.class);
        server.start();
    }
}
