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
 * Created by Hua on 16/1/22.
 */
public class ServiceObj {

    public final static String ADDRESS = "address";
    public final static String ID_CARD_NUMBER = "id_card_number";
    public final static String NAME = "name";
    public final static String OBJECT_ID = "objectId";
    public final static String DESCRIPT = "descript";
    public final static String COVER = "cover";
    public final static String SERVICE_TYPE = "serviceType";
    public final static String BUSINESS_LICENSE = "business_license";
    public final static String LINKMAN = "linkman";
    public final static String CONTACT = "contact";
    public final static String LONGITUDE = "longitude";
    public final static String LATITUDE = "latitude";
    public final static String IMAGES = "images";
    public final static String IMAGE_ID = "image_id";

    String address;
    String id_card_number;
    String name;
    String objectId;
    String descript;
    String coverUrl;
    String serviceType;
    String businessLicenseUrl;
    String linkman;
    String contact;
    double longitude;
    double latitude;
    List<String> images;
    List<String> imageIds;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId_card_number() {
        return id_card_number;
    }

    public void setId_card_number(String id_card_number) {
        this.id_card_number = id_card_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getBusinessLicenseUrl() {
        return businessLicenseUrl;
    }

    public void setBusinessLicenseUrl(String businessLicenseUrl) {
        this.businessLicenseUrl = businessLicenseUrl;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(JSONArray array) {
        this.images = new ArrayList<String>(array.length());
        this.imageIds = new ArrayList<String>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = JsonHandle.getJSON(array, i);
            images.add(JsonHandle.getString(json, "url"));
            imageIds.add(JsonHandle.getString(json,"objectId"));
        }
    }

    public String getImagesListForString() {
        if (images == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (String s : images) {
            sb.append(s);
            sb.append(",");
        }
        if (sb.length() > 1) {
            return sb.substring(0, sb.length() - 1);
        }
        return sb.toString();
    }

    public String getImageIdListForString() {
        if (imageIds == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (String s : imageIds) {
            sb.append(s);
            sb.append(",");
        }
        if (sb.length() > 1) {
            return sb.substring(0, sb.length() - 1);
        }
        return sb.toString();
    }
}
