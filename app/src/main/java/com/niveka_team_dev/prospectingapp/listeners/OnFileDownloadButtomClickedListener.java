package com.niveka_team_dev.prospectingapp.listeners;

import android.view.View;

import com.niveka_team_dev.prospectingapp.models.Attachment;
import com.niveka_team_dev.prospectingapp.models.Message;

public interface OnFileDownloadButtomClickedListener {
    void onFileDownloadClicked(Message message,View ...imgView);
    void onFileDownloadRetryClicked(View imgView, Attachment attch);
}
