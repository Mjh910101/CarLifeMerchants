package com.carlife.merchants.box;

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
 * Created by Hua on 16/1/8.
 */
public class UserCarInfoObj {

    public final static String CAR_NUNMER = "car_number";
    public final static String ENGINE_NUMBER = "engine_number";
    public final static String FRAME_NUMBER = "frame_number";
    public final static String MAJOR_CAR_LICENSE = "major_car_license";
    public final static String SECONDARY_CAR_LICENSE = "secondary_car_license";
    public final static String OBJECT_ID = "objectId";
    public final static String CREATED_AT = "createdAt";
    public final static String UPLOATE_AT = "updatedAt";

    private String car_number;
    private String engine_number;
    private String frame_number;
    private String major_car_license;
    private String secondary_car_license;
    private String objectId;
    private String createdAt;
    private String updatedAt;

    public String getCar_number() {
        return car_number;
    }

    public String getGoneCarNumber() {
        String number = getCar_number();
        if (number.length() >= 5) {
            number = number.substring(0, number.length() - 3) + "**" + number.substring(number.length() - 1, number.length());
        }
        return number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public String getEngine_number() {
        return engine_number;
    }

    public void setEngine_number(String engine_number) {
        this.engine_number = engine_number;
    }

    public String getFrame_number() {
        return frame_number;
    }

    public void setFrame_number(String frame_number) {
        this.frame_number = frame_number;
    }

    public String getMajor_car_license() {
        return major_car_license;
    }

    public void setMajor_car_license(String major_car_license) {
        this.major_car_license = major_car_license;
    }

    public String getSecondary_car_license() {
        return secondary_car_license;
    }

    public void setSecondary_car_license(String secondary_car_license) {
        this.secondary_car_license = secondary_car_license;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
