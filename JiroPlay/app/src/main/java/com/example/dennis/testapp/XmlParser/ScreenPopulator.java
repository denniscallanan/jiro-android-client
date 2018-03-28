package com.example.dennis.testapp.XmlParser;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dennis.testapp.GlobalService.GlobalService;
import com.example.dennis.testapp.Singletons.RusSingleton;

import org.jdom2.Attribute;
import org.jdom2.Element;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class ScreenPopulator {

    int width;
    int height;
    ConstraintLayout layout;
    Element controller;
    Context context;
    Activity activity;


    public ScreenPopulator(Element controller, int width, int height, ConstraintLayout layout, Context context, Activity activity){

        System.out.println("PopScreen constructor running");

        this.context = context;
        this.width = width;
        this.height = height;
        this.layout = layout;
        this.controller = controller;
        this.activity = activity;

        searchChildren(null, controller, width, height, 0, 0);

        //layout.invalidate();
        //layout.notifyAll();

        //layout.removeAllViews();
    }

    // Gets all properties / attributes of an XML Element in the form of a Hash Map
    public HashMap<String, String> getAttributeMap(Element elem) {
        HashMap<String, String> map = new HashMap();
        for(Attribute att: elem.getAttributes())
            map.put(att.getName(), att.getValue());
        return map;
    }

    // Checks if children of an XML Element are rows, columns or neither
    private String getChildType(Element elem) {
        if(elem.getChildren().size() == 0) return null;
        return elem.getChildren().get(0).getName();
    }

    // Recursively accesses all elements of the XML file and performs appropriate drawing operations
    private void searchChildren(RelativeLayout parentLayout, Element elem, int parentWidth, int parentHeight, int parentX, int parentY) {

        String bType = getChildType(elem);
        if(bType == null) {
        }else if(bType.equals("row") || bType.equals("col")) {
            handleCell(bType, elem, parentX, parentY, parentWidth, parentHeight);
        }else {
            handleLeaf(parentLayout, bType, elem, parentWidth, parentHeight);
        }
    }

    // Handles the situation when an XML element is not a row or column
    private void handleLeaf(RelativeLayout parentLayout, String type, Element elem, int parentWidth, int parentHeight){

        Element finalChild = elem.getChildren().get(0);
        HashMap<String, String> attributeMap = getAttributeMap(finalChild);

        String id = getAttributeMap(finalChild).get("id");

        if(id == null){
            id = GlobalService.getSaltString();
        }

        switch(type){

            case "text":
                drawText(parentLayout, parentWidth, parentHeight, elem.getValue(),
                        attributeMap.get("size"), attributeMap.get("color"), attributeMap.get("weight"), id);
                break;

            case "img":
                drawImage(parentLayout, parentWidth, parentHeight, attributeMap.get("src"), attributeMap.get("scale"), id);
                break;

            case "button":
                drawButton(parentLayout, parentWidth, parentHeight, attributeMap.get("text"), id);
                break;

            default:
                drawText(parentLayout, parentWidth, parentHeight, "please use correct tag",
                        "24", "#ffffff", "regular", "");

        }

    }

    // Handles the situation when an XML element is a row or column
    private void handleCell(String type, Element elem, int parentX, int parentY, int parentWidth, int parentHeight){
        ScreenPartition[] screenPartitions = new ScreenPartition[elem.getChildren().size()];
        List<Element> children = elem.getChildren();
        double totalSpan = 0;

        for(int i = 0; i < screenPartitions.length; i++) {

            HashMap<String, String> attMap = getAttributeMap(children.get(i));
            screenPartitions[i] = new ScreenPartition(attMap);
            totalSpan += screenPartitions[i].span;
        }
        int currentX = parentX;
        int currentY = parentY;

        for(int i = 0; i < screenPartitions.length; i++) {

            screenPartitions[i].setX(currentX);
            screenPartitions[i].setY(currentY);

            if(type.equals("row")){

                int exactSpan = (int)(parentHeight*(screenPartitions[i].span/totalSpan));
                screenPartitions[i].setWidth(parentWidth);
                screenPartitions[i].setHeight(exactSpan);
                currentY += exactSpan;

            }else{
                int exactSpan = (int)(parentWidth*(screenPartitions[i].span/totalSpan));
                screenPartitions[i].setHeight(parentHeight);
                screenPartitions[i].setWidth(exactSpan);
                currentX += exactSpan;
            }

            RelativeLayout newPartition = drawAndGetPartition(screenPartitions[i].x, screenPartitions[i].y, screenPartitions[i].width,
                    screenPartitions[i].height, screenPartitions[i].bgcolor, screenPartitions[i].bordersize,
                    screenPartitions[i].bordercolor, children.get(i), screenPartitions[i].padding, screenPartitions[i].id);

            int[] padding = screenPartitions[i].padding;

            searchChildren(newPartition, children.get(i),screenPartitions[i].width-(padding[1]+padding[3]),
                    screenPartitions[i].height-(padding[0]+padding[2]), screenPartitions[i].x+padding[1],
                    screenPartitions[i].y+padding[0]);
        }
    }

    // Draws row / column partition as a relative layout and returns it as a relative layout
    private RelativeLayout drawAndGetPartition(int x, int y, int width, int height, String bgcolor, int bordersize,
                                               String bordercolor, Element elem, int[] pad, String id){

        RelativeLayout partition = new RelativeLayout(this.context);
        partition.setBackgroundColor(Color.parseColor(bgcolor));

        if(bordersize != 0){

            GradientDrawable border = new GradientDrawable();
            border.setColor(Color.parseColor(bgcolor)); //white background
            border.setStroke(bordersize, Color.parseColor(bordercolor)); //black border with full opacity
            partition.setBackground(border);
        }
        partition.setX(x);
        partition.setY(y);
        partition.setMinimumHeight(height);
        partition.setMinimumWidth(width);
        RusSingleton.getInstance().interactableViews.put(id, partition);
        layout.addView(partition);

        return partition;
    }

    // Draws text to screen, given text properties and parent layout
    private void drawText(RelativeLayout parentPartition, int width, int height, String txt, String size, String color, String weight, String id){

        TextView tv = new TextView(this.context);
        RusSingleton.getInstance().interactableViews.put(id, tv);
        parentPartition.addView(tv);
        addOrRemoveProperty(tv, RelativeLayout.CENTER_IN_PARENT, true);
        tv.setHeight(height);
        tv.setWidth(width);
        tv.setText(txt);
        if(size != null)
            tv.setTextSize(Float.parseFloat(size));
        if(color != null) {
            tv.setTextColor(Color.parseColor(color));
        }else{
            tv.setTextColor(Color.parseColor("#000000"));
        }
        tv.setGravity(Gravity.CENTER);

        if(weight != null) {
            if (weight.equals("thin") || weight.equals("light")
                    || weight.equals("medium") || weight.equals("condensed") || weight.equals("black")) {
                tv.setTypeface(Typeface.create("sans-serif-" + weight, Typeface.NORMAL));
            }
        }
    }

    // Draws image to screen, given image properties and parent layout
    private void drawImage(RelativeLayout parentPartition, final int width, final int height, String src, String scale, String id){


        float toScale = 1;
        if(scale != null)
            toScale = Float.parseFloat(scale);

        if(src != null){

            final String s = src;

            final ImageView imgView = new ImageView(this.context);
            RusSingleton.getInstance().interactableViews.put(id, imgView);
            final RelativeLayout boundingRect = parentPartition;

            imgView.setMaxWidth((int)(toScale*width));
            imgView.setMinimumWidth((width));
            imgView.setMaxHeight((int)(toScale*height));
            imgView.setMinimumHeight((height));

            new Thread(){

                @Override
                public void run(){

                    try {
                        final Bitmap image = BitmapFactory.decodeStream(new URL(s).openConnection().getInputStream());

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imgView.setImageBitmap(image);
                                imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                boundingRect.addView(imgView);

                                //imgView.setZ(10);

                                addOrRemoveProperty(imgView, RelativeLayout.CENTER_IN_PARENT, true);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }.start();

        }

    }

    // Draws button to screen, given text
    public void drawButton(RelativeLayout parentPartition, int parentWidth, int parentHeight, String text, String id){

        Button button = new Button(this.context);
        RusSingleton.getInstance().interactableViews.put(id, button);
        parentPartition.addView(button);
        addOrRemoveProperty(button, RelativeLayout.CENTER_IN_PARENT, true);
        button.setHeight(parentHeight);
        button.setWidth(parentWidth);
        button.setText(text);
    }

    // Adds properties to view (eg. centering)
    private void addOrRemoveProperty(View view, int property, boolean flag){
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if(flag){
            layoutParams.addRule(property);
        }else {
            layoutParams.removeRule(property);
        }
        view.setLayoutParams(layoutParams);
    }

    public void removeChildViews(){
        //GlobalService.removeAllChildViews(layout);
        layout.removeAllViews();
    }

    public ConstraintLayout getLayout(){
        return this.layout;
    }

}


