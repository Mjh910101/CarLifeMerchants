package com.carlife.merchants.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.carlife.merchants.R;
import com.carlife.merchants.handlers.MapHandler;
import com.carlife.merchants.map.MyDrivingRouteOverlay;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
 * Created by Hua on 16/1/14.
 */
public class PositionActivity extends BaseActivity {

    public final static String IS_HAVE = "isHave";
    public final static String IS_NAVIGATION = "isNavigation";
    private final static int MAPZOOM = 14;
    public final static int REQUEST_CODE = 100;

    public final static String IS_OK = "isOk";
    public final static String ADDRESS_KEY = "address";
    public final static String LATITUDE_KEY = "latitude";
    public final static String LONGITUDE_KEY = "longitude";

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.position_baiduMap)
    private MapView mMapView;
    @ViewInject(R.id.position_progress)
    private ProgressBar progress;
    @ViewInject(R.id.position_btnBox)
    private LinearLayout btnBox;

    private BaiduMap mBaiduMap;
    private Overlay mOverlay;
    private BitmapDescriptor positionIcon;
    private BDLocation mBDLocation;
    private LatLng mapPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_position);
        context = this;
        ViewUtils.inject(this);

        initActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @OnClick({R.id.title_backBtn, R.id.position_refreshBtn, R.id.position_saveBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.position_refreshBtn:
                refreshPosition();
                break;
            case R.id.position_saveBtn:
                saveLocation();
                break;
        }
    }

    private void saveLocation() {
        if (mBDLocation != null) {
            Intent i = new Intent();
            Bundle b = new Bundle();
            b.putString(ADDRESS_KEY, mBDLocation.getAddrStr());
            b.putDouble(LATITUDE_KEY, mBDLocation.getLatitude());
            b.putDouble(LONGITUDE_KEY, mBDLocation.getLongitude());
            i.putExtras(b);
            setResult(REQUEST_CODE, i);
            finish();
        }
    }

    private void refreshPosition() {
        if (progress.getVisibility() == View.GONE) {
            getMapPosition();
        }
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("定位");

        mBaiduMap = mMapView.getMap();
        positionIcon = BitmapDescriptorFactory
                .fromResource(R.drawable.position_icon);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.getBoolean(IS_HAVE)) {
                btnBox.setVisibility(View.GONE);
//                LatLng point = new LatLng(22.58421, 113.377176);
                LatLng point = new LatLng(b.getDouble(LATITUDE_KEY), b.getDouble(LONGITUDE_KEY));
                if (b.getBoolean(IS_NAVIGATION)) {
                    setNavigation(point);
                    return;
                }
                setMapPoint(point);
                return;
            }
        }
        getMapPosition();

    }


    public void setNavigation(final LatLng start) {
        progress.setVisibility(View.VISIBLE);
        LocationClient locationClient = MapHandler.getAddress(context, new MapHandler.MapListener() {
            @Override
            public void callback(BDLocation location) {
                LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                initMap(point);
                progress.setVisibility(View.GONE);
                RoutePlanSearch mSearch = RoutePlanSearch.newInstance();
                mSearch.setOnGetRoutePlanResultListener(listener);
                PlanNode s = PlanNode.withLocation(start);
                PlanNode e = PlanNode.withLocation(point);
                mSearch.drivingSearch((new DrivingRoutePlanOption())
                        .from(e)
                        .to(s));
//                mSearch.destroy();
            }
        });
    }

    OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            //获取步行线路规划结果
        }

        public void onGetTransitRouteResult(TransitRouteResult result) {
            //获取公交换乘路径规划结果
        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            //获取驾车线路规划结果
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(context, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                //result.getSuggestAddrInfo()
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    };

    public void getMapPosition() {
        progress.setVisibility(View.VISIBLE);
        LocationClient locationClient = MapHandler.getAddress(context, new MapHandler.MapListener() {
            @Override
            public void callback(BDLocation location) {
                mBDLocation = location;
                LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                setMapPoint(point);

                progress.setVisibility(View.GONE);
            }
        });

    }

    public void setMapPoint(LatLng point) {
        initMap(point);

        if (mOverlay != null) {
            mOverlay.remove();
        }

        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(positionIcon);
        mOverlay = mBaiduMap.addOverlay(option);
    }

    private void initMap(LatLng point) {
        MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(MAPZOOM)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                .newMapStatus(mMapStatus);
        mBaiduMap.animateMapStatus(mMapStatusUpdate, 500);
    }

}
