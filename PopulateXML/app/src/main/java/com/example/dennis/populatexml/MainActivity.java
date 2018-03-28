package com.example.dennis.populatexml;

import android.content.Context;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import java.io.IOException;
import java.io.StringReader;

import com.example.dennis.populatexml.XMLParser.ScreenPopulator;
import com.example.dennis.populatexml.XMLParser.TestXML;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}


        setContentView(R.layout.activity_main);

        int navigationBarHeight = getNavigationBarHeight(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels - (navigationBarHeight/2);
        int width = displayMetrics.widthPixels;

        ConstraintLayout controllerLayout = findViewById(R.id.controller_layout);

        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = null;

        try {
            document = saxBuilder.build(new StringReader(TestXML.TEST5));
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Element controller = document.getRootElement();

        ScreenPopulator screenPopulator = new ScreenPopulator(controller, width, height, controllerLayout, getApplicationContext());

        System.out.println(screenPopulator.getAttributeMap(controller).get("id"));

    }

    public static int getNavigationBarHeight(Context pContext){
        Resources resources = pContext.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }


}
