package com.bupt.affectionfornurse.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.bupt.affectionfornurse.R;
import com.bupt.affectionfornurse.common.BaseActivity;
import com.bupt.affectionfornurse.common.PreferencesUtil;
import com.bupt.affectionfornurse.common.UserConfig;

import java.util.List;

public class MainActivity extends BaseActivity {

    private RelativeLayout rl_uploadpic, rl_uploadstatus, rl_notification;
    private ProgressDialog dialog;
    private Handler handler;
    private static final int DATAISREADY = 0X01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.loadingdata));
        dialog.show();
        getParentId();//获取需要看护的老人的objectId
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case DATAISREADY:
                        String parentId = msg.obj.toString().trim();
                        PreferencesUtil.putString(getBaseContext(), UserConfig.PRENTID, parentId);
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this,getString(R.string.dataisready),Toast.LENGTH_LONG).show();
                        break;
                }

            }
        };

    }

    private void getParentId() {
        AVQuery<AVObject> query = new AVQuery<>("Parents");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (AVObject parent : list) {
                    String id = PreferencesUtil.getString(getBaseContext(), UserConfig.MOBILE);
                    if (parent.get("nurse").equals(id)) {
                        String parentId = parent.getObjectId();
                        Message msg = new Message();
                        msg.what = DATAISREADY;
                        msg.obj = parentId;
                        handler.sendMessage(msg);
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_notification:
                if (isBindParent()){
                    startActivity(new Intent(this, NotificationActivity.class));
                }else{
                    Toast.makeText(getBaseContext(),getString(R.string.dataisnotready),Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.rl_uploadstatus:
                if (isBindParent()){
                    startActivity(new Intent(this, UploadStatusActivity.class));
                }else{
                    Toast.makeText(getBaseContext(),getString(R.string.dataisnotready),Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.rl_uploadpic:
                if (isBindParent()){
                    startActivity(new Intent(this, UploadPhotoActivity.class));
                }else {
                    Toast.makeText(getBaseContext(),getString(R.string.dataisnotready),Toast.LENGTH_LONG).show();
                };

                break;

        }
    }

    private boolean isBindParent() {
        if (null!= PreferencesUtil.getString(getBaseContext(),UserConfig.PRENTID)){
            return true;
        }else {
            return false;
        }
    }

    private void initUI() {
        rl_uploadpic = (RelativeLayout) findViewById(R.id.rl_uploadpic);
        rl_uploadstatus = (RelativeLayout) findViewById(R.id.rl_uploadstatus);
        rl_notification = (RelativeLayout) findViewById(R.id.rl_notification);
        setListener(rl_notification, rl_uploadpic, rl_uploadstatus);
    }


}
