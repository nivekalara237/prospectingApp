package com.niveka_team_dev.prospectingapp.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.niveka_team_dev.prospectingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListReportingViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.date) public TextView date;
    @BindView(R.id.nom_com) public TextView nom_com;
    @BindView(R.id.email_com) public TextView email_com;
    @BindView(R.id.type) public TextView type;
    @BindView(R.id.contenu) public TextView contenu;
    @BindView(R.id.objet) public TextView objet;
    @BindView(R.id.email_copie) public TextView email_copie;

    public ListReportingViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
