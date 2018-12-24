package com.niveka_team_dev.prospectingapp.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.niveka_team_dev.prospectingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class ForumComMessageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.letf_message_body) public EmojiconTextView left_message_body;
    @BindView(R.id.rigth_message_body) public EmojiconTextView right_message_body;
    @BindView(R.id.letf_image_message_profile) public ImageView left_imageProfile;
    @BindView(R.id.letf_message_name) public TextView left_message_name;
    @BindView(R.id.letf_message_time) public TextView left_message_time;
    @BindView(R.id.right_message_seen) public ImageView right_message_seen;
    @BindView(R.id.right_message_time) public TextView right_message_time;
    @BindView(R.id.leftLayout) public RelativeLayout leftLayout;
    @BindView(R.id.rightLayout) public RelativeLayout rightLayout;

    public ForumComMessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
