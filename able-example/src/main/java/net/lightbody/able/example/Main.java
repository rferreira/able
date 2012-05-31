package net.lightbody.able.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.lightbody.able.core.Able;
import net.lightbody.able.core.AbleModule;
import net.lightbody.able.core.middleware.LoggingMiddleware;
import net.lightbody.able.core.middleware.SessionMiddleware;
import net.lightbody.able.core.middleware.XRuntimeMiddleware;
import net.lightbody.able.core.util.Log;
import net.lightbody.able.core.views.ServeStatic;
import net.lightbody.able.example.views.API;
import net.lightbody.able.example.views.Accounts;
import net.lightbody.able.example.views.Homepage;

public class Main {
    private static final Log LOG = new Log();

    public static void main(String[] args) throws Exception {
        LOG.info("Starting Able...");

        Injector injector = Guice.createInjector(new AbleModule("example"));

        Able able = injector.getInstance(Able.class);

        // wiring middleware
        able.router.middleware.add(XRuntimeMiddleware.class);

        // enabling sessions:
        able.router.middleware.add(SessionMiddleware.class);

        // debug logging:
        able.router.middleware.add(LoggingMiddleware.class);

        // Routing:

        // sample route definition
        able.router.route("^/$", Homepage.class);

        // enable this if you want able to handle all static assets:
        able.router.route("^/static/(.*)$", ServeStatic.class);

        // with groups
        able.router.route("^/hello/(.*)/world/(.*)/$", Homepage.class);

        // with named groups
        able.router.route("^/hello/(?<person>.*)/$", Homepage.class);

        // API sample
        able.router.route("^/api/(?<model>.*)/$", API.class);

        able.router.route("^/account$", Accounts.class);

        // starting server
        able.server.start();
    }
}
