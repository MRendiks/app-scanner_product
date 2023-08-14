package com.databarang.qrcode.model.submit;

import com.google.gson.annotations.SerializedName;

public class Submit {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private SubmitData submitData;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SubmitData getSubmitData() {
        return submitData;
    }

    public void setSubmitData(SubmitData submitData) {
        this.submitData = submitData;
    }
}
