package com.example.airquality.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.airquality.Model.NetworkController;
import com.example.airquality.Presenter.MainPresenter;
import com.example.airquality.R;

public class MainActivity extends AppCompatActivity implements MainView {
    private NetworkController mNetworkController;
    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mMainPresenter = new MainPresenter(this);
        mMainPresenter.registerReceiver();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_content_layout, new AirQualityFragment()).commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.unregisterReceiver();
    }

    @Override
    public void registerReceiver() {
        if (mNetworkController == null) {
            mNetworkController = new NetworkController();
            mNetworkController.registerReceiver(this);
        }
    }

    @Override
    public void unregisterReceiver() {
        if (mNetworkController != null) {
            mNetworkController.unregisterReceiver(this);
            mNetworkController = null;
        }
    }
}
