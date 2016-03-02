package com.xamoom.android.xamoomsdk.Resource.Error;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by raphaelseher on 10/12/15.
 */
public class ErrorMessage {
    @SerializedName("errors")
    private ArrayList<Errors> errors;

    @Override
    public String toString() {
        return String.format("errors: %s", errors);
    }

    //getter & setter

    public ArrayList<Errors> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<Errors> errors) {
        this.errors = errors;
    }
}

