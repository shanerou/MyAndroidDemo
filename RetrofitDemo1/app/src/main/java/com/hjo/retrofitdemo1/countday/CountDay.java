
        package com.hjo.retrofitdemo1.countday;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class CountDay {

    @SerializedName("CityName")
    @Expose
    private String cityName;
    @SerializedName("TimeRangeValue")
    @Expose
    private String timeRangeValue;
    @SerializedName("TimeRange")
    @Expose
    private Integer timeRange;
    @SerializedName("G1")
    @Expose
    private Integer g1;
    @SerializedName("G2")
    @Expose
    private Integer g2;
    @SerializedName("G3")
    @Expose
    private Integer g3;
    @SerializedName("G4")
    @Expose
    private Integer g4;
    @SerializedName("G5")
    @Expose
    private Integer g5;
    @SerializedName("G6")
    @Expose
    private Integer g6;
    @SerializedName("G12Per")
    @Expose
    private Double g12Per;
    @SerializedName("G1Per")
    @Expose
    private Integer g1Per;
    @SerializedName("G2Per")
    @Expose
    private Double g2Per;
    @SerializedName("G3Per")
    @Expose
    private Double g3Per;
    @SerializedName("G4Per")
    @Expose
    private Double g4Per;
    @SerializedName("G5Per")
    @Expose
    private Double g5Per;
    @SerializedName("G6Per")
    @Expose
    private Double g6Per;

    @Override
    public String toString() {
        return "MorePieChartModel [TimeRangeValue=" + timeRangeValue
                + ", TimeRange=" + timeRange + ", G1=" + g1 + ", G2=" + g2
                + ", G3=" + g3 + ", G4=" + g4 + ", G5=" + g5 + ", G6=" + g6
                + ", G1Per=" + g1Per + ", G2Per=" + g2Per + ", G3Per=" +g3Per
                + ", G4Per=" + g4Per + ", G5Per=" + g5Per + ", G6Per=" + g6Per
                + ", G12Per=" + g12Per + "]";
    }


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTimeRangeValue() {
        return timeRangeValue;
    }

    public void setTimeRangeValue(String timeRangeValue) {
        this.timeRangeValue = timeRangeValue;
    }

    public Integer getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(Integer timeRange) {
        this.timeRange = timeRange;
    }

    public Integer getG1() {
        return g1;
    }

    public void setG1(Integer g1) {
        this.g1 = g1;
    }

    public Integer getG2() {
        return g2;
    }

    public void setG2(Integer g2) {
        this.g2 = g2;
    }

    public Integer getG3() {
        return g3;
    }

    public void setG3(Integer g3) {
        this.g3 = g3;
    }

    public Integer getG4() {
        return g4;
    }

    public void setG4(Integer g4) {
        this.g4 = g4;
    }

    public Integer getG5() {
        return g5;
    }

    public void setG5(Integer g5) {
        this.g5 = g5;
    }

    public Integer getG6() {
        return g6;
    }

    public void setG6(Integer g6) {
        this.g6 = g6;
    }

    public Double getG12Per() {
        return g12Per;
    }

    public void setG12Per(Double g12Per) {
        this.g12Per = g12Per;
    }

    public Integer getG1Per() {
        return g1Per;
    }

    public void setG1Per(Integer g1Per) {
        this.g1Per = g1Per;
    }

    public Double getG2Per() {
        return g2Per;
    }

    public void setG2Per(Double g2Per) {
        this.g2Per = g2Per;
    }

    public Double getG3Per() {
        return g3Per;
    }

    public void setG3Per(Double g3Per) {
        this.g3Per = g3Per;
    }

    public Double getG4Per() {
        return g4Per;
    }

    public void setG4Per(Double g4Per) {
        this.g4Per = g4Per;
    }

    public Double getG5Per() {
        return g5Per;
    }

    public void setG5Per(Double g5Per) {
        this.g5Per = g5Per;
    }

    public Double getG6Per() {
        return g6Per;
    }

    public void setG6Per(Double g6Per) {
        this.g6Per = g6Per;
    }

}