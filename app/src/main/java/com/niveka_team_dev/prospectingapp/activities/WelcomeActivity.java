package com.niveka_team_dev.prospectingapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.ui.RadarView;

import butterknife.BindView;

public class WelcomeActivity extends AppCompatActivity {
    @BindView(R.id.l1) LinearLayout l1;
    @BindView(R.id.l2) LinearLayout l2;
    @BindView(R.id.buttonsub) Button btnsub;
    public Animation uptodown,downtoup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        l1 = (LinearLayout) findViewById(R.id.l1);
        l2 = (LinearLayout) findViewById(R.id.l2);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        l1.setAnimation(uptodown);
        l2.setAnimation(downtoup);


    }

}
