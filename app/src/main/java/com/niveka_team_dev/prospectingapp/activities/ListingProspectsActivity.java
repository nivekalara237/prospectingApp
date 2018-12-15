package com.niveka_team_dev.prospectingapp.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.Utils;
import com.niveka_team_dev.prospectingapp.adapters.ListingProspectsAdapter;
import com.niveka_team_dev.prospectingapp.listeners.OnRVBottomReachedListener;
import com.niveka_team_dev.prospectingapp.models.Prospect;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListingProspectsActivity extends AppCompatActivity implements OnRVBottomReachedListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private ListingProspectsAdapter adapter ;
    Context context;
    List<Prospect> prospectList = new ArrayList<>();
    private DatabaseReference prospectingRef;
    private DatabaseReference rootref;
    public int nextPage = 1;
    public int currentPage = 0;
    private String lastKeyRetrieved = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_rapport);
        ButterKnife.bind(this);
        context = this;
        rootref = FirebaseDatabase.getInstance().getReference();
        prospectingRef = rootref.child(Utils.FIREBASE_DB_NAME).child("prospects");

        adapter = new ListingProspectsAdapter(context,prospectList);
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new  LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        getDataPerPage(null);

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (recyclerView.canScrollVertically(1)){
//                    //getDataPerPage();
//                    Log.e("SCROLLING",newState+"");
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });
    }


    private void getDataPerPage(final String _startKey) {
        progressBar.setVisibility(View.VISIBLE);
        int start = currentPage*Utils.MAX_PROSPECTS_TO_DISPLAY_PER_PAGE;
        Query query = null;
        if (_startKey!=null){
            Log.e("KEY_STARTING",_startKey);
            query = prospectingRef.orderByKey().startAt(_startKey).limitToFirst(Utils.MAX_PROSPECTS_TO_DISPLAY_PER_PAGE);
        }else{
            query = prospectingRef.orderByKey().limitToFirst(Utils.MAX_PROSPECTS_TO_DISPLAY_PER_PAGE);
        }

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Prospect prospect = dataSnapshot.getValue(Prospect.class);
                //prospectList.remove(prospect);
                Log.e("KEY",dataSnapshot.getKey());

                if (_startKey!=null && _startKey.equals(dataSnapshot.getKey())){
                    //prospectList.add(prospect);
                    //adapter.addEntry(prospect);
                    //lastKeyRetrieved = dataSnapshot.getKey();
                }else{
                    if (prospect!=null)
                        prospect.setFb_key(dataSnapshot.getKey());
                    prospectList.add(prospect);
                    adapter.addEntry(prospect);
                }

                progressBar.setVisibility(View.GONE);
                /*if (prospectList.size()!=0 && prospectList.size()%Utils.MAX_PROSPECTS_TO_DISPLAY_PER_PAGE==0){
                    currentPage = nextPage;
                    nextPage = nextPage+1;
                }*/
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

        query.addChildEventListener(childEventListener);
    }


    @Override
    public void onBottomReached(int position, boolean reached) {
        if (reached){
            getDataPerPage(prospectList.get(position).getFb_key());
        }
    }
}
