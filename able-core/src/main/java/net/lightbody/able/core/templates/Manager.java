package net.lightbody.able.core.templates;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.template.soy.SoyFileSet;
import com.google.template.soy.tofu.SoyTofu;
import net.lightbody.able.core.http.Response;
import net.lightbody.able.core.util.Log;

import javax.inject.Named;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Date: May 4, 2012
 * Time: 2:20:45 AM
 */

@Singleton
public class Manager {

    private static Log LOG = new Log();
    private SoyTofu tofu;
    private boolean isDebug;
    private File root;

    private List<File> templates = Lists.newArrayList();

    @Inject
    private Injector injector;

    @Inject
    public Manager(@Named("debug") boolean isDebug) {

        this.isDebug = isDebug;
    }

    public void load(File root) {
        Preconditions.checkNotNull(root);

        if (!root.isDirectory()) {
            root = new File(root.getParent());
        }

        File[] files = root.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.endsWith(".soy");
            }
        });

        Collections.addAll(templates, files);
    }

    public synchronized void compile() {
        SoyFileSet.Builder builder = injector.getInstance(SoyFileSet.Builder.class);

        for (File f : templates) {
            LOG.info("loading %s", f.getPath());
            builder.add(f);
        }
        LOG.fine("compiling templates...");
        SoyFileSet sfs = builder.build();
        tofu = sfs.compileToTofu();

    }


    public Response render(String name, Map context) {

        // if in debug mode we recompile every time  - this shoudl be improved later
        if (isDebug) {
            LOG.fine("recompiling templates");
            compile();
        }

        TemplateResponse r = new TemplateResponse();
        r.setContent(tofu.newRenderer(name).setData(context).render().getBytes());

        return r;
    }


}
