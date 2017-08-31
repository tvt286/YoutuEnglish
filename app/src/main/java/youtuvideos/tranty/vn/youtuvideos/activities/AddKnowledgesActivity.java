package youtuvideos.tranty.vn.youtuvideos.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.ArrayList;

import butterknife.BindView;
import youtuvideos.tranty.vn.youtuvideos.MyApplication;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.abtracts.AbstractResponse;
import youtuvideos.tranty.vn.youtuvideos.activities.parents.BaseActivity;
import youtuvideos.tranty.vn.youtuvideos.adapters.AddKnowledgeAdapter;

import youtuvideos.tranty.vn.youtuvideos.adapters.KnowledgeItemAnimator;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.KnowledgeVO;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.favorites.KnowledgesFavoritesVO;
import youtuvideos.tranty.vn.youtuvideos.dao.users.knowledges.KnowledgesUserVO;
import youtuvideos.tranty.vn.youtuvideos.interfaces.ItemAddKnowledgeListeners;
import youtuvideos.tranty.vn.youtuvideos.mics.Constants;
import youtuvideos.tranty.vn.youtuvideos.myview.RevealBackgroundView;
import youtuvideos.tranty.vn.youtuvideos.preferences.UserShared;
import youtuvideos.tranty.vn.youtuvideos.requests.FavoritesRequest;
import youtuvideos.tranty.vn.youtuvideos.requests.KnowledgesRequest;
import youtuvideos.tranty.vn.youtuvideos.requests.UsersRequest;
import youtuvideos.tranty.vn.youtuvideos.singleton.Knowledges;

public class AddKnowledgesActivity extends BaseActivity implements RevealBackgroundView.OnStateChangeListener, ItemAddKnowledgeListeners {
    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

    public static final int ITEMS_PER_AD = 5;
    private static final int NATIVE_EXPRESS_AD_HEIGHT = 150;

    @BindView(R.id.vRevealBackground)
    RevealBackgroundView vRevealBackground;
    @BindView(R.id.layout_loading)
    LinearLayout layout_loading;
    @BindView(R.id.rc_knowledges)
    RecyclerView rcKnowledges;

    private AddKnowledgeAdapter adapter;
    private ArrayList<KnowledgeVO> arrKnowledges = new ArrayList<>();
    private ArrayList<KnowledgesFavoritesVO> arrKnowledgesFavorites = new ArrayList<>();
    private int language_id, teacher_id;
    private String userId;
    private KnowledgeVO knowledgeVO;
    private ArrayList<Object> mRecyclerViewItems = new ArrayList<>();
    private ArrayList<KnowledgesUserVO> knowledgesUser = new ArrayList<>();

