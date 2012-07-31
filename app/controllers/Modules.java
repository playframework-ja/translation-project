package controllers;

import java.io.*;
import java.util.*;

import jobs.*;
import models.*;

import org.yaml.snakeyaml.*;

import play.*;
import play.mvc.*;
import play.utils.Properties;

public class Modules extends Controller {

    public static void index(String id) throws FileNotFoundException {

        String action = "modules";

        Module module = getModule(id);
        List<Version> versions = module.versions;

        render(action, module, versions);
    }

    public static void page(String id, String version) throws IOException {

        String action = "modules";

        Module module = getModule(id);
        List<Version> versions = module.versions;

        String basepath = getVersionPath(id, version);

        File manifest = new File(basepath, "/manifest");

        Properties prop = new Properties();
        prop.load(new FileInputStream(manifest));

        String frameworkVersions = prop.get("frameworkVersions");

        File page = new File(basepath, "documentation/manual/home.textile");
        if (!page.exists()) {
            notFound(page.getPath());
        }
        String html = DocumentParser.parseTextileFile(page);

        render(action, id, version, frameworkVersions, versions, html);
    }

    public static void image(String id, String version, String name) {
        File image = new File(getVersionPath(id, version) + "/documentation/images/" + name + ".png");
        if (!image.exists()) {
            notFound();
        }
        renderBinary(image);
    }

    static Module getModule(String id) throws FileNotFoundException {

        File manifest = new File(getModulePath(id) + "/manifest");

        Map<String, Object> map = (Map<String, Object>) new Yaml().load(new FileInputStream(manifest));

        return new Module(map);
    }

    static String getModulePath(String id) {

        StringBuilder builder = new StringBuilder();
        builder.append(Play.applicationPath);
        builder.append("/documentation/modules/");
        builder.append(id);

        return builder.toString();
    }

    static String getVersionPath(String id, String version) {

        StringBuilder builder = new StringBuilder();
        builder.append(getModulePath(id));
        builder.append("/");
        builder.append(id);
        builder.append("-");
        builder.append(version);

        return builder.toString();
    }
}