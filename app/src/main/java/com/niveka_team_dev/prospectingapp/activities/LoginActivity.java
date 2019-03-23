package com.niveka_team_dev.prospectingapp.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.common.ApiError;
import com.niveka_team_dev.prospectingapp.common.RetrofitBuilder;
import com.niveka_team_dev.prospectingapp.handlers.TokenManager;
import com.niveka_team_dev.prospectingapp.handlers.UserHelpers;
import com.niveka_team_dev.prospectingapp.kernels.Session;
import com.niveka_team_dev.prospectingapp.models.AccessToken;
import com.niveka_team_dev.prospectingapp.services.api.AuthApiService;
import com.niveka_team_dev.prospectingapp.ui.CustomProgressDialogOne;
import com.niveka_team_dev.prospectingapp.utilities.Utils;
import com.niveka_team_dev.prospectingapp.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "EmailPasswordAuth";

    @BindView(R.id.email) EditText emailET;
    @BindView(R.id.reset_email) EditText emailResetPassET;
    @BindView(R.id.password) EditText passwordET;
    @BindView(R.id.signup) Button signup_btn;
    @BindView(R.id.signin) Button sign_btn;
    @BindView(R.id.signin_2) Button sign_in_btn_swipe;
    @BindView(R.id.forgotpass_swipe) Button forgot_pass;
    @BindView(R.id.cardview) CardView cardView;
    @BindView(R.id.cardview_reset_pass_email) CardView cardview_reset_pass_email;
    @BindView(R.id.error) TextView error;
    @BindView(R.id.helpfull) Button helper;
    @BindView(R.id.bottom_l) LinearLayout bottom_l;

    public Animation uptodown,downtoup,downtoleft;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userRef,rootRef,channel_users,channels;
    Session session;
    private AuthApiService service;
    private AuthApiService serviceWithAuth;
    private TokenManager tokenManager;
    private Call<AccessToken> call;

    @VisibleForTesting
    public ProgressDialog progressDialog;
    private CustomProgressDialogOne customProgressDialogOne;
    private List<String> errors = new ArrayList<>();

    @SuppressLint({"CommitPrefEdits", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login0);
        ButterKnife.bind(this);
        session = new Session(this);
        service = RetrofitBuilder.createService(AuthApiService.class);
        tokenManager = TokenManager.getInstance(getApplicationContext());
        serviceWithAuth = RetrofitBuilder.createServiceWithAuth(AuthApiService.class,tokenManager);

        if (hasBeenRegister()){
            passwordET.setText(session.retrieveDataString("upass"));
            emailET.setText(session.retrieveDataString("uemail"));
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

        customProgressDialogOne = new CustomProgressDialogOne(this)
                                        .builder()
                                        .setMessage(getString(R.string.text0046));//please wait!

        //setButtonListeners();
        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        downtoleft = AnimationUtils.loadAnimation(this,R.anim.righttoleft);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        bottom_l.setAnimation(downtoup);
        cardView.setAnimation(downtoleft);

    }


    @OnClick(R.id.forgotpass_swipe)
    public void forgotpass(View v){
        cardview_reset_pass_email.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);
        forgot_pass.setVisibility(View.GONE);
        sign_in_btn_swipe.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        errors.clear();
    }

    @OnClick(R.id.helpfull)
    public void helper(View View){
        Intent i =new Intent(this, HelperActivity.class);
        i.putExtra("goto","LOGIN_HELPER");
        startActivity(i);
    }

    @OnClick(R.id.signin_2)
    public void swipe_login(View view){
        cardview_reset_pass_email.setVisibility(View.GONE);
        cardView.setVisibility(View.VISIBLE);
        forgot_pass.setVisibility(View.VISIBLE);
        sign_in_btn_swipe.setVisibility(View.GONE);
    }

    @OnClick(R.id.signup)
    public void signup(View view){
        startActivity(new Intent(this,RegisterActivity.class));
        finish();
    }

    @OnClick(R.id.reset_email_btn)
    public void reset_email_btn(View V){
        final String email = emailResetPassET.getText().toString();
        errors.clear();
        if (!isEmailValid(email)){
            errors.add(getString(R.string.text0004));
            emailResetPassET.setBackground(ContextCompat.getDrawable(this,R.drawable.edittext_rounded_corners_error));;
            displayError();
        }else {
            service.requestResetPassword(email).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    errors.clear();
                    if (response.isSuccessful()){
                        errors.add("Une message de réinitialisation vous a été envoyer par cette email." +
                                "Consultez votre boite email pour suivre les instructions.");
                        error.setTextColor(Color.GREEN);
                    }else {
                        try {
                            errors.add(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    displayError();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    errors.clear();
                    errors.add(ApiError.inThrawable(t));
                    displayError();
                }
            });
        }
    }

    public boolean isEmailValid(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

    public boolean isPasswordValid(String pass){
        return pass.length() >= 3;
    }

    private void attemptLogin() {
        View focus = null;
        boolean cancel = false;
        String em = emailET.getText().toString();
        String pass = passwordET.getText().toString();
        boolean alreadyRegister = !session.retrieveDataString("uemail").equals("");
        emailET.setError(null);
        passwordET.setError(null);

        if (TextUtils.isEmpty(em) /*|| !isEmailValid(em)*/){
            cancel = true;
            focus = emailET;
            errors.remove(getString(R.string.text0010));
            errors.add(getString(R.string.text0010));
            setEditTextBackgrounDrawable(emailET,true);
        }else {
            errors.remove(getString(R.string.text0010));
            setEditTextBackgrounDrawable(emailET,false);
        }


        if (TextUtils.isEmpty(pass) || !isPasswordValid(pass)){
            cancel = true;
            focus = passwordET;
            passwordET.setError(getString(R.string.text0011));
        }else {
            errors.remove(getString(R.string.text0011));
            setEditTextBackgrounDrawable(passwordET,false);
        }

        if (cancel)
        {
            focus.requestFocus();
            displayError();
        }
        else
            handleRegistrationLogin();

    }

    @Override
    public void onStart() {
        super.onStart();
        //showAppropriateOptions();
    }

    public void setEditTextBackgrounDrawable(EditText editText,boolean error){
        if (error){
            editText.setBackground(ContextCompat.getDrawable(this,R.drawable.edittext_rounded_corners_error));
        }else {
            editText.setBackground(ContextCompat.getDrawable(this,R.drawable.login_edittext_rounded_corners));
        }
    }

    public boolean hasBeenRegister(){
        /*String lastEmail = session.retrieveDataString("uemail");
        String lastPassword = session.retrieveDataString("upass");
        return !lastEmail.equals("") && !lastPassword.equals("");*/
        return !tokenManager.getToken().getAccessToken().equals("");
    }

    private void  handleRegistrationLogin(){
        final String email = emailET.getText().toString();
        final String password = passwordET.getText().toString();

        if (!validateEmailPass(email, password)) {
            return;
        }

        //show progress dialog
        showProgressDialog();
        //performLogin(email, password);

        login(email,password);


        //perform login and account creation depending on existence of email in firebase
        //performLoginOrAccountCreation(email, password);
    }

    private void login(final String email, String password) {
        Map<String,Object> map = new HashMap<>();
        map.put("password",password);
        map.put("username",email);
        map.put("rememberMe",true);
       service.authenticate(map)
               .enqueue(new Callback<AccessToken>() {
                   @Override
                   public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                       Log.w(TAG, "onResponse: " + response.body());
                       hideProgressDialog();
                       errors.clear();

                       if (response.isSuccessful()) {
                           tokenManager.saveToken(response.body());
                           getAccount(email);
                       } else {
                           try {
                               errors.add(ApiError.serverMessage(response.errorBody().string()));
                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                       }

                       displayError();
                   }

                   @Override
                   public void onFailure(Call<AccessToken> call, Throwable t) {
                       hideProgressDialog();
                       Log.e(TAG, "onFailure: " + t.getMessage());
                       errors.clear();
                       errors.add(ApiError.inThrawable(t));
                       displayError();
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

                            String uid = task.getResult().getUser().getUid();
                            User user = new User();
                            user.setId(uid);
                            user.setEmail(email);
                            userRef.child(uid).addValueEventListener(userValueEventListener);

                            //gotoNextActivity();
                        } else {
                            Log.e(TAG, "Login fail", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Authentifiation echouée.",
                                    Toast.LENGTH_SHORT).show();

                            errors.clear();
                            errors.add(task.getException().getMessage());
                            displayError();
                        }
                        //hide progress dialog
                        hideProgressDialog();
                        //enable and disable login, logout buttons depending on signin status
                        showAppropriateOptions();
                    }
                });
    }

    ValueEventListener userValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            User user = dataSnapshot.getValue(User.class);
            //Log.e("U1111111",dataSnapshot.getValue().toString());
            if(user!=null){
                //assert user != null;
                //session.saveDataString("user",user.toJson().toString());
                //Log.e("U1111111",user.toString());
                //Log.e("U2222222",user.toJson().toString());
                gotoNextActivity();
            }else {
                errors.clear();
                errors.add("Un probleme est survenu. Svp réessayez!!");
                displayError();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e("onCancelled",databaseError.getMessage());
            errors.clear();
            errors.add("Un probleme est survenu. Svp réessayez!!");
            displayError();
        }
    };

    void gotoNextActivity(){
        startActivity( new Intent(this,MainActivity.class));
        finish();
    }

    private boolean validateEmailPass(String email , String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            emailET.setError("Required.");
            valid = false;
        }/*else if(!email.contains("@")){
            emailET.setError("Not an email id.");
            valid = false;
        }*/ else{
            emailET.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            passwordET.setError("Required.");
            valid = false;
        }else if(password.length() < 4){
            passwordET.setError("Min 6 chars.");
            valid = false;
        }else {
            passwordET.setError(null);
        }

        return valid;
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

    private void showAppropriateOptions(){

    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public void getAccount(String login){
        serviceWithAuth.account().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                hideProgressDialog();
                if (response.isSuccessful()){
                    User user = response.body();
                    if (user!=null){
                        updateToken(user.getId());
                        session.saveDataString(UserHelpers.CURRENT_USER_ID,user.toJson().toString());
                        gotoNextActivity();
                    }
                }else{
                    errors.clear();
                    try {
                        errors.add(ApiError.serverMessage(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    displayError();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideProgressDialog();
                errors.clear();
                errors.add(ApiError.inThrawable(t));
                displayError();
            }
        });
    }

    public void updateToken(String uid){
        Map<String,String> map = new HashMap<>();
        map.put("type","android");
        map.put("token", FirebaseInstanceId.getInstance().getToken());
        serviceWithAuth.updateToken(map,uid).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        Log.e("OK",response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        Log.e("ERROR",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("THROWABLE",t.getMessage());
            }
        });
    }
}
