package controllers;

import java.io.*;
import java.net.*;
import java.util.*;

import models.*;
import net.htmlparser.jericho.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.lang.*;
import org.yaml.snakeyaml.*;

import play.*;
import play.Logger;
import play.mvc.*;
import twitter4j.*;

import com.google.gson.*;
import com.google.gson.annotations.*;

public class Application extends Controller {

	final private static String lang = play.i18n.Lang.get();
	
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

    public static void index() throws MalformedURLException, IOException {

        String action = "index";

        Source source = new Source(new URL("http://www.playframework.org/"));
        String twitter = getString(source.getElementById("twitter"));
        String event = getString(source.getElementById("event"));

        render(action, twitter, event);
    }

    public static void documentation(String version) throws Exception {
        if (version == null) {
            version = Play.configuration.getProperty("version.latest");
        }
        Documentation.page(version, "home");
    }

    public static void download(String action) throws MalformedURLException, IOException {

        Long downloads = null;
        Download latest = null;
        Download upcoming = null;
        List<Download> olders = null;
        List<Download> nightlies = null;

        Source source = new Source(new URL("http://www.playframework.org/download"));

        downloads = toDownloads(source.getFirstElementByClass("category"));

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

    private static Long toDownloads(Element element) {
        String content = element.getContent().toString().trim().replace(",", "");
        return new Long(content.substring(0, content.indexOf(" ")));
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
        String date = list.get(1).getContent().toString();
        String size = list.get(2).getContent().toString();
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

    public static void about(String action) throws TwitterException {
        render(action);
    }

    public static void translation(String version) throws Exception {
        if (version == null) version = Play.configuration.getProperty("version.latest");

        Documentation.page(version, "translation");
    }
    
    public static void introduce20() throws MalformedURLException, IOException {
        
        Source source = new Source(new URL("http://www.playframework.org/2.0"));

        String list = getString(source.getElementById("features").getChildElements().get(1));
        
        List<Map<String, String>> details = new ArrayList<Map<String,String>>();
        details.add(getMap(source, "build"));
        details.add(getMap(source, "mvc"));
        details.add(getMap(source, "apis"));
        details.add(getMap(source, "datastore"));
        details.add(getMap(source, "testing"));
        details.add(getMap(source, "documentation"));
        
        List<Element> twitters = source.getElementById("share").getChildElements();
        String twitter = twitters.isEmpty() ? "" : getString(twitters.get(0));
        
        renderTemplate("Application/"+lang+"/introduce20.html", list, details, twitter);
        
        //render(list, details, twitter);
    }
    
    private static Map<String, String> getMap(Source source, String id) {
        
        Map<String, String> map = new HashMap<String, String>();
        
        map.put("id", id);
        map.put("benefits", getString(source.getElementById(id).getChildElements().get(1)));
        
        return map;
    }
    
    private static String getString(Element elem) {
        return elem != null ? elem.toString() : "";
    }
}