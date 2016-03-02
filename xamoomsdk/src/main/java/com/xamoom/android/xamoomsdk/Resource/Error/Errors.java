package com.xamoom.android.xamoomsdk.Resource.Error;

import com.google.gson.annotations.SerializedName;

public class Errors {
    @SerializedName("id")
    private String id;
    @SerializedName("status")
    private int status;
    @SerializedName("code")
    private int code;
    @SerializedName("title")
    private String title;
    @SerializedName("detail")
    private String detail;
    @SerializedName("meta")
    private ErrorMeta meta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ErrorMeta getMeta() {
        return meta;
    }

    public void setMeta(ErrorMeta meta) {
        this.meta = meta;
    }
}
