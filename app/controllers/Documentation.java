package controllers;

import play.Play;
import play.libs.IO;
import play.mvc.Controller;
import util.*;

import java.io.File;
import java.util.*;

public class Documentation extends Controller {

    private static List<String> versions;

    private static String latestVersion;

    static {
        versions = new ArrayList<String>();
        String[] dirNames = new File(Play.applicationPath, "documentation/").list();
        for (String name : dirNames) {
            if (name.equals("modules")) {
                continue;
            }
            versions.add(name);
        }
        Collections.sort(versions);
        Collections.reverse(versions);
        latestVersion = Play.configuration.getProperty("version.latest");
    }

    public static void page(String version, String id) throws Exception {

        List<String> versions = Documentation.versions;

        String action = "documentation";

        File page = new File(
                Play.applicationPath,
                "documentation/" + version + "/manual/" + id + ".textile");

        if (!page.exists()) {
            if (!version.equals(latestVersion)) {
                page(latestVersion, id);
            }
            notFound(page.getPath());
        }

        String textile = IO.readContentAsString(page);
        String html = Textile.toHTML(textile);
        String title = getTitle(textile);

        render(action, versions, version, id, html, title);
    }

    public static void image(String version, String name) {
        String filepath = "";
        for (String s : versions) {
            filepath = String.format("documentation/%s/images/%s.png", s, name);
            if (new File(Play.applicationPath, filepath).exists()) {
                break;
            }
        }
        renderBinaryFile(filepath);
    }

    public static void file(String version, String name) {
        renderBinaryFile(String.format("documentation/%s/files/%s", version, name));
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

    public static void cheatsheet(String version, String id) {
        final String action = "documentation";
        File dir = new File(
                Play.applicationPath,
                String.format("documentation/%s/cheatsheets/%s", version, id));

        if (!dir.exists()) {
            if (!version.equals(latestVersion)) {
                cheatsheet(latestVersion, id);
            }
            notFound(dir.getPath());
        }

        File[] files = dir.listFiles();
        Arrays.sort(files);
        String[] htmls = new String[files.length];
        for (int i = 0; i<files.length; i++){
            htmls[i] = Textile.toHTML(IO.readContentAsString(files[i]));
        }
        String title = StringUtils.humanize(id);

        render(action, version, id, htmls, title);
    }
}