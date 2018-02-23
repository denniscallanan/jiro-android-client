package com.example.dennis.testapp.GlobalService;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.example.dennis.testapp.Rus.RusClient;
import com.facebook.Profile;
import java.util.Arrays;

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

}
