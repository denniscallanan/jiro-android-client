package com.example.dennis.testapp.LocalApps.MyAppsTab;

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

import java.util.ArrayList;

public class MyAppsTab extends Fragment {

    RecyclerView localMyAppsRecyclerView;
    Button moreAppsButton;

    public MyAppsTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.local_apps_myapps_tab, container, false);

        localMyAppsRecyclerView = (RecyclerView) v.findViewById(R.id.local_my_apps_list);
        moreAppsButton = (Button) v.findViewById(R.id.more_apps_button);

        moreAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AppStore.class);
                startActivity(i);
            }
        });

        ArrayList<LocalAppInfo> myAppsArr = new ArrayList<>();

        myAppsArr.add(new LocalAppInfo("0", "https://upload.wikimedia.org/wikipedia/en/9/94/FDNY_Racing_logo.png",
                "FDNY Racing", true, 4.2f,true));
        myAppsArr.add(new LocalAppInfo("1", "https://vignette.wikia.nocookie.net/logopedia/images/a/a2/Ufc-fight-night-live.png/revision/latest?cb=20150714233510",
                "UFC Finals", true, 4.4f, true));
        myAppsArr.add(new LocalAppInfo("2", "https://vignette1.wikia.nocookie.net/logopedia/images/e/e7/Fox-Soccer-Channel.png/revision/latest?cb=20111202000359",
                "Fox Soccer", true, 4.1f, true));
        myAppsArr.add(new LocalAppInfo("3", "http://media.moddb.com/images/members/1/389/388457/profile/logo_official_glow.png",
                "Crash Bandicoot", true, 3.5f, true));
        myAppsArr.add(new LocalAppInfo("4", "https://camo.githubusercontent.com/d58d4c4a59e59c563c348e26ebdc2cc7e4a4dfd5/68747470733a2f2f7261772e6769746875622e636f6d2f616f656e772f48756e647265642d446179732f6d61737465722f646f63732f696d672f616f65332d6c6f676f2e706e67",
                "Age of Empires", true, 4.4f, true));
        myAppsArr.add(new LocalAppInfo("5", "https://vignette.wikia.nocookie.net/flappybird/images/0/04/Clumsy_Bird_Android_logo.png/revision/latest?cb=20140513003930",
                "Angry Flappy Bird", true, 4.1f, true));

        LocalAppListAdapter recentGamesAdapter = new LocalAppListAdapter(myAppsArr);

        localMyAppsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        localMyAppsRecyclerView.setAdapter(recentGamesAdapter);

        return v;
    }

}