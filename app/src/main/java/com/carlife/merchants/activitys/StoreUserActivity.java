package com.carlife.merchants.activitys;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.carlife.merchants.R;
import com.carlife.merchants.box.ServiceObj;
import com.carlife.merchants.box.handlers.ServiceObjHandler;
import com.carlife.merchants.box.handlers.UserClaimsInfoObjHandler;
import com.carlife.merchants.box.handlers.UserObjHandler;
import com.carlife.merchants.dialogs.ListDialog;
import com.carlife.merchants.download.DownloadImageLoader;
import com.carlife.merchants.handlers.DateHandle;
import com.carlife.merchants.handlers.JsonHandle;
import com.carlife.merchants.handlers.MessageHandler;
import com.carlife.merchants.handlers.RegexHandler;
import com.carlife.merchants.handlers.TextHandeler;
import com.carlife.merchants.http.AVFileHandler;
import com.carlife.merchants.http.HttpUtilsBox;
import com.carlife.merchants.http.UrlHandler;
import com.carlife.merchants.tool.Passageway;
import com.carlife.merchants.tool.WinTool;
import com.carlife.merchants.views.InsideGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
 * Created by Hua on 16/1/27.
 */
public class StoreUserActivity extends BaseActivity {

    private final static String IMAGE = "image";
    private final static String CARD = "card";

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.storeUser_nameInput)
    private EditText nameInput;
    @ViewInject(R.id.storeUser_addressInput)
    private EditText addressInput;
    @ViewInject(R.id.storeUser_limanInput)
    private EditText limanInput;
    @ViewInject(R.id.storeUser_telInput)
    private EditText telInput;
    @ViewInject(R.id.storeUser_idCardInput)
    private EditText idCardInput;
    @ViewInject(R.id.storeUser_descriptInput)
    private EditText descriptInput;
    @ViewInject(R.id.progress_progress)
    private ProgressBar progress;
    @ViewInject(R.id.storeUser_storeImageGrid)
    private InsideGridView storeImageGrid;
    @ViewInject(R.id.storeUser_storeCardInput)
    private ImageView storeCardInput;

    private List<String> pathList;
    private List<String> idList;
    private List<String> uploadIdList;
    private File cardFile;

    private long start;
    private String path, where, cardId = "";
    private StoreImageAdapter mStoreImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_user);

        ViewUtils.inject(this);

        initActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Passageway.IMAGE_REQUEST_CODE:
                if (data != null) {
                    getResizeImage(data.getData());
                }
                break;
            case Passageway.CAMERA_REQUEST_CODE:
                if (isSdcardExisting()) {
                    getResizeImage(getImageUri());
                } else {
                    MessageHandler.showToast(context, "找不到SD卡");
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.title_backBtn, R.id.storeUser_uploadBtn, R.id.storeUser_storeCardInput})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.storeUser_uploadBtn:
                uoplad();
                break;
            case R.id.storeUser_storeCardInput:
                showAddImageDailod(CARD);
                break;
        }
    }

    private void uoplad() {
        if (isThrough()) {
            uploadIdList.removeAll(uploadIdList);
            uploadImage(0);
        }
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setText("商家资料");
        initStoreUSer();
        uploadIdList = new ArrayList<String>();
        mStoreImageAdapter = new StoreImageAdapter();
        storeImageGrid.setAdapter(mStoreImageAdapter);
    }

    private void initStoreUSer() {
        pathList = ServiceObjHandler.getImageList(context);
        idList = ServiceObjHandler.getImageIdList(context);
        nameInput.setText(ServiceObjHandler.getService(context, ServiceObj.NAME));
        addressInput.setText(ServiceObjHandler.getService(context, ServiceObj.ADDRESS));
        limanInput.setText(ServiceObjHandler.getService(context, ServiceObj.LINKMAN));
        telInput.setText(ServiceObjHandler.getService(context, ServiceObj.CONTACT));
        idCardInput.setText(ServiceObjHandler.getService(context, ServiceObj.ID_CARD_NUMBER));
        descriptInput.setText(ServiceObjHandler.getService(context, ServiceObj.DESCRIPT));

        String c = ServiceObjHandler.getService(context, ServiceObj.COVER);
        if (!c.equals("")) {
            loadImage(storeCardInput, c);
        }
    }

    private void loadImage(final ImageView image, final String str) {
        DownloadImageLoader.loadImage(image, str, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                DownloadImageLoader.loadImageForFile(image, str);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }

    private boolean isOnlineImage(String str) {
        return str.indexOf("http://") > -1;
    }

    private void uploadNextImage(int i) {
        int s = i + 1;
        if (s == pathList.size()) {
            uploadCard();
        } else {
            uploadImage(s);
        }
    }

    private void uploadImage(final int i) {
        if (pathList == null || pathList.isEmpty()) {
            uploadCard();
        } else {
            progress.setVisibility(View.VISIBLE);
            if (isOnlineImage(pathList.get(i))) {
                uploadIdList.add(idList.get(i));
                uploadNextImage(i);

            } else {
                File f = new File(pathList.get(i));
                AVFileHandler.uploadFile(f, new AVFileHandler.AVSaveCallback() {
                    @Override
                    public void callnack(AVException e, AVFile file) {
                        if (e == null) {
//                            MessageHandler.showToast(context, "image_" + i + " : " + file.getObjectId());
                            uploadIdList.add(file.getObjectId());
                            uploadNextImage(i);
                        } else {
                            progress.setVisibility(View.GONE);
                            MessageHandler.showToast(context, "保存失败");
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    private void uploadCard() {
        if (cardFile == null || !cardFile.exists()) {
            uploadMessage();
        } else {
            progress.setVisibility(View.VISIBLE);
            AVFileHandler.uploadFile(cardFile, new AVFileHandler.AVSaveCallback() {
                @Override
                public void callnack(AVException e, AVFile file) {
                    if (e == null) {
                        MessageHandler.showToast(context, "card : " + file.getObjectId());
                        cardId = file.getObjectId();
                        uploadMessage();
                    } else {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showToast(context, "保存失败");
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public boolean isThrough() {
        if (TextHandeler.getText(nameInput).equals("")) {
            MessageHandler.showToast(context, "车厂名称不能为空");
            return false;
        }
        if (TextHandeler.getText(addressInput).equals("")) {
            MessageHandler.showToast(context, "车厂地址不能为空");
            return false;
        }
        if (TextHandeler.getText(limanInput).equals("")) {
            MessageHandler.showToast(context, "联系人不能为空");
            return false;
        }
        if (TextHandeler.getText(telInput).equals("")) {
            MessageHandler.showToast(context, "联系人电话不能为空");
            return false;
        }
        if (TextHandeler.getText(idCardInput).equals("")) {
            MessageHandler.showToast(context, "联系人身份证不能为空");
            return false;
        }
        if (!RegexHandler.checkIdCard(TextHandeler.getText(idCardInput))) {
            MessageHandler.showToast(context, "身份证格式不正确");
            return false;
        }
        if (TextHandeler.getText(descriptInput).equals("")) {
            MessageHandler.showToast(context, "联系人身份证不能为空");
            return false;
        }
        if (pathList == null || pathList.size() < 3) {
            MessageHandler.showToast(context, "店铺照片必须上传3张");
            return false;
        }
        return true;
    }

    private void uploadMessage() {
        String url = UrlHandler.getServicesInfo();

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("sessiontoken", UserObjHandler.getSessionToken(context));
        params.addBodyParameter("name", TextHandeler.getText(nameInput));
        params.addBodyParameter("address", TextHandeler.getText(addressInput));
        params.addBodyParameter("descript", TextHandeler.getText(descriptInput));
        params.addBodyParameter("linkman", TextHandeler.getText(limanInput));
        params.addBodyParameter("id_card_number", TextHandeler.getText(idCardInput));
        params.addBodyParameter("contact", TextHandeler.getText(telInput));
        params.addBodyParameter("bussiness_license", cardId);
        params.addBodyParameter("images", getImageId());

        Log.e("sessiontoken", UserObjHandler.getSessionToken(context));
        Log.e("name", TextHandeler.getText(nameInput));
        Log.e("address", TextHandeler.getText(addressInput));
        Log.e("descript", TextHandeler.getText(descriptInput));
        Log.e("linkman", TextHandeler.getText(limanInput));
        Log.e("id_card_number", TextHandeler.getText(idCardInput));
        Log.e("contact", TextHandeler.getText(telInput));
        Log.e("images", getImageId());
        Log.e("bussiness_license", cardId);

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);
                        progress.setVisibility(View.GONE);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            int status = JsonHandle.getInt(json, "status");
                            JSONObject resultJson = JsonHandle.getJSON(json, "result");
                            if (status == 1 && resultJson != null) {
                                ServiceObjHandler.saveServiceObj(context, ServiceObjHandler.getServiceObj(resultJson));
                                MessageHandler.showToast(context, "保存成功");
                            }
                        }
                    }

                });
    }

    private void showAddImageDailod(String w) {
        where = w;
        start = DateHandle.getTime();
        final List<String> msgList = getMsgList();
        final ListDialog dialog = new ListDialog(context);
        dialog.setTitleGone();
        dialog.setList(msgList);
        dialog.setItemListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (msgList.get(position).equals("拍照")) {
                    takePhoto();
                } else {
                    selectImage();
                }
                dialog.dismiss();
            }

        });
    }

    private void takePhoto() {
        path = Passageway.takePhoto(context);
    }

    private void selectImage() {
        Passageway.selectImage(context);
    }

    public List<String> getMsgList() {
        List<String> list = new ArrayList<String>();
        list.add("拍照");
        list.add("本地相册");
        return list;
    }

    private boolean isSdcardExisting() {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private File getImageUri() {
        return new File(DownloadImageLoader.getImagePath(),
                path);
    }

    private File getImageFile() {
        String name = "store_" + where + "_" + start + ".jpg";
        return new File(DownloadImageLoader.getImagePath(), name);
    }

    private void getResizeImage(Uri uri) {
        try {
            Log.e("", uri.toString());
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor.getString(actual_image_column_index);
            File f = new File(img_path);
            getResizeImage(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getResizeImage(File f) {
        if (f.exists()) {
            try {
                FileInputStream fis = new FileInputStream(f.toString());
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                saveImage(bitmap, f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImage(Bitmap photo, File file) {
//        Drawable drawable = new BitmapDrawable(photo);
        FileOutputStream foutput = null;
        File f = getImageFile();
        try {
            foutput = new FileOutputStream(f);
            photo.compress(Bitmap.CompressFormat.JPEG, 50, foutput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (null != foutput) {
                try {
                    foutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            ExifInterface formFile = new ExifInterface(file.toString());
            ExifInterface toFile = new ExifInterface(f.toString());

            toFile.setAttribute(ExifInterface.TAG_ORIENTATION, formFile.getAttribute(ExifInterface.TAG_ORIENTATION));
            toFile.saveAttributes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (where.equals(IMAGE)) {
            mStoreImageAdapter.addPath(f.toString());
        } else {
            cardFile = f;
            DownloadImageLoader.loadImageForFile(storeCardInput, cardFile.toString());
        }
    }

    public String getImageId() {
        StringBuffer sb = new StringBuffer();
        for (String s : uploadIdList) {
            sb.append(s);
            sb.append(",");
        }

        if (sb.length() > 1) {
            return sb.substring(0, sb.length() - 1).toString();
        }
        return sb.toString();

    }

    class StoreImageAdapter extends BaseAdapter {

        final int max = 3;
        String NULL = "null";
        LayoutInflater inflater;

        StoreImageAdapter() {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addPath(String s) {
            pathList.add(s);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (pathList.size() >= max) {
                return max;
            }
            return pathList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (pathList.size() < position) {
                return pathList.get(position);
            }
            return NULL;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HolderView holder;
            if (convertView == null) {
                convertView = inflater.inflate(
                        R.layout.layout_store_image_item, null);
                holder = new HolderView();

                holder.deleteIcon = (ImageView) convertView.findViewById(R.id.damagedItem_deleteIcon);
                holder.image = (ImageView) convertView.findViewById(R.id.damagedItem_image);

                convertView.setTag(holder);
            } else {
                holder = (HolderView) convertView.getTag();
            }

            String path;
            if (pathList.size() > position) {
                path = pathList.get(position);
            } else {
                path = NULL;
            }

            setView(holder, path);
            setOnClickImage(holder.image, path);
            setDeleteImage(holder.deleteIcon, path);
            return convertView;
        }

        private void setDeleteImage(ImageView deleteIcon, final String path) {
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteImage(path);
                }
            });
        }

        private void deleteImage(String path) {
            pathList.remove(path);
            notifyDataSetChanged();
        }

        private void setOnClickImage(ImageView image, final String path) {
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (path.equals(NULL)) {
                        showAddImageDailod(IMAGE);
                    } else {
//                        Bundle b = new Bundle();
//                        b.putString(ImageActivity.PATH, path);
//                        Passageway.jumpActivity(context, ImageActivity.class, ImageActivity.REQUEST_CODE, b);
                    }
                }
            });
        }

        private void setView(final HolderView holder, final String path) {
//            int w = WinTool.getWinWidth(context) / 6;
            int w = WinTool.dipToPx(context, 50);
            holder.image.setLayoutParams(new RelativeLayout.LayoutParams(w, w));
            if (path.equals(NULL)) {
                holder.deleteIcon.setVisibility(View.INVISIBLE);
                holder.image.setScaleType(ImageView.ScaleType.FIT_XY);
                holder.image.setImageResource(R.drawable.add_pic_icon);
            } else {
                holder.deleteIcon.setVisibility(View.VISIBLE);
                holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                loadImage(holder.image, path);
            }
        }


    }


    class HolderView {
        ImageView deleteIcon;
        ImageView image;
    }

}
