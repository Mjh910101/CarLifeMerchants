package com.carlife.merchants.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carlife.merchants.R;
import com.carlife.merchants.box.RepairObj;
import com.carlife.merchants.box.Video;
import com.carlife.merchants.box.handlers.RepairObjHandler;
import com.carlife.merchants.box.handlers.UserObjHandler;
import com.carlife.merchants.download.DownloadImageLoader;
import com.carlife.merchants.handlers.JsonHandle;
import com.carlife.merchants.handlers.MessageHandler;
import com.carlife.merchants.handlers.TextHandeler;
import com.carlife.merchants.http.HttpUtilsBox;
import com.carlife.merchants.http.UrlHandler;
import com.carlife.merchants.tool.Passageway;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * *
 * * ┏┓      ┏┓
 * *┏┛┻━━━━━━┛┻┓
 * *┃          ┃
 * *┃          ┃
 * *┃ ┳┛   ┗┳  ┃
 * *┃          ┃
 * *┃    ┻     ┃
 * *┃          ┃
 * *┗━┓      ┏━┛
 * *  ┃      ┃
 * *  ┃      ┃
 * *  ┃      ┗━━━┓
 * *  ┃          ┣┓
 * *  ┃         ┏┛
 * *  ┗┓┓┏━━━┳┓┏┛
 * *   ┃┫┫   ┃┫┫
 * *   ┗┻┛   ┗┻┛
 * Created by Hua on 16/1/25.
 */
public class RepairActivity extends BaseActivity {

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.repair_progress)
    private ProgressBar progress;
    @ViewInject(R.id.repair_id)
    private TextView repairIdText;
    @ViewInject(R.id.repair_carMasterName)
    private TextView carMasterName;
    @ViewInject(R.id.repair_carMasterTel)
    private TextView carMasterTel;
    @ViewInject(R.id.repair_carNumber)
    private TextView carNumber;
    @ViewInject(R.id.repair_dayText)
    private TextView dayText;
    @ViewInject(R.id.repair_position)
    private TextView position;
    @ViewInject(R.id.repair_carDamaged)
    private TextView carDamaged;
    @ViewInject(R.id.repair_descript)
    private TextView descript;
    @ViewInject(R.id.repair_expect)
    private TextView expect;
    @ViewInject(R.id.repair_feeInput)
    private EditText feeInput;
    @ViewInject(R.id.repair_expectWorkDay)
    private TextView expectWorkDay;
    @ViewInject(R.id.repair_workDayInput)
    private EditText workDayInput;

    private Bundle mBundle;
    private String id;
    private RepairObj mRepairObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);

        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.title_backBtn, R.id.repair_uploadBtn, R.id.repair_videoideo, R.id.repair_carDamaged,
            R.id.repair_position})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.repair_uploadBtn:
                if (isUploadThrough()) {
                    uplpad();
                }
                break;
            case R.id.repair_videoideo:
                if (isThrough()) {
                    jumpVideoActivity(mRepairObj.getVideo());
                }
                break;
            case R.id.repair_carDamaged:
                if (isThrough()) {
                    jumpImageGrid();
                }
                break;
            case R.id.repair_position:
                if (isThrough()) {
                    jumpPositionActivity();
                }
                break;
        }
    }

    private boolean isThrough() {
        return mRepairObj != null;
    }

    private boolean isUploadThrough() {
        try {
            if (Double.valueOf(TextHandeler.getText(feeInput)) > 0) {
                return true;
            }
        } catch (Exception e) {

        }
        MessageHandler.showToast(context, "请填写报价");
        return false;
    }

    private void jumpPositionActivity() {
        Log.e(" geo ", mRepairObj.getLatitude() + " , " + mRepairObj.getLongitude());
        Bundle b = new Bundle();
        b.putBoolean(PositionActivity.IS_HAVE, true);
        b.putDouble(PositionActivity.LATITUDE_KEY, mRepairObj.getLatitude());
        b.putDouble(PositionActivity.LONGITUDE_KEY, mRepairObj.getLongitude());
        Passageway.jumpActivity(context, PositionActivity.class, b);
    }

    private void jumpImageGrid() {
        Bundle b = new Bundle();
        b.putStringArrayList(ImageGridActivity.LIST_KEY, (ArrayList) mRepairObj.getAreaImageList());
        Passageway.jumpActivity(context, ImageGridActivity.class, b);
    }

    private void jumpVideoActivity(Video video) {
        if (video == null) {
            return;
        }
        Bundle b = new Bundle();
        b.putBoolean(PlayVideoActivity.IS_FILE, false);
        b.putString(PlayVideoActivity.URL, video.getUrl());
        Passageway.jumpActivity(context, PlayVideoActivity.class, b);
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setText("车辆抢修");

        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            id = mBundle.getString("id");
            repairIdText.setText(id);
            downloadData(id);
        }

    }

    public void setMessageView(RepairObj obj) {
        dayText.setText(obj.getCreatedAt());
        position.setText(obj.getAddress());
        carDamaged.setText(obj.getAreaListSize());
        descript.setText(obj.getDescript());
        expect.setText("￥" + obj.getExpect());
        carMasterName.setText(obj.getUser().getUsername());
        carMasterTel.setText(obj.getUser().getGoneMobilePhoneNumber());
        carNumber.setText(obj.getCarInfoObj().getGoneCarNumber());
        expectWorkDay.setText(obj.getExpectworkdaysText());
    }

    private void downloadData(String id) {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getServicesRepairDetail() + "?repairid=" + id + "&sessiontoken=" + UserObjHandler.getSessionToken(context);

        RequestParams params = HttpUtilsBox.getRequestParams(context);

        HttpUtilsBox.getHttpUtil().send(HttpMethod.GET, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);
                        progress.setVisibility(View.GONE);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            int status = JsonHandle.getInt(json, "status");
                            if (status == 1) {
                                JSONObject resultJson = JsonHandle.getJSON(json, "result");
                                if (resultJson != null) {
                                    mRepairObj = RepairObjHandler.getRepairObj(resultJson);
                                    setMessageView(mRepairObj);
                                }
                            }
                        }
                    }

                });
    }

    private void uplpad() {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getServicesRepairGrab();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("repairid", id);
        params.addBodyParameter("fee", TextHandeler.getText(feeInput));
        params.addBodyParameter("days", TextHandeler.getText(workDayInput));

        Log.e("", "sessiontoken : " + UserObjHandler.getSessionToken(context));
        Log.e("", "repairid : " + id);

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);
                        progress.setVisibility(View.GONE);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            int status = JsonHandle.getInt(json, "status");
                            if (status == 1) {
                                JSONObject resultJson = JsonHandle.getJSON(json, "result");
                                jumpResultMessageActivity();
                            } else {
                                jumpResult(false);
                            }
                        }
                    }

                });
    }

    private void jumpResultMessageActivity() {
        Passageway.jumpActivity(context, ResultMessageActivity.class);
        finish();
    }

    private void jumpResult(boolean b) {
        Bundle bundle = new Bundle();
        bundle.putString("tel", mRepairObj.getUser().getMobilePhoneNumber());
        bundle.putString("id", mRepairObj.getObjectId());
        bundle.putString("name", mRepairObj.getUser().getUsername());
        bundle.putBoolean("isStatus", b);
        Passageway.jumpActivity(context, ResultActivity.class, bundle);
        finish();
    }


}
