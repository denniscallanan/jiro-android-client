package com.example.dennis.testapp.SearchConsole;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dennis.testapp.GameStore.GameStore;
import com.example.dennis.testapp.R;
import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{
    public ListView serverListView;
    public ProgressBar spinner;
    public HashMap<String, Boolean> seenIps;
    public boolean loggedIn;

    CallbackManager callbackManager;
    ProfilePictureView profilePictureView;

    private ProfileTracker mProfileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Set Content View
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile == null) {
                    removeUserInfo();
                }else{
                    loadUserInfo(currentProfile);
                }
            }
        };

        mProfileTracker.startTracking();

        // Setup Action Bar
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.main_action_bar, null);

        profilePictureView = (ProfilePictureView) v.findViewById(R.id.userProfilePicture);


        if(Profile.getCurrentProfile() != null) {
            loggedIn = true;
            profilePictureView.setProfileId(Profile.getCurrentProfile().getId());
        }else{loggedIn=false;}


        // declaring it here because you are setting the whole view in the actionbar now after adding the profile pic and all. So may it is a reason it is not been able to find out the actionbar to be applied on
        getSupportActionBar().setCustomView(v);

        // Assign content element variables
        serverListView = (ListView) findViewById(R.id.list);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);

        // Variables
        seenIps = new HashMap<>();
        callbackManager = CallbackManager.Factory.create();

        // On clicking a console
        serverListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(!isLoggedIn()){
                tellUserToLogin();
            }else{
                gotoGameStore();
            }
            }
        });


        SearchSocket.initializeSocket();

        ArrayList<String[]> serverList = new ArrayList<>();

        for(int i = 0; i<4; i++)
            serverList.add(new String[]{"10.98.98.1", "Greg", "greg-pc", "4", "Couch Hockey"});

        ConsoleListAdapter serversAdapter = new ConsoleListAdapter(this, serverList);

        serverListView.setAdapter(serversAdapter);

        new BroadcastListener(serverList, serversAdapter, serverListView, this, SearchSocket.getSocket(), seenIps, spinner).execute();


    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void gotoGameStore(){
        Intent i = new Intent(this, GameStore.class);
        startActivity(i);
    }

    public void loadUserInfo(Profile p){

        loggedIn = true;
        profilePictureView.setProfileId(p.getId());

    }

    public void removeUserInfo(){

        loggedIn=false;
        profilePictureView.setProfileId(null);

    }

    public void tellUserToLogin(){
        Toast.makeText(this, "Login on Facebook please",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}