package com.niveka_team_dev.prospectingapp.listeners;

import com.niveka_team_dev.prospectingapp.models.CompteRenduSuivi;

import java.util.List;

public interface OnAddCompteRenduSuiviListener {
    void newCompteRenduSuivi(CompteRenduSuivi compteRenduSuivi, List<String> images);
    void newCompteRenduSuiviCancel();
}
