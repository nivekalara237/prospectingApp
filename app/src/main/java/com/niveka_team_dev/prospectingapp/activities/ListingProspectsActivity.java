package com.niveka_team_dev.prospectingapp.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.ui.ToolbarHandler;
import com.niveka_team_dev.prospectingapp.utilities.Utils;
import com.niveka_team_dev.prospectingapp.adapters.ListingProspectsAdapter;
import com.niveka_team_dev.prospectingapp.listeners.OnRVBottomReachedListener;
import com.niveka_team_dev.prospectingapp.models.Prospect;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListingProspectsActivity extends AppCompatActivity implements OnRVBottomReachedListener,RadioGroup.OnCheckedChangeListener {

    public final static String PROSPECTION_ID = "prospects_id";
    public final static String PROSPECTION_P_ID = "prospects_p_id";
    public final static String PROSPECTION_SUIVI_ID = "prospects_suivi_id";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tabs)
    RadioGroup tabs;
    @BindView(R.id.tabAll) RadioButton tabAll;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabProspection) RadioButton tabProspection;
    @BindView(R.id.tabSuivi) RadioButton tabSuivi;
    private ListingProspectsAdapter adapter ;
    Context context;
    List<Prospect> prospectList = new ArrayList<>();
    List<Prospect> prospectListAll = new ArrayList<>();
    List<Prospect> prospectListProspection = new ArrayList<>();
    List<Prospect> prospectListSuivi = new ArrayList<>();
    private DatabaseReference prospectingRef;
    private DatabaseReference rootref;
    public int nextPage = 1;
    public int currentPage = 0;
    private String lastKeyRetrieved = null;
    private RadioButton tabChecked = null;
    private ToolbarHandler toolbarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_prospect);
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
                .setTitle(getString(R.string.prospecting_list_title))
                .build();

        //end toolbar

        rootref = FirebaseDatabase.getInstance().getReference();
        prospectingRef = rootref.child(Utils.FIREBASE_DB_NAME).child("prospects");

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            prospectList = extras.getParcelableArrayList(PROSPECTION_ID);
        }

        if (savedInstanceState!=null){
            prospectList = savedInstanceState.getParcelableArrayList(PROSPECTION_ID);
            prospectListSuivi = savedInstanceState.getParcelableArrayList(PROSPECTION_SUIVI_ID);
            prospectListProspection = savedInstanceState.getParcelableArrayList(PROSPECTION_P_ID);
        }


        adapter = new ListingProspectsAdapter(context,prospectList);
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new  LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        //layoutManager.setReverseLayout(true);
        //layoutManager.scro
        recyclerView.setLayoutManager(layoutManager);
        if (prospectList.isEmpty())
            getDataPerPage(null);
        tabChecked = tabAll;
        tabs.setOnCheckedChangeListener(this);

    }

    public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        state.putParcelableArrayList(PROSPECTION_ID, (ArrayList<? extends Parcelable>) prospectList);
        state.putParcelableArrayList(PROSPECTION_P_ID, (ArrayList<? extends Parcelable>) prospectListProspection);
        state.putParcelableArrayList(PROSPECTION_SUIVI_ID, (ArrayList<? extends Parcelable>) prospectListSuivi);
        //state.putString(ERROR,er);
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
                Log.e("KEY",""+prospect.getType());

                if (_startKey!=null && _startKey.equals(dataSnapshot.getKey())){
                    //prospectList.add(prospect);
                    //adapter.addEntry(prospect);
                    //lastKeyRetrieved = dataSnapshot.getKey();
                }else{
                    prospect.setFb_key(dataSnapshot.getKey());
                    prospectList.add(prospect);
                    adapter.addEntry(prospect);
                    prospectListAll.add(prospect);

                    if (prospect.getType() == Utils.TYPE_PROSPECTION.PROSPECTION)
                        prospectListProspection.add(prospect);
                    if (prospect.getType() == Utils.TYPE_PROSPECTION.SUIVI)
                        prospectListSuivi.add(prospect);
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

    @SuppressLint("CheckResult")
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(checkedId);
        boolean isChecked = checkedRadioButton.isChecked();
        if (isChecked)
        Log.e("RADIOGROUP","Checked:" + checkedRadioButton.getText());
        filter(checkedId);
//        switch (checkedId){
//            case R.id.tabAll:
//                tabChecked = tabAll;
//                break;
//            case R.id.tabProspection:
//                tabChecked = tabProspection;
//                break;
//            case R.id.tabSuivi:
//                tabChecked = tabSuivi;
//                break;
//        }
    }

    public void filter(int intRes){
        if (intRes == R.id.tabProspection)
        {
            adapter.setAllItems(prospectListProspection);
        }
        else if (intRes == R.id.tabSuivi)
        {
            adapter.setAllItems(prospectListSuivi);
        }else {
            adapter.setAllItems(prospectListAll);
        }

    }
}
