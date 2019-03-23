package com.niveka_team_dev.prospectingapp.listeners;

import java.util.List;

public interface OnAttachmenPickListener {
    void imagesPicked(List<String> imagesPaths);
    void filesPicked(List<String> filesPaths);
}
