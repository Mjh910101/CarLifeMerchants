package com.carlife.merchants.box.handlers;

import android.content.Context;
import android.widget.ImageView;


import com.carlife.merchants.R;
import com.carlife.merchants.box.UserObj;
import com.carlife.merchants.download.DownloadImageLoader;
import com.carlife.merchants.handlers.JsonHandle;
import com.carlife.merchants.handlers.SystemHandle;

import org.json.JSONObject;

import java.io.File;

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
 * Created by Hua on 16/1/7.
 */
public class UserObjHandler {

    private static File sessionToken;

    public static UserObj getUserObj(JSONObject json) {
        UserObj obj = new UserObj();

        obj.setMobilePhoneNumber(JsonHandle.getString(json, UserObj.MOBILE_PHONE_NUMBER));
        obj.setSessiontoken(JsonHandle.getString(json, UserObj.SESSION_TOKEN));
        obj.setUsername(JsonHandle.getString(json, UserObj.USER_NAME));
        obj.setSex(JsonHandle.getInt(json, UserObj.SEX));
        obj.setBalance(JsonHandle.getDouble(json, UserObj.BALANCE));

        JSONObject avatarJson = JsonHandle.getJSON(json, UserObj.AVATAR);
        if (avatarJson != null) {
            obj.setAvatar(JsonHandle.getString(avatarJson, "url"));
            obj.setAvatarId(JsonHandle.getString(avatarJson, "id"));
        }

        JSONObject servicesJson = JsonHandle.getJSON(json, "services");
        if (servicesJson != null) {
            obj.setObj(ServiceObjHandler.getServiceObj(servicesJson));
        }

        return obj;
    }

    private final static String KEY = "user_";

    public static void saveUser(Context context, UserObj obj) {
        SystemHandle.saveStringMessage(context, KEY + UserObj.SESSION_TOKEN, obj.getSessiontoken());
        SystemHandle.saveStringMessage(context, KEY + UserObj.MOBILE_PHONE_NUMBER, obj.getMobilePhoneNumber());
        SystemHandle.saveStringMessage(context, KEY + UserObj.AVATAR, obj.getAvatar());
        SystemHandle.saveStringMessage(context, KEY + UserObj.AVATAR + "_id", obj.getAvatarId());
        saveUserBalance(context, obj.getBalance());
        saveUserName(context, obj.getUsername());
        saveUserSex(context, obj.getSex());
    }

    public static void saveUserBalance(Context context, double d) {
        SystemHandle.saveDoubleMessage(context, KEY + UserObj.BALANCE, d);

    }

    public static double getUserBalance(Context context) {
        return SystemHandle.getDouble(context, KEY + UserObj.BALANCE);
    }

    public static boolean isLogin(Context context) {
        return !getSessiontoken(context).equals("");
    }

    private static String getSessiontoken(Context context) {
        return SystemHandle.getString(context, KEY + UserObj.SESSION_TOKEN);
    }

    public static void setUserAvatar(Context context, ImageView view) {
        String a = getUserAvatar(context);
        if (a.equals("")) {
            view.setImageResource(R.mipmap.ic_launcher);
        } else {
            DownloadImageLoader.loadImage(view, a);
        }
    }

    public static int getUserSex(Context context) {
        return SystemHandle.getInt(context, KEY + UserObj.SEX);
    }

    public static boolean isMan(int s) {
        return s == 1;
    }

    public static boolean isMan(Context context) {
        int sex = SystemHandle.getInt(context, KEY + UserObj.SEX);
        return isMan(sex);
    }

    public static String getUserName(Context context) {
        return SystemHandle.getString(context, KEY + UserObj.USER_NAME);
    }

    public static String getUserMobile(Context context) {
        return SystemHandle.getString(context, KEY + UserObj.MOBILE_PHONE_NUMBER);
    }

    public static String getSessionToken(Context context) {
        return SystemHandle.getString(context, KEY + UserObj.SESSION_TOKEN);
    }

    public static void saveUserAvatar(Context context, String url) {
        SystemHandle.saveStringMessage(context, KEY + "avatar", url);
    }

    public static String getUserAvatar(Context context) {
        return SystemHandle.getString(context, KEY + UserObj.AVATAR);
    }

    public static void saveUserName(Context context, String s) {
        SystemHandle.saveStringMessage(context, KEY + UserObj.USER_NAME, s);
    }

    public static void saveUserSex(Context context, int sex) {
        SystemHandle.saveIntMessage(context, KEY + UserObj.SEX, sex);
    }

    public static String getUserAvatarId(Context context) {
        return SystemHandle.getString(context, UserObj.AVATAR + "_id");
    }

    public static void logout(Context context) {
        SystemHandle.saveStringMessage(context, KEY + UserObj.SESSION_TOKEN, "");
        SystemHandle.saveStringMessage(context, KEY + UserObj.MOBILE_PHONE_NUMBER, "");
        SystemHandle.saveStringMessage(context, KEY + UserObj.AVATAR, "");
        SystemHandle.saveStringMessage(context, KEY + UserObj.AVATAR + "_id", "");
        saveUserBalance(context, 0);
        saveUserName(context, "");
        saveUserSex(context, 0);
    }
}
