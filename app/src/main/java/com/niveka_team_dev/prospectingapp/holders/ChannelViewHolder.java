package com.niveka_team_dev.prospectingapp.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.niveka_team_dev.prospectingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChannelViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image) public ImageView image;
    @BindView(R.id.name) public TextView name;
    @BindView(R.id.nb_message) public TextView nbMessage;
    @BindView(R.id.lastmessage) public TextView lastMessage;
    @BindView(R.id.datelastmessage) public TextView dateLastMessage;
    public ChannelViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
