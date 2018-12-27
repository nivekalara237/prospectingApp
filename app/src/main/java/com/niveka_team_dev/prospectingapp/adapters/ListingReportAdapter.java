package com.niveka_team_dev.prospectingapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.utilities.Utils;
import com.niveka_team_dev.prospectingapp.holders.ListReportingViewHolder;
import com.niveka_team_dev.prospectingapp.listeners.OnRVBottomReachedListener;
import com.niveka_team_dev.prospectingapp.models.Rapport;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.Date;
import java.util.List;

public class ListingReportAdapter extends RecyclerView.Adapter<ListReportingViewHolder> implements FastScrollRecyclerView.SectionedAdapter {

    public Context context;
    public List<Rapport> rapports;
    private OnRVBottomReachedListener onRVBottomReachedListener;

    public ListingReportAdapter(List<Rapport> rpts,Context ctx){
        this.context = ctx;
        this.rapports = rpts;
        if (ctx instanceof OnRVBottomReachedListener)
            onRVBottomReachedListener = (OnRVBottomReachedListener)ctx;
    }

    @NonNull
    @Override
    public ListReportingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.repport_item,parent,false);
        return new ListReportingViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListReportingViewHolder holder, int position) {
        Rapport rapport = rapports.get(position);
        Log.e("EMAIL",rapport.toString());
        holder.contenu.setText(rapport.getContenu());
        holder.email_com.setText(rapport.getEmail());
        String date = Utils.convertJodaTimeToReadable(rapport.getDate());
        DateTimeUtils.setTimeZone("UTC");
        if (!TextUtils.isEmpty(rapport.getCopie())){
            String[] emails = rapport.getCopie().split(";");
            StringBuilder ems = new StringBuilder();
            for (String em:emails){
                ems.append(em).append("\n");
            }
            holder.email_copie.setText(ems.toString());
        }else {
            holder.email_copie.setText(R.string.text0043);
        }

        if (rapport.getType() == Utils.TYPE_REPORT.INFO_TECH){
            holder.type.setText(R.string.text0044);
        }else {
            holder.type.setText(R.string.text0045);
        }

        if (DateTimeUtils.isToday(date)){
            if (DateTimeUtils.getDateDiff(new Date(),date, DateTimeUnits.MINUTES) < 6)
                holder.date.setText("");
            else {
                holder.date.setText(DateTimeUtils.formatTime(date,true));
            }
        }else if (DateTimeUtils.isYesterday(date)){
            holder.date.setText(context.getString(R.string.text0039)+DateTimeUtils.formatTime(date,true));
        }else {
            //DateTimeUtils.formatWithPattern("2017-06-13", "EEEE, MMMM dd, yyyy"); // Tuesday, June 13, 2017
            holder.date.setText(DateTimeUtils.formatWithPattern(date, "EEEE, dd MMMM yyyy"));
        }

        holder.nom_com.setText(rapport.getNomCom());
        if (position == rapports.size()-1){
            onRVBottomReachedListener.onBottomReached(position,true);
        }
    }

    @Override
    public int getItemCount() {
        return rapports.size();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return "";
    }
}
