package com.example.sns_project.foodmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.example.sns_project.KakaoLocal.Data;
import com.example.sns_project.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class FoodMap extends AppCompatActivity implements MapView.POIItemEventListener {
    private double lat;
    private double lng;
    private ImageButton ibBtnSearch,ibBtnLocation;
    private EditText edtSearch;


    private ArrayList<FoodData> foodDataList = new ArrayList<>();
    private LocationManager lm;
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private MapPOIItem marker;
    private boolean isGPS ;

    MutableLiveData<Data> kakao = new MutableLiveData<>();
    ArrayList<Data> dataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        ibBtnSearch = findViewById(R.id.ibBtnSearch);
        edtSearch = findViewById(R.id.edtSearch);
        ibBtnLocation = findViewById(R.id.ibBtnLocation);

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        //GPS
        getGPSLocation();

        // GPS 프로바이더 사용가능여부
        final boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        final boolean isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        // 수동으로 위치 구하기
//        getCurrentLocation();

        //초기화면설정
        init();

        //리스너설정
        onClickListener();

//        String url = "kakaomap://search?q=맛집&p=" + lm;
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        Log.d("인텐트",intent.toString());
//        startActivity(intent);


    }

    private void onClickListener() {
        //검색된 정보 가져오기
        ibBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFoodData();
                if(foodDataList.size() != 0){
                    placeMaker();
                    Log.d("ibBtnSearch", foodDataList.toString());

                }
            }
        });
        //GPS 설정하기
        ibBtnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGPS == false) {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                    ibBtnLocation.setImageResource(R.drawable.ic_baseline_my_location_red);
                    isGPS = true;
                } else {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                    ibBtnLocation.setImageResource(R.drawable.ic_baseline_my_location_24);
                    isGPS = false;
                }
            }
        });
        mapView.setPOIItemEventListener(new MapView.POIItemEventListener() {
            @Override
            public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

            }

            @Override
            public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

            }
        });

    }

    private void init() {
        mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.mapView2);
        mapViewContainer.addView(mapView);
        mapView.setCurrentLocationRadius(2000);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lng), true);
        mapView.setZoomLevel(4, true);

        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
        ibBtnLocation.setImageResource(R.drawable.ic_baseline_my_location_red);
        isGPS = true;
        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
    }


    private void getGPSLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    MODE_PRIVATE);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                            Log.d("getGPSLocation", "생성 lat= "+lat + ", lng = "+ lng);
                        }
                    }
                });
    }

    private void placeMaker() {
        Log.d("Main", "longtitude=" + lng + ", latitude=" + lat);
        marker = new MapPOIItem();
        
        for (int i = 0; i < foodDataList.size(); i++) {
            marker.setItemName(foodDataList.get(i).getName());
            double x = Double.parseDouble(foodDataList.get(i).getLongitude());
            double y = Double.parseDouble(foodDataList.get(i).getLatitude());
            marker.setTag(i);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord( x, y ));
            mapView.addPOIItem(marker);
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        }
        marker.setShowCalloutBalloonOnTouch(true);
    }
    
    

    private void getCurrentLocation() {
        String locationProvider = LocationManager.GPS_PROVIDER;
        @SuppressLint("MissingPermission")
        Location lastKnownLocation = lm.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null) {
            lng = lastKnownLocation.getLongitude();
            lat = lastKnownLocation.getLatitude();
            Log.d("Main", "longtitude=" + lng + ", latitude=" + lat);
        }
    }

    private void getFoodData() {

        Log.d("액티비티", "생성");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        dataArrayList.clear();
        String address = edtSearch.getText().toString();
        ApiService api = retrofit.create(ApiService.class);
        api.getAddress(ApiService.ApiKey, address, String.valueOf(lng), String.valueOf(lat), 2000)
                .enqueue(new Callback<Data>() {
                    @Override
                    public void onResponse(Call<Data> call, Response<Data> response) {
                        if (response.body() != null) {
                            for (int i = 0; i < 15; i++) {
                                kakao.setValue(response.body());
//                                Log.i("메인", kakao.getValue().getDocuments().get(i).getPlaceName());
                                dataArrayList.add(response.body());
                                Log.i("메인", dataArrayList.get(i).getDocuments().get(i).getPlaceName());
                                String[] colum = {
                                        dataArrayList.get(i).getDocuments().get(i).getPlaceName(),
                                        dataArrayList.get(i).getDocuments().get(i).getCategoryName(),
                                        dataArrayList.get(i).getDocuments().get(i).getRoadAddressName(),
                                        dataArrayList.get(i).getDocuments().get(i).getPhone(),
                                        dataArrayList.get(i).getDocuments().get(i).getX(),
                                        dataArrayList.get(i).getDocuments().get(i).getY(),
                                        dataArrayList.get(i).getDocuments().get(i).getPlaceUrl()
                                };
                                FoodData foodData = new FoodData(
                                        colum[0],colum[1],colum[2],colum[3],colum[4],colum[5],colum[6]
                                );
                                foodDataList.add(foodData);
                            }

                        } else {
                            Log.i("메인", "리스폰스 널" + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Data> call, Throwable t) {
                        Log.d("kakao", "실패");
                    }
                });

//        kakao.observe(this, new Observer<Data>() {
//            @Override
//            public void onChanged(Data data) {
////               Log.i("메인", data.documentsList.get(0).getAddress_name());
//            }
//        });
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }


    private interface ApiService {
        String baseUrl = "https://dapi.kakao.com/";
        String ApiKey = "KakaoAK 105421c1ffe84bf639305ce045c11e92";

        @GET("v2/local/search/keyword.json?page=1&size=15&sort=distance")
        Call<Data> getAddress(@Header("Authorization") String key,
                              @Query("query") String address,
                              @Query("x") String lon,
                              @Query("y") String lat,
                              @Query("radius") Integer rad);
    }

    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon = null;

        public CustomCalloutBalloonAdapter() {
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            return null;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            return null;
        }
    }

}