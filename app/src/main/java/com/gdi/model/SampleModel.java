package com.gdi.model;

import java.util.ArrayList;

public class SampleModel {

    int amount = 0;

    public SampleModel(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public static ArrayList<SampleModel> createList(int num){
        ArrayList<SampleModel> sampleModelList = new ArrayList<>();

        for (int i = 1 ; i <= num ; i++){
            sampleModelList.add(new SampleModel(+i));
        }
        return sampleModelList;
    }
}
