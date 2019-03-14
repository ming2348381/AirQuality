package com.example.airquality.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.airquality.Model.AirQuality;
import com.example.airquality.Model.AirQuality.AirQualitys;
import com.example.airquality.R;
import com.example.airquality.View.AirQualityView;
import com.example.airquality.View.BaseViewHolder;

public class AirQualityAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private AirQualitys mAirQualitys;
    private AirQualityView mAirQualityView;

    public AirQualityAdapter(AirQualityView airQualityView) {
        mAirQualityView = airQualityView;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.air_quality_detail_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBindViewHolder(mAirQualitys.get(position));
    }

    @Override
    public int getItemCount() {
        return mAirQualitys.size();
    }

    public void setItems(AirQualitys airQualitys) {
        mAirQualitys = airQualitys;
        notifyDataSetChanged();
    }

    class DetailViewHolder extends BaseViewHolder<AirQuality> {
        private TextView mSiteNameTextView;
        private TextView mCountyTextView;
        private TextView mAQITextView;
        private TextView mPollutantTextView;
        private TextView mStatusTextView;
        private TextView mSO2TextView;
        private TextView mCOTextView;
        private TextView mCOEvery8hrTextView;
        private TextView mO3TextView;
        private TextView mO3Every8hrTextView;
        private TextView mPM10TextView;
        private TextView mPM2Dot5TextView;
        private TextView mNO2TextView;
        private TextView mNOxTextView;
        private TextView mNOTextView;
        private TextView mWindSpeedTextView;
        private TextView mWindDirecTextView;
        private TextView mPublishTimeTextView;
        private TextView mPM2Dot5AvgTextView;
        private TextView mPM10AvgTextView;
        private TextView mSO2AvgTextView;
        private TextView mLongitudeTextView;
        private TextView mLatitudeTextView;

        private DetailViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(mAirQualityView.deleteAirQualityItem(getAdapterPosition()));
            mSiteNameTextView = itemView.findViewById(R.id.air_quality_item_site_name_text_view);
            mCountyTextView = itemView.findViewById(R.id.air_quality_item_county_text_view);
            mAQITextView = itemView.findViewById(R.id.air_quality_item_aqi_text_view);
            mPollutantTextView = itemView.findViewById(R.id.air_quality_item_pollutant_text_view);
            mStatusTextView = itemView.findViewById(R.id.air_quality_item_status_text_view);
            mSO2TextView = itemView.findViewById(R.id.air_quality_item_so2_text_view);
            mCOTextView = itemView.findViewById(R.id.air_quality_item_co_text_view);
            mCOEvery8hrTextView = itemView.findViewById(R.id.air_quality_item_co_every8hr_text_view);
            mO3TextView = itemView.findViewById(R.id.air_quality_item_o3_text_view);
            mO3Every8hrTextView = itemView.findViewById(R.id.air_quality_item_o3_every8hr_text_view);
            mPM10TextView = itemView.findViewById(R.id.air_quality_item_pm10_text_view);
            mPM2Dot5TextView = itemView.findViewById(R.id.air_quality_item_pm2dot5_text_view);
            mNO2TextView = itemView.findViewById(R.id.air_quality_item_no2_text_view);
            mNOxTextView = itemView.findViewById(R.id.air_quality_item_nox_text_view);
            mNOTextView = itemView.findViewById(R.id.air_quality_item_no_text_view);
            mWindSpeedTextView = itemView.findViewById(R.id.air_quality_item_wind_speed_text_view);
            mWindDirecTextView = itemView.findViewById(R.id.air_quality_item_wind_direc_text_view);
            mPublishTimeTextView = itemView.findViewById(R.id.air_quality_item_publish_time_text_view);
            mPM2Dot5AvgTextView = itemView.findViewById(R.id.air_quality_item_pm2dot5_avg_text_view);
            mPM10AvgTextView = itemView.findViewById(R.id.air_quality_item_pm10_avg_text_view);
            mSO2AvgTextView = itemView.findViewById(R.id.air_quality_item_so2_avg_text_view);
            mLongitudeTextView = itemView.findViewById(R.id.air_quality_item_longitude_text_view);
            mLatitudeTextView = itemView.findViewById(R.id.air_quality_item_latitude_text_view);
        }

        @Override
        public void onBindViewHolder(AirQuality data) {
            mSiteNameTextView.setText(data.getSiteName());
            mCountyTextView.setText(data.getCounty());
            mAQITextView.setText(data.getAQI());
            mPollutantTextView.setText(data.getPollutant());
            mStatusTextView.setText(data.getStatus());
            mSO2TextView.setText(data.getSO2());
            mCOTextView.setText(data.getCO());
            mCOEvery8hrTextView.setText(data.getCOEvery8hr());
            mO3TextView.setText(data.getO3());
            mO3Every8hrTextView.setText(data.getO3Every8hr());
            mPM10TextView.setText(data.getPM10());
            mPM2Dot5TextView.setText(data.getPM2Dot5());
            mNO2TextView.setText(data.getNO2());
            mNOxTextView.setText(data.getNOx());
            mNOTextView.setText(data.getNO());
            mWindSpeedTextView.setText(data.getWindSpeed());
            mWindDirecTextView.setText(data.getWindDirec());
            mPublishTimeTextView.setText(data.getPublishTime());
            mPM2Dot5AvgTextView.setText(data.getPM2Dot5Avg());
            mPM10AvgTextView.setText(data.getPM10Avg());
            mSO2AvgTextView.setText(data.getSO2Avg());
            mLongitudeTextView.setText(data.getLongitude());
            mLatitudeTextView.setText(data.getLatitude());
        }
    }
}
