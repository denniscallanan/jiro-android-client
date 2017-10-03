package com.example.dennis.testapp.SearchConsole;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.dennis.testapp.LoginScreen.LoginScreen;
import com.example.dennis.testapp.R;

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

        final Context context = this;

        getSupportActionBar().setTitle("Select a Console");

        serverListView = (ListView) findViewById(R.id.list);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        seenIps = new HashMap<>();

        serverListView.setVisibility(View.GONE);


        serverListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(context, LoginScreen.class);
                startActivity(i);
            }
        });

        SearchSocket.initializeSocket();

        ArrayList<String[]> serverList = new ArrayList<>();

        ConsoleListAdapter serversAdapter = new ConsoleListAdapter(this, serverList);

        serverListView.setAdapter(serversAdapter);

        new BroadcastListener(serverList, serversAdapter, serverListView, this, SearchSocket.getSocket(), seenIps, spinner).execute();

        Intent i = new Intent(this, LoginScreen.class);
        startActivity(i);
    }
}