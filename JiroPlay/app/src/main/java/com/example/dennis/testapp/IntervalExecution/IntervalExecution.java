package com.example.dennis.testapp.IntervalExecution;

import android.util.Log;

/**
 * Created by Dennis on 23/12/2017.
 */

public class IntervalExecution {

    private final int milliseconds;
    Thread t;

    public IntervalExecution(int milliseconds){

        this.milliseconds = milliseconds;
    }

    public void execute(){

        final int milliseconds = this.milliseconds;

        t = new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){

                    try {
                        t.sleep(milliseconds);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        continue;
                    }

                    onInterval();

                }
            }
        });
        Log.d("starting", "interval start");
        t.start();
    }

    protected void onInterval(){
        System.out.println("Running every " + this.milliseconds + " milliseconds, with no code to execute" );
    }

}
