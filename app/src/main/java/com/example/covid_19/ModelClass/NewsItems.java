package com.example.covid_19.ModelClass;

public class NewsItems {
    String title;
    String content;
    String link;
    String urlToImage;
    String reference;
   String pubDate;

    public NewsItems(String title, String content, String link) {
        this.title = title;
        this.content = content;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }
}
