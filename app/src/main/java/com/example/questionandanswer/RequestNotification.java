package com.example.questionandanswer;

import com.google.gson.annotations.SerializedName;

public class RequestNotification {
    @SerializedName("to")
    public String to;
    @SerializedName("data")
    public DataModel Data;

    public RequestNotification(String to, DataModel data) {
        this.to = to;
        Data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public DataModel getData() {
        return Data;
    }

    public void setData(DataModel data) {
        Data = data;
    }
}
