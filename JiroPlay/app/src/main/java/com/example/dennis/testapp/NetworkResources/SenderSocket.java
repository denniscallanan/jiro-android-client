package com.example.dennis.testapp.NetworkResources;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.net.SocketException;

public class SenderSocket{

    DatagramSocket socket;
    Thread t;

    public SenderSocket() {

        try {
            this.socket = new DatagramSocket();
            this.socket.setReuseAddress(true);

        } catch (SocketException e) {
            if(this.socket!=null){
                this.socket.close();
            }
            this.socket = null;
            e.printStackTrace();
        }

    }

    public void send(byte[] message, String ip, int port){

        try {

            final String ip1 = ip;
            final int port1 = port;


            final DatagramPacket packet = new DatagramPacket(message, message.length, InetAddress.getByName(ip), port);

            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally{
                        Log.d("ending", "Ending SenderSocket thread");
                        t.interrupt();
                    }
                }
            });

            t.start();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void close(){
        this.socket.close();
        this.socket = null;
    }

    public int getPort(){
        return this.socket.getLocalPort();
    }

}
