package com.niveka_team_dev.prospectingapp.kernels;

import android.app.Application;

import com.niveka_team_dev.prospectingapp.handlers.FontsOverride;
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
