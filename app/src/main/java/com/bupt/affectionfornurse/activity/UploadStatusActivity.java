package com.bupt.affectionfornurse.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.bupt.affectionfornurse.R;

public class UploadStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_status);

       // initView();
    }

    /*private void initView() {
        //MainActivity的布局文件中的主要控件初始化
        Toolbar mToolbar = (Toolbar) this.findViewById(R.id.tool_bar_ocr_result);
        //初始化ToolBar
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("OCR结果");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadStatusActivity.this, UploadPhotoActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }*/
}
