package com.example.sns_project.foodmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.sns_project.KakaoLocal.Data;
import com.example.sns_project.R;

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

public class FoodMap extends AppCompatActivity implements MapView.POIItemEventListener{
    private String lat, lng;
    private ImageButton ibBtnSearch;
    private EditText edtSearch;


    private ArrayList<FoodData> foodDataList = new ArrayList<>();
    private LocationManager lm;
    private MapView mapView;

    MutableLiveData<Data> kakao = new MutableLiveData<>();
    ArrayList<Data> dataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        ibBtnSearch = findViewById(R.id.ibBtnSearch);
        edtSearch = findViewById(R.id.edtSearch);

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        // GPS 프로바이더 사용가능여부
        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        boolean isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        // 수동으로 위치 구하기
        getCurrentLocation();

        mapView = new MapView(this);
        //검색된 정보 가져오기
        ibBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFoodData();
            }
        });




        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                lat = String.valueOf(location.getLatitude());
                lng = String.valueOf(location.getLongitude());

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }

        };
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.mapView2);
        mapViewContainer.addView(mapView);
        mapView.setCurrentLocationRadius(2000);

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(lat), Double.parseDouble(lng)), true);
        mapView.setZoomLevel(4, true);

        String url = "kakaomap://search?q=맛집&p=" + lm;

//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        Log.d("인텐트",intent.toString());
//        startActivity(intent);
    }

    private void placeMaker() {
        Log.d("Main", "longtitude=" + lng + ", latitude=" + lat);
        MapPOIItem marker = new MapPOIItem();

        dataArrayList.clear();
        for (int i = 0; i < dataArrayList.size(); i++) {
            marker.setItemName(foodDataList.get(i).getName());
            double x = Double.parseDouble(foodDataList.get(i).getLongitude());
            double y = Double.parseDouble(foodDataList.get(i).getLatitude());
            marker.setTag(i);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord( x, y ));
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            mapView.addPOIItem(marker);
        }
    }

    private void getCurrentLocation() {
        String locationProvider = LocationManager.GPS_PROVIDER;
        @SuppressLint("MissingPermission")
        Location lastKnownLocation = lm.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null) {
            lng = String.valueOf(lastKnownLocation.getLongitude());
            lat = String.valueOf(lastKnownLocation.getLatitude());
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
        api.getAddress(ApiService.ApiKey, address, lng, lat, 20000)
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

        kakao.observe(this, new Observer<Data>() {
            @Override
            public void onChanged(Data data) {
//               Log.i("메인", data.documentsList.get(0).getAddress_name());
            }
        });
        placeMaker();

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

}