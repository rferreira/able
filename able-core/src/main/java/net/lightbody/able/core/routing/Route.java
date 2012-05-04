package net.lightbody.able.core.routing;

import jregex.Pattern;

/**
 * Date: Apr 28, 2012
 * Time: 2:15:57 PM
 */
public class Route {
    private Pattern pattern;
    private Class clazz;
    private String regex;


    public String getRegex() {
        return regex;
    }


    public Route(String regex, Class clazz) {
        this.pattern = new Pattern(regex);
        this.regex = regex;
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
