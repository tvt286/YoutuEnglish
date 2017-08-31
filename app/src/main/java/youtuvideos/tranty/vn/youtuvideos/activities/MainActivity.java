package youtuvideos.tranty.vn.youtuvideos.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import youtuvideos.tranty.vn.youtuvideos.MyApplication;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.abtracts.AbstractResponse;
import youtuvideos.tranty.vn.youtuvideos.activities.parents.BaseActivity;
import youtuvideos.tranty.vn.youtuvideos.adapters.KnowledgesUserAdapter;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.KnowledgeVO;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.favorites.KnowledgesFavoritesVO;
import youtuvideos.tranty.vn.youtuvideos.dao.users.UserLoginVO;
import youtuvideos.tranty.vn.youtuvideos.dao.users.UserVO;
import youtuvideos.tranty.vn.youtuvideos.dao.users.knowledges.KnowledgesUserVO;
import youtuvideos.tranty.vn.youtuvideos.dialog.InformationLoginDialog;
import youtuvideos.tranty.vn.youtuvideos.dialog.LanguageDialog;
import youtuvideos.tranty.vn.youtuvideos.helpers.GooglePlusLoginHelper;
import youtuvideos.tranty.vn.youtuvideos.interfaces.ItemKnowledgeListeners;
import youtuvideos.tranty.vn.youtuvideos.mics.Constants;
import youtuvideos.tranty.vn.youtuvideos.mics.LoaderImage;
import youtuvideos.tranty.vn.youtuvideos.mics.ShowToast;
import youtuvideos.tranty.vn.youtuvideos.preferences.UserShared;
import youtuvideos.tranty.vn.youtuvideos.requests.KnowledgesRequest;
import youtuvideos.tranty.vn.youtuvideos.requests.UsersRequest;
import youtuvideos.tranty.vn.youtuvideos.singleton.Favorites;
import youtuvideos.tranty.vn.youtuvideos.singleton.Knowledges;

import static youtuvideos.tranty.vn.youtuvideos.activities.LoginActivity.ARG_DRAWING_START_LOCATION;

public class MainActivity extends BaseActivity implements ItemKnowledgeListeners {

    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();
    private static final int ANIM_DURATION_FAB = 400;

