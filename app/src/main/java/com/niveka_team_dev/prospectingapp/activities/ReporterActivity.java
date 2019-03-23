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
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.niveka_team_dev.prospectingapp.adapters.AttachmentAdapter;
import com.niveka_team_dev.prospectingapp.fragments.AddCompteRenduSuiviBottomSheetFragment;
import com.niveka_team_dev.prospectingapp.fragments.AttachmentOptionsBottomSheetDialogFragment;
import com.niveka_team_dev.prospectingapp.handlers.LocationHelper;
import com.niveka_team_dev.prospectingapp.listeners.OnRemoveItemAttachmentListener;
import com.niveka_team_dev.prospectingapp.models.Attachment;
import com.niveka_team_dev.prospectingapp.services.LocationMonitoringService;
import com.niveka_team_dev.prospectingapp.ui.CustomProgressDialogOne;
import com.niveka_team_dev.prospectingapp.utilities.Utils;
import com.niveka_team_dev.prospectingapp.kernels.BaseActivity;
import com.niveka_team_dev.prospectingapp.models.Rapport;
import com.niveka_team_dev.prospectingapp.models.User;

import net.ralphpina.permissionsmanager.PermissionsManager;
import net.ralphpina.permissionsmanager.PermissionsResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import rx.functions.Action1;
import zendesk.belvedere.BelvedereUi;
import zendesk.belvedere.ImageStream;
import zendesk.belvedere.MediaResult;

public class ReporterActivity extends BaseActivity implements OnRemoveItemAttachmentListener,ImageStream.Listener, ValueEventListener,RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.contenu) EditText contenu;
    @BindView(R.id.email) EditText emailsCopies;
    @BindView(R.id.email_l) TextInputLayout emailLayout;
    @BindView(R.id.object) EditText objet;
    @BindView(R.id.error) TextView error;
    @BindView(R.id.nb_attachment) TextView nbAttachment;
    @BindView(R.id.radiogroup) RadioGroup radioGroup;
    @BindView(R.id.radio_technique) RadioButton radioTechn;
    @BindView(R.id.radio_generale) RadioButton radioGen;
    @BindView(R.id.save) Button save;
    @BindView(R.id.recyclerViewFile) RecyclerView recyclerViewFile;

    private Location gpsLocation = null;
    Context context;
    private DatabaseReference rootRef,reportRef,usersRef;
    private int radioCheck = 1;
    private List<User> users = new ArrayList<>();
    private List<Attachment> attachments = new ArrayList<>();
    List<String> errors = new ArrayList<>();
    private ImageStream imageStream;
    private AttachmentAdapter attachmentAdapter;
    ChildEventListener userEvent = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (dataSnapshot.exists()){
                User u = dataSnapshot.getValue(User.class);
                users.add(u);
                //Log.e("USERS",u.toString());
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
        imageStream = BelvedereUi.install(this);
        imageStream.addListener(this);

        context = this;
        onAskForLocation();
        rootRef = FirebaseDatabase.getInstance().getReference();
        reportRef = rootRef.child(Utils.FIREBASE_DB_NAME).child("reportings");
        usersRef = rootRef.child(Utils.FIREBASE_DB_NAME).child("users");
        usersRef.addChildEventListener(userEvent);
        radioGroup.setOnCheckedChangeListener(this);
        customProgressDialogOne = new CustomProgressDialogOne(this)
                .builder()
                .setMessage(getString(R.string.text0046));
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

        nbAttachment.setText("(0)");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        attachmentAdapter = new AttachmentAdapter(attachments,this);
        recyclerViewFile.setLayoutManager(layoutManager);
        recyclerViewFile.setAdapter(attachmentAdapter);
        recyclerViewFile.setVisibility(View.GONE);
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
            errors.add(getString(R.string.text0049));
        }
        if (TextUtils.isEmpty(cont)){
            focus = contenu;
            cancel = true;
            errors.add(getString(R.string.text0050));
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
        rapport.setNomCom(currentUser.getLogin());
        rapport.setEmail(currentUser.getEmail());
        rapport.setDate(Utils.currentJodaDateStr());
        Map<String,Object> map = rapport.toMap();
        Map<String,Object> mapUpdate = new HashMap<>();
        mapUpdate.put(key,map);
        reportRef.updateChildren(mapUpdate, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError!=null){
                    errors.clear();
                    errors.add(databaseError.getMessage());
                    displayError();
                    hideProgressDialog();
                }else{
                    hideProgressDialog();
                    Toasty.success(context,getString(R.string.text0048),Toast.LENGTH_SHORT,false).show();
                    Intent i = new Intent(context,MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
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
            if (field.equals("id"))
                fields.add(u.getId());
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
            //Log.e("LOC",intent.getParcelableExtra("loc").toString());
        }
    };

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocationeReceiver);
        super.onPause();
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R.id.attachment_btn)
    public void addNew(View view){
        /*AttachmentOptionsBottomSheetDialogFragment fragment = AttachmentOptionsBottomSheetDialogFragment.newInstance();
        fragment.show(getSupportFragmentManager(),"attachment_bottom_sheet_fregment");*/
        BelvedereUi.imageStream(this)
                .withCameraIntent()
                //.withCameraIntent()
                .withDocumentIntent("*/*", true)
                .showPopup(this);
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
    public void onAskForLocation() {
        if (!PermissionsManager.get().isLocationGranted()) {
            PermissionsManager.get()
                    .requestLocationPermission()
                    .subscribe(new Action1<PermissionsResult>() {
                        @Override
                        public void call(PermissionsResult permissionsResult) {

                            Toast.makeText(ReporterActivity.this,
                                    permissionsResult.isGranted() ? getString(R.string.text0056) : getString(R.string.text0057),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        }
    }

    @Override
    public void onDismissed() {

    }

    @Override
    public void onVisible() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMediaSelected(List<MediaResult> mediaResults) {
        List<Attachment> attachss = new ArrayList<>();
        for (MediaResult media:mediaResults){
            Attachment attch = new Attachment();
            attch.setName(media.getName());
            attch.setLength(media.getSize());
            attch.setPath(media.getUri().getPath());
            attch.setType(media.getMimeType());

            if (media.getMimeType().toLowerCase().contains("image/"))
                attch.setImage(true);
            else
                attch.setImage(false);

            attachss.add(attch);
        }

        if (attachss.size()!=0){
            recyclerViewFile.setVisibility(View.VISIBLE);
            nbAttachment.setText("("+ attachss.size() +")");
            attachments = attachss;
            attachmentAdapter.addAttachments(attachss);
        }
    }

    @Override
    public void onMediaDeselected(List<MediaResult> mediaResults) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void removeAttachment(Attachment attachment) {
        Toast.makeText(this,"Removing "+attachment.getName(),Toast.LENGTH_SHORT).show();
        attachmentAdapter.rempoveAttach(attachment);
        this.attachments.remove(attachment);
        nbAttachment.setText("("+this.attachments.size()+")");
        if (this.attachments.size()==0){
            recyclerViewFile.setVisibility(View.GONE);
        }
    }
}
