package com.fantastic.bookxchange.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by gretel on 10/13/17.
 */
@Parcel
public class BookResponse {


    private String bib_key;

    private String preview;

    private String thumbnail_url;

    private String preview_url;

    private String info_url;

    public void setBib_key(String bib_key){
        this.bib_key = bib_key;
    }
    public String getBib_key(){
        return this.bib_key;
    }
    public void setPreview(String preview){
        this.preview = preview;
    }
    public String getPreview(){
        return this.preview;
    }
    public void setThumbnail_url(String thumbnail_url){
        this.thumbnail_url = thumbnail_url;
    }
    public String getThumbnail_url(){
        return this.thumbnail_url;
    }
    public void setPreview_url(String preview_url){
        this.preview_url = preview_url;
    }
    public String getPreview_url(){
        return this.preview_url;
    }
    public void setInfo_url(String info_url){
        this.info_url = info_url;
    }
    public String getInfo_url(){
        return this.info_url;
    }

}