    @BindView(R.id.tv_favorites)
    TextView tvFavorites;
    @BindView(R.id.tv_number_knowledges)
    TextView tvNumKnowledges;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.im_user)
    ImageView imUser;
    @BindView(R.id.vUserDetails)
    View vUserDetails;
    @BindView(R.id.vUserStats)
    View vUserStats;
    @BindView(R.id.vUserRoot)
    View vUserRoot;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btnCreate)
    FloatingActionButton fabCreate;
    @BindView(R.id.layout_loading_main)
    RelativeLayout layout_loading;
    @BindView(R.id.layout_network)
    RelativeLayout layout_network;
    @BindView(R.id.rc_courses)
    RecyclerView rcKnowledges;
    @BindView(R.id.layout_loading)
    LinearLayout layout_loading_recycler;
    @BindView(R.id.tv_null)
    TextView tvNull;
    @BindView(R.id.im_null)
    ImageView imNull;
    @BindView(R.id.layout_null)
    RelativeLayout layout_null;

    private KnowledgesUserAdapter adapter;
    private ArrayList<KnowledgesUserVO> arrKnowledgesUser;
    private KnowledgesUserVO knowledgesUserVO;
    private UserLoginVO userData;
    private UserVO userVO;
    private AlphaAnimation animation;

    public static void startMainFromLocation(Activity startingActivity) {
        Intent intent = new Intent(startingActivity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startingActivity.finish();
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animationRecyclerview();
        setupRecyclerview();

        if (!MyApplication.getAppInstance().getUserChange())
            getUserInfo();
    }

    private void animationRecyclerview() {
        animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(1500);
        animation.setStartOffset(200);
        animation.setFillAfter(true);
    }

    private void setupRecyclerview() {
        rcKnowledges.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new KnowledgesUserAdapter(this, this);
        rcKnowledges.setAdapter(adapter);
    }

    /*Nếu login rồi thì get khóa học user về
    * Chưa login thì không làm gì*/
    private void getUserInfo() {
        if (isLogin) { // neu da login
            btnLogin.setText(getResources().getString(R.string.logout)); // set text logout
            tvEmail.setText(userLoginVO.getEmail()); //set email
            tvName.setText(userLoginVO.getFullName()); // set fullname
            LoaderImage.ins(this).show(userLoginVO.getUserImageUri(), imUser); // load image user
            getNumberKnowledgesUser(userLoginVO.getGoogleID()); // lấy danh sách khoa học user
        } else {
            layout_network.setVisibility(View.GONE);
            layout_loading.setVisibility(View.GONE);
            layout_null.setVisibility(View.GONE);
            rcKnowledges.setVisibility(View.GONE);
            animateUserProfileHeader();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.getAppInstance().getUserChange())
            getNumberKnowledgesUser(userLoginVO.getGoogleID());
    }

    @OnClick(R.id.layout_network)
    public void clickNetwork() {
        getUserInfo();
    }

    // lay ra danh sach knowledge cua user
    private void getNumberKnowledgesUser(String id) {
        layout_network.setVisibility(View.GONE);
        layout_null.setVisibility(View.GONE);
        rcKnowledges.setVisibility(View.GONE);
        if (MyApplication.getAppInstance().getUserChange()) {
            layout_loading_recycler.setVisibility(View.VISIBLE);
            layout_loading.setVisibility(View.GONE);
        } else {
            layout_loading.setVisibility(View.VISIBLE);
            layout_loading_recycler.setVisibility(View.GONE);
        }
        UsersRequest.get(id, new AbstractResponse() {
            @Override
            public void onSuccess(int error_code, String message, Object obj) {
                super.onSuccess(error_code, message, obj);
                userVO = (UserVO) obj;
                if (userVO != null) {
                    arrKnowledgesUser = userVO.knowledges_user;
                    if (arrKnowledgesUser != null) { // neu danh sach co
                        tvNumKnowledges.setText(String.valueOf(arrKnowledgesUser.size()));
                        adapter.setArrKnowledges(arrKnowledgesUser);
                    } else { // neu khong co thi hien null voi message add
                        layout_null.setAnimation(animation);
                        layout_null.setVisibility(View.VISIBLE);
                        tvNull.setText(getResources().getString(R.string.add_register));
                        imNull.setImageResource(R.drawable.icon_add);
                    }
                }
                if (MyApplication.getAppInstance().getUserChange()) {
                    rcKnowledges.setVisibility(View.VISIBLE);
                    layout_loading_recycler.setVisibility(View.GONE);
                } else {
                    animateUserProfileHeader();
                    layout_loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure() {
                super.onFailure();
                layout_null.setVisibility(View.GONE);
                layout_loading.setVisibility(View.GONE);
                layout_network.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getIntentData(Intent i) {
        if (i != null) {
            userData = (UserLoginVO) i.getSerializableExtra(getResources().getString(R.string.key_userModel));
            btnLogin.setText(getResources().getString(R.string.logout));
            tvEmail.setText(userData.getEmail());
            tvName.setText(userData.getFullName());
            LoaderImage.ins(this).show(userData.getUserImageUri(), imUser);
            getNumberKnowledgesUser(userData.getGoogleID());
        }

    }

    private void animateUserProfileHeader() {
        fabCreate.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));
        vUserRoot.setTranslationY(-vUserRoot.getHeight());
        imUser.setTranslationY(-imUser.getHeight());
        vUserDetails.setTranslationY(-vUserDetails.getHeight());
        vUserStats.setAlpha(0);

        vUserRoot.animate().translationY(0).setDuration(300).setInterpolator(INTERPOLATOR);
        imUser.animate().translationY(0).setDuration(300).setStartDelay(100).setInterpolator(INTERPOLATOR);
        vUserDetails.animate().translationY(0).setDuration(300).setStartDelay(200).setInterpolator(INTERPOLATOR);
        vUserStats.animate().alpha(1).setDuration(200).setStartDelay(400).setInterpolator(INTERPOLATOR)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }
                }).start();


    }

    private void startContentAnimation() {
        fabCreate.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(300)
                .setDuration(ANIM_DURATION_FAB)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        fabCreate.setVisibility(View.VISIBLE);
                    }
                })
                .start();
        if (isLogin) {
            rcKnowledges.setAnimation(animation);
            rcKnowledges.setVisibility(View.VISIBLE);
        } else {
            layout_null.setVisibility(View.VISIBLE);
            tvNull.setText(getResources().getString(R.string.login_register));
            imNull.setImageResource(R.drawable.icon_login);
        }
    }

    @OnClick(R.id.btnCreate)
    public void onAddClick() {
        int[] startingLocation = new int[2];
        fabCreate.getLocationOnScreen(startingLocation);
        startingLocation[0] += fabCreate.getWidth() / 2;
        AddKnowledgesActivity.startAddKnowledgeFromLocation(startingLocation, this, null, 1, 1, arrKnowledgesUser);
        overridePendingTransition(0, 0);
    }


    @OnClick(R.id.btn_login)
    public void doLogin() {
        if (UserShared.ins(this).getIsLogin()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.dialog_logout_title));
            builder.setMessage(getString(R.string.dialog_logout_message));

            String positiveText = getString(android.R.string.ok);
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doLogout();
                        }
                    });

            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            startLoginActivity();
        }
    }

    private void doLogout() {
        GooglePlusLoginHelper gpHelper = new GooglePlusLoginHelper();
        gpHelper.createConnection(MainActivity.this);
        gpHelper.signOut();
        UsersRequest.Logout(userVO.id, new AbstractResponse() {
            @Override
            public void onSuccess(int error_code, String message, Object obj) {
                super.onSuccess(error_code, message, obj);
                MyApplication.getAppInstance().setUserChange(false);
                rcKnowledges.removeAllViews();
                UserShared.ins(getBaseContext()).clear();
                tvName.setText(Constants.LOGIN_DEFAULT.NAME);
                tvEmail.setText(Constants.LOGIN_DEFAULT.EMAIL);
                imUser.setImageResource(R.drawable.user_default);
                btnLogin.setText(Constants.LOGIN_DEFAULT.BTN_LOGIN);
                tvNumKnowledges.setText(Constants.LOGIN_DEFAULT.NUM_KNOWLEDGES);
                if (arrKnowledgesUser != null) {
                    arrKnowledgesUser.clear();
                    adapter.setArrKnowledges(arrKnowledgesUser);
                }
                rcKnowledges.setVisibility(View.GONE);
                layout_null.setVisibility(View.VISIBLE);
                tvNull.setText(getResources().getString(R.string.login_register));
                imNull.setImageResource(R.drawable.icon_login);
                resetLikeKnowledges();
                ShowToast.show(getString(R.string.logout_success), MainActivity.this);
            }
        });
    }


    /*logout thi fai reset lai*/
    private void resetLikeKnowledges() {
        ArrayList<KnowledgeVO> arrKnowledges = Knowledges.ins(this).getArrayKnowledges();
        for (int i = 0; i < arrKnowledges.size(); i++)
            arrKnowledges.get(i).isLike = 0;
        Favorites.ins(this).setArrayFavorites(new ArrayList<KnowledgesFavoritesVO>());
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(Constants.KEY_INTENT.FROM_MAIN, true);
        startActivityForResult(intent, Constants.ACTIVITY.OPEN_LOGIN);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.ACTIVITY.LOGIN_RESULT) {
            MyApplication.getAppInstance().setUserChange(true);
            isLogin = UserShared.ins(this).getIsLogin();
            if (isLogin)
                userLoginVO = UserShared.ins(this).getUserLoginVO();
            // update lai regis id cua user
            FirebaseMessaging.getInstance().subscribeToTopic("testfcm");
            String token = FirebaseInstanceId.getInstance().getToken();
            UsersRequest.updateRegId(userLoginVO.getGoogleID(), token, new AbstractResponse() {
                @Override
                public void onSuccess(int error_code, String message, Object obj) {
                    super.onSuccess(error_code, message, obj);
                }
            });

            //     isUpdate = true;
            layout_null.setVisibility(View.GONE);
            rcKnowledges.setVisibility(View.GONE);
            getIntentData(data);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_favorites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_favorites:
                isLogin = UserShared.ins(this).getIsLogin();
                if (isLogin)
                    startFavoritesActivity();
                else
                    startLoginActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startFavoritesActivity() {
        int[] startingLocation = new int[2];
        getToolbar().getLocationOnScreen(startingLocation);
        startingLocation[0] += getToolbar().getWidth() / 2;
        FavoritesActitivty.startFavoritesFromLocation(startingLocation, this);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onItemClicked(View view, int position) {
        int[] startingLocation = new int[2];
        view.getLocationOnScreen(startingLocation);
        startingLocation[0] += view.getWidth() / 2;
        KnowledgesActivity.startKnowledgeFromLocation(startingLocation, this, arrKnowledgesUser.get(position));
        overridePendingTransition(0, 0);
    }

    @Override
    public void onDeleteClicked(View view, int position) {
        showDeleteDialog(position);
    }

    @Override
    public void onStatusClicked(View view, final int position) {
        knowledgesUserVO = arrKnowledgesUser.get(position);
        KnowledgesRequest.updateStatus(knowledgesUserVO.id, new AbstractResponse() {
            @Override
            public void onSuccess(int error_code, String message, Object obj) {
                super.onSuccess(error_code, message, obj);
                if (error_code == 0) {
                    if (knowledgesUserVO.schedule_actived == 1)
                        knowledgesUserVO.schedule_actived = 0;
                    else
                        knowledgesUserVO.schedule_actived = 1;
                    adapter.notifyItemChanged(position, knowledgesUserVO);
                }
            }

            @Override
            public void onFailure() {
                super.onFailure();
                ShowToast.show(getString(R.string.network_fail), MainActivity.this);
            }
        });
    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.dialog_title));
        builder.setMessage(getString(R.string.dialog_message));

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doDelete(position);
                    }
                });

        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void doDelete(final int position) {
        knowledgesUserVO = arrKnowledgesUser.get(position);
        KnowledgesRequest.deleteKnowledgeUser(knowledgesUserVO.id, new AbstractResponse() {
            @Override
            public void onSuccess(int error_code, String message, Object obj) {
                super.onSuccess(error_code, message, obj);
                if (arrKnowledgesUser.size() != 0) {
                    arrKnowledgesUser.remove(position);
                    adapter.notifyItemRemoved(position);
                }
                if (arrKnowledgesUser.size() == 0) {
                    layout_null.setAnimation(animation);
                    layout_null.setVisibility(View.VISIBLE);
                    tvNull.setText(getResources().getString(R.string.add_register));
                    imNull.setImageResource(R.drawable.icon_add);
                }
                ShowToast.show(getString(R.string.delete_success), MainActivity.this);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                ShowToast.show(getString(R.string.network_fail), MainActivity.this);
            }
        });
    }


}
