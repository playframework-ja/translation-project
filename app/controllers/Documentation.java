package controllers;

import static org.apache.commons.lang.StringUtils.isEmpty;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.pegdown.*;
import org.pegdown.ast.*;

import play.*;
import play.libs.*;
import play.mvc.*;
import util.*;

/**
 * Documentation controller.
 * 
 * @author garbagetown
 * 
 */
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

    /**
     * page action.
     * 
     * @param version
     * @param id
     */
    public static void page(String version, String id) {
        List<String> versions = Documentation.versions;

        if (isEmpty(id)) {
            String home = version.startsWith("1") ? "home" : "Home";
            redirect(String.format("/documentation/%s/%s", version, home));
        }

        File root = new File(Play.applicationPath, String.format("documentation/%s/manual/", version));
        String ext = version.startsWith("1") ? "textile" : "md";
        File page = find(root, id, ext);
        if (!page.exists()) {
            notFound(page.getPath());
        }
        String html = null;
        String content = IO.readContentAsString(page);
        if (version.startsWith("1")) {
            html = Textile.toHTML(content);
        } else {
            PegDownProcessor processor = new PegDownProcessor(Extensions.ALL);
            html = processor.markdownToHtml(content, new LinkRenderer() {
                @Override
                public Rendering render(WikiLinkNode node) {
                    String href = "";
                    String text = "";
                    if (node.getText().contains("|")) {
                        String[] parts = node.getText().split(Pattern.quote("|"));
                        href = parts[1].trim();
                        text = parts[0].trim();
                    }
                    return new LinkRenderer.Rendering(href, text);
                }
            });
        }

        Document doc = Jsoup.parse(html);
        Elements links = doc.select("a[href~=/@api/]");
        for (Element link : links) {
            String value = link.attr("href");
            int index = value.indexOf("/@api/") + "/@api/".length();
            link.attr(
                    "href",
                    String.format("http://www.playframework.org/documentation/api/%s/%s", version,
                            value.substring(index)));
            link.attr("target", "_blank");
        }
        html = doc.body().html();

        render(versions, version, id, html);
    }

    private static File find(File dir, String id, String ext) {
        File file = null;
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                file = find(f, id, ext);
            } else if (f.getName().equals(id + "." + ext)) {
                file = f;
            }
            if (file != null) {
                break;
            }
        }
        return file;
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

    public static void cheatsheet(String version, String id) {
        final String action = "documentation";
        File dir = new File(Play.applicationPath, String.format("documentation/%s/cheatsheets/%s", version, id));

        if (!dir.exists()) {
            if (!version.equals(latestVersion)) {
                cheatsheet(latestVersion, id);
            }
            notFound(dir.getPath());
        }

        File[] files = dir.listFiles();
        Arrays.sort(files);
        String[] htmls = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            htmls[i] = Textile.toHTML(IO.readContentAsString(files[i]));
        }
        String title = StringUtils.humanize(id);

        render(action, version, id, htmls, title);
    }
}