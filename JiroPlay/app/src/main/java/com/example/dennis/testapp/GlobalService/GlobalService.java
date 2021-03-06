package com.example.dennis.testapp.GlobalService;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.dennis.testapp.Rus.RusClient;
import com.facebook.Profile;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Dennis on 04/10/2017.
 */

public class GlobalService {

    public static void doTheTask(AsyncTask task) {

            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{""});

    }

    public static byte[] truncNull(byte[] data)
    {
        int i = data.length - 1;
        while (i >= 0 && data[i] == 0)
            --i;

        return Arrays.copyOf(data, i + 1);
    }

    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    public static void removeAllChildViews(ViewGroup viewGroup) {
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
