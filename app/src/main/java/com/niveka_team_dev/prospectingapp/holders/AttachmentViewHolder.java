package com.niveka_team_dev.prospectingapp.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.niveka_team_dev.prospectingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttachmentViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.icon) public ImageView icon;
    @BindView(R.id.title) public TextView title;
    @BindView(R.id.remove) public ImageView remove;
    public AttachmentViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
