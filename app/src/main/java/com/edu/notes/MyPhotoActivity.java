package com.edu.notes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.edu.notes.adapter.PhotoAdapter;
import com.edu.notes.bean.Dynamic;
import com.edu.notes.bean.MyUser;


import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;



public class MyPhotoActivity extends AppCompatActivity {
    private ListView lv_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myphoto);
        lv_list = (ListView) findViewById(R.id.lvList);
        getPhoto();
        lv_list.setOnItemClickListener(new OnItemClick());

    }


    private class OnItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            PhotoAdapter.ViewHolder viewHolder = (PhotoAdapter.ViewHolder) arg1.getTag();
            viewHolder.tvCheck.toggle();
        }
    }

    private void getPhoto() {
        BmobQuery<Dynamic> bmobQuery = new BmobQuery();
        final Dynamic dynamic=new Dynamic();
        MyUser user = BmobUser.getCurrentUser(getApplication(), MyUser.class);
        bmobQuery.addWhereEqualTo("userid", user.getObjectId());
        bmobQuery.addQueryKeys("ContentPicture");
        bmobQuery.findObjects(getApplication(), new FindListener<Dynamic>() {
            @Override
            public void onSuccess(List<Dynamic> list) {
                PhotoAdapter photoAdapter = new PhotoAdapter(getApplication(), R.layout.myphoto_item, list);
                lv_list.setAdapter(photoAdapter);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(getApplication(), "相册获取失败" + s, Toast.LENGTH_SHORT).show();
            }
        });


    }
}
