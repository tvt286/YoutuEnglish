package youtuvideos.tranty.vn.youtuvideos.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.abtracts.AbstractAnimation;
import youtuvideos.tranty.vn.youtuvideos.abtracts.AbstractResponse;
import youtuvideos.tranty.vn.youtuvideos.activities.parents.BaseActivity;
import youtuvideos.tranty.vn.youtuvideos.dao.users.UserLoginVO;
import youtuvideos.tranty.vn.youtuvideos.helpers.GooglePlusLoginHelper;
import youtuvideos.tranty.vn.youtuvideos.interfaces.SocialConnectListener;
import youtuvideos.tranty.vn.youtuvideos.mics.Constants;
import youtuvideos.tranty.vn.youtuvideos.mics.RoundedTransformation;
import youtuvideos.tranty.vn.youtuvideos.mics.ShowToast;
import youtuvideos.tranty.vn.youtuvideos.mics.Util;
import youtuvideos.tranty.vn.youtuvideos.preferences.UserShared;
import youtuvideos.tranty.vn.youtuvideos.requests.UsersRequest;

public class LoginActivity extends BaseActivity implements SocialConnectListener, View.OnClickListener {
    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";
    private static final int GPLUS_SIGN_IN = 1001;

    @BindView(R.id.contentRoot)
    LinearLayout contentRoot;
    @BindView(R.id.layout_gplustext)
    LinearLayout btnGoogleLogin;
    @BindView(R.id.pb_gplusloading)
    ProgressBar pb_gpLoader;
    @BindView(R.id.layout_full_gplus)
    LinearLayout layout_full_gplus;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.view_infomation)
    LinearLayout viewInformation;
    @BindView(R.id.view_button)
    LinearLayout viewButton;
    @BindView(R.id.view_login)
    LinearLayout viewLogin;
    @BindView(R.id.view_profile)
    LinearLayout viewProfile;
    @BindView(R.id.im_profile)
    CircleImageView imProfile;
    @BindView(R.id.btn_begin_using)
    Button btnUsing;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    private int drawingStartLocation;
    private GooglePlusLoginHelper gplusHelper;
    private Map<String, String> data;
    private AlphaAnimation animAlpha;
    private int errorCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setFinishOnTouchOutside(false);
        ButterKnife.bind(this);
        drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
        if (drawingStartLocation != 0) {
            if (savedInstanceState == null) {
                contentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        contentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                        startIntroAnimation();
                        return true;
                    }
                });
            }
        }
        initalizeView();
    }

    private void startIntroAnimation() {
        contentRoot.setScaleY(0.1f);
        contentRoot.setPivotY(drawingStartLocation);
        contentRoot.animate()
                .scaleY(1)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .start();

    }

    private void initalizeView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                gplusHelper = new GooglePlusLoginHelper();
                gplusHelper.setUserCallbackListener(LoginActivity.this);
                pb_gpLoader.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorGrey),
                        PorterDuff.Mode.SRC_IN);
            }
        }).start();
        layout_full_gplus.setOnClickListener(LoginActivity.this);
    }

    @Override
    public void onUserConnected(int requestIdentifier, UserLoginVO userData) {
        if (requestIdentifier == GPLUS_SIGN_IN) {
            userLoginVO = userData;
            requestRegister();
        }


    }

    private void requestRegister() {
        data = new HashMap<>();
        data.put(Constants.REGISTER_USER.NAME, userLoginVO.getFullName());
        data.put(Constants.REGISTER_USER.EMAIL, userLoginVO.getEmail());
        data.put(Constants.REGISTER_USER.ID, userLoginVO.getGoogleID());
        if( userLoginVO.getUserImageUri() != null)
            data.put(Constants.REGISTER_USER.IMAGE, userLoginVO.getUserImageUri());
        UsersRequest.Login(data, new AbstractResponse() {
            @Override
            public void onSuccess(int error_code, String message, Object obj) {
                super.onSuccess(error_code, message, obj);
                errorCode = error_code;
                layout_full_gplus.setBackgroundColor(getResources().getColor(R.color.bg_gplus));
                layout_full_gplus.setEnabled(true);
                btnGoogleLogin.setVisibility(View.VISIBLE);
                pb_gpLoader.setVisibility(View.GONE);
                UserShared.ins(getBaseContext()).setUser(userLoginVO);
                UserShared.ins(getBaseContext()).setIsLogin(true);
                animViewLoginOut();
            }

            @Override
            public void onFailure() {
                super.onFailure();
            }
        });
    }

    private void setResultMain(int error_code){
        Intent MainScreen = getIntent();
        if (error_code == 0) {
            MainScreen.putExtra(Constants.MESSAGE.KEY, Constants.MESSAGE.REGISTER_SUCCESS);
        } else if (error_code == 2) {
            MainScreen.putExtra(Constants.MESSAGE.KEY, Constants.MESSAGE.WELLCOME);
        }
        if (getIntent().getBooleanExtra(Constants.KEY_INTENT.FROM_MAIN,false)) {
            MainScreen.putExtra(getResources().getString(R.string.key_userModel), userLoginVO);
            setResult(Constants.ACTIVITY.LOGIN_RESULT, MainScreen);
            finish();
        }else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @OnClick(R.id.btn_begin_using)
    public void onClickUsing(){
        setResultMain(errorCode);
    }
    private void animViewLoginOut(){
        Animation zoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
        zoomOut.setFillAfter(true);
        viewLogin.startAnimation(zoomOut);
        viewButton.startAnimation(zoomOut);
        btnCancel.setClickable(false);
        btnLogin.setClickable(false);
        animViewProfileIn();
    }

    private void animViewProfileIn() {
        Picasso.with(this)
                .load(userLoginVO.getUserImageUri())
                .placeholder(R.drawable.user_default)
                .into(imProfile);
        if (errorCode == 0){
            String message = "Wellcome " + userLoginVO.getFullName() + " to Youtu English";
            tvMessage.setText(message);
        }else {
            String message = "Wellcome " + userLoginVO.getFullName() + " commback to Youtu English";
            tvMessage.setText(message);
        }
        animAlpha = new AlphaAnimation(0f, 1f);
        animAlpha.setDuration(500);
        animAlpha.setStartOffset(400);
        animAlpha.setFillAfter(true);
        viewProfile.startAnimation(animAlpha);
        animAlpha.setAnimationListener(new AbstractAnimation(){
            @Override
            public void onAnimationStart(Animation animation) {
                super.onAnimationStart(animation);
                viewProfile.setVisibility(View.VISIBLE);
                btnUsing.setVisibility(View.VISIBLE);
            }
        });
        btnUsing.startAnimation(animAlpha);
    }

    @Override
    public void onConnectionError(int requestIdentifier, String message) {
        ShowToast.show(message, LoginActivity.this);

        if (requestIdentifier == GPLUS_SIGN_IN) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layout_full_gplus.setBackgroundColor(getResources().getColor(R.color.bg_gplus));
                    layout_full_gplus.setEnabled(true);
                    btnGoogleLogin.setVisibility(View.VISIBLE);
                    pb_gpLoader.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onCancelled(int requestIdentifier, String message) {
        ShowToast.show(message, LoginActivity.this);
    }

    @Override
    public void onClick(View v) {
        if (Util.isConnectivityAvailable(LoginActivity.this)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layout_full_gplus.setBackgroundColor(getResources().getColor(R.color.view_disable));
                    layout_full_gplus.setEnabled(false);
                    btnGoogleLogin.setVisibility(View.GONE);
                    pb_gpLoader.setVisibility(View.VISIBLE);
                }
            });

            gplusHelper.createConnection(LoginActivity.this);
            gplusHelper.signIn(GPLUS_SIGN_IN);

        } else {
            ShowToast.show("No internet connection available...", LoginActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GPLUS_SIGN_IN) {
            gplusHelper.onActivityResult(resultCode, data);
        }
    }

    @OnClick(R.id.btn_cancel)
    public void clickCancel() {
        this.finish();
    }

    @OnClick(R.id.btn_login)
    public void onClickLogin() {
        btnLogin.setClickable(false);
        btnLogin.setVisibility(View.GONE);
        animInfor();
    }

    private void animInfor() {
        animAlpha = new AlphaAnimation(1f, 0f);
        animAlpha.setDuration(300);
        animAlpha.setStartOffset(0);
        animAlpha.setFillAfter(true);
        viewInformation.startAnimation(animAlpha);
        animAlpha.setAnimationListener(new AbstractAnimation() {
            @Override
            public void onAnimationEnd(Animation animation) {
                animLogin();
            }
        });

    }

    private void animLogin() {
        Animation zoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        zoomIn.setFillAfter(true);
        viewLogin.startAnimation(zoomIn);
        zoomIn.setAnimationListener(new AbstractAnimation() {
            @Override
            public void onAnimationStart(Animation animation) {
                viewLogin.setVisibility(View.VISIBLE);
            }
        });

    }
}
