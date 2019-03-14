package com.example.airquality.View;

import android.view.View.OnLongClickListener;
import com.example.airquality.Model.AirQuality.AirQualitys;

public interface AirQualityView {
    void updateAirQualityData(AirQualitys airQuality);
    OnLongClickListener deleteAirQualityItem(int position);
}
