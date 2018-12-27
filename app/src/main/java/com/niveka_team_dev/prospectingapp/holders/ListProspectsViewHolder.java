package com.niveka_team_dev.prospectingapp.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.niveka_team_dev.prospectingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListProspectsViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.nom_client) public TextView nomClient;
    @BindView(R.id.date) public TextView date;
    @BindView(R.id.telephone_client) public TextView telClient;
    @BindView(R.id.email_client) public TextView emailClient;
    @BindView(R.id.user) public TextView nomUser;
    @BindView(R.id.impression) public TextView impression;
    @BindView(R.id.localisation_client) public TextView localisation;
    @BindView(R.id.type) public TextView type;

    public ListProspectsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
