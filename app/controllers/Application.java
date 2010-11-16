package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import models.Module;

import org.yaml.snakeyaml.JavaBeanParser;
import org.yaml.snakeyaml.Yaml;

import play.Logger;
import play.Play;
import play.mvc.Controller;

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

    public static void code(String action) {
        render(action);
    }

    public static void ecosystem(String action) {
        render(action);
    }

    public static void modules(String action) throws FileNotFoundException {
        
        File[] dirs = new File(Play.applicationPath, "documentation/modules").listFiles();
        
        List<Module> modules = new ArrayList<Module>();
        
        for (File dir : dirs) {
            
            Logger.info("loading %s's manifest...", dir.getName());
            
            File manifest = new File(dir, "manifest");
            Map<String, Object> map = (Map<String, Object>) new Yaml().load(new FileInputStream(manifest));
            modules.add(new Module(map));
        }
        
        Collections.sort(modules);
        
        render(action, modules);
    }
    
    public static void about(String action) {
        render(action);
    }
}