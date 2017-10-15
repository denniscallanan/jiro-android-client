package com.example.dennis.testapp.GameStore.ConsoleTab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dennis.testapp.GameStore.GameListAdapter;
import com.example.dennis.testapp.R;
import com.example.dennis.testapp.SearchConsole.ConsoleListAdapter;

import java.util.ArrayList;

public class ConsoleTab extends Fragment {

    ListView gamesList;

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

        View myView = inflater.inflate(R.layout.game_store_console_tab, container, false);

        gamesList = (ListView) myView.findViewById(R.id.consolegames);

        ArrayList<String[]> serverList = new ArrayList<>();

        for(int i = 0; i<4; i++)
            serverList.add(new String[]{"http://orig00.deviantart.net/fb48/f/2016/283/c/3/brawlhalla___icon_by_blagoicons-dakhyp8.png", "Super Meat Boy", "2.99", "3.5"});

        GameListAdapter serversAdapter = new GameListAdapter(getActivity(), serverList);

        gamesList.setAdapter(serversAdapter);

        return myView;


    }

}
