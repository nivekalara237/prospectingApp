package com.niveka_team_dev.prospectingapp.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.Utils;
import com.niveka_team_dev.prospectingapp.adapters.ForumComMessageAdapter;
import com.niveka_team_dev.prospectingapp.kernels.BaseActivity;
import com.niveka_team_dev.prospectingapp.models.Message;
import com.niveka_team_dev.prospectingapp.models.User;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class ForumCommerciauxActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.recyclerView) FastScrollRecyclerView recyclerView;
    @BindView(R.id.send) ImageButton sendButton;
    @BindView(R.id.emoji) ImageButton emojiButton;
    @BindView(R.id.attachment) ImageButton attachButton;
    @BindView(R.id.new_message) EmojiconEditText new_message;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.message_swipe_layout) SwipeRefreshLayout mRefreshLayout;
    View rootView;
    EmojIconActions emojIcon;

    //New Solution
    private int itemPos = 0;
    private String mLastKey = "";
    private String mPrevKey = "";
    private int mCurrentPage = 1;
    private Context context;
    private List<User> usersForum = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private ForumComMessageAdapter adapter;
    private DatabaseReference rootRef,messageChannelRef,usersRef;
    boolean isVisibleEmoji = false;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_commerciaux);
        ButterKnife.bind(this);
        context = this;
        rootRef = FirebaseDatabase.getInstance().getReference();
        messageChannelRef = rootRef.child(Utils.FIREBASE_DB_NAME).child("forum-"+currentUser.getChannelID());
        usersRef = rootRef.child(Utils.FIREBASE_DB_NAME).child("users");

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
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e(TAG, "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e(TAG, "Keyboard closed");
            }
        });


        adapter = new ForumComMessageAdapter(context,messages,currentUser.getId());
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new  LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        //messageChannelRef.addChildEventListener(childEventListener);
        usersForum.add(currentUser);
        usersRef.addValueEventListener(usersEvent);
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
    }

    public void pushNewMessage(){
        String msg = new_message.getText().toString();
        String key = messageChannelRef.push().getKey();
        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setBody(msg);
        message.setChannelID(currentUser.getChannelID());
        message.setSenderId(currentUser.getId());
        message.setSenderName(currentUser.getUsername());
        message.setTime(Utils.currentJodaDateStr());
        message.setKey(key);
        Map<String,Object> msgMap = message.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key,msgMap);
        messageChannelRef.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError==null){
                    //Toasty.success(context,"Message envoyé",Toast.LENGTH_SHORT,false);
                    new_message.setTag("");
                }else{

                }
            }
        });
        new_message.setTag("");
    }

    ValueEventListener usersEvent = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot data:dataSnapshot.getChildren()){
                if (data.exists()){
                    User u = dataSnapshot.getValue(User.class);
                    usersForum.add(u);
                }
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

//    ChildEventListener childEventListener = new ChildEventListener() {
//        @Override
//        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//            Log.e("DATA", dataSnapshot.getValue().toString());
//            if (dataSnapshot.exists()){
//                Message message = dataSnapshot.getValue(Message.class);
//                message.setKey(dataSnapshot.getKey());
//                message.setSender(searchInListUser(message.getId()));
//                messages.add(message);
//                adapter.addEntry(message);
//                scrolldToBottom();
//            }
//        }
//
//        @Override
//        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//            Toast.makeText(context,"onChildChanged",Toast.LENGTH_LONG).show();
//
//            if (dataSnapshot.exists()){
//                Message message = dataSnapshot.getValue(Message.class);
//                messages.set(messages.indexOf(adapter.searchInListMessage(message.getKey())),message);
//                adapter.updateEntry(message);
//            }
//        }
//
//        @Override
//        public void onChildRemoved(DataSnapshot dataSnapshot) {
//            //Toast.makeText(context,"onChildRemoved",Toast.LENGTH_LONG).show();
//            if (dataSnapshot.exists()){
//                Message message = dataSnapshot.getValue(Message.class);
//                messages.remove(message);
//                adapter.removeEntry(message);
//            }
//        }
//
//        @Override
//        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            //Toast.makeText(context,"onChildMoved",Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//            //Toast.makeText(context,"onCancelled",Toast.LENGTH_LONG).show();
//        }
//    };


    public User searchInListUser(long id){
        User res = null;
        for (User u:usersForum){
            if (u.getId()==id)
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
        Query messageQuery = messageChannelRef.limitToLast(mCurrentPage * Utils.TOTAL_MESSAGE_ITEMS_TO_LOAD);
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){
                    Message message = dataSnapshot.getValue(Message.class);
                    itemPos++;
                    if(itemPos == 1){
                        String messageKey = dataSnapshot.getKey();
                        mLastKey = messageKey;
                        mPrevKey = messageKey;
                    }
                    message.setKey(dataSnapshot.getKey());
                    message.setSender(searchInListUser(message.getId()));
                    messages.add(message);
                    adapter.addEntry(message);
                    recyclerView.scrollToPosition(messages.size() - 1);
                    mRefreshLayout.setRefreshing(false);
                    if (!message.isSeen() && message.getSenderId()!=currentUser.getId()){
                        markAsSeen(message);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void loadMoreMessages() {

        Query messageQuery = messageChannelRef.orderByKey().endAt(mLastKey).limitToLast(Utils.TOTAL_MESSAGE_ITEMS_TO_LOAD);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                String messageKey = dataSnapshot.getKey();
                if(!mPrevKey.equals(messageKey)){
                    message.setKey(dataSnapshot.getKey());
                    message.setSender(searchInListUser(message.getId()));
                    messages.add(itemPos++, message);
                    adapter.addEntryToTop(message);
                    if (!message.isSeen() && message.getSenderId()!=currentUser.getId()){
                        markAsSeen(message);
                    }
                } else {
                    mPrevKey = mLastKey;
                }

                if(itemPos == 1) {
                    mLastKey = messageKey;
                }

                Log.d("TOTALKEYS", "Last Key : " + mLastKey + " | Prev Key : " + mPrevKey + " | Message Key : " + messageKey);
                //mAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);
                //mLinearLayout.scrollToPositionWithOffset(10, 0);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void markAsSeen(Message message){
        message.setSeen(true);
        Map<String,Object> msgMap = message.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(message.getKey(),msgMap);
        adapter.updateEntry(message);
        messages.set(messages.indexOf(adapter.searchInListMessage(message.getKey())),message);
        messageChannelRef.updateChildren(childUpdates);
    }


}
