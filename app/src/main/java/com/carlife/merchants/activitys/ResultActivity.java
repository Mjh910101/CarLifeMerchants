package com.carlife.merchants.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlife.merchants.R;
import com.carlife.merchants.box.RepairObj;
import com.carlife.merchants.download.DownloadImageLoader;
import com.carlife.merchants.tool.Passageway;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
public class ResultActivity extends BaseActivity {

    @ViewInject(R.id.result_icon)
    private ImageView resultIcon;
    @ViewInject(R.id.result_statusText)
    private TextView statusText;
    @ViewInject(R.id.result_idText)
    private TextView idText;
    @ViewInject(R.id.result_finishBtn)
    private TextView finishBtn;

    private Bundle mBundle;
    private boolean isStatus;
    private String usetTel, usetName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick({R.id.result_finishBtn, R.id.result_bg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.result_finishBtn:
                if (isStatus) {
                    Passageway.callUp(context, usetTel);
                }
            case R.id.result_bg:
                finish();
                break;
        }
    }

    private void initActivity() {

        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            isStatus = mBundle.getBoolean("isStatus");
            usetTel = mBundle.getString("tel");
            usetName = mBundle.getString("name");
            if (isStatus) {
                setSuccessfulView(mBundle.getString("id"), usetTel, usetName);
            } else {
                setFailureView(mBundle.getString("id"));
            }

        }

    }

    private void setFailureView(String id) {
        resultIcon.setImageResource(R.drawable.failure_icon);
        statusText.setText("抢单失败");
        idText.setText("单号" + id + ",没抢到");
        finishBtn.setText("继续抢单");
    }

    private void setSuccessfulView(String id, String tel, String name) {
        resultIcon.setImageResource(R.drawable.successful_icon);
        statusText.setText("抢单成功");
        idText.setText("单号" + id + "," + "\n" + "马上联系");
        finishBtn.setText(tel + "," + name);
    }

}