    // START ACTIVITY
    public static void startAddKnowledgeFromLocation(int[] startingLocation, Activity startingActivity, String user_id, int language_id, int teacher_id, ArrayList<KnowledgesUserVO> knowledgesUser) {
        Intent intent = new Intent(startingActivity, AddKnowledgesActivity.class);
        intent.putExtra(ARG_REVEAL_START_LOCATION, startingLocation);
        intent.putExtra(Constants.KEY_INTENT.LANGUAGE_ID, language_id);
        intent.putExtra(Constants.KEY_INTENT.TEACHER_ID, teacher_id);
        intent.putExtra(Constants.KEY_INTENT.USER_ID, user_id);
        intent.putParcelableArrayListExtra(Constants.KEY_INTENT.KNOWLEDGE_USER,knowledgesUser);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_add_knowledges);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setupRecyclerview();
        setupRevealBackground();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Knowledges.ins(this).setArrayKnowledges(arrKnowledges);
    }

    /**
     * Adds Native Express ads to the items list.
     */
    private void addNativeExpressAds() {
        for (int i = 0; i <= mRecyclerViewItems.size(); i += ITEMS_PER_AD) {
            final NativeExpressAdView adView = new NativeExpressAdView(AddKnowledgesActivity.this);
            mRecyclerViewItems.add(i, adView);
        }
    }

    /**
     * Sets up and loads the Native Express ads.
     */
    private void setUpAndLoadNativeExpressAds() {
        rcKnowledges.post(new Runnable() {
            @Override
            public void run() {
                final float scale = AddKnowledgesActivity.this.getResources().getDisplayMetrics().density;
                for (int i = 0; i <= mRecyclerViewItems.size(); i += ITEMS_PER_AD) {
                    final NativeExpressAdView adView = (NativeExpressAdView) mRecyclerViewItems.get(i);
                    AdSize adSize = new AdSize((int) (rcKnowledges.getWidth() / scale), NATIVE_EXPRESS_AD_HEIGHT);
                    adView.setAdSize(adSize);
                    adView.setAdUnitId(Constants.ADS.NATIVE_ADS);
                    adView.setVisibility(View.GONE);
                }
                loadNativeExpressAd(0);
            }
        });
    }

    /**
     * Loads the Native Express ads in the items list.
     */
    private void loadNativeExpressAd(final int index) {

        if (index >= mRecyclerViewItems.size()) {
            return;
        }

        Object item = mRecyclerViewItems.get(index);
        if (!(item instanceof NativeExpressAdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a Native"
                    + " Express ad.");
        }

        final NativeExpressAdView adView = (NativeExpressAdView) item;

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }
        });

        adView.loadAd(new AdRequest.Builder().build());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /*Danh sách khóa học*/
    private void getKnowledges(int language_id, int teacher_id) {
        KnowledgesRequest.gets(language_id, teacher_id, new AbstractResponse() {
            @Override
            public void onSuccess(int error_code, String message, Object obj) {
                super.onSuccess(error_code, message, obj);
                arrKnowledges = (ArrayList<KnowledgeVO>) obj;
                if (arrKnowledges != null) {
                    if (knowledgesUser != null)
                        removeKnowledgeRegistered();
                    if (isLogin) // nếu như đã login
                        getKnowledgesFavorites(); // thì set like cho khoa hoc
                    else {
                        rcKnowledges.setVisibility(View.VISIBLE);
                        layout_loading.setVisibility(View.GONE);
                        rcKnowledges.removeAllViews();
                        addToArrayObject();
                    }
                }
            }

            @Override
            public void onFailure() {
                super.onFailure();
                layout_loading.setVisibility(View.GONE);
            }
        });


    }

    // thêm danh sách khóa học và native admob vào 1 arraylist Object
    private void addToArrayObject() {
        mRecyclerViewItems.addAll(arrKnowledges); // thêm danh sách khóa học vào array object
        addNativeExpressAds(); // them danh sách native admob vào array object
        setUpAndLoadNativeExpressAds(); // load admob
        adapter.setArrayRecyclerview(mRecyclerViewItems);
    }

    private void getKnowledgesFavorites() {
        if (userLoginVO != null) {
            String userId = userLoginVO.getGoogleID();
            KnowledgesRequest.getKnowledgesFavorites(userId, new AbstractResponse() {
                @Override
                public void onSuccess(int error_code, String message, Object obj) {
                    super.onSuccess(error_code, message, obj);
                    layout_loading.setVisibility(View.GONE);
                    arrKnowledgesFavorites = (ArrayList<KnowledgesFavoritesVO>) obj;
                    if (arrKnowledgesFavorites != null) {
                        getLikes(arrKnowledgesFavorites);
                    }
                    rcKnowledges.setVisibility(View.VISIBLE);
                    layout_loading.setVisibility(View.GONE);
                    rcKnowledges.removeAllViews();
                    Knowledges.ins(getBaseContext()).setArrayKnowledges(arrKnowledges); // lưu mảng vào singleton
                    addToArrayObject();
                    MyApplication.getAppInstance().setUserChange(false);
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                    layout_loading.setVisibility(View.GONE);
                }
            });
        }
    }

    // set like cho khóa học
    private void getLikes(ArrayList<KnowledgesFavoritesVO> favorites) {
        for (int j = 0; j < arrKnowledges.size(); j++) {
            KnowledgeVO knowledgeVO = arrKnowledges.get(j);
            MyApplication.getAppInstance().setArrModules(knowledgeVO.id,knowledgeVO.modules);
            for (int i = 0; i < favorites.size(); i++) {
                if (favorites.get(i).knowledge_id == knowledgeVO.id) {
                    arrKnowledges.get(j).isLike = 1;
                    break;
                }
            }
        }
    }

    // xóa đi các khóa học đã register
    private void removeKnowledgeRegistered() {
        //duyet cac khoa hoc de xoa di cac khoa hoc da hoc.
        //duyet nguoc mang de co the xoa ma ko gay loi.
        for (int i = arrKnowledges.size() - 1; i >= 0; i--) {
            boolean enrolled = false;
            for (int k = 0; k < knowledgesUser.size(); k++) {
                if (knowledgesUser.get(k).knowledge_id == arrKnowledges.get(i).id) {
                    enrolled = true;
                    break;
                }
            }
            if (enrolled) {
                arrKnowledges.remove(i);
            }
        }


    }


    // khỏi tạo recyclerview
    private void setupRecyclerview() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };

        rcKnowledges.setLayoutManager(linearLayoutManager);
        rcKnowledges.setItemAnimator(new KnowledgeItemAnimator());
        rcKnowledges.setHasFixedSize(true);
        adapter = new AddKnowledgeAdapter(this);
        adapter.setOnItemClickListener(this);
        arrKnowledges = new ArrayList<>();
        rcKnowledges.setAdapter(adapter);
    }

    private void setupRevealBackground() {
        vRevealBackground.setFillPaintColor(getResources().getColor(R.color.colorGrey));
        vRevealBackground.setOnStateChangeListener(this);
        final int[] startingLocation = getIntent().getIntArrayExtra(ARG_REVEAL_START_LOCATION);
        vRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                vRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                vRevealBackground.startFromLocation(startingLocation);
                return true;
            }
        });

    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) { // nếu như load finish
            rcKnowledges.setVisibility(View.VISIBLE);
            getData();
        } else
            rcKnowledges.setVisibility(View.INVISIBLE);
    }

    private void getData() {
        layout_loading.setVisibility(View.VISIBLE);
        rcKnowledges.setVisibility(View.GONE);
        arrKnowledges = Knowledges.ins(this).getArrayKnowledges();

        knowledgesUser = getIntent().getParcelableArrayListExtra(Constants.KEY_INTENT.KNOWLEDGE_USER);
        if (arrKnowledges.size() == 0 || MyApplication.getAppInstance().getUserChange()) { // neu như null thi get trên sever về
            language_id = getIntent().getIntExtra(Constants.KEY_INTENT.LANGUAGE_ID, 1);
            teacher_id = getIntent().getIntExtra(Constants.KEY_INTENT.TEACHER_ID, 1);
            userId = getIntent().getStringExtra(Constants.KEY_INTENT.USER_ID);
            getKnowledges(language_id, teacher_id);
        } else {
            if (knowledgesUser != null)
                removeKnowledgeRegistered();
            addToArrayObject();

            rcKnowledges.setVisibility(View.VISIBLE);
            layout_loading.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.ACTIVITY.LANGUAGE_RESULT) {
            language_id = data.getIntExtra(Constants.KEY_INTENT.LANGUAGE_ID, 1);
            teacher_id = data.getIntExtra(Constants.KEY_INTENT.TEACHER_ID, 1);
            getKnowledges(language_id, teacher_id);
            if (isLogin)
                updateLanguage(userId, language_id, teacher_id);

        }
    }

    // cập nhật lại language user
    private void updateLanguage(final String user_id, final int language_id, final int teacher_id) {
        UsersRequest.updateLanguage(user_id, language_id, teacher_id, new AbstractResponse() {
        });
    }

    // click comment
    @Override
    public void onCommentsClick(View v, int position) {
        int index = arrKnowledges.indexOf(mRecyclerViewItems.get(position));
        final Intent intent = new Intent(this, CommentsActivity.class);
        int[] startingLocation = new int[2];
        v.getLocationOnScreen(startingLocation);
        intent.putExtra(CommentsActivity.ARG_DRAWING_START_LOCATION, startingLocation[1]);
        intent.putExtra(Constants.KEY_INTENT.KNOWLEDGE_ID, arrKnowledges.get(index).id);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onLikesClick(View v, int position, boolean isLike) {
        isLogin = UserShared.ins(this).getIsLogin();
        if (isLogin) { // neu như dang login
            MyApplication.getAppInstance().setFavoritesChange(true);
            String user_id = userLoginVO.getGoogleID();
            int index = arrKnowledges.indexOf(mRecyclerViewItems.get(position)); // lấy ra index của khóa hoc hiện tại
            if (isLike) {
                arrKnowledges.get(index).isLike = 1;
                FavoritesRequest.addKnowledgesFavorites(user_id, arrKnowledges.get(index).id, new AbstractResponse() {
                });
            } else {
                arrKnowledges.get(index).isLike = 0;
                FavoritesRequest.removeKnowledgesFavorites(user_id, arrKnowledges.get(index).id, new AbstractResponse() {
                });
            }
        } else {// chưa login thì vào login
            startLogin(v);
        }
    }

    // mở activity login
    private void startLogin(View v){
        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    // click vào item
    @Override
    public void onImageClick(View v, int position) {
        int index = arrKnowledges.indexOf(mRecyclerViewItems.get(position));
        knowledgeVO = arrKnowledges.get(index);
        int[] startingLocation = new int[2];
        v.getLocationOnScreen(startingLocation);
        startingLocation[0] += v.getWidth() / 2;
        ModulesActivity.startModulesFromLocation(startingLocation, AddKnowledgesActivity.this, knowledgeVO);
        overridePendingTransition(0, 0);
    }
}
