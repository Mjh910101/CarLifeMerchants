package com.carlife.merchants.handlers;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

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
 * Created by Hua on 15/12/22.
 */
public class TextHandeler {

    public static String getText(Context context, int rid) {
        return context.getResources().getString(rid);
    }


    public static String getText(EditText et) {
        return et.getText().toString();
    }


    public static String getText(TextView v) {
        return v.getText().toString();
    }

}
