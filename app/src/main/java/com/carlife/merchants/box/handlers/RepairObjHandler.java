package com.carlife.merchants.box.handlers;


import com.carlife.merchants.box.RepairObj;
import com.carlife.merchants.handlers.JsonHandle;

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
 * Created by Hua on 16/1/20.
 */
public class RepairObjHandler {

    public static RepairObj getRepairObj(JSONObject json) {
        RepairObj obj = new RepairObj();

        obj.setCreatedAt(JsonHandle.getString(json, RepairObj.CREATED_AT));
        obj.setObjectId(JsonHandle.getString(json, RepairObj.OBJECT_ID));
        obj.setAddress(JsonHandle.getString(json, RepairObj.ADDRESS));
        obj.setDescript(JsonHandle.getString(json, RepairObj.DESCRIPT));
        obj.setExpect(JsonHandle.getString(json, RepairObj.EXPECT));
        obj.setExpectworkdays(JsonHandle.getInt(json, RepairObj.EXPECT_WORK_DAY));
        obj.setOrder_status(JsonHandle.getString(json, RepairObj.ORDER_STATUS));
        obj.setFee(JsonHandle.getString(json, "fee"));
        obj.setDay(JsonHandle.getInt(json, "days"));

        JSONObject geoJson = JsonHandle.getJSON(json, "geo");
        if (geoJson != null) {
            obj.setLatitude(JsonHandle.getDouble(geoJson, RepairObj.LATITUDE));
            obj.setLongitude(JsonHandle.getDouble(geoJson, RepairObj.LONGITUDE));
        }

        JSONArray array = JsonHandle.getArray(json, "area");
        if (array != null) {
            obj.setAreaList(array);
        }

        JSONObject video = JsonHandle.getJSON(json, "video");
        if (video != null) {
            obj.setVideo(video);
        }

        JSONObject storeJson = JsonHandle.getJSON(json, "store");
        if (storeJson != null) {
            obj.setStoreObj(RepairStoreObjHandler.getRepairStoreObj(storeJson));
            obj.getStoreObj().setFee(JsonHandle.getString(json, "fee"));
        }

        JSONObject userJson = JsonHandle.getJSON(json, "user");
        if (userJson != null) {

            obj.setUser(UserObjHandler.getUserObj(userJson));

            JSONObject carInfoJson = JsonHandle.getJSON(userJson, "carInfo");
            if (carInfoJson != null) {
                obj.setCarInfoObj(UserCarInfoObjHandler.getUserCarInfoObj(carInfoJson));
            }

            JSONObject insuranceJson = JsonHandle.getJSON(userJson, "insurance");
            if (insuranceJson != null) {
                obj.setClaimsInfoObj(UserClaimsInfoObjHandler.getUserClaimsInfoObj(insuranceJson));
            }
        }

        return obj;
    }

    public static List<RepairObj> getRepairList(JSONArray array) {
        List<RepairObj> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(getRepairObj(JsonHandle.getJSON(array, i)));
        }
        return list;
    }

    public static List<RepairObj> getDataRepairList(JSONArray array) {
        List<RepairObj> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(getDataRepairObj(JsonHandle.getJSON(array, i)));
        }
        return list;
    }

    public static RepairObj getDataRepairObj(JSONObject json) {
        RepairObj obj = new RepairObj();

        obj.setFee(JsonHandle.getString(json, "fee"));
        obj.setCreatedAt(JsonHandle.getString(json, RepairObj.CREATED_AT));


        JSONObject repairJson = JsonHandle.getJSON(json, "repair");
        if (repairJson != null) {
            obj.setAddress(JsonHandle.getString(repairJson, RepairObj.ADDRESS));
            obj.setDescript(JsonHandle.getString(repairJson, RepairObj.DESCRIPT));
            obj.setExpect(JsonHandle.getString(repairJson, RepairObj.EXPECT));
            obj.setOrder_status(JsonHandle.getString(repairJson, RepairObj.ORDER_STATUS));
            obj.setObjectId(JsonHandle.getString(repairJson, RepairObj.OBJECT_ID));

            JSONObject geoJson = JsonHandle.getJSON(repairJson, "geo");
            if (geoJson != null) {
                obj.setLatitude(JsonHandle.getDouble(geoJson, RepairObj.LATITUDE));
                obj.setLongitude(JsonHandle.getDouble(geoJson, RepairObj.LONGITUDE));
            }

            JSONObject userJson = JsonHandle.getJSON(repairJson, "user");
            if (userJson != null) {

                obj.setUser(UserObjHandler.getUserObj(userJson));

                JSONObject carInfoJson = JsonHandle.getJSON(userJson, "carInfo");
                if (carInfoJson != null) {
                    obj.setCarInfoObj(UserCarInfoObjHandler.getUserCarInfoObj(carInfoJson));
                }

                JSONObject insuranceJson = JsonHandle.getJSON(userJson, "insurance");
                if (insuranceJson != null) {
                    obj.setClaimsInfoObj(UserClaimsInfoObjHandler.getUserClaimsInfoObj(insuranceJson));
                }
            }

            JSONArray array = JsonHandle.getArray(repairJson, "area");
            if (array != null) {
                obj.setAreaList(array);
            }

            JSONObject video = JsonHandle.getJSON(repairJson, "video");
            if (video != null) {
                obj.setVideo(video);
            }

        }


        JSONObject storeJson = JsonHandle.getJSON(json, "store");
        if (storeJson != null) {
            obj.setStoreObj(RepairStoreObjHandler.getRepairStoreObj(storeJson));
            obj.getStoreObj().setFee(JsonHandle.getString(json, "fee"));
        }

        return obj;
    }

    private static RepairObj mRepairObj;

    public static void saveRepairObj(RepairObj obj) {
        mRepairObj = obj;
    }

    public static RepairObj getRepairObj() {
        return mRepairObj;
    }
}
