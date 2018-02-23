package com.example.dennis.testapp.ControllerScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dennis.testapp.AppStore.AppStore;
import com.example.dennis.testapp.AppStore.StoreAppInfo;
import com.example.dennis.testapp.GlobalService.GlobalService;

import com.example.dennis.testapp.LocalApps.LocalAppInfo;
import com.example.dennis.testapp.LocalApps.LocalApps;
import com.example.dennis.testapp.R;
import com.example.dennis.testapp.Rus.RusClient;
import com.example.dennis.testapp.SelectApp.App;
import com.example.dennis.testapp.SelectApp.SelectApp;
import com.example.dennis.testapp.Singletons.RusSingleton;
import com.example.dennis.testapp.XmlParser.ScreenPopulator;
import com.example.dennis.testapp.XmlParser.TestXML;
import com.facebook.Profile;
import com.facebook.ProfileTracker;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class ControllerScreen extends AppCompatActivity {

    ProgressBar progressBar;
    private ProfileTracker mProfileTracker;
    String displayName = "";
    TextView underProgressText;
    Button changeApp;
    String ip;

    int cLayoutWidth = 0;
    int cLayoutHeight = 0;

    ConstraintLayout controllerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller_screen);

        System.out.println("creating manigga");

        controllerLayout = findViewById(R.id.controller_layout);

        ViewTreeObserver vto = controllerLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                controllerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                cLayoutWidth  = controllerLayout.getMeasuredWidth();
                cLayoutHeight = controllerLayout.getMeasuredHeight();

            }
        });

        final Context context = getApplicationContext();

        // Initial Screen setups

        mProfileTracker =  new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile == null) {
                    finish();
                }else{
                    displayName = currentProfile.getName();
                }
            }
        };

        progressBar = findViewById(R.id.progress_bar);
        changeApp = findViewById(R.id.change_button);

        changeApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAllChildViews(controllerLayout);
                Intent i = new Intent(getApplicationContext(), LocalApps.class);
                startActivity(i);
            }
        });

        hideButton(changeApp);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}


        // Rus setup


        Intent intent = getIntent();
        ip = intent.getExtras().getString("ip");

        Log.d("rus", "firing up");

        underProgressText = findViewById(R.id.underProgressText);


        RusSingleton.getInstance().setRusConsoleServer(new RusClient(ip, 11026) {

            @Override
            protected void onMessage(byte[] data) {

                String message = new String(GlobalService.truncNull(data));

                String[] args = message.split(" ");

                if (args.length > 0) {

                    if (args[0].equals("app") && args[1].equals("gimme")) {

                        System.out.println("negro got gimme, opening LocalApps from ControllerScreen");

                        Intent i = new Intent(context, LocalApps.class);
                        startActivity(i);

                    } else if (args[0].equals("app") && args[1].equals("haveInfo")) {

                        System.out.println("negro " + args[3]);

                        RusSingleton.getInstance().incrementAppCounter();

                        RusSingleton.getInstance().addApp(new LocalAppInfo(args[2], "https://upload.wikimedia.org/wikipedia/commons/e/e7/Doodle_Jump.png", args[3], true, 4.4f, true));

                        this.sendr(("app getNext "
                                + String.valueOf(RusSingleton.getInstance().getAppCounter())).getBytes(Charset.forName("US-ASCII")));


                    }else if (args[0].equals("app") && args[1].equals("beingSelected")){

                        // add text FB name is choosing a gam under loading circle
                        if(RusSingleton.getInstance().getCurrentActivity().equals("ControllerScreen")){
                            underProgressText.setText("someone is choosing a app");
                        }
                        RusSingleton.getInstance().setUnderProgressString("someone is choosing a app");

                    }else if (args[0].equals("app") && args[1].equals("ready")) {

                        // change text to loading controller

                        if(RusSingleton.getInstance().getRusAppServer() != null) {

                            RusSingleton.getInstance().getRusAppServer().close();
                            RusSingleton.getInstance().setRusAppServer(null);

                        }

                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                System.out.println("set up after 3 seconds");
                                RusSingleton.getInstance().setRusAppServer(new RusClient(ip, 36883) {

                                    @Override
                                    public void onConnect() {

                                        this.sendr(("j" + displayName).getBytes(Charset.forName("US-ASCII")));

                                    }

                                    @Override
                                    public void onMessage(byte[] msg) {

                                        //String message = new String(msg, Charset.forName("US-ASCII"));

                                        if (msg[0] == 'c') {
                                            int idStart = -1;
                                            int idEnd = 1;

                                            for (int i = 1; i < msg.length && msg[i] != '^'; i++) {
                                                idStart = 1;
                                                idEnd++;
                                            }

                                            byte[] controllerId = Arrays.copyOfRange(msg, idStart, idEnd);
                                            byte[] xmlData = Arrays.copyOfRange(msg, idEnd + 1, msg.length);
                                            String xmlStrData = new String(GlobalService.truncNull(xmlData), Charset.forName("US-ASCII"));


                                            if (RusSingleton.getInstance().getCurrentActivity().equals("ControllerScreen")) {
                                                populateWithXml(xmlStrData);
                                            }

                                        }else if(msg[0] == 'l'){
                                            System.out.println("niggerz " + new String(msg, Charset.forName("US-ASCII")));
                                        }
                                    }

                                });
                            }
                        }, 3000);


                        RusSingleton.getInstance().setUnderProgressString("Loading Controller");

                        if (!RusSingleton.getInstance().getCurrentActivity().equals("ControllerScreen")) {

                            RusSingleton.getInstance().getAppsActivity().finish();

                        }else{
                            underProgressText.setText("Loading Controller");
                        }

                    }else if (args[0].equals("app") && args[1].equals("yourInCharge")){
                        System.out.println("nignigs in charge");
                        if (RusSingleton.getInstance().getCurrentActivity().equals("ControllerScreen")) {
                            showButton(changeApp);
                            System.out.println("showing button");
                        }
                        RusSingleton.getInstance().putInCharge();
                    }
                }
            }

            @Override
            protected void onConnect() {

                Log.d("rustest", "Connected to server");
                this.sendr(("join " + displayName).getBytes(Charset.forName("us-ascii")));

            }

        });


    }


    public void populateWithXml(String xmlString){

        System.out.println("negroxml " + xmlString);


        SAXBuilder saxBuilder = new SAXBuilder();
        System.out.println("GOT HERE");
        Document document = null;
        System.out.println("GOT HERE 2");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        System.out.println("GOT HERE 3");

        try {
            document = saxBuilder.build(new StringReader(xmlString));
            System.out.println("GOT HERE 4");
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element controller = document.getRootElement();
        System.out.println("GOT HERE 5");

        System.out.println("dimens " + cLayoutWidth + ", " + cLayoutHeight);

        ScreenPopulator screenPopulator = new ScreenPopulator(controller, cLayoutWidth, cLayoutHeight, controllerLayout, getApplicationContext());
        System.out.println("GOT HERE 6");

    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("resuming manigga");
        RusSingleton.getInstance().setCurrentActivity("ControllerScreen");
        underProgressText.setText(RusSingleton.getInstance().getUnderProgressString());
        if(RusSingleton.getInstance().isInCharge()){
            showButton(changeApp);
        }else{
            hideButton(changeApp);
        }
    }

    private void addOrRemoveProperty(View view, int property, boolean flag){
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if(flag){
            layoutParams.addRule(property);
        }else {
            layoutParams.removeRule(property);
        }
        view.setLayoutParams(layoutParams);
    }

    public void hideButton(Button b){
        b.setAlpha(.2f);
        b.setClickable(false);
    }

    public void showButton(Button b){
        b.setAlpha(1.0f);
        b.setClickable(true);
    }

    public void removeAllChildViews(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                if (child instanceof AdapterView) {
                    viewGroup.removeView(child);
                    return;
                }
                removeAllChildViews(((ViewGroup) child));
            } else {
                viewGroup.removeView(child);
            }
        }
    }


}
