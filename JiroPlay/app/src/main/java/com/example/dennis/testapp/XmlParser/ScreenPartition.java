package com.example.dennis.testapp.XmlParser;

import com.example.dennis.testapp.GlobalService.GlobalService;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Dennis on 29/01/2018.
 */

public class ScreenPartition {

    int width;
    int height;
    int x;
    int y;
    double span;
    String bgcolor;
    int bordersize;
    String bordercolor;
    int[] padding = {0,0,0,0};
    String id = "";

    public ScreenPartition(HashMap<String, String> attributeMap){

        String sp = attributeMap.get("span");
        if(sp == null) sp = "1";
        this.span = Integer.parseInt(sp);
        this.width = 0;
        this.height = 0;
        this.x = 0;
        this.y = 0;
        this.bgcolor = attributeMap.get("bgcolor");
        if(bgcolor == null)
            this.bgcolor = "#ffffff";
        if(attributeMap.get("bordersize") != null){
            this.bordersize = Integer.parseInt(attributeMap.get("bordersize"));
            if(attributeMap.get("bordercolor") == null){
                this.bordercolor = "#000000";
            }else{
                this.bordercolor = attributeMap.get("bordercolor");
            }
        }else{
            this.bordersize = 0;
        }

        if(attributeMap.get("padding") != null){

            String[] paddy = attributeMap.get("padding").split(" ");

            if(paddy.length == 4)
                for (int pad = 0; pad < 4; pad++)
                    padding[pad] = Integer.parseInt(paddy[pad]);

            if(paddy.length == 1)
                for (int pad = 0; pad < 4; pad++)
                    padding[pad] = Integer.parseInt(paddy[0]);
        }

        if(attributeMap.get("id") != null){
            this.id = attributeMap.get("id");
        }else{
            this.id = GlobalService.getSaltString();
        }


    }


    public void setWidth(int width) {
        this.width = width;
    }


    public void setHeight(int height) {
        this.height = height;
    }


    public void setX(int x) {
        this.x = x;
    }


    public void setY(int y) {
        this.y = y;
    }



}
