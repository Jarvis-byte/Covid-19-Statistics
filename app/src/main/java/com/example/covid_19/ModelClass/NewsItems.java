package com.example.covid_19.ModelClass;

public class NewsItems {
    String title;
    String content;
    String link;
    String urlToImage;
    String reference;
   String pubDate;

    public NewsItems(String title, String content, String link, String urlToImage, String reference, String pubDate) {
        this.title = title;
        this.content = content;
        this.link = link;
        this.urlToImage = urlToImage;
        this.reference = reference;
        this.pubDate = pubDate;
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

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getReference() {
        return reference;
    }

    public String getPubDate() {
        return pubDate;
    }
}
