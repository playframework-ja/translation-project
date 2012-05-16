package controllers;

import java.io.*;
import java.net.*;
import java.util.*;

import models.*;
import net.htmlparser.jericho.*;

import org.apache.commons.lang.*;
import org.yaml.snakeyaml.*;

import play.*;
import play.mvc.*;

public class Application extends Controller {

    static {

        String hostKey = "http.proxyHost";
        String portKey = "http.proxyPort";

        String host = Play.configuration.getProperty(hostKey);
        String port = Play.configuration.getProperty(portKey);

        if (StringUtils.isNotEmpty(host) && StringUtils.isNotEmpty(port)) {
            System.setProperty(hostKey, host);
            System.setProperty(portKey, port);
        }
    }

    /**
     * index action.
     */
    public static void index() {
        String action = "index";
        render(action);
    }

    public static void documentation(String version) throws Exception {
        if (version == null) {
            version = Play.configuration.getProperty("version.latest");
        }
        Documentation.page(version, "home");
    }

    public static void download(String action) throws MalformedURLException, IOException {

        Long downloads = 0L;
        Download latest = null;
        Download upcoming = null;
        List<Download> olders = null;
        List<Download> nightlies = null;

        Source source = new Source(new URL("http://www.playframework.org/download"));

        List<Element> tables = source.getAllElements(HTMLElementName.TABLE);

        for (int i = 0, n = tables.size(); i < n; i++) {

            List<Element> elements = tables.get(i).getAllElements(HTMLElementName.TR);

            if (elements.size() == 1) {
                if (latest == null) {
                    latest = toDownload(elements.get(0));
                } else {
                    upcoming = toDownload(elements.get(0));
                }
            } else {
                if (olders == null) {
                    olders = toDownload(elements);
                } else {
                    nightlies = toDownload(elements);
                }
            }
        }
        render(action, downloads, latest, upcoming, olders, nightlies);
    }

    private static List<Download> toDownload(List<Element> elements) {
        List<Download> downloads = new ArrayList<Download>();
        for (Element element : elements) {
            downloads.add(toDownload(element));
        }
        return downloads;
    }

    private static Download toDownload(Element element) {
        List<Element> list = element.getAllElements(HTMLElementName.TD);
        String url = list.get(0).getChildElements().get(0).getAttributeValue("href");
        String date = list.get(1).getContent().toString().trim();
        String size = list.get(2).getContent().toString().trim();
        return new Download(url, date, size);
    }

    public static void code(String action) {
        render(action);
    }

    public static void ecosystem(String action) {
        render(action);
    }

    public static void modules(String action, String keyword) throws FileNotFoundException {

        File[] dirs = new File(Play.applicationPath, "documentation/modules").listFiles();

        List<Module> modules = new ArrayList<Module>();

        for (File dir : dirs) {

            File manifest = new File(dir, "manifest");

            if (!manifest.exists()) {
                continue;
            }

            Map<String, Object> map = (Map<String, Object>) new Yaml().load(new FileInputStream(manifest));

            Module module = new Module(map);

            if (keyword == null || module.id.contains(keyword) || module.name.contains(keyword)
                    || module.description.contains(keyword)) {
                modules.add(module);
            }
        }

        Collections.sort(modules);

        render(action, modules);
    }

    public static void about(String action) throws FileNotFoundException {
        List<Map<String, String>> translators = (List<Map<String, String>>) new Yaml().load(new FileInputStream(
                Play.applicationPath + "/conf/translators.yml"));
        render(action, translators);
    }

    public static void introduce20() throws MalformedURLException, IOException {

        Source source = new Source(new URL("http://www.playframework.org/2.0"));

        String list = getString(source, "features", 1);

        List<Map<String, String>> details = new ArrayList<Map<String, String>>();
        details.add(getMap(source, "build"));
        details.add(getMap(source, "mvc"));
        details.add(getMap(source, "apis"));
        details.add(getMap(source, "datastore"));
        details.add(getMap(source, "testing"));
        details.add(getMap(source, "documentation"));

        String twitter = getString(source, "share", 0);

        render(list, details, twitter);
    }

    private static Map<String, String> getMap(Source source, String id) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("benefits", getString(source, id, 1));

        return map;
    }

    private static String getString(Source source, String id, int pos) {
        String s = "";
        Element element = source.getElementById(id);
        if (element != null) {
            List<Element> elements = element.getChildElements();
            if (elements != null && elements.size() > pos) {
                s = getString(elements.get(pos));
            }
        }
        return s;
    }

    private static String getString(Element elem) {
        return elem != null ? elem.toString() : "";
    }
}