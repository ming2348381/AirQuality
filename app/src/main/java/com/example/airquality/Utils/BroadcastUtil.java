package com.example.airquality.Utils;

import android.content.Intent;
import com.example.airquality.Model.ApiInfo;
import com.example.airquality.Model.NetworkController;
import com.example.airquality.View.MainApplication;

public class BroadcastUtil {
    public static void sendApiBroadcast(ApiInfo apiInfo) {
        MainApplication.getAppContext().sendBroadcast(new Intent().setAction(NetworkController.INTENT_ACTION).putExtra(NetworkController.KEY_API_INFO, apiInfo));
    }

    public static void sendCrawlerBroadcast(ApiInfo apiInfo) {
        MainApplication.getAppContext().sendBroadcast(new Intent().setAction(NetworkController.INTENT_ACTION_CRAWLER).putExtra(NetworkController.KEY_API_INFO, apiInfo));
    }
}
