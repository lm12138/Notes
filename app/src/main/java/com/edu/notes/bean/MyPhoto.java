package com.edu.notes.bean;

import android.net.Uri;

/**
 * Created by 跃樽 on 2017/6/4.
 */

public class MyPhoto {
    private Uri pic_path;//图片路径

    public Uri getPic_path() {
        return pic_path;
    }


    public void setPic_path(Uri pic_path) {
        this.pic_path = pic_path;
    }

    public MyPhoto(Uri pic_path) {
        this.pic_path = pic_path;
    }
}
