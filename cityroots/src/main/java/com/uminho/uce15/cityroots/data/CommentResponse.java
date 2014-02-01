package com.uminho.uce15.cityroots.data;

public class CommentResponse {

    private boolean success;

    public CommentResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
