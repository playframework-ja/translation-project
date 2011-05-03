package models;

import java.util.*;

import util.*;

public class Module implements Comparable<Module> {

    public String id;
    public String name;
    public String description;
    public String url;
    public String author;
    public List<Version> versions;
    
    public Module(Map<String, Object> outerMap) {

        this.id = (String) outerMap.get("id");
        this.name = (String) outerMap.get("name");
        this.description = Textile.toHTML((String) outerMap.get("description"));
        this.url = (String) outerMap.get("url");
        this.author = (String) outerMap.get("author");
        
        this.versions = new ArrayList<Version>();
        
        List<Map<String, Object>> innerMap =
            (List<Map<String, Object>>) outerMap.get("versions");
        
        if (innerMap != null) {
            for (Map<String, Object> version : innerMap) {
                versions.add(
                        new Version(
                                (String) version.get("version"),
                                (String) version.get("publishedAt"),
                                (Boolean) version.get("isDefault")));
            }
            Collections.sort(versions);
        }
    }

    public int compareTo(Module o) {
        return this.id.compareTo(o.id);
    }
}
