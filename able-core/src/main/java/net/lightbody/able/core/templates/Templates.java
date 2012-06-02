package net.lightbody.able.core.templates;

import com.google.common.base.Preconditions;
import net.lightbody.able.core.http.Response;

import java.util.Map;

/**
 * Static helper class for dealing with the template rendering engine
 * Date: 5/24/12
 * Time: 8:25 PM
 */
public class Templates {

    private static Manager manager = null;

    public static void setManager(Manager m) {
        manager = m;
    }

    public static Response render(String namespace, Map context) {
        Preconditions.checkNotNull(manager,"oops, framework has not been properly started yet!");
        return manager.render(namespace, context);
    }
}
