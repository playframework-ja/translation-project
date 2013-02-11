package controllers;

import java.io.*;
import java.util.*;

import models.Download;

import org.apache.commons.lang.*;
import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

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
     */
    public static void documentation() {
        redirect(String.format("/documentation/%s/", Play.configuration.getProperty("version.latest")));
    }

    /**
     * download action.
     */
    public static void download() {

        Download latest = null;
        List<Download> upcomings = null;
        List<Download> olders = null;

        try {
            Document doc = Jsoup.connect("http://www.playframework.com/download").get();
            Elements elements = doc.select("article table");

            if (elements != null && elements.size() >= 2) {
                // the first table must have latest version
                latest = toDownload(elements.first());
                // the last table must have older versions
                olders = toDownloads(elements.last());
                // if there are more than two tables, middle of them might be
                // upcomings
                if (elements.size() > 2) {
                    upcomings = toDownloads(elements.get(1));
                }
            }
        } catch (IOException e) {
            Logger.warn(e.getMessage());
        }
        render(latest, upcomings, olders);
    }

    private static Download toDownload(Element table) {
        List<Download> downloads = toDownloads(table);
        if (downloads.size() < 1) {
            return null;
        } else {
            return downloads.get(0);
        }
    }

    private static List<Download> toDownloads(Element table) {
        List<Download> downloads = new ArrayList<Download>();
        List<Element> rows = table.select("tr");
        for (Element row : rows) {
            List<Element> cols = row.select("td");
            if (cols.size() < 3) {
                continue;
            }
            String url = cols.get(0).select("a").attr("href");
            String date = cols.get(1).html().trim();
            String size = cols.get(2).html().trim();
            downloads.add(new Download(url, date, size));
        }
        return downloads;
    }
    
    /**
     * get-involved action.
     */
    public static void getInvolved() {
        render();
    }

    /**
     * code action.
     */
    public static void code() {
        List<String> zenexities = new ArrayList();
        List<String> typesafes = new ArrayList();
        List<String> lunatechLabs = new ArrayList();
        List<String> others = new ArrayList();
        try {
            Document doc = Jsoup.connect("http://www.playframework.com/code").get();
            Elements elements = doc.select("ul.contributors");
            if (elements.size() >= 4) {
                zenexities = toList(elements.get(0));
                typesafes = toList(elements.get(1));
                lunatechLabs = toList(elements.get(2));
                others = toList(elements.get(3));
            }
        } catch (IOException e) {
            Logger.warn(e.getMessage());
        }
        render(zenexities, typesafes, lunatechLabs, others);
    }

    private static List<String> toList(Element element) {
        List<String> list = new ArrayList();
        Elements elements = element.getElementsByTag("li");
        if (elements != null) {
            for (Element e : elements) {
                list.add(e.toString());
            }
        }
        return list;
    }
    
    /**
     * about action.
     * 
     * @throws IOException
     */
    public static void about() throws IOException {
        CollaboratorService service = new CollaboratorService();
        String owner = Play.configuration.getProperty("github.owner");
        String name = Play.configuration.getProperty("github.name");
        RepositoryId repository = new RepositoryId(owner, name);
        List<User> collaborators = new ArrayList<User>();
        UserService userService = new UserService();
        try {
            for (User user : service.getCollaborators(repository)) {
                collaborators.add(userService.getUser(user.getLogin()));
            }
        } catch (RequestException e) {
            Logger.warn(e.getMessage());
        }
        render(collaborators);
    }
}