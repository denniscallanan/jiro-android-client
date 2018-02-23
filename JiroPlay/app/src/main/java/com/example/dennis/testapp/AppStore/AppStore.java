package com.example.dennis.testapp.AppStore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.example.dennis.testapp.LocalApps.LocalAppInfo;
import com.example.dennis.testapp.R;
import com.example.dennis.testapp.SearchConsole.SearchConsole;
import com.example.dennis.testapp.Singletons.RusSingleton;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class AppStore extends AppCompatActivity {

    RecyclerView appStoreRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_app_store);


        ArrayList<StoreAppInfo> appStoreArr = new ArrayList<>();

        appStoreArr.add(new StoreAppInfo("0", "http://pngimg.com/uploads/pokemon/pokemon_PNG148.png", "Pokemon: Couch Arena", "https://www.muralswallpaper.co.uk/app/uploads/yellow-ombre-fade-design-plain.jpg",
                2, 24,  4.3f, true, true, "Battle friends and family in the couch arena, choose your pokemon and opponents wisely"));
        appStoreArr.add(new StoreAppInfo("1", "https://vignette2.wikia.nocookie.net/crashban/images/1/11/Crash_Bandicoot_N._Sane_Trilogy_Artwork.png/revision/latest?cb=20170608042339",
                "Crash Bandicoot: Reboot",
                "https://blogrebellive.files.wordpress.com/2013/10/grey-background-for-site11.jpg",
                4, 16,  4.8f, true, false, "Sprint through levels and defeat bosses in this remake of the original Crash Bandicoot for Jiro"));
        appStoreArr.add(new StoreAppInfo("1", "http://static2.wikia.nocookie.net/__cb20131114181814/atfanfic/images/c/c5/Sonic-_Sonic_Lost_World.png",
                "Sonic the Hedgehog",
                "http://madisongardenseducare.co.za/wp-content/uploads/2016/10/arctic-background-white-blue-gradient-1600x1084.jpg",
                2, 24,  4.3f, true, true, "Run and spin and fight your way to the end of levels. Complete mini games with friends and family"));
        appStoreArr.add(new StoreAppInfo("1", "https://orig00.deviantart.net/d9d0/f/2011/215/8/c/paper_doodle_jump_by_papermario1234-d43t0nr.png",
                "Doodle Jump for More",
                "http://www.publicdomainpictures.net/pictures/130000/velka/green-gradient-background-2.jpg",
                2, 24,  4.3f, true, true, "Race your friends to the top with this multiplayer remake of Doodle Jump"));


        appStoreRecyclerView = (RecyclerView) findViewById(R.id.app_store_list);

        appStoreRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        appStoreRecyclerView.setAdapter(RusSingleton.getInstance().getAppsAdapter());



    }








}
