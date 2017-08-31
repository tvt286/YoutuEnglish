package youtuvideos.tranty.vn.youtuvideos.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.view.ViewCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import youtuvideos.tranty.vn.youtuvideos.MyApplication;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.abtracts.AbstractResponse;
import youtuvideos.tranty.vn.youtuvideos.activities.parents.BaseActivity;
import youtuvideos.tranty.vn.youtuvideos.adapters.CommentsAdapter;
import youtuvideos.tranty.vn.youtuvideos.dao.comments.CommentVO;
import youtuvideos.tranty.vn.youtuvideos.mics.Constants;
import youtuvideos.tranty.vn.youtuvideos.mics.Util;
import youtuvideos.tranty.vn.youtuvideos.myview.SendCommentButton;
import youtuvideos.tranty.vn.youtuvideos.preferences.UserShared;
import youtuvideos.tranty.vn.youtuvideos.requests.KnowledgesRequest;
import youtuvideos.tranty.vn.youtuvideos.singleton.Comments;

public class CommentsActivity extends BaseActivity implements SendCommentButton.OnSendClickListener {
    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";

    @BindView(R.id.tv_null)
    TextView tvNull;
    @BindView(R.id.im_null)
    ImageView imNull;
    @BindView(R.id.layout_loading)
    LinearLayout layout_loading;
    @BindView(R.id.layout_null)
    RelativeLayout layout_null;
    @BindView(R.id.contentRoot)
    LinearLayout contentRoot;
    @BindView(R.id.rvComments)
    RecyclerView rvComments;
    @BindView(R.id.llAddComment)
    LinearLayout llAddComment;
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.btnSendComment)
    SendCommentButton btnSendComment;

    private ArrayList<CommentVO> arrComments;
    private CommentVO commentVO;
    private CommentsAdapter commentsAdapter;
    private int drawingStartLocation;
    private int knowledgeId;
    private long mLastClickTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getData();
        setupComments();
        setupSendCommentButton();

        etComment.setMovementMethod(new ScrollingMovementMethod());
        drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
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

    private void getData(){
        knowledgeId = getIntent().getIntExtra(Constants.KEY_INTENT.KNOWLEDGE_ID, 0);
        arrComments = Comments.ins(this).getArrayComments(knowledgeId); // lấy data từ application
    }
    private void setupComments() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.setHasFixedSize(true);

        commentsAdapter = new CommentsAdapter(this);
        rvComments.setAdapter(commentsAdapter);
        rvComments.setOverScrollMode(View.OVER_SCROLL_NEVER);
        rvComments.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    commentsAdapter.setAnimationsLocked(true);
                }
            }
        });
    }

    private void setupSendCommentButton() {
        btnSendComment.setOnSendClickListener(this);
    }

    private void startIntroAnimation() {
        ViewCompat.setElevation(getToolbar(), 0);
        contentRoot.setScaleY(0.1f);
        contentRoot.setPivotY(drawingStartLocation);
        llAddComment.setTranslationY(200);

        contentRoot.animate()
                .scaleY(1)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ViewCompat.setElevation(getToolbar(), Util.dpToPx(8));
                        if (arrComments == null)
                            setContent();
                        else {
                            commentsAdapter.updateItems(arrComments);
                            animationViewComments();
                        }

                    }
                })
                .start();
    }

    private void setContent() {
        layout_loading.setVisibility(View.VISIBLE);
        KnowledgesRequest.getKnowledgeComments(knowledgeId, new AbstractResponse() {
            @Override
            public void onSuccess(int error_code, String message, Object obj) {
                super.onSuccess(error_code, message, obj);
                layout_loading.setVisibility(View.GONE);
                arrComments = (ArrayList<CommentVO>) obj;
                if (arrComments != null) {
                    Comments.ins(getBaseContext()).setArrayComments(knowledgeId,arrComments); // save data
                    rvComments.setVisibility(View.VISIBLE);
                    layout_null.setVisibility(View.GONE);
                    commentsAdapter.updateItems(arrComments);
                    commentsAdapter.setAnimationsLocked(false);
                    commentsAdapter.setDelayEnterAnimation(false);
                } else {
                    rvComments.setVisibility(View.GONE);
                    layout_null.setVisibility(View.VISIBLE);
                    tvNull.setText(getResources().getString(R.string.add_comment));
                    imNull.setImageResource(R.drawable.icon_comment_null);
                }
                animationViewComments();

            }

            @Override
            public void onFailure() {
                super.onFailure();
            }
        });

    }

    private void animationViewComments(){
        llAddComment.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .start();
    }
    @Override
    public void onBackPressed() {
        animationBack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                animationBack();
                break;
        }
        return true;
    }

    // on back
    private void animationBack() {
        ViewCompat.setElevation(getToolbar(), 0);
        contentRoot.animate()
                .translationY(Util.getScreenHeight(this))
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finish();
                        overridePendingTransition(0, 0);
                    }
                })
                .start();
    }

    @Override
    public void onSendClickListener(View v) {
        //set delay click
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (validateComment()) {
            isLogin = UserShared.ins(this).getIsLogin();
            if (isLogin) {
                doSendComment();
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Comments.ins(this).setArrayComments(knowledgeId,arrComments);
    }

    private void doSendComment() {
        String content = etComment.getText().toString();
        String user_id = userLoginVO.getGoogleID();
        KnowledgesRequest.addKnowledgesComments(knowledgeId, user_id, content, new AbstractResponse() {
            @Override
            public void onSuccess(int error_code, String message, Object obj) {
                super.onSuccess(error_code, message, obj);
                if (error_code == 0) {
                    layout_null.setVisibility(View.GONE);
                    rvComments.setVisibility(View.VISIBLE);
                    commentVO = (CommentVO) obj;
                    commentsAdapter.addItem(commentVO);
                 if (arrComments != null)
                        rvComments.smoothScrollBy(0, rvComments.getChildAt(0).getHeight() * commentsAdapter.getItemCount());
                    etComment.setText(null);
                    btnSendComment.setCurrentState(SendCommentButton.STATE_DONE);
                }
            }

            @Override
            public void onFailure() {
                super.onFailure();
            }
        });
    }


    private boolean validateComment() {
        if (TextUtils.isEmpty(etComment.getText())) {
            btnSendComment.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_error));
            return false;
        }
        return true;
    }

}
