package com.carlife.merchants.box;


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
public class RepairObj {

    public final static String OBJECT_ID = "objectId";
    public final static String DESCRIPT = "descript";
    public final static String CREATED_AT = "createdAt";
    public final static String ORDER_STATUS = "order_status";
    public final static String ADDRESS = "address";
    public final static String EXPECT = "expect";
    public final static String LONGITUDE = "longitude";
    public final static String LATITUDE = "latitude";
    public final static String EXPECT_WORK_DAY = "expectworkdays";

    String objectId;
    String descript;
    String createdAt;
    String order_status;
    String address;
    String expect;
    String fee;
    double longitude;
    double latitude;
    List<Area> areaList;
    Video video;
    RepairStoreObj storeObj;
    private UserObj user;
    UserCarInfoObj carInfoObj;
    UserClaimsInfoObj claimsInfoObj;
    int expectworkdays;
    int day;

    public String getDaysText() {
        try {
            return String.valueOf(day);
        } catch (Exception e) {
            return "";
        }
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getExpectworkdaysText() {
        try {
            return String.valueOf(expectworkdays);
        } catch (Exception e) {
            return "";
        }
    }

    public void setExpectworkdays(int expectworkdays) {
        this.expectworkdays = expectworkdays;
    }

    public UserObj getUser() {

        return user;
    }

    public void setUser(UserObj user) {
        this.user = user;
    }

    public String getUserName() {
        if (user == null) {
            return "";
        }
        return user.getUsername();
    }

    public UserCarInfoObj getCarInfoObj() {
        return carInfoObj;
    }

    public void setCarInfoObj(UserCarInfoObj carInfoObj) {
        this.carInfoObj = carInfoObj;
    }

    public UserClaimsInfoObj getClaimsInfoObj() {
        return claimsInfoObj;
    }

    public void setClaimsInfoObj(UserClaimsInfoObj claimsInfoObj) {
        this.claimsInfoObj = claimsInfoObj;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public RepairStoreObj getStoreObj() {
        return storeObj;
    }

    public void setStoreObj(RepairStoreObj storeObj) {
        this.storeObj = storeObj;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setAreaList(JSONArray array) {
        areaList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Area obj = new Area(JsonHandle.getJSON(array, i));
            areaList.add(obj);
        }
    }

    public void setVideo(JSONObject json) {
        this.video = new Video(json);
    }

    public List<Area> getAreaList() {
        return areaList;
    }

    public Video getVideo() {
        return video;
    }

    public List<String> getAreaImageList() {
        List<String> list = new ArrayList<>();

        if (areaList != null) {
            for (int i = 0; i < areaList.size(); i++) {
                list.addAll(areaList.get(i).getImageList());
            }
        }

        return list;
    }

    public String getAreaListSize() {
        return String.valueOf(getAreaImageList().size());
    }


}
