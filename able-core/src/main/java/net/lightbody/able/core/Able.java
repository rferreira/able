package net.lightbody.able.core;

import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.lightbody.able.core.internal.HttpServer;
import net.lightbody.able.core.routing.Router;
import net.lightbody.able.core.templates.Manager;
import net.lightbody.able.core.templates.Templates;
import net.lightbody.able.core.util.Log;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class Able {
    private static Class anchorClass;

    public static final String version = "0.1 alpha";

    public Router router;
    public HttpServer server;

    public Manager templateManager;

    private static Log LOG = new Log();

    @Inject
    public Able(Router router, HttpServer server, @Named("debug") boolean debug, Manager tpm, @Named("able.debug") boolean isDebug) {

        this.router = router;
        this.server = server;
        this.templateManager = tpm;

        /// are we running in debug mode:
        if (isDebug) {
            Logger logger = Logger.getLogger("net.lightbody.able");
            logger.setLevel(Level.FINE);
            LOG.fine("framework in debug mode!");
        }

        LOG.fine("wiring the templates shortcut");
        Templates.setManager(tpm);

        // loading default templates
        URL templatesDIR = Resources.getResource(getClass(), "/templates/");

        LOG.info("loading templates from: " + templatesDIR.getPath());

        if (debug) {
            tpm.load(new File(templatesDIR.getPath().replace("target/classes","src/main/resources")));
        } else {
            tpm.load(new File(templatesDIR.getPath()));
        }

        // loading application templates:
        tpm.load( findWebAppDir());
        // compiling
        tpm.compile();

        LOG.info("able framework (v%s) started, good luck!", version);

    }

    public static File findWebAppDir() {
        findAnchorClass();

        String classPath = anchorClass.getName();
        classPath = classPath.replaceAll("\\.", "/") + ".class";
        URL url = anchorClass.getResource("/" + classPath);

        if (url == null) {
            throw new IllegalStateException("Could not find class " + anchorClass.getName() + " in the class path");
        }

        String s = url.toString();
        File webappDir;
        if (s.startsWith("jar:file:")) {
            // if the class is in a jar, assume that the webapp dir is at ../webapp
            // relative to the directory holding the jar
            s = s.substring("jar:file:".length());
            s = s.substring(0, s.indexOf('!'));

            File path = new File(s);
            webappDir = new File(path.getParentFile().getParentFile(), "webapp");
            if (!webappDir.exists()) {
                // not there either? I bet we're running under Heroku, which has a hybrid between a packaged
                // zip and a developer-friendly unpackaged environment (below), so let's try:
                File herokuHack = new File(path.getParentFile().getParentFile().getParentFile().getParentFile(), "src/main/webapp");
                if (herokuHack.exists()) {
                    webappDir = herokuHack;
                }
            }
        } else if (s.startsWith("file:")) {
            // if the class is not in a jar, assume the the webapp dir is at ../src/main/webapp
            // relative to the directory holding the class
            s = s.substring("file:".length());

            String replaced = anchorClass.getName().replaceAll("\\.", "/") + ".class";
            int index = s.lastIndexOf(replaced);
            s = s.substring(0, index);

            File path = new File(s);
            webappDir = new File(path.getParentFile().getParentFile(), "src/main/webapp");
        } else {
            throw new IllegalStateException("Unexpeted URI for anrchor class: " + s);
        }

        if (!webappDir.exists()) {
            throw new IllegalStateException("Webapp directory " + webappDir.getPath() + " not found");
        }

        return webappDir;
    }

    public static Class findAnchorClass() {
        if (anchorClass == null) {
            try {
                StackTraceElement[] trace = new Exception().getStackTrace();
                anchorClass = Class.forName(trace[trace.length - 1].getClassName());
            } catch (ClassNotFoundException e) {
                throw new Error("Could not find root class, Able framework will not work");
            }
        }

        return anchorClass;
    }

}
