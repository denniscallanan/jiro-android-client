package com.example.dennis.testapp.NetworkResources;


import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;

public class ListenerSocket extends AsyncTask<String, byte[], String>{

    DatagramSocket socket;
    String id = getSaltString();

    public ListenerSocket(int port){

        Log.d("creating", this.id);

        try {

            this.socket = new DatagramSocket(null);
            this.socket.setReuseAddress(true);
            this.socket.bind(new InetSocketAddress(port));

        } catch (Exception e) {
            if(this.socket!=null){
                this.socket.close();
            }
            this.socket = null;
            e.printStackTrace();
        }
    }

    public ListenerSocket(int port, String ip){
        try {

            this.socket = new DatagramSocket(null);
            this.socket.setReuseAddress(true);
            this.socket.bind(new InetSocketAddress(InetAddress.getByName(ip), port));

        } catch (Exception e) {
            if(this.socket!=null){
                this.socket.close();
            }
            this.socket = null;
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... strings) {

        byte[] buffer;
        DatagramPacket packet;

        while (true)
        {

            if(isCancelled()){
                break;

            }
            if(socket == null){
                break;
            }

            buffer = new byte[1152];

            try{
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                byte[] msg = packet.getData();
                publishProgress(msg);

            }catch (Exception e){
                if(socket != null){
                    socket.close();
                }
            }
            if(socket == null){
                break;
            }
            if(socket.isClosed()){
                break;
            }

        }
        Log.d("ending", this.id);
        return null;

    }


    @Override
    protected void onProgressUpdate(byte[]... values) {
        onMessage(values[0]);
    }

    protected void onMessage(byte[] message){

    }

    public void close(){
        Log.d("closing", this.id );
        this.cancel(true);
        this.socket.close();
        this.socket = null;
    }

    protected String getSaltString() {
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

}
