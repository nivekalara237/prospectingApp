package com.niveka_team_dev.prospectingapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "EmailPasswordAuth";

    @BindView(R.id.email) EditText emailET;
    @BindView(R.id.password) EditText passwordET;
    @BindView(R.id.repassword) EditText repasswordET;
    @BindView(R.id.login_b) Button button;
    @BindView(R.id.password_l2) TextInputLayout loginTL;

    private FirebaseAuth firebaseAuth;
    Session session;

    @VisibleForTesting
    public ProgressDialog progressDialog;

    @SuppressLint({"CommitPrefEdits", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        session = new Session(this);
        firebaseAuth = FirebaseAuth.getInstance();

        if (hasBeenRegister()){
            repasswordET.setVisibility(View.GONE);
            passwordET.setText(session.retrieveDataString("upass"));
            emailET.setText(session.retrieveDataString("uemail"));
            loginTL.setVisibility(View.GONE);
            button.setText("Login");
        }

        setButtonListeners();
    }

    public boolean isEmailValid(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isPasswordValid(String pass){
        return pass.length() >= 4;
    }

    private void setButtonListeners(){
        //login button
        findViewById(R.id.login_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attemptLogin();
                //handleRegistrationLogin();
            }
        });
        //reset password - for unauthenticated user
        findViewById(R.id.rest_password_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResetPasswordEmail();
            }
        });

        //logout button
        findViewById(R.id.logout_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        //Verify email button
        findViewById(R.id.verify_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmailVerificationMsg();
            }
        });

        //update password - for signed in user
        findViewById(R.id.update_password_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });

        //Order functionality to show how to secure firestore data
        //using firebase authentication and firestore security rules
        findViewById(R.id.order_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void attemptLogin() {
        View focus = null;
        boolean cancel = false;
        String em = emailET.getText().toString();
        String pass = passwordET.getText().toString();
        String repass = repasswordET.getText().toString();
        boolean alreadyRegister = !session.retrieveDataString("uemail").equals("");
        emailET.setError(null);
        passwordET.setError(null);
        repasswordET.setError(null);

        if (TextUtils.isEmpty(em)){
            cancel = true;
            focus = emailET;
            emailET.setError("Email obligatoire");
        }
        if (TextUtils.isEmpty(pass)){
            cancel = true;
            focus = passwordET;
            passwordET.setError("Mot de pass obligatoire");
        }

        if (!isEmailValid(em)){
            cancel = true;
            focus = emailET;
            emailET.setError("Email invalide");
        }

        if (!isPasswordValid(pass)){
            cancel = true;
            focus = passwordET;
            passwordET.setError("le mode de passe doit avoir au moins 4 caracteres");
        }

        if (!alreadyRegister && !repass.equals(pass)){
            cancel = true;
            focus = repasswordET;
            repasswordET.setError("Vous saisi un mot de passe different");
        }

        if (cancel)
            focus.requestFocus();
        else
            handleRegistrationLogin();

    }

    @Override
    public void onStart() {
        super.onStart();
        //showAppropriateOptions();
    }

    public boolean hasBeenRegister(){
        String lastEmail = session.retrieveDataString("uemail");
        String lastPassword = session.retrieveDataString("upass");
        return !lastEmail.equals("") && !lastPassword.equals("");
    }

    private void  handleRegistrationLogin(){
        final String email = emailET.getText().toString();
        final String password = passwordET.getText().toString();

        if (!validateEmailPass(email, password)) {
            return;
        }

        //show progress dialog
        showProgressDialog();

        //perform login and account creation depending on existence of email in firebase
        performLoginOrAccountCreation(email, password);
    }
    private void performLoginOrAccountCreation(final String email, final String password){
        firebaseAuth.fetchProvidersForEmail(email).addOnCompleteListener(
                this, new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "checking to see if user exists in firebase or not");
                            ProviderQueryResult result = task.getResult();

                            if(result != null && result.getProviders()!= null
                                    && result.getProviders().size() > 0){
                                Log.d(TAG, "User exists, trying to login using entered credentials");
                                performLogin(email, password);
                            }else{
                                Log.d(TAG, "User doesn't exist, creating account");
                                registerAccount(email, password);
                            }
                        } else {
                            Log.w(TAG, "User check failed", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "There is a problem, please try again later.",
                                    Toast.LENGTH_SHORT).show();

                        }
                        //hide progress dialog
                        hideProgressDialog();
                        //enable and disable login, logout buttons depending on signin status
                        showAppropriateOptions();
                    }
                });
    }
    private void performLogin(final String email, final String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "login success",Toast.LENGTH_SHORT).show();
                            session.saveDataString("uemail",email);
                            session.saveDataString("upass",password);
                            gotoNextActivity();
                        } else {
                            Log.e(TAG, "Login fail", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Authentifiation echouée.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //hide progress dialog
                        hideProgressDialog();
                        //enable and disable login, logout buttons depending on signin status
                        showAppropriateOptions();
                    }
                });
    }

    void gotoNextActivity(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
    private void registerAccount(final String email, final String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "account created");
                            Toast.makeText(LoginActivity.this, "Compte créer avec succès", Toast.LENGTH_SHORT).show();
                            session.saveDataString("uemail",email);
                            session.saveDataString("upass",password);
                            gotoNextActivity();
                        } else {
                            Log.d(TAG, "register account failed", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "account registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //hide progress dialog
                        hideProgressDialog();
                        //enable and disable login, logout buttons depending on signin status
                        showAppropriateOptions();
                    }
                });
    }
    private boolean validateEmailPass(String email , String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            emailET.setError("Required.");
            valid = false;
        }else if(!email.contains("@")){
            emailET.setError("Not an email id.");
            valid = false;
        } else{
            emailET.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            passwordET.setError("Required.");
            valid = false;
        }else if(password.length() < 6){
            passwordET.setError("Min 6 chars.");
            valid = false;
        }else {
            passwordET.setError(null);
        }

        return valid;
    }
    private boolean validateResetPassword(String password) {
        boolean valid = true;
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            valid = false;
        }
        return valid;
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
    private void showAppropriateOptions(){
        /*hideProgressDialog();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            findViewById(R.id.login_items).setVisibility(View.GONE);
            findViewById(R.id.logout_items).setVisibility(View.VISIBLE);

            findViewById(R.id.verify_b).setEnabled(!user.isEmailVerified());
        } else {
            findViewById(R.id.login_items).setVisibility(View.VISIBLE);
            findViewById(R.id.logout_items).setVisibility(View.GONE);
        }*/
        //gotoNextActivity();
    }

    private void sendEmailVerificationMsg() {
        findViewById(R.id.verify_b).setEnabled(false);

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        findViewById(R.id.verify_b).setEnabled(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Verification email has been sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Error in sending verification email",
                                    task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    //non-singed in user reset password email
    private void sendResetPasswordEmail() {
        final String email = ((EditText) findViewById(R.id.reset_password_email))
                .getText().toString();
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Reset password code has been emailed to "
                                            + email,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Error in sending reset password code",
                                    task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "There is a problem with reset password, try later.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updatePassword() {

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        final String newPwd = ((EditText) findViewById(R.id.update_password_t)).getText().toString();
        if(!validateResetPassword(newPwd)){
            Toast.makeText(LoginActivity.this,
                    "Invalid password, please enter valid password",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        user.updatePassword(newPwd)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Password has been updated",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Error in updating passowrd",
                                    task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Failed to update passwrod.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void logOut() {
        firebaseAuth.signOut();
        showAppropriateOptions();
    }
    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}
