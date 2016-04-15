package com.carlife.merchants.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carlife.merchants.R;
import com.carlife.merchants.adapters.RepairAdapter;
import com.carlife.merchants.adapters.RepairDataAdapter;
import com.carlife.merchants.box.RepairObj;
import com.carlife.merchants.box.handlers.RepairObjHandler;
import com.carlife.merchants.box.handlers.UserObjHandler;
import com.carlife.merchants.download.DownloadImageLoader;
import com.carlife.merchants.handlers.ColorHandler;
import com.carlife.merchants.handlers.JsonHandle;
import com.carlife.merchants.handlers.MessageHandler;
import com.carlife.merchants.http.HttpUtilsBox;
import com.carlife.merchants.http.UrlHandler;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

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
public class RepairDataListActivity extends BaseActivity {

    private final static int DATA_TAP = 1;
    private final static int NOT_PAY_TAP = 2;
    private final static int PAY_TAP = 3;
    private final static int LIMIT = 12;

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.repairDataList_dataTapIcon)
    private ImageView dataTapIcon;
    @ViewInject(R.id.repairDataList_dataTapText)
    private TextView dataTapText;
    @ViewInject(R.id.repairDataList_payTapIcon)
    private ImageView payTapIcon;
    @ViewInject(R.id.repairDataList_payTapText)
    private TextView payTapText;
    @ViewInject(R.id.repairDataList_notPayTapIcon)
    private ImageView notPayTapIcon;
    @ViewInject(R.id.repairDataList_notPayTapText)
    private TextView notPayTapText;
    @ViewInject(R.id.progress_progress)
    private ProgressBar progress;
    @ViewInject(R.id.repairDataList_dataList)
    private ListView dataList;

    private int page = 1, pages = 1, tap;
    private RepairDataAdapter repairAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_data_list);

        ViewUtils.inject(this);

        initActivity();
        setDataListScrollListener();
    }

    @OnClick({R.id.title_backBtn, R.id.repairDataList_dataTap, R.id.repairDataList_payTap, R.id.repairDataList_notPayTap})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.repairDataList_dataTap:
                onClickTap(DATA_TAP);
                break;
            case R.id.repairDataList_payTap:
                onClickTap(PAY_TAP);
                break;
            case R.id.repairDataList_notPayTap:
                onClickTap(NOT_PAY_TAP);
                break;

        }
    }

    private void onClickTap(int tap) {
        initTap();
        this.tap = tap;
        switch (tap) {
            case DATA_TAP:
                dataTapIcon.setImageResource(R.drawable.on_data_icon);
                dataTapText.setTextColor(ColorHandler.getColorForID(context, R.color.title_bg));
                break;
            case PAY_TAP:
                payTapIcon.setImageResource(R.drawable.on_pay_icon);
                payTapText.setTextColor(ColorHandler.getColorForID(context, R.color.title_bg));
                break;
            case NOT_PAY_TAP:
                notPayTapIcon.setImageResource(R.drawable.on_not_pay_icon);
                notPayTapText.setTextColor(ColorHandler.getColorForID(context, R.color.title_bg));
                break;
        }
        downloadData(tap);
    }

    private void setDataListScrollListener() {
        dataList.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() >= (view.getCount() - 1)) {
                        if (page < pages) {
                            if (progress.getVisibility() == View.GONE) {
                                downloadData(tap);
                            }
                        } else {
                            MessageHandler.showLast(context);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

            }
        });
    }

    private void initTap() {
        dataTapIcon.setImageResource(R.drawable.off_data_icon);
        payTapIcon.setImageResource(R.drawable.off_pay_icon);
        notPayTapIcon.setImageResource(R.drawable.off_not_pay_icon);

        dataTapText.setTextColor(ColorHandler.getColorForID(context, R.color.text_blue_03));
        payTapText.setTextColor(ColorHandler.getColorForID(context, R.color.text_blue_03));
        notPayTapText.setTextColor(ColorHandler.getColorForID(context, R.color.text_blue_03));

        repairAdapter = null;
        dataList.setAdapter(null);
        page = 1;
        pages = 1;
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setText("商家抢单");
        onClickTap(DATA_TAP);
    }

    public void setDataView(List<RepairObj> list) {
        if (repairAdapter == null) {
            repairAdapter = new RepairDataAdapter(context, list);
            dataList.setAdapter(repairAdapter);
        } else {
            repairAdapter.addItems(list);
        }
    }


    private void downloadData(int tap) {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getServicesRepairOrde() + "?sessiontoken=" + UserObjHandler.getSessionToken(context) + "&limit=" + LIMIT + "&page=" + page + "&order_status=" + tap;

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
                                JSONArray array = JsonHandle.getArray(json, "result");
                                if (array != null) {
                                    List<RepairObj> list = RepairObjHandler.getDataRepairList(array);
                                    setDataView(list);
                                    page += 1;
                                }
                                pages = JsonHandle.getInt(json, "pages");
                            }
                        }
                    }

                });
    }

}
