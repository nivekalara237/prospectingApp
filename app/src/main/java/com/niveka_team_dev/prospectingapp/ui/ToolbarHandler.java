package com.niveka_team_dev.prospectingapp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.niveka_team_dev.prospectingapp.R;

public class ToolbarHandler {
    public TextView title;
    public ImageView notif;
    public TextView notifBadge;
    public ImageButton backBtn;
    public ImageButton more;
    public Activity activity;
    public Toolbar toolbar;
    public FrameLayout notiflayout;
    private Dialog myDialog;

    public ToolbarHandler(Activity act,Toolbar toolbar){
        this.activity = act;
        this.toolbar = toolbar;
    }

    public ToolbarHandler initUI(){
        title = (TextView)toolbar.findViewById(R.id.toolbar_title);
        notifBadge = (TextView)toolbar.findViewById(R.id.cart_badge);
        backBtn = (ImageButton) toolbar.findViewById(R.id.toolbar_left);
        more = (ImageButton) toolbar.findViewById(R.id.toolbar_more);
        notif = (ImageView) toolbar.findViewById(R.id.toolbar_pro_img);
        notiflayout = (FrameLayout) toolbar.findViewById(R.id.notiflayout);
        return this;
    }

    public void build(){
        myDialog = new Dialog(activity);
        sharedApp();
        moreOptions();
        onback();
        notiflayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(activity.getApplicationContext(), ListingLocalNotificationActivity.class);
                //activity.startActivity(i);
            }
        });
    }

    public void moreOptions() {
       more.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               showPopup();
           }
       });
    }

    public void onback(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
    }
    public ToolbarHandler backTo(Class clazz){
        Intent i = new Intent(activity.getApplicationContext(),clazz);
        activity.startActivity(i);
        return this;
    }

    public ToolbarHandler setTitle(String _title){
        title.setText(_title);
        return this;
    }

    public ToolbarHandler setBadgeContent(String val){
        if (TextUtils.isEmpty(val) || TextUtils.equals(val,"0"))
            notifBadge.setText("");
        else if (TextUtils.isDigitsOnly(val)){
            if (Integer.parseInt(val)>9){
                notifBadge.setText("+9");
            }else
            notifBadge.setText(val);
        }
        return this;
    }

    public void sharedApp(){
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(Intent.ACTION_MEDIA_SHARED);
                String shareBody = "Hi, je viens de télécharger l'application "+activity.getResources().getString(R.string.app_name)+", elle permet de faire des livraisons. " +
                        "Télécharges la en cliquant ici www.goo.gl/LKJLJOI";
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getResources().getString(R.string.app_name));
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

                activity.startActivity(Intent.createChooser(sharingIntent, activity.getResources().getString(R.string.app_name)));
            }
        });

    }

    public void showPopup() {
        TextView txtclose;
        myDialog.setContentView(R.layout.toolbar_more_options);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        LinearLayout logout,about;
        logout = (LinearLayout) myDialog.findViewById(R.id.logout);
        about = (LinearLayout) myDialog.findViewById(R.id.about);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity,"LOGOUT",Toast.LENGTH_SHORT).show();
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity,"ABOUT",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
