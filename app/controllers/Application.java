package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import models.Download;

import org.apache.commons.lang.StringUtils;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.CollaboratorService;
import org.eclipse.egit.github.core.service.UserService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.test.Fixtures;

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
        redirect(String.format("/documentation/%s/",
                Play.configuration.getProperty("version.latest")));
    }

    /**
     * download action.
     */
    public static void download() {
        Map<String, List<Download>> downloads = new LinkedHashMap<String, List<Download>>();
        for (Download d : (List<Download>) Fixtures.loadYaml("downloads.yml")) {
            String key = d.version.substring(0, 3);
            List<Download> list = downloads.get(key);
            if (list == null) {
                list = new ArrayList<Download>();
            }
            list.add(d);
            downloads.put(key, list);
        }
        render(downloads);
    }

    /**
     * changelog action.
     */
    public static void changelog() {
        render();
    }

    /**
     * get-involved action.
     */
    public static void getInvolved() {
        render();
    }

    /**
     * security action.
     */
    public static void security() {
        redirect("/security/vulnerability");
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