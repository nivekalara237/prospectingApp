package com.niveka_team_dev.prospectingapp.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.niveka_team_dev.prospectingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimeLineSuiviViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.timeline) public TimelineView mTimelineView;
    @BindView(R.id.icon_date) public ImageView iconDate;
    @BindView(R.id.content) public TextView content;
    @BindView(R.id.created_at) public TextView createdAt;
    @BindView(R.id.date_rdv) public TextView dateRdv;
    @BindView(R.id.validate) public Button validate;

    public TimeLineSuiviViewHolder(View itemView, int viewType) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        mTimelineView.initLine(viewType);
    }
}
