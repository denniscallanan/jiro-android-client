package com.example.dennis.testapp.GameStore;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dennis.testapp.R;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GameStore extends AppCompatActivity {

    ImageView userpic;
    TextView username;

    ProfilePictureView profilePictureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_store);

        userpic = (ImageView)findViewById(R.id.userpic);
        username = (TextView) findViewById(R.id.username);

        Profile profile = Profile.getCurrentProfile();
        String firstName = profile.getFirstName();
        username.setText(firstName);

        profilePictureView = (ProfilePictureView) findViewById(R.id.userProfilePicture);
        profilePictureView.setProfileId(profile.getId());
    }



    public static Bitmap getFacebookProfilePicture(String userID) throws IOException {
        URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

        return bitmap;
    }


}
