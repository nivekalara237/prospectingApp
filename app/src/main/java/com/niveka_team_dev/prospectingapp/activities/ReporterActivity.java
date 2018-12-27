package com.niveka_team_dev.prospectingapp.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.handlers.LocationHelper;
import com.niveka_team_dev.prospectingapp.services.LocationMonitoringService;
import com.niveka_team_dev.prospectingapp.ui.CustomProgressDialogOne;
import com.niveka_team_dev.prospectingapp.utilities.Utils;
import com.niveka_team_dev.prospectingapp.kernels.BaseActivity;
import com.niveka_team_dev.prospectingapp.models.Rapport;
import com.niveka_team_dev.prospectingapp.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class ReporterActivity extends BaseActivity implements ValueEventListener,RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.contenu) EditText contenu;
    @BindView(R.id.email) EditText emailsCopies;
    @BindView(R.id.email_l) TextInputLayout emailLayout;
    @BindView(R.id.object) EditText objet;
    @BindView(R.id.error) TextView error;
    @BindView(R.id.radiogroup) RadioGroup radioGroup;
    @BindView(R.id.radio_technique) RadioButton radioTechn;
    @BindView(R.id.radio_generale) RadioButton radioGen;
    @BindView(R.id.save) Button save;

    private Location gpsLocation = null;
    Context context;
    private DatabaseReference rootRef,reportRef,usersRef;
    private int radioCheck = 1;
    private List<User> users = new ArrayList<>();
    List<String> errors = new ArrayList<>();

    ChildEventListener userEvent = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (dataSnapshot.exists()){
                User u = dataSnapshot.getValue(User.class);
                users.add(u);
                Log.e("USERS",u.toString());
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private CustomProgressDialogOne customProgressDialogOne;


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

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
             ArrayList<MultiSelectModel> emails = getSelectModels();
             MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                    .title(getResources().getString(R.string.multi_select_dialog_title)) //setting title for dialog
                    .titleSize(18)
                    .positiveText(getString(R.string.done))
                    .negativeText(getString(R.string.cancel))
                    .setMinSelectionLimit(0) //you can set minimum checkbox selection limit (Optional)
                    .setMaxSelectionLimit(emails.size()) //you can set maximum checkbox selection limit (Optional)
                    .preSelectIDsList(getUsersIds()) //List of ids that you need to be selected
                    .multiSelectList(emails) // the multi select model list with ids and name
                    .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                        @Override
                        public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                            //will return list of selected IDS
                            StringBuilder ems = new StringBuilder();
                            for (int i = 0; i < selectedIds.size(); i++) {
                                ems.append(selectedNames.get(i)).append("; ");
                            }

                            emailsCopies.setText(ems.toString());

                        }

                        @Override
                        public void onCancel() {
                            //Log.d(TAG,"Dialog cancelled");
                        }


                    });
            multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporter);
        ButterKnife.bind(this);
        context = this;
        rootRef = FirebaseDatabase.getInstance().getReference();
        reportRef = rootRef.child(Utils.FIREBASE_DB_NAME).child("reportings");
        usersRef = rootRef.child(Utils.FIREBASE_DB_NAME).child("users");
        usersRef.addChildEventListener(userEvent);
        radioGroup.setOnCheckedChangeListener(this);
        customProgressDialogOne = new CustomProgressDialogOne(this)
                .builder()
                .setMessage("Please wait!");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSavely();
            }
        });

        emailLayout.setOnClickListener(clickListener);
        emailsCopies.setOnClickListener(clickListener);
        //emailsCopies.setTextIsSelectable(true);
        //contenu.setTextIsSelectable(true);
        //objet.setTextIsSelectable(true);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocationeReceiver,new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST));
    }

    private void attemptSavely() {
        boolean cancel = false;
        View focus = null;
        String copyEmails = emailsCopies.getText().toString();
        String obj = objet.getText().toString();
        String cont = contenu.getText().toString();

        errors.clear();

        if (TextUtils.isEmpty(obj)){
            focus = objet;
            cancel = true;
            errors.add(" objet obligatoire");
        }
        if (TextUtils.isEmpty(cont)){
            focus = contenu;
            cancel = true;
            errors.add("le contenu ne doit pas etre vide");
        }


        if (cancel){
            focus.requestFocus();
            displayError();
        }else{
            savaData(cont,obj,copyEmails);
        }
    }

    private void savaData(String cont, String obj, String copyEmails) {
        //Toasty.success(context,"IIIIISSSSSS",Toast.LENGTH_SHORT,true);
        showProgressDialog();
        String key = reportRef.push().getKey();
        Rapport rapport = new Rapport();
        rapport.setId(System.currentTimeMillis());
        rapport.setContenu(cont);
        rapport.setCopie(copyEmails);
        rapport.setType(radioCheck);
        rapport.setKey(key);
        rapport.setObjet(obj);
        if (gpsLocation!=null){
            String gpsloc = gpsLocation.getLatitude()+";"+gpsLocation.getLongitude();
            rapport.setPosition(gpsloc);
        }
        rapport.setNomCom(currentUser.getUsername());
        rapport.setEmail(currentUser.getEmail());
        rapport.setDate(Utils.currentJodaDateStr());
        Map<String,Object> map = rapport.toMap();
        Map<String,Object> mapUpdate = new HashMap<>();
        mapUpdate.put(key,map);
        reportRef.updateChildren(mapUpdate, new DatabaseReference.CompletionListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onComplete( DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError!=null){
                    errors.clear();
                    errors.add(databaseError.getMessage());
                    displayError();
                    hideProgressDialog();
                    //Toasty.success(context,"Rapport enregister avec succès",Toast.LENGTH_SHORT,false);
                }else{
                    hideProgressDialog();
                    Toasty.success(context,"Rapport enregister avec succès",Toast.LENGTH_SHORT,false);
                    startActivity(new Intent(context,MainActivity.class));
                }

            }
        });
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == R.id.radio_generale)
            radioCheck = 1;
        else if (i==R.id.radio_technique)
            radioCheck = 0;
    }

    public List<String> getUsersField(String field){
        List<String> fields =  new ArrayList<>();
        for (User u:users){
            if (field.equals("uid"))
                fields.add(u.getUid());
            else if (field.equals("email"))
                fields.add(u.getEmail());
            else if (field.equals("id"))
                fields.add(String.valueOf(u.getId()));
        }

        return fields;
    }

    public ArrayList<Integer> getUsersIds(){
        ArrayList<Integer> fields =  new ArrayList<>();
        int i=0;
        for (User u:users){
            fields.add(i);
            i++;
        }

        return fields;
    }

    public ArrayList<MultiSelectModel> getSelectModels(){
        ArrayList<MultiSelectModel> fields =  new ArrayList<>();
        int i=0;
        for (User u:users){
            MultiSelectModel model = new MultiSelectModel(i,u.getEmail());
            fields.add(model);
            i++;
        }

        return fields;
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
            ReporterActivity.this.gpsLocation = intent.getParcelableExtra("loc");
            Log.e("LOC",intent.getParcelableExtra("loc").toString());
        }
    };

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocationeReceiver);
        super.onPause();
    }

    public void showProgressDialog() {
        if (customProgressDialogOne == null) {
            customProgressDialogOne = new CustomProgressDialogOne(this)
                    .builder()
                    .setMessage("Please wait!");
            //progressDialog.setIndeterminate(true);
        }
        customProgressDialogOne.show();
    }

    public void hideProgressDialog() {
        customProgressDialogOne.dismiss();
    }
}
