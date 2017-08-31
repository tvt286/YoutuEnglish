package youtuvideos.tranty.vn.youtuvideos.activities;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.abtracts.AbstractResponse;
import youtuvideos.tranty.vn.youtuvideos.activities.parents.BaseActivity;
import youtuvideos.tranty.vn.youtuvideos.requests.UsersRequest;

public class SplashScreenActivity extends BaseActivity {

    @BindView(R.id.logo_flash)
    ImageView imLogoFlash;
    @BindView(R.id.img_loading_1)
    ImageView loading1;
    @BindView(R.id.img_loading_3)
    ImageView loading3;
    @BindView(R.id.img_loading_2)
    ImageView loading2;

    private ObjectAnimator waveOneAnimator;
    private ObjectAnimator waveTwoAnimator;
    private ObjectAnimator waveThreeAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        waveAnimation();
        new IntentLauncher().start();
    }

    public void waveAnimation() {
        PropertyValuesHolder tvOne_Y = PropertyValuesHolder.ofFloat(loading1.TRANSLATION_Y, -40.0f);
        PropertyValuesHolder tvOne_X = PropertyValuesHolder.ofFloat(loading1.TRANSLATION_X, 0);
        waveOneAnimator = ObjectAnimator.ofPropertyValuesHolder(loading1, tvOne_X, tvOne_Y);
        waveOneAnimator.setRepeatCount(-1);
        waveOneAnimator.setRepeatMode(ValueAnimator.REVERSE);
        waveOneAnimator.setDuration(300);
        waveOneAnimator.start();

        PropertyValuesHolder tvTwo_Y = PropertyValuesHolder.ofFloat(loading2.TRANSLATION_Y, -40.0f);
        PropertyValuesHolder tvTwo_X = PropertyValuesHolder.ofFloat(loading2.TRANSLATION_X, 0);
        waveTwoAnimator = ObjectAnimator.ofPropertyValuesHolder(loading2, tvTwo_X, tvTwo_Y);
        waveTwoAnimator.setRepeatCount(-1);
        waveTwoAnimator.setRepeatMode(ValueAnimator.REVERSE);
        waveTwoAnimator.setDuration(300);
        waveTwoAnimator.setStartDelay(100);
        waveTwoAnimator.start();

        PropertyValuesHolder tvThree_Y = PropertyValuesHolder.ofFloat(loading3.TRANSLATION_Y, -40.0f);
        PropertyValuesHolder tvThree_X = PropertyValuesHolder.ofFloat(loading3.TRANSLATION_X, 0);
        waveThreeAnimator = ObjectAnimator.ofPropertyValuesHolder(loading3, tvThree_X, tvThree_Y);
        waveThreeAnimator.setRepeatCount(-1);
        waveThreeAnimator.setRepeatMode(ValueAnimator.REVERSE);
        waveThreeAnimator.setDuration(300);
        waveThreeAnimator.setStartDelay(200);
        waveThreeAnimator.start();
    }

    private class IntentLauncher extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {

            }
            // neu da login roi get token lai luu
            if (isLogin) {
                FirebaseMessaging.getInstance().subscribeToTopic("testfcm");
                String token = FirebaseInstanceId.getInstance().getToken();
                UsersRequest.updateRegId(userLoginVO.getGoogleID(), token, new AbstractResponse() {
                    @Override
                    public void onSuccess(int error_code, String message, Object obj) {
                        super.onSuccess(error_code, message, obj);
                        gotoMain();
                    }

                    @Override
                    public void onFailure() {
                        super.onFailure();
                        Toast.makeText(getBaseContext(), "network disconnect", Toast.LENGTH_LONG).show();
                    }
                });
            }else {
                gotoMain();
            }


        }
    }

    private void gotoMain() {
        MainActivity.startMainFromLocation(SplashScreenActivity.this);
        overridePendingTransition(0, 0);
    }
}
