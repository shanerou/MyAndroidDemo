

        package com.hjo.reforfitdemo2.model;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class TestModel {

    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("CityName")
    @Expose
    private String cityName;
    @SerializedName("Value")
    @Expose
    private String value;
    @SerializedName("Level")
    @Expose
    private String level;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        if (date!=null)date=date.replace("T"," ");
        this.date = date;
    }

    public String getCityName() {

        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "TestModel{" +
                "date='" + date + '\'' +
                ", cityName='" + cityName + '\'' +
                ", value='" + value + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}