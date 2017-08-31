package youtuvideos.tranty.vn.youtuvideos.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import youtuvideos.tranty.vn.youtuvideos.MyApplication;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.abtracts.AbstractResponse;
import youtuvideos.tranty.vn.youtuvideos.activities.parents.BaseActivity;
import youtuvideos.tranty.vn.youtuvideos.adapters.ModulesAdapter;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.KnowledgeVO;
import youtuvideos.tranty.vn.youtuvideos.dao.modules.ModuleVO;
import youtuvideos.tranty.vn.youtuvideos.dialog.SettingDialog;
import youtuvideos.tranty.vn.youtuvideos.interfaces.ItemModulesListeners;
import youtuvideos.tranty.vn.youtuvideos.mics.CircleTransformation;
import youtuvideos.tranty.vn.youtuvideos.mics.Constants;
import youtuvideos.tranty.vn.youtuvideos.myview.CustomFontTextView;
import youtuvideos.tranty.vn.youtuvideos.myview.RevealBackgroundView;
import youtuvideos.tranty.vn.youtuvideos.preferences.UserShared;
import youtuvideos.tranty.vn.youtuvideos.requests.KnowledgesRequest;
import youtuvideos.tranty.vn.youtuvideos.requests.ModulesRequest;
import youtuvideos.tranty.vn.youtuvideos.requests.UsersRequest;

import static youtuvideos.tranty.vn.youtuvideos.activities.MainActivity.ARG_REVEAL_START_LOCATION;

public class ModulesActivity extends BaseActivity {
    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();

    @BindView(R.id.rc_modules) RecyclerView rcModules;
    @BindView(R.id.tv_register) TextView tvRegister;
    @BindView(R.id.tv_number_modules) TextView tvNumModules;
    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.im_modules) ImageView imModule;
    @BindView(R.id.vModuleDetails) View vModuleDetails;
    @BindView(R.id.vModuleStats) View vModuleStats;
    @BindView(R.id.vModuleRoot) View vModuleRoot;
    @BindView(R.id.layout_loading_main) RelativeLayout layout_loading_main;


    private KnowledgeVO knowledgeVO;
    private int imageSize;
    private ModulesAdapter adapter;
    private ArrayList<ModuleVO> arrModules;
    private int knowledgeId;

    public static void startModulesFromLocation(int[] startingLocation, Activity startingActivity, KnowledgeVO knowledgeVO) {
        Intent intent = new Intent(startingActivity, ModulesActivity.class);
        intent.putExtra(Constants.KEY_INTENT.KNOWLEDGE, (Serializable) knowledgeVO);
        intent.putExtra(ARG_REVEAL_START_LOCATION, startingLocation);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        cancelNoti();
        setupRecycler();
        getData();
    }

    private void cancelNoti(){
        int notification_id = getIntent().getIntExtra(Constants.KEY_INTENT.NOTIFICATION_ID,0);
        if (notification_id != 0) {
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(notification_id);
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


    private void getContent() {
        layout_loading_main.setVisibility(View.GONE);
        rcModules.setVisibility(View.VISIBLE);
        vModuleRoot.setVisibility(View.VISIBLE);
        adapter = new ModulesAdapter(this);
        rcModules.setAdapter(adapter);

        //get danh sach bai hoc
        arrModules = MyApplication.getAppInstance().getArrModules(knowledgeVO.id);
        if (arrModules != null) {
            adapter.setArrModules(arrModules);
        }

        // animation
        animateUserProfileHeader();
    }


    private void animateUserProfileHeader() {
        vModuleRoot.setTranslationY(-vModuleRoot.getHeight());
        imModule.setTranslationY(-imModule.getHeight());
        vModuleDetails.setTranslationY(-vModuleDetails.getHeight());
        vModuleStats.setAlpha(0);

        vModuleRoot.animate().translationY(0).setDuration(300).setInterpolator(INTERPOLATOR);
        imModule.animate().translationY(0).setDuration(300).setStartDelay(100).setInterpolator(INTERPOLATOR);
        vModuleDetails.animate().translationY(0).setDuration(300).setStartDelay(200).setInterpolator(INTERPOLATOR);
        vModuleStats.animate().alpha(1).setDuration(200).setStartDelay(400).setInterpolator(INTERPOLATOR).start();
    }

    private void setupRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        rcModules.setLayoutManager(linearLayoutManager);
    }


    private void getData() {
        layout_loading_main.setVisibility(View.VISIBLE);
        knowledgeVO = (KnowledgeVO) getIntent().getSerializableExtra(Constants.KEY_INTENT.KNOWLEDGE);
        if (knowledgeVO == null){// neu click từ favorite
            knowledgeId = getIntent().getIntExtra(Constants.KEY_INTENT.KNOWLEDGE_ID,1);
            KnowledgesRequest.getKnowledgeById(knowledgeId, new AbstractResponse() {
                @Override
                public void onSuccess(int error_code, String message, Object obj) {
                    super.onSuccess(error_code, message, obj);
                    knowledgeVO = (KnowledgeVO) obj;
                    setData();
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                }
            });
        }else { // click tu add knowledge
            setData();
        }
    }


    private void setData() {
        tvTitle.setText(knowledgeVO.title);
        tvRegister.setText(String.valueOf(knowledgeVO.registers));
        tvNumModules.setText(String.valueOf(knowledgeVO.total_video));
        this.imageSize = getResources().getDimensionPixelSize(R.dimen.image_course_size);
        Picasso.with(this)
                .load(knowledgeVO.image)
                .placeholder(R.color.colorGrey)
                .resize(imageSize, imageSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(imModule);

        getContent();
    }

    @OnClick(R.id.btn_register)
    public void clickRegister() {
        Intent intent;
        if (isLogin) {
            intent = new Intent(this, SettingDialog.class);
            intent.putExtra(Constants.KEY_INTENT.KNOWLEDGE, knowledgeVO);
        } else
            intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
        overridePendingTransition(0, 0);
    }


    @OnClick(R.id.btn_view)
    public void clickView() {
        Intent intent = new Intent(this, PlayModulesActivity.class);
        intent.putExtra(Constants.KEY_INTENT.KNOWLEDGE, knowledgeVO);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

}
