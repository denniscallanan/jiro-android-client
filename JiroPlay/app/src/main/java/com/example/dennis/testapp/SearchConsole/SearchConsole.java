package com.example.dennis.testapp.SearchConsole;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dennis.testapp.ControllerScreen.ControllerScreen;
import com.example.dennis.testapp.GlobalService.GlobalService;
import com.example.dennis.testapp.LocalApps.LocalApps;
import com.example.dennis.testapp.NetworkResources.ListenerSocket;
import com.example.dennis.testapp.NetworkResources.SenderSocket;
import com.example.dennis.testapp.R;
import com.example.dennis.testapp.Rus.RusClient;
import com.example.dennis.testapp.Singletons.RusSingleton;
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
    ListView serverListView;
    ProgressBar spinner;
    EditText nameTextView;
    String name = "";
    CallbackManager callbackManager;
    ProfilePictureView profilePictureView;
    ProfileTracker mProfileTracker;
    ListenerSocket ls;
    HashMap<String, Integer> seen = new HashMap<>();
    ArrayList<String[]> serverList = new ArrayList<>();
    ConsoleListAdapter serversAdapter;
    boolean loggedIn;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        serversAdapter = new ConsoleListAdapter(this, serverList);
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_search_console);

        // Setup Action Bar
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.main_action_bar, null);
        getSupportActionBar().setCustomView(v);

        profilePictureView = v.findViewById(R.id.userProfilePicture);


        // Window Info
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        serverListView = findViewById(R.id.list);
        spinner = findViewById(R.id.progressBar1);
        nameTextView = findViewById(R.id.name_text_view);
        nameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name = convertName(charSequence.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });



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
        if(Profile.getCurrentProfile() != null){
            loadUserInfo(Profile.getCurrentProfile());
        }


        serverListView.setAdapter(serversAdapter);

        serverListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                saveName();

                String ip = ((String[]) parent.getItemAtPosition(position))[1];

                Intent i = new Intent(getApplicationContext(), ControllerScreen.class);
                i.putExtra("ip", ip);
                i.putExtra("displayName", name);
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
        ls = null;
        serverList.clear();
        serversAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume(){

        super.onResume();
        spinner.setVisibility(View.VISIBLE);
        RusSingleton.getInstance().setAppsActivity(this);
        seen.clear();
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
                                if(serverList.size() > seen.get(args[1])) {
                                    serverList.set(seen.get(args[1]), args);
                                }else{
                                    if(seen.size() == 0){
                                        spinner.setVisibility(View.GONE);
                                    }
                                    serverList.add(args);
                                    seen.put(args[1], serverList.size()-1);
                                }
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

    public void loadUserInfo(Profile p){

        loggedIn = true;
        profilePictureView.setProfileId(p.getId());
        nameTextView.setText(p.getFirstName());

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public void saveName(){

        SharedPreferences sharedPref = getSharedPreferences("mainPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Save your string in SharedPref
        editor.putString("user_name", name);
        editor.commit();

    }

    public String convertName(String s){

        String newStr = s.trim().replaceAll(" +", " ");

        newStr = newStr.replaceAll("^", " ");

        if(newStr.length() > 32){
            newStr = newStr.substring(0, 29) + "...";
        }
        return newStr;

    }

}