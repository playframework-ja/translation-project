package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.textile.core.TextileLanguage;

import play.Play;
import play.libs.IO;
import play.mvc.Controller;
import util.Textile;

public class Documentation extends Controller {

    public static void page(String version, String id) throws Exception {

        String action = "documentation";
        
        String latest = Play.configuration.getProperty("version.latest");

        File page = new File(
                Play.applicationPath,
                "documentation/" + version + "/manual/" + id + ".textile");

        if (!page.exists()) {
            if (!version.equals(latest)) {
                page(latest, id);
            }
            notFound(page.getPath());
        }

        String textile = IO.readContentAsString(page);
        String html = Textile.toHTML(textile);
        String title = getTitle(textile);

        render(action, version, id, html, title);
    }

    public static void image(String version, String name) {
        renderBinaryFile("documentation/" + version + "/images/" + name + ".png");
    }

    public static void file(String version, String name) {
        renderBinaryFile("documentation/" + version + "/files/" + name);
    }
    
    static void renderBinaryFile(String filepath) {
        File file = new File(Play.applicationPath, filepath);
        if (!file.exists()) {
            notFound(file.getPath());
        }
        renderBinary(file);
    }
    
    static String getTitle(String textile) {
        if (textile.length() == 0) {
            return "";
        }
        return textile.split("\n")[0].substring(3).trim();
    }

}