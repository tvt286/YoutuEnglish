package youtuvideos.tranty.vn.youtuvideos.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import youtuvideos.tranty.vn.youtuvideos.MyApplication;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.activities.parents.BaseActivity;
import youtuvideos.tranty.vn.youtuvideos.adapters.ModulesAdapter;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.KnowledgeVO;
import youtuvideos.tranty.vn.youtuvideos.dao.modules.ModuleVO;
import youtuvideos.tranty.vn.youtuvideos.dialog.SettingDialog;
import youtuvideos.tranty.vn.youtuvideos.interfaces.ItemModulesListeners;
import youtuvideos.tranty.vn.youtuvideos.mics.Constants;
import youtuvideos.tranty.vn.youtuvideos.mics.ShowToast;
import youtuvideos.tranty.vn.youtuvideos.mics.Util;

public class PlayModulesActivity extends BaseActivity implements YouTubePlayer.OnInitializedListener {
    private static final int RECOVERY_REQUEST = 1;

    @BindView(R.id.rc_modules)
    RecyclerView recyclerView;
    @BindView(R.id.view_network)
    RelativeLayout viewNetwork;
    @BindView(R.id.layout_loading)
    LinearLayout layout_loading;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_length)
    TextView tvLength;
    @BindView(R.id.video_fragment)
    FrameLayout youtubeFrament;
    @BindView(R.id.layout_admob)
    RelativeLayout layoutAdmob;
    @BindView(R.id.item_knowledge)
    LinearLayout itemKnowledge;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.btn_expand)
    ImageView imExpand;
    @BindView(R.id.tv_title_knowledge)
    TextView tvTitleKnowledge;
    @BindView(R.id.tv_total_video_knowledge)
    TextView tvTotalVideos;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.im_knowledge)
    ImageView imKnowledge;

    private KnowledgeVO knowledgeVO;
    private YouTubePlayer youTubePlayer;
    private ModulesAdapter adapter;
    private ArrayList<ModuleVO> arrModules;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private AdView mAdView;
    private boolean isExpand = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_modules);
        addYoutubeFragment();
        addRecycler();
        getData();
        setData();
        inItAdmob();
    }

    private void inItAdmob() {
        // Initialize the Mobile Ads SDK.
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                layoutAdmob.setVisibility(View.VISIBLE);
            }
        });
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
    }


    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void addYoutubeFragment() {
        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.video_fragment, youTubePlayerFragment).commit();
    }


    private void getData() {
        knowledgeVO = (KnowledgeVO) getIntent().getSerializableExtra(Constants.KEY_INTENT.KNOWLEDGE);
        arrModules = MyApplication.getAppInstance().getArrModules(knowledgeVO.id);
        if (arrModules != null) {
            adapter.setArrModules(arrModules);
        }
    }

    private void setData() {
        // set data cho view knowledge
        tvTitleKnowledge.setText(knowledgeVO.title);
        //  tvDescription.setText(knowledgeVO.description);
        Picasso.with(this)
                .load(knowledgeVO.image)
                .placeholder(R.color.colorGrey)
                .into(imKnowledge);
        tvTotalVideos.setText(String.valueOf(knowledgeVO.total_video + " videos"));
        // set data module
        tvTitle.setText(arrModules.get(0).title);
        tvLength.setText(Util.secondsToString(arrModules.get(0).length));
        youTubePlayerFragment.initialize(Constants.KEY.YOUTUB_API_KEY, PlayModulesActivity.this);
    }

    private void addRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ModulesAdapter(this);
        adapter.setListeners(new ItemModulesListeners() {
            @Override
            public void onItemClicked(View v, int position) {
                ModuleVO moduleVO = arrModules.get(position);
                tvTitle.setText(moduleVO.title);
                tvLength.setText(Util.secondsToString(moduleVO.length));
                youTubePlayer.loadVideo(moduleVO.youtube_id);
                youTubePlayer.play();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.item_knowledge)
    public void onClickItemKnowledge() {
        if (!isExpand) {
            isExpand = true;
            tvDescription.setMaxLines(10);
            tvTotalVideos.setVisibility(View.VISIBLE);
            imExpand.setBackgroundResource(R.drawable.ic_collapse);
            btnRegister.setVisibility(View.GONE);

        } else {
            isExpand = false;
            btnRegister.setVisibility(View.VISIBLE);
            imExpand.setBackgroundResource(R.drawable.ic_expand);
            tvDescription.setMaxLines(2);
            tvTotalVideos.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.btn_register)
    public void onClickRegister() {
        if (youTubePlayer != null)
            youTubePlayer.pause();
        Intent intent = new Intent(this,SettingDialog.class);
        intent.putExtra(Constants.KEY_INTENT.FROM_PLAY_MODULES,true);
        intent.putExtra(Constants.KEY_INTENT.KNOWLEDGE, knowledgeVO);
        startActivityForResult(intent,Constants.ACTIVITY.OPEN_SETTING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.ACTIVITY.SETTING_RESULT){
            if (youTubePlayer != null)
                youTubePlayer.play();
            ShowToast.show(getString(R.string.register_success),PlayModulesActivity.this);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            this.youTubePlayer = player;
            this.youTubePlayer.setFullscreen(false);
            this.youTubePlayer.loadVideo(arrModules.get(0).youtube_id);
            this.youTubePlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


}
