package com.example.airquality.Presenter;

import com.example.airquality.Model.AirQuality.AirQualitys;
import com.example.airquality.Model.ApiInfo;
import com.example.airquality.Model.NetworkController.ApiRequest;
import com.example.airquality.Utils.BroadcastUtil;
import com.example.airquality.View.AirQualityView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirQualityPresenter {
    private AirQualityView mAirQualityView;
    private AirQualitys mAirQualities;

    public AirQualityPresenter(AirQualityView airQualityView) {
        mAirQualityView = airQualityView;
    }

    public void loadAirQualityData() {
        mAirQualities = AirQualitys.getAllDataForDatabase();
        if (mAirQualities != null) {
            mAirQualityView.updateAirQualityData(mAirQualities);
        }

        Map<String, String> parameter = new HashMap<>();
        parameter.put("format", "json");
        parameter.put("token", "q0/ZURMyp0yVWE2w2sLtMw");
        BroadcastUtil.sendApiBroadcast(new ApiInfo<>(AirQualitys.class, parameter));
    }

    public void deleteAirQualityData(int position) {
        mAirQualities.get(position).deleteToDatabase();
        mAirQualities.remove(position);
        mAirQualityView.updateAirQualityData(mAirQualities);
    }

    public void ApiBroadcastReceiver(String action) {
        if (AirQualitys.class.getAnnotation(ApiRequest.class).path().equals(action)) {
            mAirQualities = AirQualitys.getAllDataForDatabase();
            mAirQualityView.updateAirQualityData(mAirQualities);
        }
    }

    public List<String> getFilterActions() {
        List<String> actions = new ArrayList<>();
        ApiRequest apiRequest = AirQualitys.class.getAnnotation(ApiRequest.class);
        actions.add(apiRequest.path());
        return actions;
    }
}
