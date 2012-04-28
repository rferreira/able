package net.lightbody.able.core.routing;

import jregex.Pattern;

/**
 * Date: Apr 28, 2012
 * Time: 2:15:57 PM
 */
public class Route {
    private Pattern pattern;
    private Class clazz;

    public Route(Pattern pattern, Class clazz) {
        this.pattern = pattern;
        this.clazz = clazz;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }
}
