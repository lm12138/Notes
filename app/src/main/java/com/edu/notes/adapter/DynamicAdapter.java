package com.edu.notes.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.notes.bean.Dynamic;
import com.edu.notes.bean.MyUser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.edu.notes.R;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by liuzhe on 2017/6/3.
 */

public class DynamicAdapter extends ArrayAdapter<Dynamic> {
    private int resourceId;
    List<Dynamic> data;
    MyUser myUser;
    public DynamicAdapter(Context context, int resource, List<Dynamic> data) {
        super(context, resource, data);
        resourceId=resource;

         myUser=BmobUser.getCurrentUser(getContext(),MyUser.class);




        this.data = data;
    }
    public Dynamic getid(int d) {
        return data.get(d);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            holder = new ViewHolder();
            holder.iv_head = (ImageView) view.findViewById(R.id.head_iv);
            holder.tv_name = (TextView) view.findViewById(R.id.name_tv);
            holder.tv_time = (TextView) view.findViewById(R.id.time_tv);
            holder.tv_contents = (TextView) view.findViewById(R.id.contents_tv);
            holder.iv_picture = (ImageView) view.findViewById(R.id.picture_iv);
            view.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        Log.d("1111111:",data.get(position).getContent());
        holder.tv_contents.setText(data.get(position).getContent());
        //holder.tv_name.setText(data.get(position).getAuthor().getNickname());
        holder.tv_name.setText(myUser.getNickname());
        holder.tv_time.setText(data.get(position).getUpdatedAt());


        //头像
        BmobFile headBitmap = myUser.getHeadPortrait();
        //BmobFile headBitmap = data.get(position).getAuthor().getHeadPortrait();

        if (headBitmap != null){
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getContext()));
            ImageLoader.getInstance().displayImage(
                    headBitmap.getFileUrl(getContext()), holder.iv_head,//图片
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(
                                String imageUri, View view,
                                Bitmap loadedImage) {
                            // TODO Auto-generated method
                            // stub
                            super.onLoadingComplete(
                                    imageUri, view,
                                    loadedImage);
                        }

                    });
        }
        //获取每条记录的用户信息

//        内容图片
        BmobFile icon = data.get(position).getContentPicture();
        if (icon != null){
            holder.iv_picture.setVisibility(View.VISIBLE);
            holder.iv_picture.setDrawingCacheEnabled(true);//设置缓存
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getContext()));
                ImageLoader.getInstance().displayImage(
                        icon.getFileUrl(getContext()), holder.iv_picture,//图片
                        new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(
                                    String imageUri, View view,
                                    Bitmap loadedImage) {
                                // TODO Auto-generated method
                                // stub
                                super.onLoadingComplete(
                                        imageUri, view,
                                        loadedImage);
                            }

                        });
        }else {
            holder.iv_picture.setVisibility(View.GONE); //隐藏
        }
        return view;
    }
    class ViewHolder{
        ImageView iv_head;      //  头像
        TextView tv_name;       //  昵称
        TextView tv_time;       //  时间
        TextView tv_contents;   //  内容
        ImageView iv_picture;   //  图片
        ImageView iv_like;      //  赞图标


    }

}
