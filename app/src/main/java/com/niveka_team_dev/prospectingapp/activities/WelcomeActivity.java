package com.niveka_team_dev.prospectingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.kernels.Session;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {
    @BindView(R.id.l1) RelativeLayout l1;
    @BindView(R.id.l2) LinearLayout l2;
    //@BindView(R.id.buttonsub) Button btnsub;
    public Animation uptodown,downtoup;
    @BindView(R.id.login) Button loginBtn;
    @BindView(R.id.signin) Button signinBtn;
    private Session session;
    Context context ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context = this;
        session = new Session(context);
        ButterKnife.bind(this);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        l1.setAnimation(uptodown);
        l2.setAnimation(downtoup);

        if (hasBeenRegister()){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

    }

    @OnClick(R.id.signin)
    public void signin(View v){
        startActivity(new Intent(this,RegisterActivity.class));
    }


    @OnClick(R.id.login)
    public void login(View v){
        startActivity(new Intent(this,LoginActivity.class));
    }


    public boolean hasBeenRegister(){
        String lastEmail = session.retrieveDataString("uemail");
        String lastPassword = session.retrieveDataString("upass");
        Log.e("TAG",lastEmail+"/"+lastPassword);
        return !lastEmail.equals("") && !lastPassword.equals("");
    }

}
