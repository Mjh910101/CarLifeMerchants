package com.carlife.merchants.box.handlers;

import android.content.Context;

import com.carlife.merchants.box.RepairStoreObj;
import com.carlife.merchants.box.ServiceObj;
import com.carlife.merchants.handlers.JsonHandle;
import com.carlife.merchants.handlers.SystemHandle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
 * Created by Hua on 16/1/22.
 */
public class ServiceObjHandler {

    public static ServiceObj getServiceObj(JSONObject json) {
        ServiceObj obj = new ServiceObj();


        obj.setObjectId(JsonHandle.getString(json, ServiceObj.OBJECT_ID));
        obj.setAddress(JsonHandle.getString(json, ServiceObj.ADDRESS));
        obj.setName(JsonHandle.getString(json, ServiceObj.NAME));
        obj.setDescript(JsonHandle.getString(json, ServiceObj.DESCRIPT));
        obj.setContact(JsonHandle.getString(json, ServiceObj.CONTACT));
        obj.setLinkman(JsonHandle.getString(json, ServiceObj.LINKMAN));
        obj.setServiceType(JsonHandle.getString(json, ServiceObj.SERVICE_TYPE));
        obj.setId_card_number(JsonHandle.getString(json, ServiceObj.ID_CARD_NUMBER));

        JSONObject geoJson = JsonHandle.getJSON(json, "geo");
        if (geoJson != null) {
            obj.setLongitude(JsonHandle.getDouble(geoJson, ServiceObj.LONGITUDE));
            obj.setLatitude(JsonHandle.getDouble(geoJson, ServiceObj.LATITUDE));
        }

        JSONObject coverJson = JsonHandle.getJSON(json, ServiceObj.COVER);
        if (coverJson != null) {
            obj.setCoverUrl(JsonHandle.getString(coverJson, "url"));
        }

        JSONArray imageArray = JsonHandle.getArray(json, ServiceObj.IMAGES);
        if (imageArray != null) {
            obj.setImages(imageArray);
        }

        JSONObject businessJson = JsonHandle.getJSON(json, ServiceObj.BUSINESS_LICENSE);
        if (businessJson != null) {
            obj.setBusinessLicenseUrl(JsonHandle.getString(businessJson, "url"));
        }

        return obj;
    }

    private final static String KEY = "service_";

    public static void saveServiceObj(Context context, ServiceObj obj) {
        SystemHandle.saveStringMessage(context, KEY + ServiceObj.ADDRESS, obj.getAddress());
        SystemHandle.saveStringMessage(context, KEY + ServiceObj.BUSINESS_LICENSE, obj.getBusinessLicenseUrl());
        SystemHandle.saveStringMessage(context, KEY + ServiceObj.NAME, obj.getName());
        SystemHandle.saveStringMessage(context, KEY + ServiceObj.ID_CARD_NUMBER, obj.getId_card_number());
        SystemHandle.saveStringMessage(context, KEY + ServiceObj.OBJECT_ID, obj.getObjectId());
        SystemHandle.saveStringMessage(context, KEY + ServiceObj.CONTACT, obj.getContact());
        SystemHandle.saveStringMessage(context, KEY + ServiceObj.COVER, obj.getCoverUrl());
        SystemHandle.saveDoubleMessage(context, KEY + ServiceObj.LATITUDE, obj.getLatitude());
        SystemHandle.saveDoubleMessage(context, KEY + ServiceObj.LONGITUDE, obj.getLongitude());
        SystemHandle.saveStringMessage(context, KEY + ServiceObj.DESCRIPT, obj.getDescript());
        SystemHandle.saveStringMessage(context, KEY + ServiceObj.SERVICE_TYPE, obj.getServiceType());
        SystemHandle.saveStringMessage(context, KEY + ServiceObj.LINKMAN, obj.getLinkman());
        SystemHandle.saveStringMessage(context, KEY + ServiceObj.IMAGES, obj.getImagesListForString());
        SystemHandle.saveStringMessage(context, KEY + ServiceObj.IMAGE_ID, obj.getImageIdListForString());
    }

    public static String getService(Context context, String ksy) {
        return SystemHandle.getString(context, KEY + ksy);
    }

    public static double getLatitude(Context context) {
        return SystemHandle.getDouble(context, KEY + ServiceObj.LATITUDE);
    }

    public static double getLongitude(Context context) {
        return SystemHandle.getDouble(context, KEY + ServiceObj.LONGITUDE);
    }

    public static List<String> getImageList(Context context) {
        List<String> list = new ArrayList<>();
        String s = SystemHandle.getString(context, KEY + ServiceObj.IMAGES);
        String[] images = s.split(",");
        for (String str : images) {
            list.add(str);
        }
        return list;
    }

    public static List<String> getImageIdList(Context context) {
        List<String> list = new ArrayList<>();
        String s = SystemHandle.getString(context, KEY + ServiceObj.IMAGE_ID);
        String[] images = s.split(",");
        for (String str : images) {
            list.add(str);
        }
        return list;
    }


}
