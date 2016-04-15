package com.carlife.merchants.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
 * Created by Hua on 16/1/26.
 */
public class RepairDataActivity extends BaseActivity {

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
    @ViewInject(R.id.repair_storeNameText)
    private TextView storeNameText;
    @ViewInject(R.id.repair_storeMasterName)
    private TextView storeMasterName;
    @ViewInject(R.id.repair_storeMasterTel)
    private TextView storeMasterTel;
    @ViewInject(R.id.repair_storeAddressText)
    private TextView storeAddressText;
    @ViewInject(R.id.repair_maneyText)
    private TextView fee;
    @ViewInject(R.id.repair_callBox)
    private RelativeLayout callBox;
    @ViewInject(R.id.repair_statusText)
    private TextView status;
    @ViewInject(R.id.repair_shortBox)
    private LinearLayout shortBox;
    @ViewInject(R.id.repair_daysText)
    private TextView daysText;

    private Bundle mBundle;
    private String id;
    private RepairObj mRepairObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_data);

        ViewUtils.inject(this);
        DownloadImageLoader.initLoader(context);

        initActivity();
    }

    @OnClick({R.id.title_backBtn, R.id.repair_callBtn, R.id.repair_backBtn, R.id.repair_videoideo,
            R.id.repair_carMasterTel, R.id.repair_storeMasterTel, R.id.repair_position, R.id.repair_carDamaged})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.repair_backBtn:
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.repair_callBtn:
                callUser(mRepairObj);
                break;
            case R.id.repair_videoideo:
                if (isThrough()) {
                    jumpVideoActivity(mRepairObj.getVideo());

                }
                break;
            case R.id.repair_carMasterTel:
                callUser(mRepairObj);
                break;
            case R.id.repair_storeMasterTel:
                callUser(mRepairObj.getStoreObj().getContact());
                break;
            case R.id.repair_position:
                if (isThrough()) {
                    jumpPositionActivity();
                }
                break;
            case R.id.repair_carDamaged:
                if (isThrough()) {
                    jumpImageGrid();
                }
                break;
        }
    }

    private boolean isThrough() {
        return mRepairObj != null;
    }

    private void jumpImageGrid() {
        Bundle b = new Bundle();
        b.putStringArrayList(ImageGridActivity.LIST_KEY, (ArrayList) mRepairObj.getAreaImageList());
        Passageway.jumpActivity(context, ImageGridActivity.class, b);
    }

    private void jumpPositionActivity() {
        Bundle b = new Bundle();
        b.putBoolean(PositionActivity.IS_HAVE, true);
        b.putDouble(PositionActivity.LATITUDE_KEY, mRepairObj.getLatitude());
        b.putDouble(PositionActivity.LONGITUDE_KEY, mRepairObj.getLongitude());
        Passageway.jumpActivity(context, PositionActivity.class, b);
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
            fee.setText("￥" + mBundle.getString("fee"));
            downloadData(id);
        }

    }


    private void callUser(RepairObj obj) {
        if (obj != null) {
            callUser(obj.getUser().getMobilePhoneNumber());
        }
    }

    private void callUser(String tel) {
        Passageway.callUp(context, tel);
    }

    public void setMessageView(RepairObj obj) {
        dayText.setText(obj.getCreatedAt());
        position.setText(obj.getAddress());
        carDamaged.setText(obj.getAreaListSize());
        descript.setText(obj.getDescript());
//        fee.setText("￥" + obj.getFee());
        carMasterName.setText(obj.getUser().getUsername());
        carMasterTel.setText(obj.getUser().getMobilePhoneNumber());
        carNumber.setText(obj.getCarInfoObj().getCar_number());
        storeNameText.setText(obj.getStoreObj().getName());
        storeMasterName.setText(obj.getStoreObj().getLinkman());
        storeMasterTel.setText(obj.getStoreObj().getContact());
        storeAddressText.setText(obj.getStoreObj().getAddress());

        daysText.setText(obj.getDaysText());

        switch (obj.getOrder_status()) {
            case "2":
                status.setText("未付款");
                callBox.setVisibility(View.VISIBLE);
                break;
            case "3":
                status.setText("已付款");
                callBox.setVisibility(View.GONE);
                break;
            default:
                status.setText("待确定");
                callBox.setVisibility(View.GONE);
                shortBox.setVisibility(View.GONE);
                break;
        }
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

}
