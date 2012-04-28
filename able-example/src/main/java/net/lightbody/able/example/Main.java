package net.lightbody.able.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.lightbody.able.core.HttpServer;
import net.lightbody.able.core.config.ConfigurationModule;
import net.lightbody.able.core.routing.Router;
import net.lightbody.able.core.util.Log;

public class Main {
    private static final Log LOG = new Log();

    public static void main(String[] args) throws Exception {
        LOG.info("Starting Able...");

        Injector injector = Guice.createInjector(new ConfigurationModule("example"));
        HttpServer server = injector.getInstance(HttpServer.class);
        Router router = injector.getInstance(Router.class);
        router.route("^/hello-world/", Homepage.class);
        server.start();
    }
}
