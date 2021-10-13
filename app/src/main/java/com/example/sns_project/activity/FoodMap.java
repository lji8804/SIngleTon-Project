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

import com.example.sns_project.R;

import net.daum.mf.map.api.MapView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;

public class FoodMap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                MODE_PRIVATE);

        final LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        MapView mapView = new MapView(this);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.mapView2);
        mapViewContainer.addView(mapView);

        String url = "kakaomap://search?q=맛집&p="+lm;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Log.d("인텐트",intent.toString());
        startActivity(intent);

    }

}