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

/**
 * Application controller.
 * 
 * @author garbagetown
 * 
 */
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
        render();
    }

    /**
     * documentation action.
     * 
     * @param version
     */
    public static void documentation(String version) {
        if (version == null) {
            version = Play.configuration.getProperty("version.latest");
        }
        Documentation.page(version, "home");
    }

    /**
     * download action.
     * 
     * @param action
     * @throws MalformedURLException
     * @throws IOException
     */
    public static void download() throws MalformedURLException, IOException {

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
        render(downloads, latest, upcoming, olders, nightlies);
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

    /**
     * code action.
     * 
     * @param action
     */
    public static void code() {
        render();
    }

    /**
     * about action.
     * 
     * @throws FileNotFoundException
     */
    public static void about() throws FileNotFoundException {
        List<Map<String, String>> translators = (List<Map<String, String>>) new Yaml().load(new FileInputStream(
                Play.applicationPath + "/conf/translators.yml"));
        render(translators);
    }
}