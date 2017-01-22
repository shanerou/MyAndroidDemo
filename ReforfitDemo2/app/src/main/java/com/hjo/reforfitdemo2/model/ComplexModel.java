
package com.hjo.reforfitdemo2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ComplexModel {

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Age")
    @Expose
    private Integer age;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("TimePoint")
    @Expose
    private String timePoint;
    @SerializedName("Develop")
    @Expose
    private List<Develop> develop = null;

    @Override
    public String toString() {
        return "ComplexModel{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", timePoint='" + timePoint + '\'' +
                ", develop=" + develop +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(String timePoint) {
        this.timePoint = timePoint;
    }

    public List<Develop> getDevelop() {
        return develop;
    }

    public void setDevelop(List<Develop> develop) {
        this.develop = develop;
    }

}
