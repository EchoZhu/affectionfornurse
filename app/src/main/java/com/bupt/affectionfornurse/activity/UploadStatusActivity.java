package com.bupt.affectionfornurse.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.bupt.affectionfornurse.R;
import com.bupt.affectionfornurse.common.BaseActivity;
import com.bupt.affectionfornurse.common.PreferencesUtil;
import com.bupt.affectionfornurse.common.UserConfig;

import java.util.ArrayList;
import java.util.List;

public class UploadStatusActivity extends BaseActivity {

    private TextView food_1, food_2, food_3, food_4, food_5, food_6;
    private TextView fun_1, fun_2, fun_3, fun_4, fun_5, fun_6;
    private TextView sleep_1, sleep_2, sleep_3, sleep_4;
    private TextView fit_1, fit_2, fit_3;
    private ImageView iv_updatefood, iv_updatefun, iv_updatesleep, iv_updatefit,iv_common_back;
    private List<TextView> foodTextViewList, funTextViewList, sleepTextViewList,fitTextViewList;
    private List<String> foodUpdateList, funUpdateList, sleepUpdateList,fitUpdateList;
    private String parentId;
    private ProgressDialog dialog,uploadDialog;
    private AVObject parent;
    private List<String> foodsFromCLList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_status);
        initUI();
        initData();
        getFoodsFromCL();
    }

    private void getFoodsFromCL() {
        uploadDialog.show();
        AVQuery<AVObject> query = new AVQuery<>("Foods");
        query.getInBackground("57afee268ac247005f091146", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                foodsFromCLList = new ArrayList<String>();
                foodsFromCLList = (List<String>) avObject.get("food");
                for (int i = 0;i<foodTextViewList.size();i++){
                    if (i<foodsFromCLList.size()){
                        foodTextViewList.get(i).setText(foodsFromCLList.get(i));
                    }
                }
                uploadDialog.dismiss();
            }
        });
    }

    private void initData() {
        parentId = PreferencesUtil.getString(getBaseContext(), UserConfig.PRENTID);
        // 第一参数是 className,第二个参数是 objectId
        parent = AVObject.createWithoutData("Parents", parentId);

        foodTextViewList = new ArrayList<>();
        foodTextViewList.add(food_1);
        foodTextViewList.add(food_2);
        foodTextViewList.add(food_3);
        foodTextViewList.add(food_4);
        foodTextViewList.add(food_5);
        foodTextViewList.add(food_6);

        funTextViewList = new ArrayList<>();
        funTextViewList.add(fun_1);
        funTextViewList.add(fun_2);
        funTextViewList.add(fun_3);
        funTextViewList.add(fun_4);
        funTextViewList.add(fun_5);
        funTextViewList.add(fun_6);

        sleepTextViewList = new ArrayList<>();
        sleepTextViewList.add(sleep_1);
        sleepTextViewList.add(sleep_2);
        sleepTextViewList.add(sleep_3);
        sleepTextViewList.add(sleep_4);

        fitTextViewList = new ArrayList<>();
        fitTextViewList.add(fit_1);
        fitTextViewList.add(fit_2);
        fitTextViewList.add(fit_3);

        foodUpdateList = new ArrayList<>();
        funUpdateList = new ArrayList<>();
        sleepUpdateList = new ArrayList<>();
        fitUpdateList = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.food_1:
                switchstatus(food_1);
                break;
            case R.id.food_2:
                switchstatus(food_2);
                break;
            case R.id.food_3:
                switchstatus(food_3);
                break;
            case R.id.food_4:
                switchstatus(food_4);
                break;
            case R.id.food_5:
                switchstatus(food_5);
                break;
            case R.id.food_6:
                switchstatus(food_6);
                break;

            case R.id.fun_1:
                switchstatus(fun_1);
                break;
            case R.id.fun_2:
                switchstatus(fun_2);
                break;
            case R.id.fun_3:
                switchstatus(fun_3);
                break;
            case R.id.fun_4:
                switchstatus(fun_4);
                break;
            case R.id.fun_5:
                switchstatus(fun_5);
                break;
            case R.id.fun_6:
                switchstatus(fun_6);
                break;

            case R.id.sleep_1:
                switchstatus(sleep_1);
                break;
            case R.id.sleep_2:
                switchstatus(sleep_2);
                break;
            case R.id.sleep_3:
                switchstatus(sleep_3);
                break;
            case R.id.sleep_4:
                switchstatus(sleep_4);
                break;

            case R.id.fit_1:
                switchstatus(fit_1);
                break;
            case R.id.fit_2:
                switchstatus(fit_2);
                break;
            case R.id.fit_3:
                switchstatus(fit_3);
                break;

            case R.id.iv_updatefood:
                dialog.show();
                foodUpdateList.clear();
                for (TextView tv : foodTextViewList) {
                    if (tv.getTag() != null) {
                        foodUpdateList.add(tv.getText().toString());
                    }
                }
                parent.put("food", foodUpdateList);
                Log.e("123", foodUpdateList.toString());
                saveToLeancloud();
                break;
            case R.id.iv_updatefun:
                dialog.show();
                funUpdateList.clear();
                for (TextView tv : funTextViewList) {
                    if (tv.getTag() != null) {
                        funUpdateList.add(tv.getText().toString());
                    }
                }
                parent.put("act", funUpdateList);
                saveToLeancloud();
                break;
            case R.id.iv_updatesleep:
                dialog.show();
                sleepUpdateList.clear();
                for (TextView tv : sleepTextViewList) {
                    if (tv.getTag() != null) {
                        sleepUpdateList.add(tv.getText().toString());
                    }
                }
                parent.put("sleep", sleepUpdateList);
                saveToLeancloud();
                break;
            case R.id.iv_updatefit:
                dialog.show();
                fitUpdateList.clear();
                for (TextView tv : fitTextViewList) {
                    if (tv.getTag() != null) {
                        fitUpdateList.add(tv.getText().toString());
                    }
                }
                parent.put("fit", fitUpdateList);
                saveToLeancloud();
                break;
            case R.id.iv_common_back:
                finish();
                break;

        }
    }

    private void saveToLeancloud() {
        // 保存到云端
        parent.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Toast.makeText(getBaseContext(), "更新数据成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "更新数据失败，请重试", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });
    }

    private void switchstatus(TextView tv) {
        if (tv.getTag() == null) {
            tv.setTag("isChosed");
            tv.setBackgroundResource(R.color.back_1);
        } else {
            tv.setBackgroundResource(R.color.alpha0);
            tv.setTag(null);
        }
    }

    private void initUI() {
        food_1 = (TextView) findViewById(R.id.food_1);
        food_2 = (TextView) findViewById(R.id.food_2);
        food_3 = (TextView) findViewById(R.id.food_3);
        food_4 = (TextView) findViewById(R.id.food_4);
        food_5 = (TextView) findViewById(R.id.food_5);
        food_6 = (TextView) findViewById(R.id.food_6);

        fun_1 = (TextView) findViewById(R.id.fun_1);
        fun_2 = (TextView) findViewById(R.id.fun_2);
        fun_3 = (TextView) findViewById(R.id.fun_3);
        fun_4 = (TextView) findViewById(R.id.fun_4);
        fun_5 = (TextView) findViewById(R.id.fun_5);
        fun_6 = (TextView) findViewById(R.id.fun_6);

        sleep_1 = (TextView) findViewById(R.id.sleep_1);
        sleep_2 = (TextView) findViewById(R.id.sleep_2);
        sleep_3 = (TextView) findViewById(R.id.sleep_3);
        sleep_4 = (TextView) findViewById(R.id.sleep_4);

        fit_1 = (TextView) findViewById(R.id.fit_1);
        fit_2 = (TextView) findViewById(R.id.fit_2);
        fit_3 = (TextView) findViewById(R.id.fit_3);


        iv_updatefood = (ImageView) findViewById(R.id.iv_updatefood);
        iv_updatefun = (ImageView) findViewById(R.id.iv_updatefun);
        iv_updatesleep = (ImageView) findViewById(R.id.iv_updatesleep);
        iv_updatefit = (ImageView) findViewById(R.id.iv_updatefit);
        iv_common_back = (ImageView) findViewById(R.id.iv_common_back);

        setListener(
                food_1, food_2, food_3, food_4, food_5, food_6, iv_updatefood,
                fun_1, fun_2, fun_3, fun_4, fun_5, fun_6, iv_updatefun,
                sleep_1, sleep_2, sleep_3, sleep_4, iv_updatesleep,
                fit_1, fit_2, fit_3, iv_updatefit,
                iv_common_back);
        dialog = new ProgressDialog(this);
        uploadDialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.uploadiong));
        uploadDialog.setMessage(getString(R.string.loadingdata));
    }


}
