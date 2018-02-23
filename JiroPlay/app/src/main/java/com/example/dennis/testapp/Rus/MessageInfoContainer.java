package com.example.dennis.testapp.Rus;


public class MessageInfoContainer {

    int millisToRetry;
    int remainingRetries;
    byte[] message;

    public MessageInfoContainer(int millisToRetry, int remainingRetries, byte[] message) {
        this.millisToRetry = millisToRetry;
        this.remainingRetries = remainingRetries;
        this.message = message;
    }


    public int getMillisToRetry() {
        return millisToRetry;
    }

    public void setMillisToRetry(int millisToRetry) {
        this.millisToRetry = millisToRetry;
    }

    public int getRemainingRetries() {
        return remainingRetries;
    }

    public void setRemainingRetries(int remainingRetries) {
        this.remainingRetries = remainingRetries;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }




}
