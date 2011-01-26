package controllers;

import java.io.*;
import java.net.*;
import java.util.*;

import models.*;
import net.htmlparser.jericho.*;

import org.yaml.snakeyaml.*;

import play.*;
import play.mvc.*;

public class Application extends Controller {

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
        
        Long downloads = null;
        Download latest = null;
        List<Download> olders = null;
        List<Download> nightlies = null;
        
        Source source = new Source(new URL("http://www.playframework.org/download"));
        
        downloads = toDownloads(source.getFirstElementByClass("category"));
        
        List<Element> tables = source.getAllElements(HTMLElementName.TABLE);
        
        for (int i = 0, n = tables.size(); i < n; i++) {
            
            List<Element> elements = tables.get(i).getAllElements(HTMLElementName.TR);
            
            switch (i) {
            case 0:
                latest = toDownload(elements).get(0);
                break;
            case 1:
                olders = toDownload(elements);
            case 2:
                nightlies = toDownload(elements);
            default:
                break;
            }
        }
        render(action, downloads, latest, olders, nightlies);
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

            if (keyword == null
                    || module.id.contains(keyword)
                    || module.name.contains(keyword)
                    || module.description.contains(keyword)) {
                modules.add(module);
            }
        }
        
        Collections.sort(modules);
        
        render(action, modules);
    }
    
    public static void about(String action) {
        render(action);
    }
}