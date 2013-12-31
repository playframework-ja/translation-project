package models;

public class Download {

    public String url;
    public String txt;
    public String date;
    public String size;
    public String misc;
    public String version;

    public Download(String url, String txt, String date, String size, String misc) {
        this.url = url;
        this.txt = txt;
        this.date = date;
        this.size = size;
        this.misc = misc;
        String[] arr = url.split("/");
        this.version = arr[arr.length - 2];
    }
}
