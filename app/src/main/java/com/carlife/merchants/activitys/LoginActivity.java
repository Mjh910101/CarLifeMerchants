package com.carlife.merchants.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carlife.merchants.R;
import com.carlife.merchants.box.handlers.ServiceObjHandler;
import com.carlife.merchants.box.handlers.UserObjHandler;
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
 * Created by Hua on 16/1/22.
 */
public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.login_mobileInput)
    private EditText mobileInput;
    @ViewInject(R.id.login_codeInput)
    private EditText codeInput;
    @ViewInject(R.id.ldgin_getCodeBtn)
    private TextView getCodeBtn;
    @ViewInject(R.id.ldgin_getCodeIcon)
    private ImageView getCodeIcon;
    @ViewInject(R.id.progress_progress)
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        context = this;
        ViewUtils.inject(this);
    }

    @OnClick({R.id.ldgin_getCodeBtn, R.id.login_loginBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ldgin_getCodeBtn:
                if (isHaveMobile()) {
                    if (progress.getVisibility() != View.VISIBLE) {
                        getLoginRequestSms();
                    }
                }
                break;
            case R.id.login_loginBtn:
                if (isHaveMobile() && isHaveCode()) {
                    if (progress.getVisibility() != View.VISIBLE) {
                        login();
                    }
                }
                break;
        }
    }

    private void login() {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getLogin();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("mobile", TextHandeler.getText(mobileInput));
        params.addBodyParameter("pwd", TextHandeler.getText(codeInput));
        if (TextHandeler.getText(codeInput).equals("1")) {
            params.addBodyParameter("isdebug", "1");
        }
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
                                UserObjHandler.saveUser(context, UserObjHandler.getUserObj(resultJson));
                                JSONObject serviceJson = JsonHandle.getJSON(resultJson, "services");
                                if (serviceJson != null) {
                                    ServiceObjHandler.saveServiceObj(context, ServiceObjHandler.getServiceObj(serviceJson));
                                }
                                Passageway.jumpActivity(context, MainActivity.class);
                                finish();
                            }
                        }
                    }

                });
    }

    public void getLoginRequestSms() {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getLoginRequestSms();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("mobile", TextHandeler.getText(mobileInput));

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
                            MessageHandler.showToast(context, JsonHandle.getString(json, "result"));
                            if (status == 1) {
                                startClock();
                            }
                        }
                    }

                });

    }

    private void startClock() {
        getCodeBtn.setClickable(false);
        getCodeIcon.setVisibility(View.GONE);
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 99; i >= 0; i--) {
                    Message.obtain(clockHandler, i).sendToTarget();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private Handler clockHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            int time = msg.what;
            getCodeBtn.setText(time + "秒");
            if (time == 0) {
                getCodeBtn.setText("点击获取验证码");
                getCodeBtn.setClickable(true);
                getCodeIcon.setVisibility(View.VISIBLE);
            }
        }

    };

    public boolean isHaveMobile() {
        boolean isHave = !TextHandeler.getText(mobileInput).equals("");
        if (!isHave) {
            MessageHandler.showToast(context, "手机号不能为空");
        }
        return isHave;
    }

    public boolean isHaveCode() {
        boolean isHave = !TextHandeler.getText(codeInput).equals("");
        if (!isHave) {
            MessageHandler.showToast(context, "密码不能为空");
        }
        return isHave;
    }

}
