package com.example.airquality.View;

import com.example.airquality.Model.AirQuality.AirQualitys;

public interface AirQualityView {
    void updateAirQualityData(AirQualitys airQuality);
    void updateDailyQuote(String dailyQuote);
    void deleteAirQualityItem(int position);
}
