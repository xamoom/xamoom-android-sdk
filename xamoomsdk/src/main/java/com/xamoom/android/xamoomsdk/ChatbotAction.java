package com.xamoom.android.xamoomsdk;

public class ChatbotAction {

    private int type;
    private String value;

    public ChatbotAction(int type, String value){
        this.type = type;
        this.value = value;
    }

    public int getType(){
        return this.type;
    }

    public String getValue(){
        return this.value;
    }

}
