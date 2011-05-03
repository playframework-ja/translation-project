package models;

import java.util.*;

import org.joda.time.*;
import org.joda.time.format.*;

public class Version implements Comparable<Version> {

    private static DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM dd, yyyy").withLocale(Locale.US);

    public String version;
    public String publishedAt;
    public Boolean isDefault;

    public Version(String version, String publishedAt, Boolean isDefault) {
        this.version = version;
        this.publishedAt = publishedAt;
        this.isDefault = isDefault;
    }

    @Override
    public int compareTo(Version o) {

        DateTime dt1 = formatter.parseDateTime(this.publishedAt);
        DateTime dt2 = formatter.parseDateTime(o.publishedAt);

        return Days.daysBetween(dt1, dt2).getDays();
    }
}
