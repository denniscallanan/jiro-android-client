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

        localConsoleAppsRecyclerView = (RecyclerView) myView.findViewById(R.id.console_apps);
        moreGamesBut = (Button) myView.findViewById(R.id.more_apps_button);

        if(!RusSingleton.getInstance().seenAppStore()){

            RusSingleton.getInstance().setAppStoreSeen(true);

            RusSingleton.getInstance().initializeAppCounter();
            RusSingleton.getInstance().getRusConsoleServer().sendr("app getNext 0".getBytes(Charset.forName("US-ASCII")));


        }

        moreGamesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AppStore.class);
                startActivity(i);
            }
        });

        /*
        ArrayList<LocalAppInfo> localConsoleAppList = new ArrayList<>();
        localConsoleAppList.add(new LocalAppInfo("0", "https://vignette.wikia.nocookie.net/ssb4-wiki-ita/images/b/ba/Super_meat_boy_by_mrcbleck-d5tsgeo.png/revision/latest?cb=20130602155559", "Super Meat Boy", true, 3.5f, true));
        localConsoleAppList.add(new LocalAppInfo("1", "http://wiki.brawlhalla.com/images/7/7f/GnashFull.png", "Brawlhalla", false, 4.4f, true));
        localConsoleAppList.add(new LocalAppInfo("2", "https://upload.wikimedia.org/wikipedia/commons/e/e7/Doodle_Jump.png", "Doodle Jump", true, 4.1f, false));
        localConsoleAppList.add(new LocalAppInfo("3", "https://orig00.deviantart.net/fb40/f/2017/155/b/4/pacman_actualizado_facebook_nuevo_2016_png_png_by_imagenes_en_png-da7cgjp.png", "Pacman", true, 2.7f, true));
        localConsoleAppList.add(new LocalAppInfo("4", "http://provenlogic.com/careers/tt.png", "Arcade Pong", true, 2.7f, false));
        */

        localConsoleAppsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        localConsoleAppsRecyclerView.setAdapter(RusSingleton.getInstance().getAppsAdapter());

        return myView;


    }

}
