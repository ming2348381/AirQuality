package com.example.airquality.Presenter;

import com.example.airquality.View.MainView;

public class MainPresenter {
    private MainView mMainView;

    public MainPresenter(MainView mainView) {
        mMainView = mainView;
    }

    public void registerReceiver() {
        mMainView.registerReceiver();
    }

    public void unregisterReceiver() {
        mMainView.unregisterReceiver();
    }
}
