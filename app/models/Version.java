package models;

public class Version {

    public String version;
    public String publishedAt;
    public Boolean isDefault;

    public Version(String version, String publishedAt, Boolean isDefault) {
        this.version = version;
        this.publishedAt = publishedAt;
        this.isDefault = isDefault;
    }
}
