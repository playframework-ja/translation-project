package jobs;

import static org.apache.commons.lang.StringUtils.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jj.play.org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import jj.play.org.eclipse.mylyn.wikitext.textile.core.TextileLanguage;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pegdown.Extensions;
import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.WikiLinkNode;

import play.Logger;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.IO;
import play.templates.Template;
import play.templates.TemplateLoader;

/**
 * 
 * @author garbagetown
 * 
 */
@OnApplicationStart(async = true)
public class DocumentParser extends Job {

    public static List<String> versions;
    public static String latestVersion;

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

    @Override
    public void doJob() throws Exception {
        if (Play.mode == Play.Mode.PROD) {
            return;
        }
        for (String version : versions) {
            File srcDir = new File(Play.applicationPath, String.format("documentation/%s/manual",
                    srcdir(version)));
            for (File src : sources(srcDir)) {
                String name = src.getName();
                String id = name.substring(0, name.lastIndexOf("."));
                File dest = new File(Play.applicationPath, String.format("html/%s/%s.html",
                        version, id));
                if (dest.exists() && dest.lastModified() > src.lastModified()) {
                    continue;
                }
                parse(version, id);
            }
        }
    }

    private static String srcdir(String version) {
        if (version.equals("2.0.5") || version.equals("2.0.6")) {
            return "2.0.4";
        }
        return version;
    }

