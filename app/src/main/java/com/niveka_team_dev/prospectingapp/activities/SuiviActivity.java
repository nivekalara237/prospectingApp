package com.niveka_team_dev.prospectingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.developerpaul123.filepickerlibrary.FilePickerActivity;
import com.niveka_team_dev.prospectingapp.fragments.AddCompteRenduSuiviBottomSheetFragment;
import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.adapters.SuiviAdapter;
import com.niveka_team_dev.prospectingapp.common.RetrofitBuilder;
import com.niveka_team_dev.prospectingapp.kernels.BaseActivity;
import com.niveka_team_dev.prospectingapp.listeners.OnAddCompteRenduSuiviListener;
import com.niveka_team_dev.prospectingapp.models.CompteRenduSuivi;
import com.niveka_team_dev.prospectingapp.models.Prospect;
import com.niveka_team_dev.prospectingapp.models.Suivi;
import com.niveka_team_dev.prospectingapp.services.api.CompteRenduSuiviApiService;
import com.niveka_team_dev.prospectingapp.ui.SwipeController;
import com.niveka_team_dev.prospectingapp.ui.SwipeControllerActions;
import com.niveka_team_dev.prospectingapp.ui.ToolbarHandler;
import com.niveka_team_dev.prospectingapp.utilities.Utils;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zendesk.belvedere.Belvedere;
import zendesk.belvedere.BelvedereUi;
import zendesk.belvedere.ImageStream;
import zendesk.belvedere.MediaResult;

import static com.github.developerpaul123.filepickerlibrary.FilePickerActivity.REQUEST_FILE;

public class SuiviActivity extends BaseActivity implements OnAddCompteRenduSuiviListener,ImageStream.Listener{

