package com.bupt.affectionfornurse.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.bupt.affectionfornurse.R;
import com.bupt.affectionfornurse.adapter.MessageAdapter;
import com.bupt.affectionfornurse.common.BaseActivity;
import com.bupt.affectionfornurse.common.PreferencesUtil;
import com.bupt.affectionfornurse.common.UserConfig;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends BaseActivity {
    private ImageView iv_back;
    private ListView lv_feedback;
    private SwipeRefreshLayout refresh;
    private MessageAdapter adapter;
    private List<String> messageList;
    private String id;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initUI();
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.loadingdata));
        dialog.show();
        messageList = new ArrayList<>();
        messageList.add("1");
        messageList.add("2");
        messageList.add("3");
        adapter = new MessageAdapter(NotificationActivity.this,messageList);
        lv_feedback.setAdapter(adapter);
        getMessageFromLeancloud();
    }
    private void getMessageFromLeancloud() {
        id = PreferencesUtil.getString(NotificationActivity.this, UserConfig.PRENTID);
        AVQuery<AVObject> avQuery = new AVQuery<>("Parents");
        avQuery.getInBackground(id, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                List<String> dataList = new ArrayList<String>();
                dataList = (List<String>) avObject.get("message");
                messageList.clear();
                for (String data :dataList){
                    messageList.add(data);
                    Logger.d(data);
                }
                adapter.notifyDataSetChanged();
                refresh.setRefreshing(false);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch(v.getId()){
            case R.id.iv_back:
                finish();
                break;

        }
    }

    private void initUI() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        lv_feedback = (ListView) findViewById(R.id.lv_feedback);


        refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMessageFromLeancloud();
            }
        });
        setListener(iv_back);
    }
}
