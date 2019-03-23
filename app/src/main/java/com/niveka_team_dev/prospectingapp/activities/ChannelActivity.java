package com.niveka_team_dev.prospectingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.adapters.ChannelAdapter;
import com.niveka_team_dev.prospectingapp.kernels.Session;
import com.niveka_team_dev.prospectingapp.listeners.OnItemClickListener;
import com.niveka_team_dev.prospectingapp.models.Channel;
import com.niveka_team_dev.prospectingapp.models.Message;
import com.niveka_team_dev.prospectingapp.models.User;
import com.niveka_team_dev.prospectingapp.ui.ToolbarHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.niveka_team_dev.prospectingapp.activities.ForumCommerciauxActivity.CHANNEL_ID;

public class ChannelActivity extends AppCompatActivity implements OnItemClickListener {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private ChannelAdapter channelAdapter;
    private Context context;
    private Session session;
    private ToolbarHandler toolbarHandler;
    private List<Channel> channels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        context = this;
        ButterKnife.bind(this);
        //toolbar

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayShowCustomEnabled();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        toolbarHandler = new ToolbarHandler(this,toolbar);
        toolbarHandler
                .initUI()
                .setBadgeContent("0")
                .setTitle(getString(R.string.channel_list_title))
                .build();

        //end toolbar
        channels = loadChannel();
        channelAdapter = new ChannelAdapter(channels,context);
        channelAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(channelAdapter);
        final LinearLayoutManager layoutManager = new  LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(false);
        //layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    public List<Channel> loadChannel(){
        List<Channel> chs = new ArrayList<>();
        User u = new User();
        u.setLogin("Niveka");
        u.setId("mlkdmlkmlsdkf");
        u.setFirstName("Kevin");
        u.setLastName("Lactio");
        Channel c = new Channel();
        c.setCode("XCODE-0089");
        c.setDesignation("AIMPHARMA SARL.");
        c.setId("568kkjdklnmpzijxfjkdnlozidjhjdk");
        c.setEntrepriseId("aaaaaaaaaaaaaaaaaaaaaaaaaa");
        Channel c1 = new Channel();
        c1.setCode("XCODE-0786");
        c1.setDesignation("AIMPHARMA SARL. Delegué medicaux");
        c1.setId("%ML%LM%ML%MLML");
        c1.setEntrepriseId("cccccccccccccccccccccccccccccccc");
        Channel c2 = new Channel();
        c2.setCode("XCODE-0090");
        c2.setDesignation("FIRED TEAM DEV.");
        c2.setId("8lkjkljça_zaààcxàcçx_cz__ze");
        c2.setEntrepriseId("bbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        Message me = new Message();
        me.setContenu("have a text document (.txt). I want to convert it to an image (.png or .jpg). For example, black text on white background. How can I do that programmatically?");
        me.setUser(u);
        me.setUserId(u.getId());
        Message me1 = new Message();
        me1.setContenu("have a text document (.txt). I want to convert it to an image (.png or .jpg). For example, black text on white background. How can I do that programmatically?");
        me1.setUser(u);
        me1.setUserId(u.getId());

        Message me2 = new Message();
        me2.setContenu("have a text document (.txt). I want to convert it to an image (.png or .jpg). For example, black text on white background. How can I do that programmatically?");
        me2.setUser(u);
        me2.setUserId(u.getId());

        c.setLastMessage(me);
        c1.setLastMessage(me1);
        c2.setLastMessage(me2);
        c2.setCountUnread(37);
        chs.add(c);
        chs.add(c1);
        chs.add(c2);
        return chs;
    }

    @Override
    public void onItemClick(int position, View view) {
        Intent i = new Intent(this,ForumCommerciauxActivity.class);
        i.putExtra(CHANNEL_ID, (Serializable) channels.get(position));
        startActivity(i);
    }
}
