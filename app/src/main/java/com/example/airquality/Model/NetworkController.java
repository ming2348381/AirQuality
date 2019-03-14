package com.example.airquality.Model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import com.example.airquality.Utils.DatabaseUtil;
import com.example.airquality.Utils.GsonHelper;
import com.example.airquality.Utils.okHttpUtil;
import com.example.airquality.View.MainApplication;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.airquality.Model.NetworkController.RequestType.GET;

public class NetworkController {
    public static final String KEY_API_INFO = "apiInfo";
    public static final String INTENT_ACTION = "api_request";

    private BroadcastReceiver mBroadcastReceiver;

    public void registerReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_ACTION);
        mBroadcastReceiver = new ApiBroadcastReceiver();
        context.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    public void unregisterReceiver(Context context) {
        if (mBroadcastReceiver != null) {
            context.unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
    }

    private class ApiBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ApiInfo apiInfo = (ApiInfo) intent.getSerializableExtra(KEY_API_INFO);
            if (apiInfo != null) {
                get(apiInfo.getResponseClass(), apiInfo.getParameter());
            }
        }
    }

    private <T> void get(Class<T> tClass, Map<String, String> parameter) {
        ApiRequest apiRequest = tClass.getAnnotation(ApiRequest.class);
        executeApi(GET, getRequestUrl(apiRequest, parameter), apiRequest.path(), apiRequest.get(), TimeoutType.DEFAULT, apiRequest.certificate());
    }

    private <T> void executeApi(RequestType requestType, final String requestUrl, final String broadcastAction, final Class responseClass, TimeoutType timeoutType, String certificate) {
        int timeout = timeoutType.getMilliseconds();

        okHttpUtil.getTrustClient(certificate).newBuilder().connectTimeout(timeout, TimeUnit.MILLISECONDS).readTimeout(timeout, TimeUnit.MILLISECONDS).writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .build().newCall(createRequest(requestType, requestUrl)).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                onFailed(e.toString());
            }

            @Override
            public void onResponse(Call call, final Response response) {
                try {
                    Object json = new JSONTokener(response.body().string()).nextValue();
                    if (json == null) {
                        onFailed();
                    } else if (json instanceof JSONArray) {
                        doJsonArray(response, json);
                    } else {
                        onFailed();
                    }
                } catch (Exception e) {
                    onFailed(e.toString());
                }
            }

            private void doJsonArray(Response response, Object json) {
                if (response.isSuccessful()) {
                    onSuccess((T) GsonHelper.fromJson(json.toString(), responseClass));
                } else {
                    onFailed();
                }
            }

            private void onSuccess(final T data) {
                if (data instanceof DatabaseStorable) {
                    ((DatabaseStorable) data).setObjectToDatabase();
                    MainApplication.getAppContext().sendBroadcast(new Intent().setAction(broadcastAction));
                }
            }

            private void onFailed() {
                MainApplication.getAppContext().sendBroadcast(new Intent().setAction(requestUrl).putExtra("result", false));
            }

            private void onFailed(String message) {
                Log.d("exception", message);
                onFailed();
            }
        });
    }

    private Request createRequest(RequestType requestType, String requestUrl) {
        Request.Builder builder = new Request.Builder()
                .url(requestUrl)
                .addHeader("content-type", "application/json");

        setBody(requestType, builder);

        return builder.build();
    }

    private void setBody(RequestType requestType, Request.Builder builder) {
        switch (requestType) {
            case GET:
                builder.get();
                break;
        }
    }

    private static String getRequestUrl(ApiRequest apiRequest, Map<String, String> parameter) {
        apiRequest.path();
        Uri.Builder builder = Uri.parse(apiRequest.path()).buildUpon();

        if (parameter != null) {
            for (Map.Entry<String, String> entry : parameter.entrySet()) {
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        return builder.toString();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = {ElementType.TYPE})
    public @interface ApiRequest {
        Class get() default EmptyResponse.class;

        String path() default "";

        String certificate() default "";
    }

    public enum RequestType {
        GET;
    }

    public enum TimeoutType {
        DEFAULT(10 * 1000);

        private int mMilliseconds;

        TimeoutType(int milliseconds) {
            mMilliseconds = milliseconds;
        }

        public int getMilliseconds() {
            return mMilliseconds;
        }
    }

    public static class EmptyResponse {

    }
}
