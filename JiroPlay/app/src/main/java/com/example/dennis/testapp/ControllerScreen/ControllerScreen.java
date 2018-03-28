package com.example.dennis.testapp.ControllerScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.abs;

public class ControllerScreen extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    ProgressBar progressBar;
    String displayName = "";
    Button changeApp;
    String ip;

    int cLayoutWidth = 0;
    int cLayoutHeight = 0;

    float currentX = 1;
    float currentY = 1;
    float currentZ = 1;

    float lastTime = 0;

    ConstraintLayout controllerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller_screen);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Log.d("oncreate", "ControllerScreen");

        controllerLayout = findViewById(R.id.controller_layout);


        ViewTreeObserver vto = controllerLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                controllerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                cLayoutWidth  = controllerLayout.getMeasuredWidth();
                cLayoutHeight = controllerLayout.getMeasuredHeight();

                if(!RusSingleton.getInstance().currentControllerData.equals("")){
                    populateWithXml(RusSingleton.getInstance().currentControllerData);
                }


            }
        });

        final Context context = getApplicationContext();

        // Initial Screen setups

        progressBar = findViewById(R.id.progress_bar);
        changeApp = findViewById(R.id.change_button);

        changeApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RusSingleton.getInstance().controllerPop.removeChildViews();
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
        displayName = intent.getExtras().getString("displayName");

        Log.d("rus", "firing up");

        if(RusSingleton.getInstance().getRusConsoleServer()==null) {

            RusSingleton.getInstance().setRusConsoleServer(new RusClient(ip, 11026) {

                @Override
                protected void onMessage(byte[] data) {

                    String message = new String(GlobalService.truncNull(data));

                    String[] args = message.split(" ");

                    if (args.length > 0) {

                        if (args[0].equals("app") && args[1].equals("gimme")) {

                            Intent i = new Intent(context, LocalApps.class);
                            startActivity(i);

                        } else if (args[0].equals("app") && args[1].equals("haveInfo")) {

                            RusSingleton.getInstance().incrementAppCounter();

                            if(!RusSingleton.hasApp(args[2])) {

                                RusSingleton.getInstance().addApp(new LocalAppInfo(args[2], args[4], args[3], true, 4.4f, true));

                            }

                            this.sendr(("app getNext "
                                    + String.valueOf(RusSingleton.getInstance().getAppCounter())).getBytes(Charset.forName("US-ASCII")));




                        } else if (args[0].equals("app") && args[1].equals("beingSelected")) {

                            // add text FB name is choosing a gam under loading circle

                        } else if (args[0].equals("app") && args[1].equals("ready")) {

                            // change text to loading controller


                            if (RusSingleton.getInstance().getRusAppServer() != null) {

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

                                                clearEvents();

                                                int idStart = -1;
                                                int idEnd = 1;

                                                for (int i = 1; i < msg.length && msg[i] != '^'; i++) {
                                                    idStart = 1;
                                                    idEnd++;
                                                }

                                                byte[] xmlData = Arrays.copyOfRange(msg, idEnd + 1, msg.length);
                                                String xmlStrData = new String(GlobalService.truncNull(xmlData), Charset.forName("US-ASCII"));
                                                RusSingleton.getInstance().currentControllerData = xmlStrData;

                                                Log.d("nignog27", "are we entirely sure we received une message?");

                                                if (RusSingleton.getInstance().getActivityString().equals("ControllerScreen")) {
                                                    populateWithXml(xmlStrData);
                                                }

                                            } else if (msg[0] == 'l') {
                                                System.out.println("niggerz " + new String(msg, Charset.forName("US-ASCII")));

                                                int idStart = -1;
                                                int idEnd = 1;

                                                for (int i = 1; i < msg.length && msg[i] != '^'; i++) {
                                                    idStart = 1;
                                                    idEnd++;
                                                }

                                                String id = new String(Arrays.copyOfRange(msg, idStart, idEnd), Charset.forName("US-ASCII"));
                                                String eventType = new String(GlobalService.truncNull(Arrays.copyOfRange(msg, idEnd + 1, msg.length)));


                                                if(!RusSingleton.getInstance().eventsList.containsKey(id)){
                                                    ArrayList<String> newList = new ArrayList<>();
                                                    newList.add(eventType);
                                                    RusSingleton.getInstance().eventsList.put(id, newList);
                                                    Log.d("putting",id);
                                                }else{
                                                    ArrayList<String> updatedList = RusSingleton.getInstance().eventsList.get(id);
                                                    updatedList.add(eventType);
                                                    RusSingleton.getInstance().eventsList.put(id, updatedList);
                                                }

                                                setEvent(id);

                                            }else if(msg[0]=='a'){
                                                Log.d("accel", "setting true");
                                                RusSingleton.getInstance().sendAccelerometerData = true;
                                            }
                                        }

                                    });
                                }
                            }, 1500);


                        } else if (args[0].equals("app") && args[1].equals("yourInCharge")) {
                            if (RusSingleton.getInstance().getActivityString().equals("ControllerScreen")) {
                                showButton(changeApp);
                            }
                            RusSingleton.getInstance().putInCharge();
                        }
                    }
                }

                @Override
                protected void onConnect() {
                    System.out.println("JONINING DAVIDS NIGGER LAPTOP");
                    this.sendr(("join " + displayName).getBytes(Charset.forName("us-ascii")));
                }
            });
        }
    }

    public void populateWithXml(String xmlString){

        System.out.println("niggerxml " + xmlString);

        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = null;

        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        try {
            document = saxBuilder.build(new StringReader(xmlString));
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element controller = document.getRootElement();

        RusSingleton.getInstance().interactableViews.clear();

        //clearEvents();
        //removeAllChildViews(controllerLayout);

        //controllerLayout.removeAllViews();

        ConstraintLayout theControllerLayout = controllerLayout;

        if(RusSingleton.getInstance().controllerPop != null) {
            Log.d("view", "removing all views from controller");
            RusSingleton.getInstance().controllerPop.removeChildViews();
            //removeAllChildViews(RusSingleton.getInstance().controllerPop.getLayout());
            Log.d("view", "All views removed");
            //controllerPop = null;
            if (RusSingleton.getInstance().controllerPop.getLayout() != theControllerLayout)
                theControllerLayout = RusSingleton.getInstance().controllerPop.getLayout();
            // todo: the controller layout from the controllerPop doesn't seem to have the correct width and height?
        }



        Log.d("niggleshmiggle", Integer.toString(cLayoutWidth));
        Log.d("niggleshmiggle", Integer.toString(cLayoutHeight));

        cLayoutWidth = theControllerLayout.getMeasuredWidth();
        cLayoutHeight = theControllerLayout.getMeasuredHeight();

        RusSingleton.getInstance().controllerId = controller.getAttributeValue("id");
        RusSingleton.getInstance().controllerPop = new ScreenPopulator(controller, cLayoutWidth, cLayoutHeight, theControllerLayout, getApplicationContext(), this);

        updateEvents();
    }

    public void clearEvents(){

        RusSingleton.getInstance().eventsList.clear();

    }

    public void updateEvents(){


        for(String id: RusSingleton.getInstance().interactableViews.keySet()){

            setEvent(id);

        }

    }

    public void setEvent(String id){

        View view = RusSingleton.getInstance().interactableViews.get(id);

        final String identifier = id;

        if(RusSingleton.getInstance().eventsList.containsKey(id)) {
            Log.d("an event happens",id);
            boolean tapEnd = false;
            boolean tapStart = false;

            for (String eventType : RusSingleton.getInstance().eventsList.get(id)) {

                if (eventType.equals("tapEnd")) {
                    tapEnd = true;
                }else if (eventType.equals("tapStart")) {
                    tapStart = true;
                }

            }

            if(tapEnd && tapStart){

                view.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            RusSingleton.getInstance().getRusAppServer().sendr(("e" + RusSingleton.getInstance().controllerId + "^"
                                    + identifier + "^tapEnd").getBytes(Charset.forName("US-ASCII")));
                            return true;
                        }
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            RusSingleton.getInstance().getRusAppServer().sendr(("e" + RusSingleton.getInstance().controllerId + "^"
                                    + identifier + "^tapStart").getBytes(Charset.forName("US-ASCII")));
                            return true;
                        }
                        return false;
                    }
                });

            }else if(tapEnd && !tapStart){

                view.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            RusSingleton.getInstance().getRusAppServer().sendr(("e" + RusSingleton.getInstance().controllerId + "^"
                                    + identifier + "^tapEnd").getBytes(Charset.forName("US-ASCII")));
                            return true;
                        }
                        return false;
                    }
                });

            }else if(!tapEnd && tapStart){

                view.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            RusSingleton.getInstance().getRusAppServer().sendr(("e" + RusSingleton.getInstance().controllerId + "^"
                                    + identifier + "^tapStart").getBytes(Charset.forName("US-ASCII")));
                            return true;
                        }
                        return false;
                    }
                });

            }

        }

    }

    @Override
    public void onResume(){
        super.onResume();

        Log.d("resuming", "ControllerScreen");

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);


        RusSingleton.getInstance().setAppsActivity(this);

        if(RusSingleton.getInstance().isInCharge()){
            showButton(changeApp);
        }else{
            hideButton(changeApp);
        }
    }

    @Override
    public void onBackPressed() {

        Log.d("AccelerometerSent", ""+RusSingleton.getInstance().accelerometerSendCounter);
        //Toast.makeText(getApplicationContext(), ""+RusSingleton.getInstance().accelerometerSendCounter, Toast.LENGTH_LONG).show();

        super.onBackPressed();

        RusSingleton.getInstance().currentControllerData = "";
        RusSingleton.getInstance().getRusConsoleServer().close();
        RusSingleton.getInstance().setRusConsoleServer(null);

        if(RusSingleton.getInstance().getRusAppServer() != null){
            RusSingleton.getInstance().getRusAppServer().close();
            RusSingleton.getInstance().setRusAppServer(null);
        }

    }

    @Override
    public void onDestroy(){
        RusSingleton.getInstance().controllerPop = null;
        super.onDestroy();
    }

    @Override
    public void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);

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
            Log.d("views", "removing child");
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


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER && RusSingleton.getInstance().sendAccelerometerData) {

            float ax = sensorEvent.values[0];
            float ay = sensorEvent.values[1];
            float az = sensorEvent.values[2];

            //Log.d("accel",ax+","+ay+","+az);

            float dx_perc = abs(ax-currentX);
            float dy_perc = abs(ay-currentY);
            float dz_perc = abs(az-currentZ);

            if(dx_perc + dy_perc + dz_perc > 0.5 && System.nanoTime() - lastTime > 100000000) {

                lastTime = System.nanoTime();

                currentX = ax;
                currentY = ay;
                currentZ = az;

                if (RusSingleton.getInstance().getRusAppServer() != null) {
                    RusSingleton.getInstance().getRusAppServer().send(("a" + RusSingleton.getInstance().controllerId + "^" + ax + "," +
                            ay + "," + az).getBytes(Charset.forName("US-ASCII")));
                }

                RusSingleton.getInstance().accelerometerSendCounter++;

            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
