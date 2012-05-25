package net.lightbody.able.core.routing;


/**
 * Date: Apr 28, 2012
 * Time: 2:15:57 PM
 */
public class Route {
    private NamedPattern pattern;
    private Class clazz;
    private String regex;


    public String getRegex() {
        return regex;
    }


    public Route(String regex, Class clazz) {
        this.pattern = NamedPattern.compile(regex);
        this.regex = regex;
        this.clazz = clazz;
    }

    public NamedPattern getPattern() {
        return pattern;
    }

    public void setPattern(NamedPattern pattern) {
        this.pattern = pattern;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }
}
