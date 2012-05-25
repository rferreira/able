package net.lightbody.able.core;

import com.google.inject.AbstractModule;
import com.google.template.soy.SoyModule;
import net.lightbody.able.core.config.ConfigurationModule;

/**
 * Date: 5/24/12
 * Time: 8:12 PM
 */
public class AbleModule extends AbstractModule {


    private String val;

    public AbleModule(String val) {
        this.val = val;
    }

    @Override
    protected void configure() {
        // something cool will happen here.
        install(new ConfigurationModule(val));
        install(new SoyModule());
    }
}
