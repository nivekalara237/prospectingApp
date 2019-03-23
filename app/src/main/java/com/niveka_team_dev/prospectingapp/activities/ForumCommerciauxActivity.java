package com.niveka_team_dev.prospectingapp.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.util.ByteBufferBackedOutputStream;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.adapters.AttachmentMessageAdapter;
import com.niveka_team_dev.prospectingapp.kernels.GlideApp;
import com.niveka_team_dev.prospectingapp.listeners.OnFileDownloadButtomClickedListener;
import com.niveka_team_dev.prospectingapp.listeners.OnRemoveItemAttachmentListener;
import com.niveka_team_dev.prospectingapp.models.Attachment;
import com.niveka_team_dev.prospectingapp.models.Channel;
import com.niveka_team_dev.prospectingapp.ui.ToolbarHandler;
import com.niveka_team_dev.prospectingapp.utilities.ImageUtils;
import com.niveka_team_dev.prospectingapp.utilities.Utils;
import com.niveka_team_dev.prospectingapp.adapters.ForumComMessageAdapter;
import com.niveka_team_dev.prospectingapp.kernels.BaseActivity;
import com.niveka_team_dev.prospectingapp.models.Message;
import com.niveka_team_dev.prospectingapp.models.User;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import me.echodev.resizer.Resizer;

