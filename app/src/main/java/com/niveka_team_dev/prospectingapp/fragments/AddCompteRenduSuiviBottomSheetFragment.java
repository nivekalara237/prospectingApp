package com.niveka_team_dev.prospectingapp.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.adapters.ImageCRSAdapter;
import com.niveka_team_dev.prospectingapp.listeners.OnAddCompteRenduSuiviListener;
import com.niveka_team_dev.prospectingapp.models.CompteRenduSuivi;
import com.niveka_team_dev.prospectingapp.models.Prospect;
import com.niveka_team_dev.prospectingapp.models.Suivi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zendesk.belvedere.ImageStream;
import zendesk.belvedere.MediaResult;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link AddCompteRenduSuiviBottomSheetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCompteRenduSuiviBottomSheetFragment extends BottomSheetDialogFragment implements ImageStream.Listener{
    private static final String ARG_PROSPACT = "prospect_id";
    private static final String ARG_SUIVI = "suivi_id";
    private final static int RequestCode = 38709;
    private final static int NumberOfImagesToSelect = 5;

    @BindView(R.id.content) EditText content;
    @BindView(R.id.pickdate) ImageButton pickdate;
    @BindView(R.id.picktime) ImageButton picktime;
    @BindView(R.id.pickcamera) ImageButton pickcamera;
    @BindView(R.id.date) EditText date;
    @BindView(R.id.time) EditText time;
    @BindView(R.id.save) Button addBtn;
    @BindView(R.id.recyclerViewPhoto) RecyclerView recyclerView;
    public OnAddCompteRenduSuiviListener addCallback;

    public ImageCRSAdapter myAdapter;
    private Prospect prospect;
    private Suivi suivi;
    private List<String> imagesArr = new ArrayList<>();
    private CompteRenduSuivi compteRenduSuivi = new CompteRenduSuivi();
    private Calendar myCalendar = Calendar.getInstance();
    private AppCompatActivity activity;

    DatePickerDialog.OnDateSetListener _date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelDate();
        }
    };

    TimePickerDialog.OnTimeSetListener _time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            myCalendar.set(Calendar.HOUR,i);
            myCalendar.set(Calendar.MINUTE,i1);
            updateLabelTime();
        }
    };

    private void updateLabelDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        date.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelTime(){
        String f = "hh:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(f,Locale.FRANCE);
        time.setText(sdf.format(myCalendar.getTime()));
    }

    public AddCompteRenduSuiviBottomSheetFragment() {
        // Required empty public constructor
    }

    public static AddCompteRenduSuiviBottomSheetFragment newInstance(Prospect p,Suivi s) {
        AddCompteRenduSuiviBottomSheetFragment fragment = new AddCompteRenduSuiviBottomSheetFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROSPACT,p);
        args.putSerializable(ARG_SUIVI, s);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            prospect = (Prospect) getArguments().getSerializable(ARG_PROSPACT);
            suivi = (Suivi) getArguments().getSerializable(ARG_SUIVI);
        }
        try {
            addCallback = (OnAddCompteRenduSuiviListener) getParentFragment();
        }catch (ClassCastException e){
            throw new ClassCastException("Calling Fragment must implement OnAddCompteRenduSuiviListener");
        }

        if (getActivity() instanceof AppCompatActivity){
            Log.e("IS_APPCOMPATACTIVITY","TRUEEEE");
            activity = (AppCompatActivity)getActivity();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.add_compte_rendu_suivi_bottom_sheet_fragment,container,false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this,v);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter = new ImageCRSAdapter(getActivity());
        recyclerView.setAdapter(myAdapter);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddCompteRenduSuiviListener) {
            addCallback = (OnAddCompteRenduSuiviListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddCompteRenduSuiviListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addCallback = null;
    }

    @OnClick(R.id.pickcamera)
    public void addPhoto(View view){
        Pix.start(getActivity(),RequestCode,10);
        //Pix.start(getActivity(), Options.init().setRequestCode(100).setCount(2).setFrontfacing(true).getRequestCode(),5);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == RequestCode) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Log.e("IMAGES",returnValue.toString());
            this.imagesArr = returnValue;
            myAdapter.addImage(returnValue);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this.getActivity(), RequestCode,NumberOfImagesToSelect);
                } else {
                    Toast.makeText(this.getActivity(), "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    @OnClick(R.id.pickdate)
    public void pdate(View view){
        new DatePickerDialog(getActivity(), _date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.picktime)
    public void ptime(View view){
        new TimePickerDialog(getActivity(),_time,myCalendar.get(Calendar.HOUR),myCalendar.get(Calendar.MINUTE),false).show();
    }

    @OnClick(R.id.save)
    public void save(View view){
        attemptSave();
    }

    private void attemptSave() {
        content.setError(null);
        date.setError(null);
        String ctnt = content.getText().toString();
        String dte = date.getText().toString();
        String tme = time.getText().toString();
        View requestFocus = null;
        boolean cancel= false;

        if (TextUtils.isEmpty(ctnt)){
            content.setError("Champs obligatoire");
            requestFocus = content;
            requestFocus.requestFocus();
            cancel = true;

        }

        if (!TextUtils.isEmpty(tme) && TextUtils.isEmpty(dte)){
            date.setError("Renseignez une date svp");
            requestFocus = date;
            requestFocus.requestFocus();
            cancel = true;
        }

        if (cancel){
            requestFocus.requestFocus();
        }else {
            saveToServer(ctnt,dte,tme);
        }
    }

    private void saveToServer(String ctnt, String d, String t) {
        String dte = !TextUtils.isEmpty(d)?d.concat(" ").concat(t):null;
        compteRenduSuivi.setDateProchaineRdv(dte);
        compteRenduSuivi.setContenu(ctnt);
        addCallback.newCompteRenduSuivi(compteRenduSuivi,imagesArr);
        dismiss();
    }


    @Override
    public void onDismissed() {

    }

    @Override
    public void onVisible() {

    }

    @Override
    public void onMediaSelected(List<MediaResult> mediaResults) {

    }

    @Override
    public void onMediaDeselected(List<MediaResult> mediaResults) {

    }
}
