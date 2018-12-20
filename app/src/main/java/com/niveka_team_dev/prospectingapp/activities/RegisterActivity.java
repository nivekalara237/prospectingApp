package com.niveka_team_dev.prospectingapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.Session;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "EmailPasswordAuth";

    @BindView(R.id.email) EditText emailET;
    @BindView(R.id.code_group) EditText codeGroupeET;
    @BindView(R.id.password) EditText passwordET;
    @BindView(R.id.repassword) EditText repasswordET;
    @BindView(R.id.signup) Button signup_btn;
    @BindView(R.id.signin) Button sign_btn;
    @BindView(R.id.cardview) CardView cardView;
    @BindView(R.id.error) TextView error;

    public Animation uptodown,downtoup,downtoleft;
    private FirebaseAuth firebaseAuth;
    Session session;
    Context context;
    private List<String> codesGroup = new ArrayList<>();
    List<String> errors = new ArrayList<>();
    @VisibleForTesting
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;
        ButterKnife.bind(this);
        session = new Session(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.useAppLanguage();
        codesGroup.add("12345");
        codesGroup.add("abcde");
        codesGroup.add("azerty");

        downtoleft = AnimationUtils.loadAnimation(this,R.anim.letftoright);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        sign_btn.setAnimation(downtoup);
        cardView.setAnimation(downtoleft);
    }

    @OnClick(R.id.signin)
    public void signin(View view){
        startActivity(new Intent(context, LoginActivity.class));
    }

    @OnClick(R.id.signup)
    public void signup(View view){
        attemptSignup();
    }

    @OnClick(R.id.helpfull)
    public void helper(View View){
        Intent i =new Intent(this, HelperActivity.class);
        i.putExtra("goto","REGISTER_HELPER");
        startActivity(i);
    }

    private void attemptSignup() {
        View focus = null;
        boolean cancel = false;
        errors.clear();
        String code = codeGroupeET.getText().toString();
        String passw = passwordET.getText().toString();
        String repassw = repasswordET.getText().toString();
        String email = emailET.getText().toString();

        if (TextUtils.isEmpty(code) || !isCodeGroupValid(code)){
            focus = codeGroupeET;
            errors.remove(getString(R.string.text0003));
            errors.add(getString(R.string.text0003));
            cancel = true;
            setEditTextBackgrounDrawable(codeGroupeET,true);
        }else{
            errors.remove(getString(R.string.text0003));
            setEditTextBackgrounDrawable(codeGroupeET,false);
        }

        if (TextUtils.isEmpty(email) || !isEmailValid(email)){
            errors.remove(getString(R.string.text0004));
            errors.add(getString(R.string.text0004));
            focus = emailET;
            cancel = true;
            setEditTextBackgrounDrawable(emailET,true);
        }else{
            setEditTextBackgrounDrawable(emailET,false);
            errors.remove(getString(R.string.text0004));
        }

        if (TextUtils.isEmpty(passw) || !isPasswordValid(passw)){
            errors.remove(getString(R.string.text0005));
            errors.add(getString(R.string.text0005));
            focus = passwordET;
            cancel = true;
            setEditTextBackgrounDrawable(passwordET,true);
        }else{
            errors.remove(getString(R.string.text0005));
            setEditTextBackgrounDrawable(passwordET,false);
        }


        if (!passw.equals(repassw)){
            errors.remove(getString(R.string.text0006));
            errors.add(getString(R.string.text0006));
            focus = repasswordET;
            cancel = true;
            setEditTextBackgrounDrawable(repasswordET,true);
        }else{
            errors.remove(getString(R.string.text0006));
            setEditTextBackgrounDrawable(repasswordET,false);
        }


        if (cancel){
            focus.requestFocus();
            displayError();
        }else {
            signupToFirebase(code, email, passw);
        }
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

    private void signupToFirebase(String code, final String email, final String passw) {
        showProgressDialog();
        firebaseAuth.createUserWithEmailAndPassword(email,passw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "account created");
                            Toast.makeText(RegisterActivity.this, "Compte créer avec succès", Toast.LENGTH_SHORT).show();
                            session.saveDataString("uemail",email);
                            session.saveDataString("upass",passw);
                            gotoNextActivity();
                        } else {
                            Log.d(TAG, "register account failed", task.getException());
                            Toast.makeText(RegisterActivity.this,
                                    "account registration failed.",
                                    Toast.LENGTH_SHORT).show();
                            errors.clear();
                            errors.add(task.getException().getMessage());

                            displayError();
                        }
                        //hide progress dialog
                        hideProgressDialog();
                    }
                });
        ////------------------;

    }

    private void gotoNextActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public boolean isEmailValid(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isPasswordValid(String pass){
        return pass.length() >= 4;
    }
    public boolean isCodeGroupValid(String code){
        return codesGroup.contains(code);
    }

    public void setEditTextBackgrounDrawable(EditText editText,boolean error){
        if (error){
            editText.setBackground(ContextCompat.getDrawable(context,R.drawable.edittext_rounded_corners_error));
        }else {
            editText.setBackground(ContextCompat.getDrawable(context,R.drawable.login_edittext_rounded_corners));
        }
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait!");
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
