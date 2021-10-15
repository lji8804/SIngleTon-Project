package com.example.sns_project.activity;

import android.Manifest;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.sns_project.KakaoLocal.Data;
import com.example.sns_project.R;

import net.daum.mf.map.api.MapView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class FoodMap extends AppCompatActivity {
    ArrayList<Data> dataArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final MutableLiveData<Data> kakao = new MutableLiveData<>();
        Log.d("액티비티", "생성");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService api = retrofit.create(ApiService.class);
        api.getAddress(ApiService.ApiKey, "음식점", "127.038757", "37.560650", 20000)
                .enqueue(new Callback<Data>() {
                    @Override
                    public void onResponse(Call<Data> call, Response<Data> response) {
                        if(response.body() != null){
                            for (int i = 0; i < 15; i++) {
                                kakao.setValue(response.body());
                                Log.i("메인", kakao.getValue().getDocuments().get(i).getPlaceName());
                                dataArrayList.add(response.body());
                                Log.i("메인", dataArrayList.get(i).getDocuments().get(i).getPlaceName());
                            }

                        }else{
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
    }

    private interface ApiService{
        String baseUrl = "https://dapi.kakao.com/";
        String ApiKey = "KakaoAK 105421c1ffe84bf639305ce045c11e92";

        @GET("v2/local/search/keyword.json")
        Call<Data> getAddress(@Header("Authorization")String key,
                              @Query("query")String address,
                              @Query("x")String lon,
                              @Query("y")String lat,
                              @Query("radius")Integer rad);

    }
}
//        ActivityCompat.requestPermissions(this,new String[]{
//                        Manifest.permission.INTERNET,
//                        Manifest.permission.ACCESS_FINE_LOCATION},
//                MODE_PRIVATE);
//
//        final LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        MapView mapView = new MapView(this);
//
//        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.mapView2);
//        mapViewContainer.addView(mapView);
//
//        String url = "kakaomap://search?q=맛집&p="+lm;
//
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        Log.d("인텐트",intent.toString());
//        startActivity(intent);
