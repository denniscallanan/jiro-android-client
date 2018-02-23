package com.example.dennis.testapp.SearchConsole;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dennis.testapp.ControllerScreen.ControllerScreen;
import com.example.dennis.testapp.GlobalService.GlobalService;
import com.example.dennis.testapp.LocalApps.LocalApps;
import com.example.dennis.testapp.NetworkResources.ListenerSocket;
import com.example.dennis.testapp.NetworkResources.SenderSocket;
import com.example.dennis.testapp.R;
import com.example.dennis.testapp.Rus.RusClient;
import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.ProfilePictureView;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class SearchConsole extends AppCompatActivity
{
    public ListView serverListView;
    public ProgressBar spinner;
    public HashMap<String, Integer> seenIps;
    public boolean loggedIn;

    CallbackManager callbackManager;
    ProfilePictureView profilePictureView;

    private ProfileTracker mProfileTracker;

    HashMap<String, Integer> seen = new HashMap<>();

    ListenerSocket ls;

    ArrayList<String[]> serverList = new ArrayList<>();
    ConsoleListAdapter serversAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        Log.d("main", "creating main activity");
        // Set Content View
        super.onCreate(savedInstanceState);

        serversAdapter = new ConsoleListAdapter(this, serverList);

        setContentView(R.layout.activity_search_console);

        mProfileTracker =  new ProfileTracker() {
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
                gotoGameStore();
            }
        });

        serverListView.setAdapter(serversAdapter);

        fireUpListener(serverList, serversAdapter);

        serverListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String ip = ((String[]) parent.getItemAtPosition(position))[1];

                Intent i = new Intent(getApplicationContext(), ControllerScreen.class);
                i.putExtra("ip", ip);
                startActivity(i);

            }

        });


    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }

    @Override
    public void onPause(){

        super.onPause();
        ls.close();


    }

    @Override
    public void onResume(){

        super.onResume();
        fireUpListener(serverList, serversAdapter);


    }

    public void fireUpListener(final ArrayList<String[]> serverList, final ConsoleListAdapter serversAdapter){

        ls = new ListenerSocket(17417){

            @Override
            protected void onMessage(byte[] data) {

                //message = message.substring(0, message.indexOf('\0'));
                String message = new String(GlobalService.truncNull(data));
                final String[] args = message.split(" ");
                if (args.length == 5 && args[0].equals("jiroc"))
                {
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            if(!seen.containsKey(args[1])){
                                if(seen.size() == 0){
                                    spinner.setVisibility(View.GONE);
                                }
                                serverList.add(args);
                                seen.put(args[1], serverList.size()-1);
                            }else{
                                serverList.set(seen.get(args[1]), args);
                            }
                            serversAdapter.notifyDataSetChanged();
                            serverListView.invalidateViews();
                        }
                    });
                }
            }
        };

        GlobalService.doTheTask(ls);

    }

    public void gotoGameStore(){
        Intent i = new Intent(this, LocalApps.class);
        i.putExtra("LOGGED_IN", loggedIn);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}