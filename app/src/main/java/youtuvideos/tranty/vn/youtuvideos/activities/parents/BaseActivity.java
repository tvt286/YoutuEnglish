package youtuvideos.tranty.vn.youtuvideos.activities.parents;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.dao.users.UserLoginVO;
import youtuvideos.tranty.vn.youtuvideos.preferences.UserShared;


/**
 * Created by Miroslaw Stanek on 19.01.15.
 */
public class BaseActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    public UserLoginVO userLoginVO;
    public boolean isLogin;
    public String mLabel;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getLabelActivity();
        bindViews();
        isLogin = UserShared.ins(this).getIsLogin();
        if (isLogin)
            userLoginVO = UserShared.ins(this).getUserLoginVO();
    }

    protected  void getLabelActivity(){
        ActivityInfo activityInfo = null;
        try {
            activityInfo = getPackageManager().getActivityInfo(
                    getComponentName(), PackageManager.GET_META_DATA);
            mLabel = activityInfo.loadLabel(getPackageManager())
                    .toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    protected void bindViews() {
        ButterKnife.bind(this);
        setupToolbar();
        setupTitleAppBar();
    }


    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(mLabel);
        }
    }

    public Toolbar getToolbar(){
        return toolbar;
    }

    protected void setupTitleAppBar(){
        if (collapsingToolbarLayout!= null) {
            collapsingToolbarLayout.setTitleEnabled(false);
        }
    }

}
