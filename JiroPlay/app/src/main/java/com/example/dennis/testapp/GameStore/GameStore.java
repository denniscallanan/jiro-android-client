package com.example.dennis.testapp.GameStore;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dennis.testapp.GameStore.ConsoleTab.ConsoleTab;
import com.example.dennis.testapp.GameStore.MyGamesTab.MyGamesTab;
import com.example.dennis.testapp.R;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.List;

public class GameStore extends AppCompatActivity {

    ProfilePictureView profilePictureView;
    private ProfileTracker mProfileTracker;
    TextView mainTitle;
    public ListView gameListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_store);

        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    loadUserInfo(currentProfile);
                }
            }
        };

        mProfileTracker.startTracking();

        // Setup Action Bar
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.main_action_bar, null);

        mainTitle = (TextView)v.findViewById(R.id.mainTitle);
        profilePictureView = (ProfilePictureView) v.findViewById(R.id.userProfilePicture);


        if(Profile.getCurrentProfile() != null) {
            profilePictureView.setProfileId(Profile.getCurrentProfile().getId());
        }

        mainTitle.setText("Pick a Game");


        // declaring it here because you are setting the whole view in the actionbar now after adding the profile pic and all. So may it is a reason it is not been able to find out the actionbar to be applied on
        getSupportActionBar().setCustomView(v);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new ConsoleTab(), "On this Console");
        adapter.addFragment(new MyGamesTab(), "My Games");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }



    // Adapter for the viewpager using FragmentPagerAdapter
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void loadUserInfo(Profile p){

        profilePictureView.setProfileId(p.getId());

    }
}
