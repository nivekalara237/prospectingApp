package com.niveka_team_dev.prospectingapp.listeners;

public interface OnFileUploadResponseServerListener {
    void successUpload(Object result);
    void failureUpload(String errMessage);
}
