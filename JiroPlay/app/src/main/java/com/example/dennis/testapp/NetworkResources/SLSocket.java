package com.example.dennis.testapp.NetworkResources;


import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.util.Arrays;

public class SLSocket extends AsyncTask<String, byte[], String>{

    DatagramSocket socket;
    Thread t;
    int port;

    public SLSocket(int port){
        this.port = port;

        setupSocket(port);

        if(this.socket == null){
            setupSocket(port);
        }
    }

    private void setupSocket(int port){

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
        Log.d("ending", "Ending SL");
        return null;
    }


    @Override
    protected void onProgressUpdate(byte[]... values) {
        onMessage(values[0]);
    }

    protected void onMessage(byte[] message){

    }

    public void close(){
        Log.d("closing", "closing SL");
        this.cancel(true);
        this.socket.close();
        this.socket = null;
    }


    public void send(final byte[] message, final String ip, final int port){

        final DatagramPacket packet;
        final SLSocket that = this;

        final int p = this.port;

        try {
            packet = new DatagramPacket(message, message.length, InetAddress.getByName(ip), port);
        } catch (UnknownHostException e) {
            return;
        }

        t = new Thread(new Runnable() {
                @Override
                public void run() {

                    if(socket != null){

                        try {
                            socket.send(packet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            if(t!=null){
                                t.interrupt();
                            }
                        }

                    }else{

                        setupSocket(p);
                        //that.send(message, ip, port);


                    }

                }
            });

        try {
            t.start();
        }catch(Exception e){

        }
    }



}
