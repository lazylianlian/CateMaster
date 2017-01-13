package com.catemaster.catemaster.bean;

import java.util.List;

/**
 * Created by 文捷 on 2017/1/9.
 */

public class CateCollectionInfo {
    private String id;
    private String title;
    private String albums;
    private int checked;

    public CateCollectionInfo() {
        super();
    }

    public CateCollectionInfo(String id, String title, String albums, int checked) {
        this.id = id;
        this.title = title;
        this.albums = albums;
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbums() {
        return albums;
    }

    public void setAlbums(String albums) {
        this.albums = albums;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }
}
