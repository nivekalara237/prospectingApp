package com.niveka_team_dev.prospectingapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.utilities.Utils;
import com.niveka_team_dev.prospectingapp.holders.ListProspectsViewHolder;
import com.niveka_team_dev.prospectingapp.listeners.OnRVBottomReachedListener;
import com.niveka_team_dev.prospectingapp.models.Prospect;

import java.util.List;

import butterknife.ButterKnife;

public class ListingProspectsAdapter extends RecyclerView.Adapter<ListProspectsViewHolder> {

    public List<Prospect> prospects;
    public Context context;
    public OnRVBottomReachedListener onRVBottomReachedListener;

    public ListingProspectsAdapter(Context ctx, List<Prospect> prospectList){
        this.context = ctx;
        this.prospects = prospectList;
        if (ctx instanceof OnRVBottomReachedListener)
            onRVBottomReachedListener = (OnRVBottomReachedListener) ctx;
    }


    @NonNull
    @Override
    public ListProspectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.prospect_item,parent,false);
        ButterKnife.bind(this,v);
        return new ListProspectsViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListProspectsViewHolder holder, int position) {
        Prospect prospect = prospects.get(position);
        holder.nomClient.setText(prospect.getNom());
        holder.localisation.setText(prospect.getLocalisation());
        if (!TextUtils.isEmpty(prospect.getEmail())){
            holder.emailClient.setText(prospect.getEmail());
        }else {
            holder.emailClient.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(prospect.getTelephone()))
            holder.telClient.setText(prospect.getTelephone());
        else
            holder.telClient.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(prospect.getImpression()))
            holder.impression.setText(prospect.getImpression());
        else
            holder.impression.setText(R.string.text0001);

        if (prospect.getDate()!=0){
            holder.date.setText(DateUtils.getRelativeTimeSpanString(prospect.getDate()));
        }else
            holder.date.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(prospect.getEmailUser()))
            holder.nomUser.setText(context.getString(R.string.text0002)+prospect.getEmailUser());
        else
            holder.nomUser.setVisibility(View.GONE);

        if (prospect.getType() == Utils.TYPE_PROSPECTION.PROSPECTION){
            holder.type.setText(R.string.text0042);
        }else
            holder.type.setText(R.string.text0041);

        if (/*position%Utils.MAX_PROSPECTS_TO_DISPLAY_PER_PAGE==0 && */position == prospects.size()-1){
            onRVBottomReachedListener.onBottomReached(position,true);
        }

    }

    @Override
    public int getItemCount() {
        return prospects.size();
    }

    public void addEntry(Prospect prospect) {
        prospects.remove(prospect);
        notifyItemRemoved(prospects.indexOf(prospect));
        prospects.add(prospect);
        notifyItemChanged(prospects.size());
        notifyDataSetChanged();
    }

    public void setAllItems(List<Prospect> prospectList) {
        prospects.clear();
        prospects.addAll(prospectList);
        notifyDataSetChanged();
    }
}
