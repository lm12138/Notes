package com.edu.notes.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.edu.notes.R;
import com.edu.notes.bean.Dynamic;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;


public class PhotoAdapter extends ArrayAdapter <Dynamic>{

    List<Dynamic> photo;

    public PhotoAdapter(Context context, int resource, List <Dynamic>photo) {
        super(context, resource, photo);
        this.photo = photo;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=null;
        ViewHolder viewHolder;
        //如果是第一次创建view
        if (convertView == null) {
            //将子布局传到上下文
            view = LayoutInflater.from(getContext()).inflate(R.layout.myphoto_item, null);
            viewHolder = new ViewHolder();

            viewHolder.tvImage= (ImageView) view.findViewById(R.id.tvImage);
            viewHolder.tvCheck= (CheckBox) view.findViewById(R.id.tvCheck);
            view.setTag(viewHolder);
        }
        //之前已经创建了类似的view了
        else{
            view=convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
BmobFile file=photo.get(position).getContentPicture();
            if(file!=null) {
                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getContext()));
                ImageLoader.getInstance().displayImage(
                        file.getFileUrl(getContext()), viewHolder.tvImage,//图片
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
            }else{

            }

       // viewHolder.tvCheck.setChecked(getIsSelected().get(position));
        return view;
    }

    //为了提高查找调高效率，ViewHolder类
    public static class ViewHolder {
        public ImageView tvImage;
        public CheckBox tvCheck;
    }
}
