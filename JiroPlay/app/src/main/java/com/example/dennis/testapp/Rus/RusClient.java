package com.example.dennis.testapp.Rus;

import android.util.Log;

import com.example.dennis.testapp.BitManipulation.ByteManipulator;
import com.example.dennis.testapp.GlobalService.GlobalService;
import com.example.dennis.testapp.IntervalExecution.IntervalExecution;
import com.example.dennis.testapp.NetworkResources.ListenerSocket;
import com.example.dennis.testapp.NetworkResources.SLSocket;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class RusClient {

    // STATIC CONSTANTS

    final static int DATA = 0, NO_DATA = 1;
    final static int EMPTY = 0, CONNECT = 1, DISCONNECT = 2, DISCONNECT_WARNING = 3;
    final static int NORMAL_MESSAGE = 0, RELIABLE_MESSAGE = 1, ORDERED_RELIABLE_MESSAGE = 2, MESSAGE_RECEIVED = 3;


    // INSTANCE VARIABLES

    String serverIp;
    int serverPort;
    SLSocket socket;
    ListenerSocket bcastListener;
    int lastId;
    HashMap<Integer, MessageInfoContainer> sentReliableMessages;
    HashMap<Integer, MessageInfoContainer> receivedReliableMessages;


    public RusClient(String serverIp, int serverPort){

        this.serverIp = serverIp;
        this.serverPort = serverPort;

        final RusClient that = this;

        this.socket = new SLSocket(0){

            @Override
            protected void onMessage(byte[] data){

                //ByteManipulator header = new ByteManipulator(((int)bytes[0])+128);
                int firstByte = data[0];
                firstByte = firstByte >= 0 ? firstByte : firstByte + 256;
                ByteManipulator header = new ByteManipulator(firstByte);
                //for(byte d: data)System.out.println(d);

                data = Arrays.copyOfRange(data, 1, data.length);                                // this is very slow

                int headerBitZero = header.getBit(0);
                if(headerBitZero == NO_DATA){
                    int intFromBitRange = header.getIntFromBitRange(1, 7);
                    if(intFromBitRange == CONNECT){
                        int port = 0;                                              // this  is slow covnerting to string
                        try {
                            port = Integer.parseInt(new String(GlobalService.truncNull(data), "us-ascii"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        that.bcastListener = new ListenerSocket(port, "255.255.255.255"){

                            @Override
                            protected void onMessage(byte[] message){

                                that.onMessage(message);
                            }
                        };

                        that.onConnect();
                    }else if(intFromBitRange == DISCONNECT_WARNING){
                        ByteManipulator responseHeader = new ByteManipulator();
                        responseHeader.setBitToInt(0, NO_DATA);
                        responseHeader.setBitRangeToInt(1,7,EMPTY);
                        this.send(new byte[]{responseHeader.getByte()}, that.serverIp, that.serverPort);
                    }
                }else if(headerBitZero == DATA){

                    int headerBitsOneToTwo = header.getIntFromBitRange(1,2);

                    if(headerBitsOneToTwo == MESSAGE_RECEIVED){

                        int id = data[0];
                        sentReliableMessages.remove(id);

                    }else if(headerBitsOneToTwo == RELIABLE_MESSAGE){

                        byte idByte = data[0];                                                   // both variables prob unnecessary
                        int id = idByte;
                        data = Arrays.copyOfRange(data, 1, data.length);                        // again VERY SLOW!
                        ByteManipulator resHeader = new ByteManipulator();
                        resHeader.setBitToInt(0, DATA);
                        resHeader.setBitRangeToInt(1, 2, MESSAGE_RECEIVED);

                        byte[] response = new byte[data.length+2];
                        response[0] = resHeader.getByte();
                        response[1] = idByte;

                        for(int i = 2; i<data.length+2; i++){
                            response[i] = data[i-2];
                        }

                        this.send(response, that.serverIp, that.serverPort);

                        System.out.println("nigger " + id);
                        if(!receivedReliableMessages.containsKey(id) || !(Arrays.equals(receivedReliableMessages.get(id).message, data))){
                            System.out.println("nigger didnt handle message yet");
                            System.out.println(new String(data, Charset.forName("US-ASCII")));
                            MessageInfoContainer mic = new MessageInfoContainer(1000, 0, data);
                            receivedReliableMessages.put(id, mic);
                            //String transformedMessage = message.substring(0, message.indexOf('\0'));
                            that.onMessage(data);
                        }


                    }else if(headerBitsOneToTwo == NORMAL_MESSAGE){

                        that.onMessage(data);

                    }

                }
            }
        };

        Log.d("rus", "about to run slSocket async task");

        GlobalService.doTheTask(this.socket);

        this.lastId = -1;

        this.sentReliableMessages = new HashMap<Integer, MessageInfoContainer>();
        this.receivedReliableMessages = new HashMap<Integer, MessageInfoContainer>();

        ByteManipulator header = new ByteManipulator();
        header.setBitToInt(0, NO_DATA);
        header.setBitRangeToInt(1, 7, CONNECT);

        this.socket.send(new byte[]{header.getByte()}, this.serverIp, this.serverPort);

        new IntervalExecution(1){

            @Override
            protected void onInterval(){

                that.resendMessages();

                Iterator it = receivedReliableMessages.entrySet().iterator();
                while (it.hasNext())
                {
                    Map.Entry item = (Map.Entry) it.next();

                    MessageInfoContainer mic = (MessageInfoContainer) item.getValue();

                    mic.millisToRetry--;
                    if(mic.millisToRetry <= 0){
                        System.out.println("nigg removing id " + item.getKey());
                        it.remove();
                    }


                }




            }

        }.execute();



    }

    public void resendMessages(){

        Iterator it = sentReliableMessages.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry item = (Map.Entry) it.next();

            MessageInfoContainer mic = (MessageInfoContainer) item.getValue();
            int id = (int) item.getKey();

            mic.millisToRetry--;
            if(mic.millisToRetry <= 0){
                mic.millisToRetry = 3;
                mic.remainingRetries--;
                if(mic.remainingRetries <= 0){
                    it.remove();
                }
                this.socket.send(mic.message, this.serverIp, this.serverPort);
            }


        }


    }

    public void send(byte[] data) {
        ByteManipulator header = new ByteManipulator();
        header.setBitToInt(0, DATA);
        byte[] response = new byte[data.length + 1];
        response[0] = header.getByte();
        for (int i = 0; i < data.length; i++)
            response[i + 1] = data[i];
        this.socket.send(response, this.serverIp, this.serverPort);
    }

    public void sendr(byte[] data)
    {
        int msgId = this.getNextId();

        ByteManipulator header = new ByteManipulator();
        header.setBitToInt(0, DATA);
        header.setBitRangeToInt(1, 2, RELIABLE_MESSAGE);
        byte[] response = new byte[data.length + 2];
        response[0] = header.getByte();
        response[1] = (byte) msgId;
        for (int i = 0; i < data.length; i++)
            response[i + 2] = data[i];
        this.sentReliableMessages.put(msgId, new MessageInfoContainer(3,5, response));
        this.socket.send(response, this.serverIp, this.serverPort);
    }

    protected int getNextId() {
        this.lastId++;
        if (this.lastId > 255) this.lastId = 0;
        return this.lastId;
    }

    protected void onConnect(){
        // To be overridden
    }

    protected void onMessage(byte[] message) {
        // To be overridden
    }

    public void close(){
        this.socket.close();
        this.socket = null;
        if(this.bcastListener!=null)
            this.bcastListener.close();
        this.bcastListener = null;
    }


}
