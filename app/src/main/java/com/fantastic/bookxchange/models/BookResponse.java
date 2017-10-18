package com.fantastic.bookxchange.models;

/**
 * Created by gretel on 10/18/17.
 */

public class BookResponse {

    private String bib_key;
    private String thumbnail_url;
    private String title;
    private String[] authors;
    private String[] publishers;
    private String description;


    public BookResponse() {
        this.bib_key = bib_key;
        this.thumbnail_url = thumbnail_url;
        this.title = title;
        this.authors = authors;
        this.publishers = publishers;
        this.description = description;
    }

    public String getBib_key() {
        return bib_key;
    }

    public void setBib_key(String bib_key) {
        this.bib_key = bib_key;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public String[] getPublishers() {
        return publishers;
    }

    public void setPublishers(String[] publishers) {
        this.publishers = publishers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
