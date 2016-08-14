package com.bupt.affectionfornurse.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.bupt.affectionfornurse.R;
import com.bupt.affectionfornurse.common.BaseActivity;
import com.bupt.affectionfornurse.common.PreferencesUtil;
import com.bupt.affectionfornurse.common.UserConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class UploadPhotoActivity extends BaseActivity {

    private SimpleDraweeView my_image_view;
    private Button btn_takephoto, btn_chosepic;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 0x01;
    private static final int REQUEST_CODE_PICK_IMAGE = 0x02;
    private ImageView iv_common_back;
    private TextView tv_confirm;
    private ProgressDialog dialog;
    private ProgressDialog processDialog;
    private List<String> messageList;
    private String parentId;
    private String lastPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_upload_photo);
        initUI();
        messageList = new ArrayList<>();
        dialog = new ProgressDialog(this);
        processDialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.uploadiong));
        processDialog.setMessage(getString(R.string.processing));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_take:
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
                } else {
                    Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_chose:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");//相片类型
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                break;
            case R.id.iv_common_back:
                finish();
                break;
            case R.id.tv_confirm:
                if (null != my_image_view.getTag()){
                    if (lastPic != my_image_view.getTag().toString()){
                        dialog.show();
                        parentId = PreferencesUtil.getString(getBaseContext(), UserConfig.PRENTID);

                        // 执行 CQL 语句实现更新一个 Parents 对象的pic字段
                        AVQuery<AVObject> avQuery = new AVQuery<>("Parents");
                        avQuery.getInBackground(parentId, new GetCallback<AVObject>() {
                            @Override
                            public void done(AVObject avObject, AVException e) {
                                List<String> dataList = new ArrayList<String>();
                                dataList = (List<String>) avObject.get("pic");
                                messageList.clear();
                                for (String data :dataList){
                                    messageList.add(data);
                                }
                                messageList.add(my_image_view.getTag().toString());
                                AVObject parent = AVObject.createWithoutData("Parents",parentId);
                                parent.put("pic",messageList);
                                parent.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null){
                                            dialog.dismiss();
                                            lastPic = my_image_view.getTag().toString();
                                            Toast.makeText(UploadPhotoActivity.this, "上传成功", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });
                    }else {
                        Toast.makeText(UploadPhotoActivity.this, getString(R.string.voiddup), Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(UploadPhotoActivity.this, getString(R.string.nopic), Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    private void initUI() {
        my_image_view = (SimpleDraweeView) findViewById(R.id.my_image_view);
        btn_takephoto = (Button) findViewById(R.id.btn_take);
        btn_chosepic = (Button) findViewById(R.id.btn_chose);
        iv_common_back = (ImageView) findViewById(R.id.iv_common_back);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        setListener(btn_takephoto,btn_chosepic,iv_common_back,tv_confirm);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        processDialog.show();
        if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            Uri uri = data.getData();
//            Log.d("uri", uri.toString());
            if (uri == null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Bitmap photo = (Bitmap) bundle.get("data"); //get bitmap
                    String path = saveImage(photo);

                    if (null != path) {
                        try {
                            String fileName = System.currentTimeMillis() + ".jpg";
                            final AVFile file = AVFile.withAbsoluteLocalPath(fileName, path);
                            file.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    // 成功或失败处理...

                                    if (e == null) {
                                        String url = file.getUrl().toString();
                                        Uri uri = Uri.parse(url);
                                        my_image_view.setImageURI(uri);
                                        my_image_view.setTag(url);

                                    } else {
                                        Toast.makeText(UploadPhotoActivity.this, "获取图片失败", Toast.LENGTH_LONG).show();

                                    }
                                    processDialog.dismiss();

                                }
                            });

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "err****", Toast.LENGTH_LONG).show();
                }

            }
        } else if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            Uri uri = data.getData();
//            Log.e("123",uri.getPath());
            ContentResolver resolver = getContentResolver();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, uri);
                //获取图片路径
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(uri, proj, null, null, null);
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                //根据索引值获取图片
                String path = cursor.getString(index);
                String fileName = System.currentTimeMillis() + ".jpg";
                final AVFile file = AVFile.withAbsoluteLocalPath(fileName, path);
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        // 成功或失败处理...

                        if (e == null) {
                            String url = file.getUrl().toString();
                            Log.e("123",url);
                            Uri uri = Uri.parse(url);
                            my_image_view.setImageURI(uri);
                            my_image_view.setTag(url);
                        } else {
                            Toast.makeText(UploadPhotoActivity.this, "获取图像失败", Toast.LENGTH_LONG).show();

                        }
                        processDialog.dismiss();

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String saveImage(Bitmap photo) {

        String fileName = System.currentTimeMillis() + ".jpg";
        String path = Environment.getExternalStorageDirectory() + "/" + fileName;
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(path, false));
            photo.compress(Bitmap.CompressFormat.JPEG,100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return path;
    }
}
