package com.edu.notes;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.notes.bean.Dynamic;
import com.edu.notes.R;
import com.edu.notes.bean.MyUser;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;


public class CreateActivity extends AppCompatActivity {
    TextView iv_right;
    TextView iv_left;
    EditText etContent;
    ImageView ivPhoto;
    String picturePath;
    private static int RESULT_LOAD_IMAGE = 1;
    public Dynamic myPublish=new Dynamic();
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        init();
    }

    private void init() {
        iv_right = (TextView) findViewById(R.id.iv_right);
        iv_left = (TextView) findViewById(R.id.iv_left);
        etContent = (EditText) findViewById(R.id.etContent);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        iv_right.setOnClickListener(new submit());
        iv_left.setOnClickListener(new cancel());
        ivPhoto.setOnClickListener(new addPhoto());
    }
//取消（返回按钮）
    private class cancel implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }
    }
//拉取相册
    private class addPhoto implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
    }
//startActivityForResult回调函数得到图片地址
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {


            Uri selectedImage = data.getData();//获得图片的结对路径
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            ivPhoto.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            System.out.println("图片地址："+picturePath);

        }
    }
    //上传内容和图片（可以为空）
    private class submit implements View.OnClickListener {
        private String  s;
        public void onClick(View view) {

           s = String.valueOf(etContent.getText());
            if (!s.isEmpty()){

                MyUser myUser=BmobUser.getCurrentUser(getApplicationContext(),MyUser.class);
                myPublish.setUserid(myUser.getObjectId());
                myPublish.setContent(s);
                myPublish.setAuthor(myUser);

            }else {
                Toast.makeText(getApplication(),"内容不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            if(null==picturePath){
                upload();
            }else{
                final BmobFile file = new BmobFile(new File(picturePath));
                file.uploadblock(CreateActivity.this,new UploadFileListener() {
                    @Override
                    public void onProgress(Integer value) {
                        // 返回的上传进度（百分比）
                    }
                    @Override
                    public void onSuccess() {

                        Toast.makeText(getApplication(),"文件上传成功！",Toast.LENGTH_SHORT).show();
                        myPublish.setContentPicture(file);
                        upload();
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        Log.i("错误信息", "onFailure: "+i);
                        Toast.makeText(getApplication(),"文件上传失败！",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
    public void upload(){
        finish();
        myPublish.save(CreateActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
//                finish();
                Toast.makeText(getApplication(),"发表成功...",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplication(),"发表失败...",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
