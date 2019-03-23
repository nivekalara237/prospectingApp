package com.niveka_team_dev.prospectingapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.developerpaul123.filepickerlibrary.FilePickerActivity;
import com.github.developerpaul123.filepickerlibrary.FilePickerBuilder;
import com.github.developerpaul123.filepickerlibrary.enums.MimeType;
import com.github.developerpaul123.filepickerlibrary.enums.Request;
import com.github.developerpaul123.filepickerlibrary.enums.Scope;
import com.niveka_team_dev.prospectingapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zendesk.belvedere.BelvedereUi;
import zendesk.belvedere.ImageStream;
import zendesk.belvedere.MediaResult;

import static android.app.Activity.RESULT_OK;
import static com.github.developerpaul123.filepickerlibrary.FilePickerActivity.REQUEST_FILE;

public class AttachmentOptionsBottomSheetDialogFragment extends BottomSheetDialogFragment implements ImageStream.Listener{

    @BindView(R.id.image) ImageButton image;
    @BindView(R.id.file) ImageButton file;
    private ArrayList<String> docPaths;
    private ArrayList<String> photoPaths;
    private ImageStream imageStream;
    private AppCompatActivity appCompatActivity;

    public AttachmentOptionsBottomSheetDialogFragment() {
        // Required empty public constructor
    }

    public static AttachmentOptionsBottomSheetDialogFragment newInstance() {
        AttachmentOptionsBottomSheetDialogFragment fragment = new AttachmentOptionsBottomSheetDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

//        imageStream = BelvedereUi.install((AppCompatActivity) getActivity());
//        imageStream.addListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_attachment_options_bottom_sheet_dialog, container, false);
        ButterKnife.bind(this,v);
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }

    @OnClick(R.id.image)
    public void img(View view){
        new FilePickerBuilder(getActivity())
                .withColor(android.R.color.holo_blue_bright)
                .withRequest(Request.FILE)
                .withScope(Scope.ALL)
                .withMimeType(MimeType.JPEG)
                .useMaterialActivity(true)
                .launch(REQUEST_FILE);
    }

    @OnClick(R.id.file)
    public void file(View view){
        BelvedereUi.imageStream(getActivity())
                .withCameraIntent()
                .withCameraIntent()
                .withDocumentIntent("*/*", true)
                .showPopup((AppCompatActivity) getActivity());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 233) && (resultCode == RESULT_OK)) {
            Toast.makeText(getActivity(), "Directory Selected: " + data.getStringExtra(FilePickerActivity.FILE_EXTRA_DATA_PATH), Toast.LENGTH_LONG).show();
        } else if ((requestCode == REQUEST_FILE) && (resultCode == RESULT_OK)) {
            Toast.makeText(getActivity(), "File Selected: " + data.getStringExtra(FilePickerActivity.FILE_EXTRA_DATA_PATH), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDismissed() {

    }

    @Override
    public void onVisible() {

    }

    @Override
    public void onMediaSelected(List<MediaResult> mediaResults) {
        Log.e("MEDIA_SELECTED",mediaResults.toString());
    }

    @Override
    public void onMediaDeselected(List<MediaResult> mediaResults) {

    }
}
