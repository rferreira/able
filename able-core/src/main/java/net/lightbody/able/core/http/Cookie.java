package net.lightbody.able.core.http;


public class Cookie {
    boolean secure, httpOnly;
    String path, name, value, domain;
    int maxAge;

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", maxAge=" + maxAge +
                ", domain='" + domain + '\'' +
                '}';
    }
}