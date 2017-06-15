package com.edu.notes.bean;

import  cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;


public class Dynamic extends BmobObject {
    private String userid;
    private String content;
    private BmobFile ContentPicture;
    private String Fabulous;
    private MyUser Author;

    public MyUser getAuthor() {
        return Author;
    }

    public void setAuthor(MyUser author) {
        Author = author;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobFile getContentPicture() {
        return ContentPicture;
    }

    public void setContentPicture(BmobFile contentPicture) {
        ContentPicture = contentPicture;
    }

    public String getFabulous() {
        return Fabulous;
    }

    public void setFabulous(String fabulous) {
        Fabulous = fabulous;
    }
}
