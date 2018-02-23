package com.example.dennis.testapp.IntervalExecution;

/**
 * Created by Dennis on 23/12/2017.
 */

public class IntervalExecution {

    private final int milliseconds;

    public IntervalExecution(int milliseconds){

        this.milliseconds = milliseconds;
    }

    public void execute(){

        final int milliseconds = this.milliseconds;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                long lastTime = System.nanoTime();

                while(true){

                    long currentTime = System.nanoTime();

                    if(currentTime - lastTime  > milliseconds*1000000) {
                        lastTime = currentTime;
                        onInterval();
                    }
                }
            }
        });
        t.start();
    }

    protected void onInterval(){
        System.out.println("Running every " + this.milliseconds + " milliseconds, with no code to execute" );
    }

}
