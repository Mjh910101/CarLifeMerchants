package com.carlife.merchants.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carlife.merchants.R;
import com.carlife.merchants.adapters.RepairAdapter;
import com.carlife.merchants.box.RepairObj;
import com.carlife.merchants.box.handlers.RepairObjHandler;
import com.carlife.merchants.box.handlers.UserObjHandler;
import com.carlife.merchants.download.DownloadImageLoader;
import com.carlife.merchants.handlers.JsonHandle;
import com.carlife.merchants.handlers.MessageHandler;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


public class MainActivity extends BaseActivity {


    private final static long EXITTIME = 2000;
    private long EXIT = 0;

    private final static int LIMIT = 12;

    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.progress_progress)
    private ProgressBar progress;
    @ViewInject(R.id.repairList_dataList)
    private ListView dataList;
    @ViewInject(R.id.title_dataIcon)
    private ImageView dataIcon;
    @ViewInject(R.id.title_storeIcon)
    private ImageView storeIcon;
    @ViewInject(R.id.title_logoutIcon)
    private ImageView logoutIcon;

    private RepairAdapter repairAdapter;

    private int page = 1, pages = 1;

    private Thread mThread;
    private boolean isRun = false;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_list);

        ViewUtils.inject(this);
        DownloadImageLoader.initLoader(context);

        initActivity();
        setDataListScrollListener();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (System.currentTimeMillis() - EXIT < EXITTIME) {
                finish();
            } else {
                MessageHandler.showToast(context, "再次点击退出");
            }
            EXIT = System.currentTimeMillis();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startRun();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRun();
    }

    @OnClick({R.id.title_dataIcon, R.id.repairList_refreshBtn, R.id.title_storeIcon, R.id.title_logoutIcon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_dataIcon:
                Passageway.jumpActivity(context, RepairDataListActivity.class);
                break;
            case R.id.repairList_refreshBtn:
                refresh();
                break;
            case R.id.title_storeIcon:
                Passageway.jumpActivity(context, StoreUserActivity.class);
                break;
            case R.id.title_logoutIcon:
                UserObjHandler.logout(context);
                jumpLoginActivity();
                break;
        }
    }

    private void refresh() {
        page = 1;
        pages = 1;
        repairAdapter = null;
        dataList.setAdapter(null);
        downloadData();
    }

    private void setDataListScrollListener() {
        dataList.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() >= (view.getCount() - 1)) {
                        if (page < pages) {
                            if (progress.getVisibility() == View.GONE) {
                                downloadData();
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


    private void initActivity() {
        dataIcon.setVisibility(View.VISIBLE);
        storeIcon.setVisibility(View.VISIBLE);
        logoutIcon.setVisibility(View.VISIBLE);

        if (UserObjHandler.isLogin(context)) {
            titleName.setText("商家抢单");
            downloadData();
        } else {
            jumpLoginActivity();
        }
    }

    private void jumpLoginActivity() {
        Passageway.jumpActivity(context, LoginActivity.class);
        finish();
    }

    public void setDataView(List<RepairObj> list) {
        if (repairAdapter == null) {
            repairAdapter = new RepairAdapter(context, list);
            dataList.setAdapter(repairAdapter);
        } else {
            repairAdapter.addItems(list);
        }
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getServicesRepairGrab() + "?sessiontoken=" + UserObjHandler.getSessionToken(context) + "&limit=" + LIMIT + "&page=" + page;

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
                                    List<RepairObj> list = RepairObjHandler.getRepairList(array);
                                    setDataView(list);
                                    page += 1;
                                }
                                pages = JsonHandle.getInt(json, "pages");
                            }
                        }
                    }

                });
    }

    private void startRun() {
        if (mThread == null) {
            mThread = new Thread(mRunnable);
            isRun = true;
            mThread.start();
        }
    }

    private void stopRun() {
        if (mThread != null) {
            isRun = false;
            mThread = null;
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRun) {
                try {
                    Thread.sleep(60 * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                uploadCount();
            }
        }
    };

    private void uploadCount() {
        String url = UrlHandler.getServicesRepairGrabCount() + "?sessiontoken=" + UserObjHandler.getSessionToken(context);

        RequestParams params = HttpUtilsBox.getRequestParams(context);

        HttpUtilsBox.getHttpUtil().send(HttpMethod.GET, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            int status = JsonHandle.getInt(json, "status");
                            if (status == 1) {
                                int r = JsonHandle.getInt(json, "result");
                                if (r != count) {
                                    count = r;
                                    MessageHandler.showToast(context, "有新订单，请刷新!");
                                }
                            }
                        }
                    }

                });
    }
}
