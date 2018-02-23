package com.example.dennis.testapp.LocalApps;


public class LocalAppInfo {

    String id;
    String smallAppIconUrl;
    String appTitle;
    boolean payedFor;
    float rating;
    boolean installed;


    public LocalAppInfo(String id, String smallAppIconUrl, String appTitle, boolean payedFor, float rating, boolean installed){

        this.id = id;
        this.smallAppIconUrl = smallAppIconUrl;
        this.appTitle = appTitle;
        this.payedFor = payedFor;
        this.rating = rating;
        this.installed = installed;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSmallAppIconUrl() {
        return smallAppIconUrl;
    }

    public void setSmallAppIconUrl(String smallAppIconUrl) {
        this.smallAppIconUrl = smallAppIconUrl;
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public boolean isPayedFor() {
        return payedFor;
    }

    public void setPayedFor(boolean payedFor) {
        this.payedFor = payedFor;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isInstalled() {
        return installed;
    }

    public void setInstalled(boolean installed) {
        this.installed = installed;
    }


    public static boolean getRandomBoolean() {
        return Math.random() < 0.5;
        //I tried another approaches here, still the same result
    }

}
