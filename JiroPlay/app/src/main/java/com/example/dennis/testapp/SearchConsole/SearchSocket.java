package com.example.dennis.testapp.SearchConsole;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class SearchSocket {

    static DatagramSocket socket;

    public static void initializeSocket(){

        try {
            socket =  new DatagramSocket(36883, InetAddress.getByName("0.0.0.0"));
        } catch (Exception e) {
            socket = null;
            e.printStackTrace();
        }

    }

    public static DatagramSocket getSocket(){
        return socket;
    }

    public static void closeSocket(){
        socket.close();
    }

}