    public static final String TAG = SuiviActivity.class.getSimpleName();
    private static int firstVisibleInListview;
    @BindView(R.id.recyclerView) FastScrollRecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.add)
    Button addNew;

    private List<CompteRenduSuivi> compteRenduSuivis = new ArrayList<>();
    private Context context;
    private CompteRenduSuiviApiService apiService;
    private SuiviAdapter suiviAdapter;
    private ToolbarHandler toolbarHandler;
    private ImageStream imageStream;


    SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
        @Override
        public void onLeftClicked(int position) {
            super.onLeftClicked(position);
        }

        @Override
        public void onRightClicked(int position) {
            super.onRightClicked(position);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suivi);
        context = this;
        ButterKnife.bind(this);
        apiService = RetrofitBuilder.createServiceWithAuth(CompteRenduSuiviApiService.class,tokenManager);
        imageStream = BelvedereUi.install(this);
        imageStream.addListener(this);

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
                .setTitle(getString(R.string.crs_list_title))
                .build();
        //end toolbar
        list();
        suiviAdapter = new SuiviAdapter(context,compteRenduSuivis);
        recyclerView.setAdapter(suiviAdapter);
        final LinearLayoutManager layoutManager = new  LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(false);
        firstVisibleInListview = layoutManager.findFirstVisibleItemPosition();
        //layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c,context);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    Utils.slideUp(addNew);
                }else{
                    //Utils.slideDown(addNew);
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                overallXScroll = overallXScroll + dy;
                if (overallXScroll==0){
                    addNew.setVisibility(View.GONE);
                    addDown = true;
                }
            }
        });
    }
    private int overallXScroll = 0;
    private boolean addDown = true;
    public void list(){
        Prospect prospect = new Prospect();
        prospect.setTelephone("696031671");
        prospect.setLocalisation("Obili chapelle");
        prospect.setEmail("kevin@fired.com");
        prospect.setNom("Djaja moto");
        prospect.setType(1);
        prospect.setImpression("a littérature latine classique datant de 45 av. J.-C., le rendant vieux de 2000 ans. Un professeur du Hampden-Sydney College, en Virginie, s'est intéressé à un des mots latins les plus obscurs, consectetur, ");
        prospect.setId(300888978);
        Suivi suivi = new Suivi();
        suivi.setCreatedAt("2018-12-26T16:35:23.878+01:00");
        suivi.setId("kjhd887KJkjhjkhjkhd_hjgjhgd");
        suivi.setUserId("user00001");
        //suivi.setDateRdv("a littérature latine classique datant de 45 av. J.-C., le rendant vieux de 2000 ans. Un professeur du Hampden-Sydney College, en Virginie, s'est intéressé à un des mots latins les plus obscurs, consectetur, ns le texte. Tous les générateurs de Lorem Ipsum sur Internet tendent à reproduire le même extrait sans fin, ce qui fait de lipsum.com le seul vrai générateur de Lorem Ipsum. Iil utilise un dictionnaire de plus de 200 mots latins, en combinaison de plusieurs struct");
        suivi.setDateRdv("2018-14-26T16:00:00.000+01:00");


        CompteRenduSuivi crs1 = new CompteRenduSuivi();
        crs1.setContenu("a littérature latine classique datant de 45 av. J.-C., le rendant vieux de 2000 ans. Un professeur du Hampden-Sydney College, en Virginie, s'est intéressé à un des mots latins les plus obscurs, consectetur, ns le texte. Tous les générateurs de Lorem Ipsum sur Internet tendent à reproduire le même extrait sans fin, ce qui fait de lipsum.com le seul vrai générateur de Lorem Ipsum. Iil utilise un dictionnaire de plus de 200 mots latins, en combinaison de plusieurs struct");
        crs1.setId("LKJlkjlkjlksdjklsd");
        crs1.setCreatedAt("2018-12-26T16:35:23.878+01:00");
        crs1.setProspect(prospect);
        crs1.setSuivi(suivi);
        crs1.setDateProchaineRdv("2018-12-28 08:40");

        CompteRenduSuivi crs2 = new CompteRenduSuivi();
        crs2.setContenu("a littérature latine classique datant de 45 av. J.-C., le rendant vieux de 2000 ans. Un professeur du Hampden-Sydney College, en Virginie, s'est intéressé à un des mots latins les plus obscurs, consectetur, ns le texte. Tous les générateurs de Lorem Ipsum sur Internet tendent à reproduire le même extrait sans fin, ce qui fait de lipsum.com le seul vrai générateur de Lorem Ipsum. Iil utilise un dictionnaire de plus de 200 mots latins, en combinaison de plusieurs struct");
        crs2.setId("LKJlkjlkjlksdjklsd");
        crs2.setCreatedAt("2018-12-26T16:35:23.878+01:00");
        crs2.setProspect(prospect);
        crs2.setSuivi(suivi);
        crs2.setDateProchaineRdv("2018-12-29 08:40");

        CompteRenduSuivi crs3 = new CompteRenduSuivi();
        crs3.setContenu("a littérature latine classique datant de 45 av. J.-C., le rendant vieux de 2000 ans. Un professeur du Hampden-Sydney College, en Virginie, s'est intéressé à un des mots latins les plus obscurs, consectetur, ns le texte. Tous les générateurs de Lorem Ipsum sur Internet tendent à reproduire le même extrait sans fin, ce qui fait de lipsum.com le seul vrai générateur de Lorem Ipsum. Iil utilise un dictionnaire de plus de 200 mots latins, en combinaison de plusieurs struct");
        crs3.setId("LKJlkjlkjlksdjklsd");
        crs3.setCreatedAt("2018-12-28T16:35:23.878+01:00");
        crs3.setProspect(prospect);
        crs3.setSuivi(suivi);
        crs3.setDateProchaineRdv("2018-12-31 08:40");

        compteRenduSuivis.add(crs1);
        compteRenduSuivis.add(crs1);
        compteRenduSuivis.add(crs2);
        compteRenduSuivis.add(crs2);
        compteRenduSuivis.add(crs3);
        compteRenduSuivis.add(crs3);

    }

    @OnClick(R.id.add)
    public void addNew(View view){
        AddCompteRenduSuiviBottomSheetFragment fragment = AddCompteRenduSuiviBottomSheetFragment.newInstance(null,null);
        fragment.show(getSupportFragmentManager(),"add_compte_rendu_suivi_fregment");
//        BelvedereUi.imageStream(this)
//                .withCameraIntent()
//                //.withCameraIntent()
//                .withDocumentIntent("*/*", true)
//                .showPopup(this);
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

//        if ((requestCode == 233) && (resultCode == RESULT_OK)) {
//            Toast.makeText(this, "Directory Selected: " + data.getStringExtra(FilePickerActivity.FILE_EXTRA_DATA_PATH), Toast.LENGTH_LONG).show();
//        } else if ((requestCode == REQUEST_FILE) && (resultCode == RESULT_OK)) {
//            Toast.makeText(this, "File Selected: " + data.getStringExtra(FilePickerActivity.FILE_EXTRA_DATA_PATH), Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    public void newCompteRenduSuivi(CompteRenduSuivi compteRenduSuivi, List<String> images) {

    }

    @Override
    public void newCompteRenduSuiviCancel() {

    }

    @Override
    public void onDismissed() {

    }

    @Override
    public void onVisible() {

    }

    @Override
    public void onMediaSelected(List<MediaResult> mediaResults) {
        Log.e("MEDIA_SELECTED",mediaResults.toString());
        int i=0;
        for (MediaResult m:mediaResults){
            Log.e("MEDIA-"+i,"NAME="+m.getName()+"; TYPE"+m.getMimeType());
            i++;
        }
    }

    @Override
    public void onMediaDeselected(List<MediaResult> mediaResults) {
        Log.e("MEDIA_UNSELECTED",mediaResults.toString());
    }
}
