package com.carlife.merchants.activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.avos.avoscloud.AVOSCloud;

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
public class BaseActivity extends Activity {

    public  Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        AVOSCloud.initialize(this, "E4G44AwCMV2DGLdthLdYy3lo-gzGzoHsz", "83Qt7Ol6yK8kcpMm8G4KXiuF");
    }

}
