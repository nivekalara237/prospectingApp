package com.niveka_team_dev.prospectingapp.kernels;

import android.app.Application;
import android.content.Intent;
import android.support.text.emoji.EmojiCompat;

import com.niveka_team_dev.prospectingapp.Session;
import com.niveka_team_dev.prospectingapp.activities.LoginActivity;
import com.niveka_team_dev.prospectingapp.handlers.FontsOverride;
import com.niveka_team_dev.prospectingapp.handlers.UserHelpers;
import com.niveka_team_dev.prospectingapp.models.User;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/futura_book_font.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/futura_book_font.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/futura_book_font.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/futura_book_font.ttf");


        ConnectionBuddyConfiguration networkInspectorConfiguration = new ConnectionBuddyConfiguration.Builder(this).build();
        ConnectionBuddy.getInstance().init(networkInspectorConfiguration);


        //EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
        //EmojiCompat.init(config);
    }
}
