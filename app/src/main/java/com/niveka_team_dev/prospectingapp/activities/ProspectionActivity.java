package com.niveka_team_dev.prospectingapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.niveka_team_dev.prospectingapp.Utils;
import com.niveka_team_dev.prospectingapp.models.Prospect;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProspectionActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    public final static String ERROR_ID = "error_id";

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

    @Override
    public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        state.putString(ERROR_ID,errorTxt);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prospection);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        //auth.getUid();
        ButterKnife.bind(this);
        radio_group.setOnCheckedChangeListener(this);

        if (savedInstanceState!=null){
            errorTxt = savedInstanceState.getString(ERROR_ID);
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

        if (TextUtils.isEmpty(nom)){
            cancel = true;
            focus = name;
            name.setError("champ obligatoire");
        }

        if (TextUtils.isEmpty(loc)){
            cancel = true;
            focus = location;
            location.setError("champ obligatoire");
        }
        if (TextUtils.isEmpty(tel) && TextUtils.isEmpty(em)){

            cancel = true;
            focus = phone;
            phone.setError("Telephone ou email requis");
            Toast.makeText(this, "Vous devez renseigner l'email et le numero de telephone, ou bien l'un des deux", Toast.LENGTH_SHORT).show();
        }

        if (!TextUtils.isEmpty(em) && !em.contains("@")){
            cancel = true;
            focus = email;
            email.setError("Email invalide");
        }

        if (radioChecked == -1){
            cancel = true;
            focus = radio_group;
            //radio_group.setError("Email invalide");
            radio_group.setBackgroundColor(Color.parseColor("#4AB4040D"));
        }

        if (cancel)
            focus.requestFocus();
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
        Map<String,Object> map = prospect.toMap();
        //String key = propectsRef.getKey();
        propectsRef.push().setValue(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError!=null){
                    errorTxt = databaseError.getMessage();
                }else {
                    Toast.makeText(ProspectionActivity.this,"Prospection enregistrée avec succès",Toast.LENGTH_SHORT).show();
                    gotoMain();
                }

                displayError();
                hideProgressDialog();
            }
        });

    }

    private void gotoMain(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }


    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait!, running save data...");
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    public void displayError(){
        if (TextUtils.isEmpty(errorTxt)){
            error.setVisibility(View.GONE);
        }else{
            error.setVisibility(View.VISIBLE);
            error.setText(errorTxt);
        }
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
}
