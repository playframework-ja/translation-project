package controllers;

import play.Play;
import play.libs.IO;
import play.mvc.Controller;
import util.Textile;

import java.io.File;
import java.util.Arrays;

public class Documentation extends Controller {
    private static final String LATEST = Play.configuration.getProperty("version.latest");

    public static void page(String version, String id) throws Exception {
        final String action = "documentation";

        File page = new File(
                Play.applicationPath,
                "documentation/" + version + "/manual/" + id + ".textile");

        if (!page.exists()) {
            if (!version.equals(LATEST)) {
                page(LATEST, id);
            }
            notFound(page.getPath());
        }

        String textile = IO.readContentAsString(page);
        String html = Textile.toHTML(textile);
        String title = getTitle(textile);

        render(action, version, id, html, title);
    }

    public static void image(String version, String name) {
        renderBinaryFile(String.format("documentation/%s/images/%s.png", version, name));
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
            if (!version.equals(LATEST)) {
                cheatsheet(LATEST, id);
            }
            notFound(dir.getPath());
        }

        File[] files = dir.listFiles();
        Arrays.sort(files);
        StringBuilder sb = new StringBuilder();
        String[] htmls = new String[files.length];
        for (int i = 0; i<files.length; i++){
            htmls[i] = Textile.toHTML(IO.readContentAsString(files[i]));
        }
        String title = humanize(id);

        render(action, version, id, htmls, title);
    }

    static String humanize(String camelCase) {
        char[] chars = camelCase.toCharArray();
        StringBuilder sb = new StringBuilder();
        sb.append(toUpper(chars[0]));
        for (int i = 1; i < chars.length; i++) {
            if (isUpperCase(chars[i])) {
                sb.append(' ');
            }
            sb.append(chars[i]);
        }
        return sb.toString();
    }

    private static boolean isLowerCase(char c) {
        return c >= 'a' && c <= 'z' ? true : false;
    }

    private static boolean isUpperCase(char c) {
        return c >= 'A' && c <= 'Z' ? true : false;
    }

    private static char toUpper(char c) {
        return isLowerCase(c) ? (char) (c - 0x20) : c;
    }

}