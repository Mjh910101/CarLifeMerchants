package com.carlife.merchants.http;

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
public class UrlHandler {

    //    private final static String index = "http://192.168.1.237:3000";
    private final static String index = "http://dev.carlive.avosapps.com";
    private final static String api = "/api/v1";

    public final static String getIndex() {
        return index;
    }

    private final static String getApiIndex() {
        return getIndex() + api;
    }

    public final static String getLoginRequestSms() {
        return getApiIndex() + "/login/request/sms";
    }

    public static String getLogin() {
        return getApiIndex() + "/services/login";
    }

    public static String getServicesRepairGrab() {
        return getApiIndex() + "/services/repair/grab";
    }

    public static String getRepair() {
        return getApiIndex() + "/repair";
    }

    public static String getServicesRepairDetail() {
        return getApiIndex() + "/services/repair/detail";
    }

    public static String getServicesRepairOrde() {
        return getApiIndex() + "/services/repair/order";
    }

    public static String getServicesInfo() {
        return getApiIndex() + "/services/info";
    }

    public static String getServicesRepairGrabCount(){
        return getApiIndex()+"/services/repair/grab/count";
    }
}
