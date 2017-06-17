package com.edu.notes.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.edu.notes.LoginActivity;
import com.edu.notes.bean.MyUser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.edu.notes.HeadActivity;
import com.edu.notes.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by WangChang on 2016/5/15.
 */
public class MeFragment extends Fragment {

    View view;
    private Button bt_quit;
    private Button bt_theme;
    private Button bt_collect;
    private Button bt_help;
    CircleImageView head_image;
    TextView username;
    TextView phone;
    Button lv_myPhoto;
    Button our;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.person_info, container, false);

        init();
        getUserInfo();
        return view;
    }


    private void getUserInfo() {
        MyUser myUser=BmobUser.getCurrentUser(getActivity(),MyUser.class);


        username.setText(myUser.getNickname());
        phone.setText(myUser.getUsername());

        BmobFile icon=myUser.getHeadPortrait();
        if(icon!=null) {
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getContext()));
            ImageLoader.getInstance().displayImage(
                    icon.getFileUrl(getActivity()), head_image,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(
                                String imageUri, View view,
                                Bitmap loadedImage) {

                            super.onLoadingComplete(
                                    imageUri, view,
                                    loadedImage);
                        }

                    });
        }
    }

    public void init() {
        username=(TextView)view.findViewById(R.id.username);
        phone=(TextView)view.findViewById(R.id.phone);
        head_image = (CircleImageView) view.findViewById(R.id.head_image);
        head_image.setOnClickListener(new CutPic());
        bt_quit = (Button) view.findViewById(R.id.quit);
        bt_quit.setOnClickListener(new quitclock());
        lv_myPhoto= (Button) view.findViewById(R.id.button4);
        lv_myPhoto.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                showBuilder();
            }
        });
        bt_theme = (Button) view.findViewById(R.id.button2);
        bt_theme.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                showBuilder();
            }
        });
        bt_collect = (Button) view.findViewById(R.id.button3);
        bt_collect.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                showBuilder();
            }
        });
        our=(Button)view.findViewById(R.id.our);
        our.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showabout();
            }
        });



        bt_help = (Button) view.findViewById(R.id.help);
        bt_help.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                showBuilder();
            }
        });
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showabout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.about);
        builder.setCancelable(true);
        builder.show();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showBuilder( ){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.dialog_layout);
        builder.setCancelable(true);
        builder.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }


    private class CutPic implements View.OnClickListener {
        @Override
        public void onClick(View view) {
          Intent intent=new Intent(getActivity(),HeadActivity.class);
            intent.putExtra("bitmap",((BitmapDrawable) head_image.getDrawable()).getBitmap());
            startActivity(intent);
        }
    }
    private class quitclock implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            BmobUser.logOut(getActivity());
            Intent intent = new Intent(getActivity(),LoginActivity.class);
            getActivity().finish();
            startActivity(intent);


        }
    }

}