public class ForumCommerciauxActivity extends BaseActivity implements OnFileDownloadButtomClickedListener,
        OnRemoveItemAttachmentListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String CHANNEL_ID = "channel_id";
    private static final int imagePickRequestCode = 7840;

    @BindView(R.id.recyclerView) FastScrollRecyclerView recyclerView;
    @BindView(R.id.send) ImageButton sendButton;
    @BindView(R.id.emoji) ImageButton emojiButton;
    @BindView(R.id.attachment) ImageButton attachButton;
    @BindView(R.id.new_message) EmojiconEditText new_message;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.message_swipe_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerViewFile) RecyclerView recyclerViewFile;
    @BindView(R.id.fileFragmentLayout) FrameLayout fileFragmentLayout;
    private  View rootView;
    private  EmojIconActions emojIcon;

    //New Solution
    private int itemPos = 0;
    private String mLastKey = "";
    private String mPrevKey = "";
    private int mCurrentPage = 1;
    private Context context;
    private Channel currentChannel ;
    private List<User> usersForum = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private ForumComMessageAdapter adapter;
    private DatabaseReference rootRef,messageChannelRef,usersRef;
    boolean isVisibleEmoji = false;
    private ToolbarHandler toolbarHandler;
    private List<Attachment> files = new ArrayList<>();
    private AttachmentMessageAdapter attachmentAdapter;
    private FirebaseFirestore firestore;
    private CollectionReference entrepriseRef,messageCollecRef,channelCollecRef;
    private DocumentReference channelRef;
    private FirebaseStorage firebaseStoragestorage;
    private StorageReference storageRef;
    private StorageReference forumFileRef;
    private UploadTask uploadTask;
    private FirebaseAuth mAuth;
    private FirebaseUser fireUser;

    private String FIRED_DOWNLOAD_DIR = Environment.getExternalStoragePublicDirectory
            ("FIRE-D-APP").getPath();
    public boolean createDirIfNotExists(String path) {
        boolean ret = true;

        File file = new File(Environment.getExternalStorageDirectory(), path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Image folder");
                ret = false;
            }
        }
        return ret;
    }
    @Override
    protected void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        state.putSerializable(CHANNEL_ID,currentChannel);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_commerciaux);
        ButterKnife.bind(this);
        context = this;
        fileFragmentLayout.setVisibility(View.GONE);
        Bundle extras = getIntent().getExtras();
        if (savedInstanceState!=null && savedInstanceState.containsKey(CHANNEL_ID)){
            currentChannel = (Channel) savedInstanceState.getSerializable(CHANNEL_ID);
        }

        if (extras!=null && extras.containsKey(CHANNEL_ID)){
            currentChannel = (Channel) extras.getSerializable(CHANNEL_ID);
        }

        if (currentChannel==null){
            Toast.makeText(this,"Select a channel please",Toast.LENGTH_SHORT).show();
            onBackPressed();
            finish();
        }

        mAuth = FirebaseAuth.getInstance();
        firebaseStoragestorage = FirebaseStorage.getInstance();
        storageRef = firebaseStoragestorage.getReference();
        forumFileRef = storageRef.child("forum");
        firestore = FirebaseFirestore.getInstance();
        entrepriseRef = firestore.collection("enterprise_"+currentUser.getEntrepriseId());
        channelRef = entrepriseRef.document("channel_"+currentChannel.getId());
        messageCollecRef = firestore.collection("enterprise_"+currentChannel.getEntrepriseId())
                .document("channel_"+currentChannel.getId())
                .collection("messages");

        //Log.e("USER",currentUser.toString());

        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayShowCustomEnabled();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        toolbarHandler = new ToolbarHandler(this,toolbar);
        toolbarHandler
                .initUI()
                .setBadgeContent("0")
                .setTitle(getString(R.string.forum_com_title))
                .build();

        //end toolbar

        rootView = findViewById(R.id.rootView);
        emojIcon = new EmojIconActions(
                this,
                rootView,
                new_message,
                emojiButton,
                getResources().getString(R.color.colorApp3),
                getResources().getString(R.color.lightorangedark),
                getResources().getString(R.color.lightorangedark)
        );
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(
                R.drawable.ic_keyboard_32dp,
                R.drawable.smiley);
        /*emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e(TAG, "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e(TAG, "Keyboard closed");
            }
        });*/

        //message adaptation

        adapter = new ForumComMessageAdapter(context,messages,currentUser.getId());
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new  LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        //layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        //messageChannelRef.addChildEventListener(childEventListener);
        usersForum.add(currentUser);
        loadMessages();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushNewMessage();
            }
        });
        mRefreshLayout.setProgressBackgroundColor(R.color.colorApp3);
        mRefreshLayout.setProgressBackgroundColorSchemeColor(R.color.colorWrite);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage++;
                itemPos = 0;
                loadMoreMessages();
            }
        });

        //file attachs adaptation
        files = new ArrayList<>();
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        attachmentAdapter = new AttachmentMessageAdapter(files,this);
        recyclerViewFile.setLayoutManager(layoutManager2);
        recyclerViewFile.setAdapter(attachmentAdapter);
        //recyclerViewFile.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        fireUser= mAuth.getCurrentUser();
        //updateUI(currentUser);

        if (fireUser==null){
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInAnonymously:success");
                                fireUser = mAuth.getCurrentUser();
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInAnonymously:failure", task.getException());
                                Toast.makeText(ForumCommerciauxActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                fireUser = null;
                            }

                            // ...
                        }
                    });
        }

        createDirIfNotExists(getString(R.string.firedapp_directory));
    }

    public void pushNewMessage(){
        //Toast.makeText(this,"GGGGGGGGGGGGG",Toast.LENGTH_SHORT).show();
        fileFragmentLayout.animate()
                .translationY(fileFragmentLayout.getHeight())
                .setDuration(500)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        fileFragmentLayout.setVisibility(View.GONE);
                    }
                });
        String msg = new_message.getText().toString();
        if (!TextUtils.isEmpty(msg)){
            DocumentReference doc = messageCollecRef.document();
            String key = doc.getId();
            Message message = new Message();
            //Log.e("RANDOM",Utils.ramdomString(20));
            //Toast.makeText(this,Utils.ramdomString(20),Toast.LENGTH_SHORT).show();
            message.setId(Utils.ramdomString(20));
            message.setContenu(msg);
            message.setChannelId(currentUser.getChannelID());
            message.setUserId(currentUser.getId());
            //message.setSenderName(currentUser.getLogin());
            message.setUser(currentUser);
            message.setTime(Utils.currentJodaDateStr());
            message.setKey(key);
            Map<String,Object> msgMap = message.toMap();
            //Map<String, Object> childUpdates = new HashMap<>();
            //childUpdates.put(key,msgMap);
            doc.set(msgMap)
                    //messageCollecRef.add(childUpdates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Toast.makeText(context,"Message added",Toast.LENGTH_SHORT).show();
                            new_message.setText("");
                            scrolldToBottom();
                            emojIcon.closeEmojIcon();
                            if(!files.isEmpty()){
                                pushFiles();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toasty.error(context,"Message non envoyé", Toast.LENGTH_SHORT,false).show();
                        }
                    });
            Utils.hideKeyboard(this);
            new_message.setText("");
        }else{
            if (!files.isEmpty()){
                pushFiles();
            }
        }

    }

    private void pushFiles() {
        for (Attachment attach:files){
            uploadFileFromLocale(attach);
        }
    }


    public User searchInListUser(String id){
        User res = null;
        for (User u:usersForum){
            if (u.getId().equals(id))
            {
                res = u;
                break;
            }
        }
        return res;
    }

    public void scrolldToBottom(){
        recyclerView.scrollToPosition(messages.size());
    }


    private void loadMessages() {

        // Source can be CACHE, SERVER, or DEFAULT.
        Source source = Source.CACHE;

        messageCollecRef.get(source).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    Log.e("IN_CACHE",task.toString());
                }
            }
        });

        messageCollecRef
                .orderBy("key")
                .limit(Utils.TOTAL_MESSAGE_ITEMS_TO_LOAD)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (!task.getResult().isEmpty()){
                        List<Message> ms = new ArrayList<>();
                        for (DocumentSnapshot doc:task.getResult().getDocuments()){
                            Message m = new Message();
                            if (doc.contains("id"))
                                m.setId(doc.getString("id"));

                            if (doc.contains("key") && doc.get("key")!=null)
                                m.setKey(doc.getString("key"));

                            if (doc.contains("contenu") && doc.get("contenu")!=null)
                                m.setContenu(doc.getString("contenu"));

                            if (doc.contains("channelId") && doc.get("channelId")!=null)
                                m.setChannelId(doc.getString("channelId"));

                            if (doc.contains("userId") && doc.get("userId")!=null)
                                m.setUserId(doc.getString("userId"));

                            if (doc.contains("time") && doc.get("time")!=null)
                                m.setTime(doc.getString("time"));

                            if (doc.contains("createdtAt") && doc.get("createdtAt")!=null)
                                m.setCreatedAt(doc.getString("createdtAt"));

                            if (doc.contains("listSeen") && doc.get("listSeen")!=null){
                                String[]  gti = (String[]) doc.get("listSeen");
                                m.setListSeen(gti);
                            }

                            if (doc.contains("user") && doc.get("user")!=null)
                            {
                                m.setUser(doc.get("user",User.class));
                            }

                            if (doc.contains("attachment") && doc.get("attachment")!=null)
                            {
                                Attachment attch = doc.get("attachment",Attachment.class);
                                m.setAttachment(attch);
                            }
                            ms.add(m);
                            adapter.addEntry(m);
                        }

                    }
                }else{
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void loadMoreMessages() {

    }
    public void markAsSeen(Message message){
        message.setVu(true);
        Map<String,Object> msgMap = message.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(message.getKey(),msgMap);
        adapter.updateEntry(message);
        messages.set(messages.indexOf(adapter.searchInListMessage(message.getKey())),message);
        messageChannelRef.updateChildren(childUpdates);
    }

    @OnClick(R.id.attachment)
    public void pickFile(View view){
        files.clear();
        Pix.start(this,imagePickRequestCode,5);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        if (resultCode == Activity.RESULT_OK && requestCode == imagePickRequestCode) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Log.e("IMAGES",returnValue.toString());
            for (String im:returnValue){
                File f = new File(im);
                Uri imageUri = Uri.fromFile(f);
                Bitmap d = new BitmapDrawable(context.getResources(), f.getAbsolutePath()).getBitmap();
                Bitmap scaled = com.fxn.utility.Utility.getScaledBitmap(512, d);
                Attachment attachment = new Attachment();
                attachment.setUri(imageUri);
                attachment.setPath(im);
                attachment.setLength(scaled.getByteCount());
                attachment.setImage(true);
                attachment.setName(f.getName());
                attachment.setType(Utils.getMimeType(context,imageUri));
                files.add(attachment);
                //attachmentAdapter.addItemAttachment(attachment);
            }

            if (!files.isEmpty()){
                fileFragmentLayout.setVisibility(View.VISIBLE);
            }
        }else{
            fileFragmentLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, imagePickRequestCode,5);
                } else {
                    Toast.makeText(this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void removeAttachment(Attachment attachment) {
        Toast.makeText(this,"Removing "+attachment.getName(),Toast.LENGTH_SHORT).show();
        attachmentAdapter.rempoveAttach(attachment);
        this.files.remove(attachment);
        if (this.files.size()==0){
            fileFragmentLayout.setVisibility(View.GONE);
        }
    }

    public void uploadFileStream(File file,Attachment attachment) throws FileNotFoundException {
        InputStream stream = new FileInputStream(file);
        StorageReference mountainsRef = forumFileRef.child(attachment.getName());
        uploadTask = mountainsRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toasty.error(context,"Upload failed - "+exception.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(context,"file upload successfull",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void uploadFileFromLocale(final Attachment attachment){
        File f = new File(attachment.getPath());
        File resizedImage=null;
        try {
            resizedImage = new Resizer(this)
                    .setTargetLength(1080)
                    .setSourceImage(f)
                    .getResizedFile();
        } catch (IOException e) {
            resizedImage = f;
            e.printStackTrace();
        }


        final Uri file = Uri.fromFile(resizedImage);
        StorageReference riversRef = forumFileRef.child(file.getLastPathSegment());
        uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        final File finalResizedImage = resizedImage;
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toasty.error(context,"Upload failed - "+exception.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                //Log.e("META1",taskSnapshot.getStorage().getDownloadUrl().getResult().getPath());
                Toast.makeText(context,"file upload successfull",Toast.LENGTH_LONG).show();
                //taskSnapshot.getUploadSessionUri().getLastPathSegment();
                StorageMetadata meta = taskSnapshot.getMetadata();
                Message message = new Message();
                attachment.setName(meta.getName());
                attachment.setPath(meta.getPath());
                attachment.setLength(finalResizedImage.length());
                message.setChannelId(currentUser.getChannelID());
                message.setUserId(currentUser.getId());
                //message.setSenderName(currentUser.getLogin());
                message.setUser(currentUser);
                message.setTime(Utils.currentJodaDateStr());
                message.setAttachment(attachment);
                DocumentReference doc = messageCollecRef.document();
                String key = doc.getId();
                message.setKey(key);
                Map<String,Object> map = message.toMap();

                doc.set(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(context,"Message added",Toast.LENGTH_SHORT).show();
                                new_message.setText("");
                                scrolldToBottom();
                                emojIcon.closeEmojIcon();
                                files.clear();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                                Toasty.error(context,"Message non envoyé", Toast.LENGTH_SHORT,false).show();
                            }
                        });
                Utils.hideKeyboard(ForumCommerciauxActivity.this);
                new_message.setText("");

            }
            // Observe state change events such as progress, pause, and resume
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");

            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");

            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                System.out.println("Upload is completed - ");

            }
        });
    }

    public void downloadFileFromFbStorage(Attachment attch, final ImageView imageView, final LinearLayout errorLayout, final TextView indicator, final ProgressBar progressBar){
        StorageReference downloadRef = storageRef.child(attch.getPath());
        indicator.setVisibility(View.VISIBLE);
        final File fileNameOnDevice = new File(FIRED_DOWNLOAD_DIR+"/"+attch.getName());

        downloadRef.getFile(fileNameOnDevice).addOnSuccessListener(
                new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("File ", "downloaded the file");
                        imageView.setImageBitmap(ImageUtils.fileToBitmap(context,fileNameOnDevice));
                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("File RecylerView", "Failed to download the file");
                    Toast.makeText(context,
                            "Couldn't be downloaded",
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                    indicator.setVisibility(View.GONE);
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                //System.out.println("Upload is " + progress + "% done");
                String v = Math.ceil(progress)+" / 100%";
                indicator.setText(v);
            }
        });

    }