    /**
     * 
     * @param dir
     * @return
     */
    private List<File> sources(File dir) {
        List<File> sources = new ArrayList<File>();
        File[] files = dir.listFiles();
        if (files.length == 0) {
            return sources;
        }
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                sources.addAll(sources(file));
            } else {
                String name = file.getName();
                if (!name.endsWith(".textile") && !name.endsWith(".md")) {
                    continue;
                }
                if (name.equals("_Sidebar.md")) {
                    continue;
                }
                sources.add(file);
            }
        }
        return sources;
    }

    /**
     * 
     * @param version
     * @param id
     * @return
     * @throws FileNotFoundException
     */
    public static String parse(String version, String id) throws FileNotFoundException {

        Logger.debug(String.format("version:%s, id:%s", version, id));

        File root = new File(Play.applicationPath, String.format("documentation/%s/manual/",
                srcdir(version)));
        String ext = isTextile(version) ? "textile" : "md";
        File page = findDown(root, id, ext);
        if (page == null || !page.exists()) {
            throw new FileNotFoundException();
        }

        Logger.debug(String.format("src:%s", page.getAbsolutePath()));

        String article = null;
        String navigation = null;
        boolean translated = false;
        if (isTextile(version)) {
            article = parseTextile(page);
            navigation = null;
        } else {
            article = parseMarkdown(page);
            File sidebar = findUp(page.getParentFile(), "_Sidebar", "md");
            if (sidebar.exists()) {
                navigation = parseMarkdown(sidebar);
            }
        }
        article = replaceHref(version, article);
        article = markAbsent(root, article, ext);
        navigation = markAbsent(root, navigation, ext);
        translated = hasBeenTranslated(page);

        Template template = TemplateLoader.load("Documentation/page.html");
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("versions", versions);
        args.put("version", version);
        args.put("id", id);
        args.put("article", article);
        args.put("navigation", navigation);
        args.put("translated", translated);
        String html = template.render(args);

        File destdir = new File(Play.applicationPath, String.format("html/%s", version));
        if (!destdir.exists()) {
            destdir.mkdirs();
        }
        File file = new File(destdir, String.format("%s.html", id));
        IO.writeContent(html, file);

        Logger.debug(String.format("dest:%s", file.getAbsolutePath()));

        return html;
    }

    /**
     * 
     * @param version
     * @return
     */
    public static boolean isTextile(String version) {
        return version.startsWith("1");
    }

    /**
     * 
     * @param dir
     * @param id
     * @param ext
     * @return
     */
    private static File findDown(File dir, String id, String ext) {
        File file = null;
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                file = findDown(f, id, ext);
            } else if (f.getName().equals(id + "." + ext)) {
                file = f;
            }
            if (file != null) {
                break;
            }
        }
        return file;
    }

    /**
     * 
     * @param dir
     * @param id
     * @param ext
     * @return
     */
    private static File findUp(File dir, String id, String ext) {
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                continue;
            } else if (f.getName().equals(id + "." + ext)) {
                return f;
            }
        }
        return findUp(dir.getParentFile(), id, ext);
    }

    /**
     * 
     * @param version
     * @param html
     * @return
     */
    private static String replaceHref(String version, String html) {
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("a[href~=/@api/]");
        for (Element link : links) {
            String value = link.attr("href");
            int index = value.indexOf("/@api/") + "/@api/".length();
            link.attr("href", String.format("http://www.playframework.com/documentation/api/%s/%s",
                    version, value.substring(index)));
            link.attr("target", "_blank");
        }
        return doc.body().html();
    }

    /**
     * 
     * @param root
     * @param html
     * @param ext
     * @return
     */
    private static String markAbsent(File root, String html, String ext) {
        if (html == null) {
            return html;
        }
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("a");
        for (Element link : links) {
            String href = link.attr("href");
            if (isEmpty(href) || href.startsWith("http") || href.contains("/")) {
                continue;
            }
            href = href.contains("#") ? href.substring(0, href.indexOf("#")) : href;
            File file = findDown(root, href, ext);
            if (file == null) {
                link.attr("class", "absent");
                continue;
            }
            if (ext.equals("md") && !hasBeenTranslated(file)) {
                link.attr("class", "absent");
            }
        }
        return doc.body().html();
    }

    /**
     * 
     * @param file
     * @return
     */
    private static boolean hasBeenTranslated(File file) {
        List<String> lines = IO.readLines(file);
        if (lines == null || lines.size() < 1) {
            return false;
        }
        if (file.getName().endsWith(".textile")) {
            return true;
        }
        if (lines.get(0).matches(Pattern.quote("<!-- translated -->"))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @param page
     * @return
     */
    public static String parseTextileFile(File page) {
        return parseTextile(IO.readContentAsString(page));
    }

    /**
     * 
     * @param contents
     * @return
     */
    public static String parseTextile(String contents) {
        MarkupParser parser = new MarkupParser(new TextileLanguage());
        String html = parser.parseToHtml(contents);
        int begin = html.indexOf("<body>") + "<body>".length();
        int end = html.lastIndexOf("</body>");
        return html.substring(begin, end);
    }

    /**
     * 
     * @param page
     * @return
     */
    private static String parseTextile(File page) {
        MarkupParser parser = new MarkupParser(new TextileLanguage());
        String html = parser.parseToHtml(IO.readContentAsString(page));
        int begin = html.indexOf("<body>") + "<body>".length();
        int end = html.lastIndexOf("</body>");
        return html.substring(begin, end);
    }

    /**
     * 
     * @param page
     * @return
     */
    public static String parseMarkdown(File page) {
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL);
        LinkRenderer renderer = new GithubLinkRenderer(page);
        return processor.markdownToHtml(toMarkdownSource(page), renderer);
    }

    private static String toMarkdownSource(File page) {
        List<String> lines = IO.readLines(page);
        Pattern p = Pattern.compile("^@\\[(.+)\\]\\((.+)\\)");
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            Matcher m = p.matcher(line);
            if (m.find()) {
                String id = m.group(1);
                File file = new File(page.getParentFile(), m.group(2));
                builder.append("```").append("\n");
                builder.append(extractCode(file, id));
                builder.append("```").append("\n");
            } else {
                builder.append(line).append("\n");
            }
        }
        return builder.toString();
    }

    private static String extractCode(File file, String id) {
        List<String> lines = IO.readLines(file);

        Pattern p_id = Pattern.compile(String.format("(\\s*)//#%s", id));
        Pattern p_replace = Pattern.compile("//#+replace:(.+)");

        boolean should_be_extracted = false;
        String replace = "";
        String spaces = "";

        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            Matcher m = p_id.matcher(line);
            if (m.find()) {
                spaces = m.group(1);
                should_be_extracted = !should_be_extracted;
                continue;
            }
            if (!should_be_extracted) {
                continue;
            }

            m = p_replace.matcher(line);
            if (m.find()) {
                replace = m.group(1);
                continue;
            }

            if (StringUtils.isNotEmpty(replace)) {
                builder.append(replace.trim()).append("\n");
                replace = "";
            } else {
                builder.append(line.replace(spaces, "")).append("\n");
            }
        }
        return builder.toString();
    }

    /**
     * 
     * @author garbagetown
     * 
     */
    private static class GithubLinkRenderer extends LinkRenderer {

        private File page;

        /**
         * 
         * @param page
         */
        public GithubLinkRenderer(File page) {
            this.page = page;
        }

        @Override
        public Rendering render(WikiLinkNode node) {
            String text = node.getText();
            String href = "";
            if (text.contains("|")) {
                String[] parts = text.split(Pattern.quote("|"));
                text = parts[0].trim();
                href = parts[1].trim();
            } else if (text.endsWith(".png")) {
                String version = "";
                Matcher m = Pattern.compile("/([0-9¥¥.]+)/").matcher(page.getPath());
                if (m.find()) {
                    version = m.group(1);
                }
                String docroot = String.format("documentation/%s/", version);
                String parent = page.getParent();
                String path = parent.substring(parent.indexOf(docroot) + docroot.length());
                href = String.format("resources/%s/%s", path, text);
                text = String.format("<img src=%s>", href);
            } else {
                href = text;
            }
            return new LinkRenderer.Rendering(href, text);
        }
    }
}
