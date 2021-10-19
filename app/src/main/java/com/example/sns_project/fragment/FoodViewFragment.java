package com.example.sns_project.fragment;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.sns_project.R;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class FoodViewFragment extends Fragment {
    double lat, lng;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_view, container, false);


        final LocationManager lm = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

        // 수동으로 위치 구하기
//        String locationProvider = LocationManager.GPS_PROVIDER;
//        Location lastKnownLocation = lm.getLastKnownLocation(locationProvider);
//        if (lastKnownLocation != null) {
//            double lng = lastKnownLocation.getLatitude();
//            double lat = lastKnownLocation.getLatitude();
//            Log.d("Main", "longtitude=" + lng + ", latitude=" + lat);
//        }

        // GPS 프로바이더 사용가능여부
        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        boolean isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        MapView mapView = new MapView(getContext());

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lng = location.getLongitude();

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };



        ViewGroup mapViewContainer = (ViewGroup) view.findViewById(R.id.mapView2);
        mapViewContainer.addView(mapView);

//        mapView.setCurrentLocationRadius(2000);

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lng), true);

        mapView.setZoomLevel(4, true);

        String url = "kakaomap://search?q=맛집&p=" + lm;

//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        Log.d("인텐트",intent.toString());
//        startActivity(intent);


        return view;

    }
}
