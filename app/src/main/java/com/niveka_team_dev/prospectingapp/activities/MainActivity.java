package com.niveka_team_dev.prospectingapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.niveka_team_dev.prospectingapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addProspect(View view) {
        Intent i = new Intent();
        i.setClass(this, ProspectionActivity.class);
        startActivity(i);
    }

    public void listProspects(View view) {
        Intent i = new Intent();
        i.setClass(this, ListingProspectsActivity.class);
        startActivity(i);
    }
}