//
//    public void uploadFileFromLocale(Attachment attachment, final ProgressBar progressBar, final TextView indicator, final RelativeLayout error, ImageView imageView){
//        Uri file = Uri.fromFile(new File(attachment.getPath()));
//        StorageReference riversRef = forumFileRef.child(file.getLastPathSegment());
//        uploadTask = riversRef.putFile(file);
//
//        // Register observers to listen for when the download is done or if it fails
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//                Toasty.error(context,"Upload failed - "+exception.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                // ...
//                Toast.makeText(context,"file upload successfull",Toast.LENGTH_LONG).show();
//                //taskSnapshot.getUploadSessionUri().getLastPathSegment();
//            }
//            // Observe state change events such as progress, pause, and resume
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @SuppressLint("DefaultLocale")
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                System.out.println("Upload is " + progress + "% done");
//                indicator.setText(String.format("%f/100",progress));
//            }
//        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
//                System.out.println("Upload is paused");
//                indicator.setText("Upload is paused");
//                progressBar.setVisibility(View.GONE);
//                error.setVisibility(View.VISIBLE);
//                ((TextView)error.getChildAt(1)).setText(String.valueOf(taskSnapshot.getBytesTransferred()));
//            }
//        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                System.out.println("Upload is completed - ");
//                indicator.setText("");
//                indicator.setVisibility(View.GONE);
//            }
//        });
//    }

    @Override
    public void onFileDownloadClicked(Message message, View... imgView) {
        ImageView attachement = (ImageView) imgView[0];
        TextView progressIndicator = (TextView) imgView[1];
        ProgressBar progressBar = (ProgressBar) imgView[2];
        LinearLayout errorLayout = (LinearLayout) imgView[3];
        errorLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Log.e("CLICKED",message.toString());

        downloadFileFromFbStorage(message.getAttachment(),attachement,errorLayout,progressIndicator,progressBar);
    }

    @Override
    public void onFileDownloadRetryClicked(View imgView, Attachment attch) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
            if (!files.isEmpty()){
                attachmentAdapter.removeAttachments(files);
                fileFragmentLayout.setVisibility(View.GONE);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
