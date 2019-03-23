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
import com.google.firebase.auth.UserProfileChangeRequest;
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
import com.niveka_team_dev.prospectingapp.models.Channel;
import com.niveka_team_dev.prospectingapp.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "EmailPasswordAuth";
    private static Pattern userNamePattern = Pattern.compile("^[a-z0-9_-]{6,14}$");

    @BindView(R.id.email) EditText emailET;
    @BindView(R.id.code_enterprise) EditText codeGroupeET;
    @BindView(R.id.password) EditText passwordET;
    @BindView(R.id.username) EditText usernameET;
    @BindView(R.id.repassword) EditText repasswordET;
    @BindView(R.id.signup) Button signup_btn;
    @BindView(R.id.signin) Button sign_btn;
    @BindView(R.id.cardview) CardView cardView;
    @BindView(R.id.error) TextView error;

    public Animation uptodown,downtoup,downtoleft;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference prospectingRef;
    private DatabaseReference channel_user_ref,channels;
    private DatabaseReference rootref;
    Session session;
    Context context;
    private List<String> codesGroup = new ArrayList<>();
    List<String> errors = new ArrayList<>();
    @VisibleForTesting
    public ProgressDialog progressDialog;
    private CustomProgressDialogOne customProgressDialogOne;
    private AuthApiService service;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;
        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(context);
        service = RetrofitBuilder.createService(AuthApiService.class);
        session = new Session(this);

        customProgressDialogOne = new CustomProgressDialogOne(this)
                .builder()
                .setMessage(getString(R.string.text0046));

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
        String uname = usernameET.getText().toString();
        String passw = passwordET.getText().toString();
        String repassw = repasswordET.getText().toString();
        String email = emailET.getText().toString();

        if (TextUtils.isEmpty(uname) || !isUnameValid(uname)){
            focus = usernameET;
            errors.remove(getString(R.string.text0003));
            errors.add(getString(R.string.text0003));
            cancel = true;
            setEditTextBackgrounDrawable(usernameET,true);
        }else{
            errors.remove(getString(R.string.text0003));
            setEditTextBackgrounDrawable(usernameET,false);
        }

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
            signupToServer(uname,code, email, passw);
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

    private void signupToServer(final String uname,final String code, final String email, final String passw) {
        showProgressDialog();
        Map<String,Object> map = new HashMap<>();
        map.put("langKey","en");
        map.put("password",passw);
        map.put("email",email);
        map.put("login",uname);
        map.put("androidFcmToken", FirebaseInstanceId.getInstance().getToken());
        service.createAccount(map,code).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                hideProgressDialog();
                errors.clear();
                if (response.isSuccessful()){
                    User user = response.body();
                    Toasty.success(context,getString(R.string.text0058),Toast.LENGTH_LONG,true).show();
                    loginUer(user);
                }else {
                    if (response.code() == 500) {
                        try {
                            errors.add(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if(response.code() == 400){
                        try {
                            errors.add(ApiError.serverMessage(response.errorBody().string()));
                            Log.e("ERROR",response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        errors.add(ApiError.message(response.code()));
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

    private void loginUer(final User user) {
        Map<String,Object> map = new HashMap<>();
        map.put("password",passwordET.getText().toString());
        map.put("username",user.getEmail());
        map.put("rememberMe",true);
        service.authenticate(map)
                .enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        Log.w(TAG, "onResponse: " + response.body());
                        hideProgressDialog();

                        if (response.isSuccessful()) {
                            tokenManager.saveToken(response.body());
                            session.saveDataString(UserHelpers.CURRENT_USER_ID,user.toJson().toString());
                            gotoNextActivity();
                        } else {
                            gotoLoginPage();
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        hideProgressDialog();
                        Log.e(TAG, "onFailure: " + t.getMessage());
                        gotoLoginPage();
                    }
                });
    }

    private void gotoLoginPage() {
        startActivity(new Intent(this,LoginActivity.class));
        finish();
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
        return true;
    }
    public boolean isUnameValid(String uname){
        Matcher mtch = userNamePattern.matcher(uname);
        if (mtch.matches()) {
            return true;
        }
        return false;
    }

    public void setEditTextBackgrounDrawable(EditText editText,boolean error){
        if (error){
            editText.setBackground(ContextCompat.getDrawable(context,R.drawable.edittext_rounded_corners_error));
        }else {
            editText.setBackground(ContextCompat.getDrawable(context,R.drawable.login_edittext_rounded_corners));
        }
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

}
