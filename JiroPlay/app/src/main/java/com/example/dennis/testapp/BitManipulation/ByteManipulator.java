package com.example.dennis.testapp.BitManipulation;

import java.util.ArrayList;

public class ByteManipulator {

    int value;
    boolean isValueProcessed = true;
    ArrayList<Integer> bits;

    public ByteManipulator(int value) {
        this.value = value;
        this.createBits();
    }

    public ByteManipulator() {
        this.value = 0;
        this.createBits();
    }

    private void createBits(){
        int num = this.value;
        this.bits = new ArrayList<>();
        this.bits.add(num%2);

        while(num>1){
            num = num/2;
            this.bits.add(num%2);

        }

        while(this.bits.size()<8){
            this.bits.add(0);
        }

        this.bits = reverseArray(this.bits);


    }

    public void setBitRangeToInt(int start_idx, int end_idx, int val){

        ArrayList<Integer> bits = new ArrayList<>();

        bits.add(val%2);

        while(val > 1){
            val = val/2;
            bits.add(val%2);

        }

        while(bits.size() <= end_idx - start_idx){
           bits.add(0);
        }

        bits = reverseArray(bits);
        this.injectArrayList(this.bits, bits, start_idx);
        this.isValueProcessed = false;

    }

    public void setBitToInt(int idx, int val){
        this.bits.set(idx, val);
        this.isValueProcessed = false;
    }

    public int getValue() {

        if(this.isValueProcessed)
            return this.value;
        this.value = this.getIntFromBitRange(0,7);
        this.isValueProcessed = true;
        return this.value;
    }

    private void injectArrayList(ArrayList<Integer> consumingArr, ArrayList<Integer> arr, int index){

        for(int i = 0; i<arr.size(); i++){
            consumingArr.set(i+index, arr.get(i));
        }
    }

    public int getIntFromBitRange(int start_idx, int end_idx){

        int result = 0;

        for(int i = 0; i<= end_idx - start_idx; i++){
            int multiplier = (int)(Math.pow(2, end_idx-start_idx-i));
            result += (this.bits.get(i + start_idx)*multiplier);
        }
        return result;

    }

    public ArrayList<Integer> getBits(){
        return this.bits;
    }

    public int getBit(int index){

        return this.bits.get(index);
    }

    private ArrayList<Integer> reverseArray(ArrayList<Integer> arr){

        ArrayList<Integer> newList = new ArrayList<>();
        for(int i = arr.size()-1; i>=0; i--){
            newList.add(arr.get(i));
        }
        return newList;
    }

    public char getChar(){return (char)this.getValue();}            // prob not needed any more this method
    public byte getByte() { return (byte) this.getValue(); }

}
