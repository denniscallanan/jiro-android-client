package com.example.dennis.testapp.Singletons;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.example.dennis.testapp.AppStore.AppStoreListAdapter;
import com.example.dennis.testapp.AppStore.StoreAppInfo;
import com.example.dennis.testapp.LocalApps.LocalAppInfo;
import com.example.dennis.testapp.LocalApps.LocalAppListAdapter;
import com.example.dennis.testapp.Rus.RusClient;
import com.example.dennis.testapp.SelectApp.App;
import com.example.dennis.testapp.XmlParser.ScreenPopulator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dennis on 12/02/2018.
 */

public class RusSingleton {

    public static int accelerometerSendCounter = 0;
    private static final RusSingleton instance = new RusSingleton();
    private static RusClient rusConsoleServer;
    private static RusClient rusAppServer;
    private static Activity appsActivity;
    private static boolean inCharge = false;
    private static boolean appStoreSeen = false;
    private static int i = 0;
    public String currentControllerData = "";
    public HashMap<String, View> interactableViews = new HashMap<>();
    public HashMap<String, ArrayList<String>> eventsList = new HashMap<>();
    public String controllerId = "noid";
    public boolean sendAccelerometerData = false;
    public ScreenPopulator controllerPop;
    private static ArrayList<LocalAppInfo> apps = new ArrayList<>();
    private static LocalAppListAdapter appsAdapter = new LocalAppListAdapter(apps);


    private RusSingleton() {
    }

    public static RusClient getRusAppServer() {
        return rusAppServer;
    }

    public static void setRusAppServer(RusClient rusAppServer) {
        RusSingleton.rusAppServer = rusAppServer;
    }

    public static Activity getAppsActivity() {
        return appsActivity;
    }

    public static void setAppsActivity(Activity appsActivity) {
        RusSingleton.appsActivity = appsActivity;
    }


    public static void putInCharge(){
        inCharge = true;
    }

    public static void takeAwayCharge(){
        inCharge = false;
    }

    public static boolean isInCharge(){
        return inCharge;
    }

    public static void setRusConsoleServer(RusClient rc){
        rusConsoleServer = rc;
        Log.d("consoleServer", "new console server");
    }

    public static RusClient getRusConsoleServer(){
        return rusConsoleServer;
    }

    public static void resetAppsList(){
        apps.clear();
        appsAdapter.notifyDataSetChanged();
    }

    public static RusSingleton getInstance() {
        return instance;
    }

    public static void incrementAppCounter(){
        i++;
    }

    public static int getAppCounter(){
        return i;
    }

    public static void initializeAppCounter(){
        i=0;
    }

    public static LocalAppInfo getApp(int i){
        return apps.get(i);
    }

    public static void addApp(LocalAppInfo app){
        apps.add(app);
        appsAdapter.notifyDataSetChanged();
    }

    public static LocalAppListAdapter getAppsAdapter(){
        return appsAdapter;
    }

    public static boolean seenAppStore(){
        return appStoreSeen;
    }

    public static void setAppStoreSeen(boolean x){
        appStoreSeen = x;
    }

    public static String getActivityString(){
        return appsActivity.getClass().getSimpleName();
    }

    public static boolean hasApp(String s){

        boolean hasApp = false;

        for(LocalAppInfo app: apps){
            if(app.getId().equals(s)){
                hasApp = true;
            }
        }
        return hasApp;

    }


}
