package com.niveka_team_dev.prospectingapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.niveka_team_dev.prospectingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelperActivity extends AppCompatActivity {

    @BindView(R.id.text)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            textView.setText(extras.getString("goto"));
        }
    }
}
