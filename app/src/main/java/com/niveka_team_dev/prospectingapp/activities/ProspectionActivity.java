package com.niveka_team_dev.prospectingapp.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.handlers.LocationHelper;
import com.niveka_team_dev.prospectingapp.services.LocationMonitoringService;
import com.niveka_team_dev.prospectingapp.ui.CustomProgressDialogOne;
import com.niveka_team_dev.prospectingapp.utilities.Utils;
import com.niveka_team_dev.prospectingapp.models.Prospect;

import net.ralphpina.permissionsmanager.PermissionsManager;
import net.ralphpina.permissionsmanager.PermissionsResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import rx.functions.Action1;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

public class ProspectionActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    public final static String ERROR_ID = "error_id";
    public final static String LOC_ID = "error_id";

    @BindView(R.id.name) EditText name;
    @BindView(R.id.phone) EditText phone;
    @BindView(R.id.email) EditText email;
    @BindView(R.id.location) EditText location;
    @BindView(R.id.impression) EditText impression;
    @BindView(R.id.save) Button save;
    @BindView(R.id.error) TextView error;
    @BindView(R.id.radio_suivi) RadioButton radio_suivi;
    @BindView(R.id.radio_prospect) RadioButton radio_prospect;
    @BindView(R.id.radiogroup) RadioGroup radio_group;

    public ProgressDialog progressDialog;
    DatabaseReference rootRef,propectsRef;
    FirebaseUser auth;
    String errorTxt = "";
    int radioChecked = -1;
    private Location gpsLocation = null;
    ArrayList<String> errors = new ArrayList<>();
    private CustomProgressDialogOne customProgressDialogOne;

    @Override
    public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        state.putStringArrayList(ERROR_ID, errors);
        state.putParcelable(LOC_ID,gpsLocation);
    }

    private void displayError() {
        if (!errors.isEmpty()){
            error.setVisibility(View.VISIBLE);
            StringBuilder errs = new StringBuilder();
            for (String err:errors){
                errs.append("* ").append(err).append("\n");
            }
            error.setText(errs.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prospection);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        //auth.getUid();

        onClickAskForLocation();

        ButterKnife.bind(this);
        radio_group.setOnCheckedChangeListener(this);
        customProgressDialogOne = new CustomProgressDialogOne(this)
                .builder()
                .setMessage("Please wait!");
        if (savedInstanceState!=null){
            errorTxt = savedInstanceState.getString(ERROR_ID);
            gpsLocation = savedInstanceState.getParcelable(LOC_ID);
        }

        displayError();

        //database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();
        propectsRef = rootRef.child(Utils.FIREBASE_DB_NAME).child("prospects");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mLocationeReceiver,new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST));
    }

    public void save(){
        attemptSave();
    }

    private void attemptSave() {
        name.setError(null);
        phone.setError(null);
        email.setError(null);
        impression.setError(null);
        location.setError(null);
        boolean cancel = false;
        String nom = name.getText().toString();
        String em = email.getText().toString();
        String tel = phone.getText().toString();
        String loc = location.getText().toString();
        String impr = impression.getText().toString();
        View focus = null;
        errors.clear();

        if (TextUtils.isEmpty(nom)){
            cancel = true;
            focus = name;
            errors.add(getString(R.string.text0051));
        }

        if (TextUtils.isEmpty(loc)){
            cancel = true;
            focus = location;
            errors.add(getString(R.string.text0052));
        }
        if (TextUtils.isEmpty(tel) && TextUtils.isEmpty(em)){

            cancel = true;
            focus = phone;
            errors.add(getString(R.string.text0053));
            //Toasty.success(this, getString(R.string.text0054), Toast.LENGTH_SHORT,false).show();
        }

        if (!TextUtils.isEmpty(em) && !em.contains("@")){
            cancel = true;
            focus = email;
            errors.add(getString(R.string.text0055));
        }

        if (radioChecked == -1){
            cancel = true;
            focus = radio_group;
            //radio_group.setError("Email invalide");
            radio_group.setBackgroundColor(Color.parseColor("#4AB4040D"));
        }

        if (cancel)
        {
            focus.requestFocus();
            displayError();
        }
        else {
            saveData(nom,tel,em,loc,impr,radioChecked);
        }

    }

    private void saveData(String nom, String tel, String em, String loc, String impr,int type) {
        showProgressDialog();

        Date d = Calendar.getInstance().getTime();

        Prospect prospect = new Prospect();
        prospect.setId(System.currentTimeMillis());
        prospect.setEmail(em);
        prospect.setEmailUser(auth.getEmail());
        prospect.setImpression(impr);
        prospect.setNom(nom);
        prospect.setLocalisation(loc);
        prospect.setTelephone(tel);
        prospect.setType(type);
        prospect.setDate(d.getTime());
        if (gpsLocation!=null){
            String gpsloc = gpsLocation.getLatitude()+";"+gpsLocation.getLongitude();
            prospect.setPosition(gpsloc);
        }
        Map<String,Object> map = prospect.toMap();
        //String key = propectsRef.getKey();
        propectsRef.push().setValue(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                errors.clear();
                if (databaseError!=null){
                    errors.add(databaseError.getMessage());
                }else {
                    Toasty.success(ProspectionActivity.this, getString(R.string.text0047), Toast.LENGTH_SHORT).show();
                    gotoMain();
                }
                displayError();
                hideProgressDialog();
            }
        });

    }

    private void gotoMain(){
        Intent i = new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }


    public void showProgressDialog() {
        if (customProgressDialogOne == null) {
            customProgressDialogOne = new CustomProgressDialogOne(this)
                    .builder()
                    .setMessage(getString(R.string.text0046));
            //progressDialog.setIndeterminate(true);
        }
        customProgressDialogOne.show();
    }

    public void hideProgressDialog() {
        customProgressDialogOne.dismiss();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
        switch (checkedId){
            case R.id.radio_prospect:
              radioChecked = 1;
              break;
            case R.id.radio_suivi:
                radioChecked = 0;
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!Utils.isServiceRunning(this,LocationMonitoringService.class))
            LocationHelper.startLocationMonitoringService(this);

    }

    private BroadcastReceiver mLocationeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ProspectionActivity.this.gpsLocation = intent.getParcelableExtra("loc");
            //Log.e("LOC",intent.getParcelableExtra("loc").toString());
        }
    };

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocationeReceiver);
        super.onPause();
    }


    public void onClickAskForLocation() {
         if (!PermissionsManager.get().isLocationGranted()) {
             PermissionsManager.get()
                 .requestLocationPermission()
                 .subscribe(new Action1<PermissionsResult>() {
                     @Override
                     public void call(PermissionsResult permissionsResult) {

                         Toast.makeText(ProspectionActivity.this,
                                 permissionsResult.isGranted() ? getString(R.string.text0056) : getString(R.string.text0057),
                                 Toast.LENGTH_SHORT)
                                 .show();
                     }
                 });
         }
    }

}
