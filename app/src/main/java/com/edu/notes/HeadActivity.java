package com.edu.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.notes.adapter.DynamicAdapter;
import com.edu.notes.bean.Dynamic;
import com.edu.notes.bean.MyUser;
import com.edu.notes.R;
import com.edu.notes.fragment.MeFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class HeadActivity extends AppCompatActivity {
    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    public static final String IMAGE_UNSPECIFIED = "image/*";
    CircleImageView head_image_cut;
    public MyUser user;
    Bitmap photo;
    File  pictureld;
    String picturePath;
    TextView user_phone;
    EditText user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head);
        user_phone=(TextView)findViewById(R.id.user_phone);
        getUser();
        user_name=(EditText)findViewById(R.id.user_name);
        getName();
        head_image_cut=(CircleImageView)findViewById(R.id.head_image_cut);
        Intent intent=getIntent();
//        intent.getParcelableExtra("bitmap");
        intent.getParcelableExtra("bitmap");
        head_image_cut.setImageBitmap((Bitmap) intent.getParcelableExtra("bitmap"));
        user=BmobUser.getCurrentUser(getApplicationContext(),MyUser.class);

    }
    public void photoCancle(View view){
        finish();
    }

    public void cutPhoto(View view){
        new AlertDialog.Builder(this)
                .setTitle("选择来源")
                .setItems(new String[]{"拍照", "图库"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0://拍照
                                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
                                startActivityForResult(intent2, PHOTOHRAPH);
                                break;
                            case 1://图库
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, PHOTOZOOM);
                                break;
                        }
                    }
                }).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == NONE)
            return;
        // 拍照
        if (requestCode == PHOTOHRAPH) {
            //设置文件保存路径这里放在跟目录下
            pictureld = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
            startPhotoZoom(Uri.fromFile(pictureld));
        }

        if (requestCode == PHOTOZOOM && resultCode == RESULT_OK && null != data) {


            Uri selectedImage = data.getData();//获得图片的结对路径
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            pictureld=new File(picturePath);
            startPhotoZoom(Uri.fromFile(pictureld));

        }

        // 处理结果
        if (requestCode == PHOTORESOULT) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                photo = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0 - 100)压缩文件
                head_image_cut.setImageBitmap(photo);

                //获取剪裁后的图片uri
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), photo, null,null));

                picturePath = getImagePath(uri, null);
            }

        }

    }

    //获取path
    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTORESOULT);
    }
    //上传图片
    //--------------------------------------杂乱区--------------------------------------------------
    public void submitPhoto(View v) {
        //获取昵称
        final String name=user_name.getText().toString();
        //上传头像文件
        if (picturePath!=null){
            final BmobFile bmobFile = new BmobFile(new File (picturePath));
            bmobFile.uploadblock(HeadActivity.this, new UploadFileListener() {
                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                }

                @Override
                public void onSuccess() {
                    //头像文件上传成功
                    upload(bmobFile,name);
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.i("错误信息", "onFailure: " + i);
                    Toast.makeText(getApplication(), "头像文件上传失败！"+i, Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(getApplication(), "头像没变！", Toast.LENGTH_SHORT).show();
            upload(null,name);
        }
    }
    //更新数据库信息
    public void upload(BmobFile file,String name){
        user.setHeadPortrait(file);
        user.setNickname(name);
        user.update(getApplication(), new UpdateListener() {
            @Override
            public void onSuccess() {
                finish();
                Toast.makeText(getApplication(),"更新成功",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplication(),"更新失败"+i,Toast.LENGTH_SHORT).show();
            }
        });
    }
//--------------------------------------------------------------------------------------------------

    private void getUser() {
        String username = (String) BmobUser.getObjectByKey(getApplicationContext(), "username");
        user_phone.setText(username);
    }
    private void getName() {
        String username = (String) BmobUser.getObjectByKey(getApplicationContext(), "nickname");
        user_name.setText(username);
    }
}
