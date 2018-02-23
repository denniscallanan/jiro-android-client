package com.example.dennis.testapp.AppStore;


public class StoreAppInfo {

    String id;
    String mainAppIconUrl;
    String appTitle;
    String appBackgroundUrl;
    int minPlayers;
    int maxPlayers;
    float rating;
    boolean gyroscope;
    boolean accelerometer;
    String description;

    public StoreAppInfo(String id, String mainAppIconUrl, String appTitle, String appBackgroundUrl, int minPlayers, int maxPlayers, float rating, boolean gyroscope, boolean accelerometer, String description){

        this.id = id;
        this.mainAppIconUrl = mainAppIconUrl;
        this.appTitle = appTitle;
        this.appBackgroundUrl = appBackgroundUrl;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.rating = rating;
        this.gyroscope = gyroscope;
        this.accelerometer = accelerometer;
        this.description = description;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMainAppIconUrl() {
        return mainAppIconUrl;
    }

    public void setMainAppIconUrl(String mainAppIconUrl) {
        this.mainAppIconUrl = mainAppIconUrl;
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public String getAppBackgroundUrl() {
        return appBackgroundUrl;
    }

    public void setAppBackgroundUrl(String appBackgroundUrl) {
        this.appBackgroundUrl = appBackgroundUrl;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean usesGyroscope() {
        return gyroscope;
    }

    public void setGyroscope(boolean gyroscope) {
        this.gyroscope = gyroscope;
    }

    public boolean usesAccelerometer() {
        return accelerometer;
    }

    public void setAccelerometer(boolean accelerometer) {
        this.accelerometer = accelerometer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
