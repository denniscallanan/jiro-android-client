package com.example.dennis.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{
    public ListView serverListView;
    public ProgressBar spinner;
    public HashMap<String, Boolean> seenIps;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Select a Console");

        serverListView = (ListView) findViewById(R.id.list);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        seenIps = new HashMap<>();

        serverListView.setVisibility(View.GONE);

        SearchSocket.initializeSocket();

        ArrayList<String[]> serverList = new ArrayList<>();

        ConsoleListAdapter serversAdapter = new ConsoleListAdapter(this, serverList);

        serverListView.setAdapter(serversAdapter);

        new BroadcastListener(serverList, serversAdapter, serverListView, this, SearchSocket.getSocket(), seenIps, spinner).execute();

    }
}