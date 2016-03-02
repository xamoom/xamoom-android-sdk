package com.xamoom.android.xamoomsdk.Resource.Error;

import com.google.gson.annotations.SerializedName;

/**
 * Created by raphaelseher on 10/12/15.
 */
public class ErrorMeta {
    @SerializedName("traceback")
    private String traceback;

    @Override
    public String toString() {
        return String.format("traceback: %s", traceback);
    }

    //getter & setter

    public String getTraceback() {
        return traceback;
    }

    public void setTraceback(String traceback) {
        this.traceback = traceback;
    }
}
