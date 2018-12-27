package com.niveka_team_dev.prospectingapp.ui;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.niveka_team_dev.prospectingapp.R;

public class CustomProgressDialogOne {
    public AlertDialog b = null;
    public TextView message;
    public Context context;
    public CustomProgressDialogOne(Context ctx){
        this.context = ctx;
    }
    public CustomProgressDialogOne builder() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog_one, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        RelativeLayout mainLayout = (RelativeLayout) dialogView.findViewById(R.id.mainLayout);
        message = (TextView) dialogView.findViewById(R.id.tv_progressmsg);
        mainLayout.setBackgroundResource(R.drawable.my_progress_one);
        AnimationDrawable frameAnimation = (AnimationDrawable)mainLayout.getBackground();
        frameAnimation.start();
        b = dialogBuilder.create();
        return this;
    }

    public void show(){
        if (b!=null)
            b.show();
    }
    public void dismiss(){
        if (b!=null)
            b.dismiss();
    }

    public CustomProgressDialogOne setMessage(String title){
        if (b!=null){
            message.setText(title);
        }
        return this;
    }
}
