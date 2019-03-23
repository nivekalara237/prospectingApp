package com.niveka_team_dev.prospectingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.github.vipulasri.timelineview.TimelineView;
import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.holders.TimeLineSuiviViewHolder;
import com.niveka_team_dev.prospectingapp.models.CompteRenduSuivi;
import com.niveka_team_dev.prospectingapp.models.Prospect;
import com.niveka_team_dev.prospectingapp.models.Suivi;
import com.niveka_team_dev.prospectingapp.utilities.Utils;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SuiviAdapter extends RecyclerView.Adapter<TimeLineSuiviViewHolder> implements FastScrollRecyclerView.SectionedAdapter {
    private List<CompteRenduSuivi> compteRenduSuivis = new ArrayList<>();
    private Context context;
    private Suivi suivi;
    private Prospect prospect;


    public SuiviAdapter(Context context,List<CompteRenduSuivi> compteRenduSuiviList){
        this.compteRenduSuivis = compteRenduSuiviList;
        this.context = context;
    }

    @NonNull
    @Override
    public TimeLineSuiviViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(context, R.layout.timeline_cr_suivi_item,null);
        return new TimeLineSuiviViewHolder(v,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineSuiviViewHolder holder, int position) {

        CompteRenduSuivi cr = compteRenduSuivis.get(position);
        if (position == compteRenduSuivis.size()-1)
            holder.mTimelineView.setMarker(context.getResources().getDrawable(R.drawable.ic_unchecked_red_32dp));
        else
            holder.mTimelineView.setMarker(context.getResources().getDrawable(R.drawable.ic_checked_greeb_32dp), Utils.randomColor());
        holder.content.setText(cr.getContenu());
        holder.createdAt.setText(cr.getCreatedAt());
        holder.dateRdv.setText(cr.getDateProchaineRdv());

    }

    @Override
    public int getItemCount() {
        return compteRenduSuivis.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return "";
    }
}
