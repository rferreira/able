package net.lightbody.able.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;
import com.google.sitebricks.SitebricksModule;
import com.google.sitebricks.SitebricksServletModule;
import net.lightbody.able.core.config.ConfigurationModule;
import net.lightbody.able.example.bricks.TestBrick;
import net.lightbody.able.jetty.JettyModule;
import net.lightbody.able.jetty.JettyServer;
import net.lightbody.able.loggly.LogglyModule;
import net.lightbody.able.stripes.GuiceRuntimeConfiguration;
import net.lightbody.able.stripes.GuiceStripesFilter;
import net.lightbody.able.stripes.StripesModule;
import net.sourceforge.stripes.controller.DynamicMappingFilter;
import net.sourceforge.stripes.controller.StripesFilter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class Main extends HttpServlet {
    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new ConfigurationModule("example"),
                new LogglyModule(),
                new JettyModule(Main.class),
                new StripesModule(Main.class),
                new SitebricksModule() {
                    @Override
                    protected void configureSitebricks() {
                        scan(TestBrick.class.getPackage());
                    }
                },
                new SitebricksServletModule() {
                    @Override
                    protected void configureCustomServlets() {
                        serve("/test").with(TestServlet.class);
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("Extension.Packages", GuiceRuntimeConfiguration.class.getPackage().getName());
                        filter("/*").through(new GuiceStripesFilter(), params);
                    }
                });
        JettyServer server = injector.getInstance(JettyServer.class);
        server.start(injector);
    }
}
