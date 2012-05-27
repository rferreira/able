package net.lightbody.able.core;

import com.google.inject.AbstractModule;
import net.lightbody.able.core.config.ConfigurationModule;

/**
 * Created with IntelliJ IDEA.
 * User: raferrei
 * Date: 5/26/12
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class AbleModule extends AbstractModule {
    private String name;

    public AbleModule(String name) {
        this.name = name;
    }

    @Override
    protected void configure() {
        install(new ConfigurationModule(name));

    }
}
