package com.example.airquality.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AirQuality {
    public static class AirQualitys extends ArrayList<AirQuality> {

    }

    private String SiteName;
    private String County;
    private String AQI;
    private String Pollutant;
    private String Status;
    private String SO2;
    private String CO;
    @SerializedName("CO_8hr")
    private String COEvery8hr;
    private String O3;
    @SerializedName("O3_8hr")
    private String O3Every8hr;
    private String PM10;
    @SerializedName("PM2.5")
    private String PM2Dot5;
    private String NO2;
    private String NOx;
    private String NO;
    private String WindSpeed;
    private String WindDirec;
    private String PublishTime;
    @SerializedName("PM2.5_AVG")
    private String PM2Dot5Avg;
    @SerializedName("PM10_AVG")
    private String PM10Avg;
    @SerializedName("SO2_AVG")
    private String SO2Avg;
    private String Longitude;
    private String Latitude;

    public String getSiteName() {
        return SiteName;
    }

    public String getCounty() {
        return County;
    }

    public String getAQI() {
        return AQI;
    }

    public String getPollutant() {
        return Pollutant;
    }

    public String getStatus() {
        return Status;
    }

    public String getSO2() {
        return SO2;
    }

    public String getCO() {
        return CO;
    }

    public String getCOEvery8hr() {
        return COEvery8hr;
    }

    public String getO3() {
        return O3;
    }

    public String getO3Every8hr() {
        return O3Every8hr;
    }

    public String getPM10() {
        return PM10;
    }

    public String getPM2Dot5() {
        return PM2Dot5;
    }

    public String getNO2() {
        return NO2;
    }

    public String getNOx() {
        return NOx;
    }

    public String getNO() {
        return NO;
    }

    public String getWindSpeed() {
        return WindSpeed;
    }

    public String getWindDirec() {
        return WindDirec;
    }

    public String getPublishTime() {
        return PublishTime;
    }

    public String getPM2Dot5Avg() {
        return PM2Dot5Avg;
    }

    public String getPM10Avg() {
        return PM10Avg;
    }

    public String getSO2Avg() {
        return SO2Avg;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getLatitude() {
        return Latitude;
    }
}
