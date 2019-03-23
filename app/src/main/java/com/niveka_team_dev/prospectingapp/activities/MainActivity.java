package com.niveka_team_dev.prospectingapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.jackandphantom.blurimage.BlurImage;
import com.niveka_team_dev.prospectingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.l1)
    RelativeLayout l1;
    @BindView(R.id.rootView) RelativeLayout rootViex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Bitmap bitmap = BlurImage.with(getApplicationContext())
                .load(R.drawable.background)
                .intensity(2)
                .Async(true)
                .scale(0.6f)
                .getImageBlur();
        Bitmap bitmap2 = BlurImage.with(getApplicationContext()).load(R.drawable.landscape_savana).intensity(0).Async(true).getImageBlur();
        rootViex.setBackground(new BitmapDrawable(bitmap));
        l1.setBackground(new BitmapDrawable(bitmap2));
    }

    public void addProspect(View view) {
        Intent i = new Intent();
        i.setClass(this, SuiviActivity.class);
        startActivity(i);
    }

    public void listProspects(View view) {
        Intent i = new Intent();
        i.setClass(this, ListingProspectsActivity.class);
        startActivity(i);
    }

    public void forum(View view) {
        Intent i = new Intent();
        i.setClass(this, ChannelActivity.class);
        startActivity(i);
    }

    public void reporter(View view) {
        Intent i = new Intent();
        i.setClass(this, ReporterActivity.class);
        startActivity(i);
    }

    public void listReporter(View view) {
        Intent i = new Intent();
        i.setClass(this, ListingReportActivity.class);
        startActivity(i);
    }

    public void helpfull(View view) {
        Intent i = new Intent();
        i.setClass(this, HelperActivity.class);
        i.putExtra("goto","MAIN_HELPER");
        startActivity(i);
    }
}
