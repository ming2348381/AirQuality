package com.example.airquality.Presenter;

import com.example.airquality.Model.AirQuality.AirQualitys;
import com.example.airquality.View.AirQualityView;

public class AirQualityPresenter {
    private AirQualityView mAirQualityView;

    public AirQualityPresenter(AirQualityView airQualityView) {
        mAirQualityView = airQualityView;
    }

    public void loadAirQualityData() {
        //Todo temp data
        mAirQualityView.updateAirQualityData(new AirQualitys());
    }

    public void deleteAirQualityData(int position){
        //Todo implement
    }
}
