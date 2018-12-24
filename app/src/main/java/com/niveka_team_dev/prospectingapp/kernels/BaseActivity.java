package com.niveka_team_dev.prospectingapp.kernels;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.Session;
import com.niveka_team_dev.prospectingapp.activities.LoginActivity;
import com.niveka_team_dev.prospectingapp.handlers.UserHelpers;
import com.niveka_team_dev.prospectingapp.models.User;
import com.zplesac.connectionbuddy.interfaces.ConnectivityChangeListener;
import com.zplesac.connectionbuddy.models.ConnectivityEvent;

import es.dmoral.toasty.Toasty;

@SuppressLint("Registered")
public class BaseActivity  extends AppCompatActivity implements ConnectivityChangeListener {

    public  Session session  = null;
    public User currentUser=null;
    @Override
    protected void onCreate(Bundle state){
        super.onCreate(state);
        session = new Session(this);
        currentUser = UserHelpers.getCurrentUser(this);
        if (currentUser==null){
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void onConnectionChange(ConnectivityEvent event) {
        if(event!=null && event.getState()!=null && event.getState().getValue()==1){
            ;//no_connection.setVisibility(View.GONE);
        }else {
            //no_connection.setVisibility(View.VISIBLE);
            Toasty.error(this,"Aucune ", Toast.LENGTH_LONG,true).show();

        }

    }
}
