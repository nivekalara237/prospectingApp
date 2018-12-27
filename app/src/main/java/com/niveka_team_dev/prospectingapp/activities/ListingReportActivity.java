package com.niveka_team_dev.prospectingapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.ui.ToolbarHandler;
import com.niveka_team_dev.prospectingapp.utilities.Utils;
import com.niveka_team_dev.prospectingapp.adapters.ListingReportAdapter;
import com.niveka_team_dev.prospectingapp.kernels.BaseActivity;
import com.niveka_team_dev.prospectingapp.listeners.OnRVBottomReachedListener;
import com.niveka_team_dev.prospectingapp.models.Rapport;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListingReportActivity extends BaseActivity implements OnRVBottomReachedListener{
    @BindView(R.id.recyclerView) FastScrollRecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ListingReportAdapter adapter ;
    Context context;
    List<Rapport> reportList = new ArrayList<>();
    private DatabaseReference reportRef;
    private DatabaseReference rootref;
    private ToolbarHandler toolbarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_report);
        ButterKnife.bind(this);
        context = this;
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
                .setTitle(getString(R.string.report_list_title))
                .build();

        //end toolbar
        rootref = FirebaseDatabase.getInstance().getReference();
        reportRef = rootref.child(Utils.FIREBASE_DB_NAME).child("reportings");

        adapter = new ListingReportAdapter(reportList,context);
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new  LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        //layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        getDataPerPage(null);
    }

    private void getDataPerPage(final String _startKey) {
        progressBar.setVisibility(View.VISIBLE);
        //int start = currentPage*Utils.MAX_PROSPECTS_TO_DISPLAY_PER_PAGE;

        Query query = null;
        query = reportRef.orderByChild("email").equalTo(currentUser.getEmail()).limitToFirst(256);
//        if (_startKey!=null){
//            Log.e("KEY_STARTING",_startKey);
//            query = prospectingRef.orderByChild("email").equalTo(currentUser.getEmail()).limitToFirst(Utils.MAX_PROSPECTS_TO_DISPLAY_PER_PAGE);
//        }else{
//            query = prospectingRef.orderByChild("email").equalTo(currentUser.getEmail()).limitToFirst(Utils.MAX_PROSPECTS_TO_DISPLAY_PER_PAGE);
//        }

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Rapport rep = dataSnapshot.getValue(Rapport.class);

//                if (_startKey!=null && !_startKey.equals(dataSnapshot.getKey())){
//                    if (rep!=null){
//                        reportList.add(rep);
//                        adapter.notifyDataSetChanged();
//                    }
//                }
                reportList.add(rep);
                adapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);

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
            //getDataPerPage(reportList.get(position).getKey());
        }
    }
}
