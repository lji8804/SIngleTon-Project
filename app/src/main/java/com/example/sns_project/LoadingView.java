package com.example.sns_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.sns_project.activity.MainActivity;

public class LoadingView extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_view);

        startLoading();


    }
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);  //Loagin화면을 띄운다.
                finish();   //현재 액티비티 종료
            }
        }, 2000); // 화면에 Logo 2초간 보이기
    }// startLoading Method..
}
