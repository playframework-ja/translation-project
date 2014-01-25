package controllers;

import static org.apache.commons.lang.StringUtils.*;
import play.mvc.Controller;

/**
 * Security Controller.
 * 
 * @author garbagetown
 */
public class Security extends Controller {

    public static void index() {
        vulnerability(null);
    }

    public static void vulnerability(String id) {
        if (isEmpty(id)) {
            render();
        } else {
            renderTemplate(String.format("Security/%s.html", id));
        }

    }
}
