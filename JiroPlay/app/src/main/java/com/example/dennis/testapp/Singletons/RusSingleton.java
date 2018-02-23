package com.example.dennis.testapp.Singletons;

import android.app.Activity;

import com.example.dennis.testapp.AppStore.AppStoreListAdapter;
import com.example.dennis.testapp.AppStore.StoreAppInfo;
import com.example.dennis.testapp.LocalApps.LocalAppInfo;
import com.example.dennis.testapp.LocalApps.LocalAppListAdapter;
import com.example.dennis.testapp.Rus.RusClient;
import com.example.dennis.testapp.SelectApp.App;

import java.util.ArrayList;

/**
 * Created by Dennis on 12/02/2018.
 */

public class RusSingleton {
    private static final RusSingleton instance = new RusSingleton();

    private static RusClient rusConsoleServer;

    public static RusClient getRusAppServer() {
        return rusAppServer;
    }

    public static void setRusAppServer(RusClient rusGamesServer) {
        RusSingleton.rusAppServer = rusAppServer;
    }

    private static RusClient rusAppServer;
    private static int i = 0;

    private static String currentActivity = "";

    private static boolean inCharge = false;

    public static Activity getAppsActivity() {
        return appsActivity;
    }

    public static void setAppsActivity(Activity appsActivity) {
        RusSingleton.appsActivity = appsActivity;
    }

    private static Activity appsActivity;

    public static String getUnderProgressString() {
        return underProgressString;
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

    public static void setUnderProgressString(String underProgressString) {
        RusSingleton.underProgressString = underProgressString;
    }

    private static String underProgressString = "";

    private static boolean appStoreSeen = false;

    private static boolean rusSetup = false;

    private static ArrayList<LocalAppInfo> apps = new ArrayList<>();

    private static LocalAppListAdapter appsAdapter = new LocalAppListAdapter(apps);

    public static void setRusConsoleServer(RusClient rc){
        rusConsoleServer = rc;
    }

    public static RusClient getRusConsoleServer(){
        return rusConsoleServer;
    }

    public static void resetAppsList(){
        apps = new ArrayList<>();
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

    public static ArrayList<LocalAppInfo> getAppsList(){
        return apps;
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

    private RusSingleton() {
    }

    public static void setupRus(){
        rusSetup = true;
    }

    public static boolean isRusSetup(){
        return rusSetup;
    }

    public static String getCurrentActivity(){
        return currentActivity;
    }

    public static void setCurrentActivity(String s){
        currentActivity = s;
    }
}
