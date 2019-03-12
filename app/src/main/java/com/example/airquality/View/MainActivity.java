package com.example.airquality.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.airquality.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_content_layout, new AirQualityFragment()).commit();
    }
}
