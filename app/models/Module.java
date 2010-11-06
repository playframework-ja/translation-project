package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.textile.core.TextileLanguage;

import util.Textile;

import controllers.Documentation;

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
        
        for (Map<String, Object> version : innerMap) {
            versions.add(
                    new Version(
                            (String) version.get("version"),
                            (String) version.get("publishedAt"),
                            (Boolean) version.get("isDefault")));
        }
    }

    public int compareTo(Module o) {
        return this.id.compareTo(o.id);
    }
}
