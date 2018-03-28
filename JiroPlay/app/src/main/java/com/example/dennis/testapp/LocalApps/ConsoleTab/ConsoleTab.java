package com.example.dennis.testapp.LocalApps.ConsoleTab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dennis.testapp.LocalApps.LocalAppInfo;
import com.example.dennis.testapp.LocalApps.LocalAppListAdapter;
import com.example.dennis.testapp.AppStore.AppStore;
import com.example.dennis.testapp.R;
import com.example.dennis.testapp.Singletons.RusSingleton;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class ConsoleTab extends Fragment {

    RecyclerView localConsoleAppsRecyclerView;
    Button moreGamesBut;

    public ConsoleTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.local_apps_console_tab, container, false);

        localConsoleAppsRecyclerView = myView.findViewById(R.id.console_apps);
        moreGamesBut = myView.findViewById(R.id.more_apps_button);



        return myView;


    }

    @Override
    public void onResume(){

        System.out.println("devil resuming ctab");

        super.onResume();

        localConsoleAppsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        localConsoleAppsRecyclerView.setAdapter(RusSingleton.getInstance().getAppsAdapter());


        RusSingleton.getInstance().initializeAppCounter();
        RusSingleton.getInstance().getRusConsoleServer().sendr("app getNext 0".getBytes(Charset.forName("US-ASCII")));

    }

    @Override
    public void onPause(){
        super.onPause();

        System.out.println("devil pausing ctab");

        RusSingleton.getInstance().resetAppsList();

    }

}
