package net.lightbody.able.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.lightbody.able.core.HttpServer;
import net.lightbody.able.core.config.ConfigurationModule;
import net.lightbody.able.core.middleware.LoggingMiddleware;
import net.lightbody.able.core.middleware.XRuntimeMiddleware;
import net.lightbody.able.core.routing.Router;
import net.lightbody.able.core.Able;
import net.lightbody.able.core.util.Log;
import net.lightbody.able.core.views.ServeStatic;

public class Main {
    private static final Log LOG = new Log();

    public static void main(String[] args) throws Exception {
        LOG.info("Starting Able...");

        Injector injector = Guice.createInjector(new ConfigurationModule("example"));

        Able able = injector.getInstance(Able.class);

        // wiring middleware
        able.router.middlewares.add(XRuntimeMiddleware.class);
        able.router.middlewares.add(LoggingMiddleware.class);

        // sample route definition
        able.router.route("^/$", Homepage.class);

        // handle all static assets:
        able.router.route("^/static/(.*)$", ServeStatic.class);

        //example with args
        able.router.route("^/account/{id}/$", Accounts.class);

        // starting server
        able.server.start();
    }
}
