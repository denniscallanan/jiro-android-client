package com.example.dennis.populatexml.XMLParser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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


    public ScreenPopulator(Element controller, int width, int height, ConstraintLayout layout, Context context){

        this.context = context;
        this.width = width;
        this.height = height;
        this.layout = layout;
        this.controller = controller;

        searchChildren(controller, width, height, 0, 0, null);
    }


    public HashMap<String, String> getAttributeMap(Element elem) {
        HashMap<String, String> map = new HashMap();
        for(Attribute att: elem.getAttributes())
            map.put(att.getName(), att.getValue());
        return map;
    }

    private String getChildType(Element elem) {
        if(elem.getChildren().size() == 0) return null;
        return elem.getChildren().get(0).getName();
    }


    private void searchChildren(Element elem, int parentWidth, int parentHeight, int parentX, int parentY, RelativeLayout pton) {

        String bType = getChildType(elem);
        if(bType == null) {
        }else if(bType.equals("row") || bType.equals("col")) {
            handleCell(bType, elem, parentX, parentY, parentWidth, parentHeight);
        }else {
            handleLeaf(bType, elem, parentX, parentY, parentWidth, parentHeight, pton);
        }
    }

    private void handleLeaf(String type, Element elem, int parentX, int parentY, int parentWidth, int parentHeight, RelativeLayout pton){

        Element finalChild = elem.getChildren().get(0);
        HashMap<String, String> attributeMap = getAttributeMap(finalChild);

        switch(type){

            case "text":
                drawText(elem.getValue(), parentWidth, parentHeight,
                        attributeMap.get("size"), attributeMap.get("color"), attributeMap.get("weight"), pton);
                break;

            case "img":
                drawImage(parentWidth, parentHeight, attributeMap.get("src"), attributeMap.get("scale"), pton);
                break;

            default:
                drawText("please use correct tag", parentWidth, parentHeight,
                        "24", "ffffff", "regular", pton);

        }

    }

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

            RelativeLayout part = drawAndGetPartition(screenPartitions[i].x, screenPartitions[i].y, screenPartitions[i].width,
                    screenPartitions[i].height, screenPartitions[i].bgcolor, screenPartitions[i].bordersize,
                    screenPartitions[i].bordercolor, screenPartitions[i].clickid, children.get(i), screenPartitions[i].padding);

            int[] padding = screenPartitions[i].padding;

            searchChildren(children.get(i),screenPartitions[i].width-(padding[1]+padding[3]),
                    screenPartitions[i].height-(padding[0]+padding[2]), screenPartitions[i].x+padding[1],
                    screenPartitions[i].y+padding[0], part);
        }
    }




    private RelativeLayout drawAndGetPartition(int x, int y, int width, int height, String bgcolor, int bordersize,
                              String bordercolor, String clickid, Element elem, int[] pad){

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
        layout.addView(partition);

        if(elem.getChildren().size() > 0){

            Element firstChild = elem.getChildren().get(0);

            if(clickid != null && firstChild.getName().equals("text")){

                Button but = new Button(this.context);
                but.setX(x+pad[1]);
                but.setY(y+pad[0]);
                but.setHeight(height-(pad[0]+pad[2]));
                but.setWidth(width-(pad[1]+pad[3]));
                but.setText(firstChild.getValue());
                layout.addView(but);

            }
        }

        return partition;
    }

    private void drawText(String txt, int width, int height, String size, String color, String weight, RelativeLayout boundingRect){

        TextView tv = new TextView(this.context);
        boundingRect.addView(tv);
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

    private void drawImage(final int width, final int height, String src, String scale, RelativeLayout boundRect){


        float toScale = 1;
        if(scale != null)
            toScale = Float.parseFloat(scale);

        if(src != null){

            final ImageView imgView = new ImageView(this.context);
            final RelativeLayout boundingRect = boundRect;

            imgView.setMaxWidth((int)(toScale*width));
            imgView.setMinimumWidth((width));
            imgView.setMaxHeight((int)(toScale*height));
            imgView.setMinimumHeight((height));

            new AsyncTask<String, Void, Bitmap>() {

                protected Bitmap  doInBackground(String... urls) {
                    Bitmap image = null;
                    try {
                        image = BitmapFactory.decodeStream(new URL(urls[0]).openConnection().getInputStream());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return image;
                }

                protected void onPostExecute(Bitmap result) {


                    imgView.setImageBitmap(result);
                    imgView.setAdjustViewBounds(true);
                    boundingRect.addView(imgView);
                    imgView.setZ(10);
                    addOrRemoveProperty(imgView, RelativeLayout.CENTER_IN_PARENT, true);

                }

            }.execute(src);

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

}


