package com.example.airquality.View;

import android.content.*;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.airquality.Adapter.AirQualityAdapter;
import com.example.airquality.Model.AirQuality.AirQualitys;
import com.example.airquality.Model.ApiInfo;
import com.example.airquality.Presenter.AirQualityPresenter;
import com.example.airquality.R;
import com.example.airquality.Utils.DialogUtil;

public class AirQualityFragment extends Fragment implements AirQualityView {
    private TextView mDailyQuote;
    private RecyclerView mDetailRecyclerView;

    private AirQualityPresenter mAirQualityPresenter;
    private AirQualityAdapter mAirQualityAdapter;
    private ApiBroadcastReceiver mBroadcastReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.air_quality_fragment, container, false);
        mDailyQuote = view.findViewById(R.id.air_quality_fragment_daily_quote_text_view);
        mDetailRecyclerView = view.findViewById(R.id.air_quality_fragment_detail_recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initDate();
        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        registerReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver();
    }

    private void initDate() {
        mAirQualityPresenter = new AirQualityPresenter(this);
        mAirQualityAdapter = new AirQualityAdapter(this);
    }

    private void initView() {
        mDetailRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDetailRecyclerView.setAdapter(mAirQualityAdapter);
        mAirQualityPresenter.loadAirQualityData();
    }

    @Override
    public void updateAirQualityData(AirQualitys airQualitys) {
        mAirQualityAdapter.setItems(airQualitys);
    }

    @Override
    public void deleteAirQualityItem(final int position) {
        DialogUtil.showDialog(getContext(), R.string.delete_item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAirQualityPresenter.deleteAirQualityData(position);
            }
        });
    }

    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        for (String action : mAirQualityPresenter.getFilterActions()) {
            intentFilter.addAction(action);
        }
        mBroadcastReceiver = new ApiBroadcastReceiver();
        getContext().registerReceiver(mBroadcastReceiver, intentFilter);
    }

    public void unregisterReceiver() {
        if (mBroadcastReceiver != null) {
            getContext().unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
    }

    private class ApiBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mAirQualityPresenter.ApiBroadcastReceiver(intent.getAction());
        }
    }

}
