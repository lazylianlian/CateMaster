package com.catemaster.catemaster.bean;

/**
 * 发现美食模块实体类
 * Created by 文捷 on 2017/1/8.
 */

public class FindCateInfo {
    private String userName;
    private String title;
    private String content;
    private String imgUrl;

    public FindCateInfo() {
        super();
    }

    public FindCateInfo(String userName, String title, String content, String imgUrl) {
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
