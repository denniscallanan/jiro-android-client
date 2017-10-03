package com.example.dennis.testapp.SearchConsole;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashMap;

class BroadcastListener extends AsyncTask<String, String, String>
{
    ArrayList<String[]> serverList;
    ConsoleListAdapter serversAdapter;
    Activity activity;
    DatagramSocket socket;
    HashMap<String, Boolean> seen;
    ProgressBar spinner;
    ListView serverListView;

    public BroadcastListener(ArrayList<String[]> s, ConsoleListAdapter sa, ListView lv,
                             Activity activity, DatagramSocket ds, HashMap<String, Boolean> hm,
                             ProgressBar spin)
    {
        this.serverList = s;
        this.serversAdapter = sa;
        this.serverListView = lv;
        this.socket = ds;
        this.activity = activity;
        this.seen = hm;
        this.spinner = spin;
    }

    @Override
    protected String doInBackground(String... params)
    {
        byte[] buffer = new byte[512];
        DatagramPacket packet;
        try
        {
            packet = new DatagramPacket(buffer, buffer.length);

            while (true)
            {
                try {
                    socket.receive(packet);
                }catch (Exception e){
                }
                if(socket.isClosed()){
                    break;
                }
                String msg = new String(packet.getData());
                publishProgress(msg);

            }
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String msg)
    {

    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(String... values) {
        final String[] data = values[0].split(" ");

        if (data.length == 5)
        {
            activity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    if(!seen.containsKey(data[1])){
                        if(seen.size() == 0){
                            spinner.setVisibility(View.GONE);
                            serverListView.setVisibility(View.VISIBLE);
                        }
                        serverList.add(data);
                        serversAdapter.notifyDataSetChanged();
                        serverListView.invalidateViews();
                        seen.put(data[1], true);
                    }

                }
            });
        }
    }
}