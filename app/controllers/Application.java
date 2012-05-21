package controllers;

import java.io.*;
import java.net.*;
import java.util.*;

import models.Download;

import org.apache.commons.lang.*;
import org.eclipse.egit.github.core.*;
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
        String news = "";
        try {
            Document doc = Jsoup.connect("http://www.playframework.org/").get();
            news = doc.select("div#news").first().html();
        } catch (IOException e) {
            // do anything
        }
        render(news);
    }

    /**
     * documentation action.
     * 
     * @param version
     */
    public static void documentation() {
        redirect(String.format("/documentation/%s/home", Play.configuration.getProperty("version.latest")));
    }

    /**
     * download action.
     * 
     * @param action
     * @throws MalformedURLException
     * @throws IOException
     */
    public static void download() throws MalformedURLException, IOException {

        Download latest = null;
        List<Download> upcomings = null;
        List<Download> olders = null;

        Document doc = Jsoup.connect("http://www.playframework.org/download").get();
        Elements elements = doc.select("article table");

        // the first table must have latest version
        latest = toDownload(elements.first());
        // the last table must have older versions
        olders = toDownloads(elements.last());
        // if there are more than two tables, middle of them might be upcomings
        if (elements.size() > 2) {
            upcomings = toDownloads(elements.get(1));
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
     * @throws IOException
     */
    public static void about() throws IOException {
        CollaboratorService service = new CollaboratorService();
        String owner = Play.configuration.getProperty("github.owner");
        String name = Play.configuration.getProperty("github.name");
        RepositoryId repository = new RepositoryId(owner, name);
        List<User> collaborators = new ArrayList<User>();
        UserService userService = new UserService();
        for (User user : service.getCollaborators(repository)) {
            collaborators.add(userService.getUser(user.getLogin()));
        }
        render(collaborators);
    }
}