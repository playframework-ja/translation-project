package models;

import java.text.*;
import java.util.*;

public class Download {

    public static final String DATE_FORMAT = "MMM dd yyyy";

    public static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);

    public String url;
    public String name;
    public String version;
    public Date date;
    public Double size;

    public Download(String url, String date, String size) {
        try {
            this.url = url;
            this.name = url.substring(url.lastIndexOf("/") + 1, url.length());
            this.version = name.substring("play-".length(), name.lastIndexOf(".zip"));
            this.date = dateFormat.parse(date);
            this.size = new Double(size.replace("M", ""));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
