package youtuvideos.tranty.vn.youtuvideos.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import youtuvideos.tranty.vn.youtuvideos.MyApplication;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.abtracts.AbstractResponse;
import youtuvideos.tranty.vn.youtuvideos.activities.parents.BaseActivity;
import youtuvideos.tranty.vn.youtuvideos.adapters.FavoritesAdapter;
import youtuvideos.tranty.vn.youtuvideos.adapters.KnowledgeItemAnimator;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.KnowledgeVO;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.favorites.KnowledgesFavoritesVO;
import youtuvideos.tranty.vn.youtuvideos.interfaces.ItemFavoritesListeners;
import youtuvideos.tranty.vn.youtuvideos.mics.Constants;
import youtuvideos.tranty.vn.youtuvideos.myview.RevealBackgroundView;
import youtuvideos.tranty.vn.youtuvideos.preferences.UserShared;
import youtuvideos.tranty.vn.youtuvideos.requests.FavoritesRequest;
import youtuvideos.tranty.vn.youtuvideos.singleton.Favorites;
import youtuvideos.tranty.vn.youtuvideos.singleton.Knowledges;

public class FavoritesActitivty extends BaseActivity implements RevealBackgroundView.OnStateChangeListener, ItemFavoritesListeners {
    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

    @BindView(R.id.vRevealBackground)
    RevealBackgroundView vRevealBackground;
    @BindView(R.id.rc_favorites)
    RecyclerView rcFavorites;
    @BindView(R.id.layout_loading)
    LinearLayout layout_loading;
    @BindView(R.id.view_network)
    RelativeLayout layout_network;
    @BindView(R.id.layout_null)
    RelativeLayout layout_null;
    @BindView(R.id.tv_null)
    TextView tvNull;
    @BindView(R.id.im_null)
    ImageView imNull;

    private FavoritesAdapter adapter;
    private ArrayList<KnowledgesFavoritesVO> arrKnowledgesFavorites;
    private ArrayList<KnowledgeVO> arrKnowledges;

    public static void startFavoritesFromLocation(int[] startingLocation, Activity startingActivity) {
        Intent intent = new Intent(startingActivity, FavoritesActitivty.class);
        intent.putExtra(ARG_REVEAL_START_LOCATION, startingLocation);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitivty_favorites);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setupRecycler();
        setupRevealBackground(savedInstanceState);
        arrKnowledges = Knowledges.ins(this).getArrayKnowledges();
    }

    private void isLoading() {
        layout_loading.setVisibility(View.VISIBLE);
        layout_null.setVisibility(View.GONE);
        layout_network.setVisibility(View.GONE);
        rcFavorites.setVisibility(View.GONE);
    }

    private void getFavorites() {

        if (isLogin) {
            isLoading();
            FavoritesRequest.getKnowledgesFavorites(userLoginVO.getGoogleID(), new AbstractResponse() {
                @Override
                public void onSuccess(int error_code, String message, Object obj) {
                    super.onSuccess(error_code, message, obj);
                    layout_network.setVisibility(View.GONE);
                    layout_loading.setVisibility(View.GONE);
                    if (error_code == 0) {
                        arrKnowledgesFavorites = (ArrayList<KnowledgesFavoritesVO>) obj;
                        if (arrKnowledgesFavorites != null) {
                            adapter.updateItems(arrKnowledgesFavorites);
                            rcFavorites.setVisibility(View.VISIBLE);
                        }
                    } else if (error_code == 3) {
                        imNull.setImageResource(R.drawable.ic_favorites_null);
                        tvNull.setText(getString(R.string.null_favorites));
                        layout_null.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                    layout_null.setVisibility(View.GONE);
                    layout_network.setVisibility(View.VISIBLE);
                    layout_loading.setVisibility(View.GONE);
                    rcFavorites.setVisibility(View.GONE);
                }
            });
        }
    }


    @OnClick(R.id.view_network)
    public void clickNetwork() {
        getFavorites();
    }

    private void setupRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adapter = new FavoritesAdapter(this);
        rcFavorites.setLayoutManager(linearLayoutManager);
        rcFavorites.setItemAnimator(new KnowledgeItemAnimator());
        rcFavorites.setHasFixedSize(true);
        rcFavorites.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    private void setupRevealBackground(Bundle savedInstanceState) {
        vRevealBackground.setOnStateChangeListener(this);
        if (savedInstanceState == null) {
            final int[] startingLocation = getIntent().getIntArrayExtra(ARG_REVEAL_START_LOCATION);
            vRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    vRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                    vRevealBackground.startFromLocation(startingLocation);
                    return true;
                }
            });
        } else {
            vRevealBackground.setToFinishedFrame();

        }
    }


    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
            if (!MyApplication.getAppInstance().getFavoritesChange()) {
                arrKnowledgesFavorites = Favorites.ins(this).getArrayFavorites();
                if (arrKnowledgesFavorites.size() == 0)
                    getFavorites();
                else {
                    rcFavorites.setVisibility(View.VISIBLE);
                    adapter.updateItems(arrKnowledgesFavorites);
                }
            }else {
                getFavorites();
                MyApplication.getAppInstance().setFavoritesChange(false);
            }
        } else {
            rcFavorites.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCommentsClick(View v, int position) {
        final Intent intent = new Intent(this, CommentsActivity.class);
        int[] startingLocation = new int[2];
        v.getLocationOnScreen(startingLocation);
        intent.putExtra(CommentsActivity.ARG_DRAWING_START_LOCATION, startingLocation[1]);
        intent.putExtra(Constants.KEY_INTENT.KNOWLEDGE_ID, arrKnowledgesFavorites.get(position).knowledge.id);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onItemClick(View v, int position) {
        KnowledgeVO knowledgeVO = arrKnowledgesFavorites.get(position).knowledge;
        int[] startingLocation = new int[2];
        v.getLocationOnScreen(startingLocation);
        startingLocation[0] += v.getWidth() / 2;
        ModulesActivity.startModulesFromLocation(startingLocation, FavoritesActitivty.this, knowledgeVO);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onDeleteClick(View v, final int position) {
        showDeleteDialog(position);
    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FavoritesActitivty.this);
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

        KnowledgesFavoritesVO knowledgesFavoritesVO = arrKnowledgesFavorites.get(position);
        FavoritesRequest.removeKnowledgesFavorites(userLoginVO.getGoogleID(), knowledgesFavoritesVO.id, new AbstractResponse() {
            @Override
            public void onSuccess(int error_code, String message, Object obj) {
                super.onSuccess(error_code, message, obj);
                if (error_code == 0) {
                    arrKnowledgesFavorites.remove(position);
                    adapter.notifyItemRemoved(position);
                    if (arrKnowledgesFavorites.size() == 0) {
                        imNull.setImageResource(R.drawable.ic_favorites_null);
                        tvNull.setText(getString(R.string.null_favorites));
                        layout_null.setVisibility(View.VISIBLE);
                        rcFavorites.setVisibility(View.GONE);
                    }
                    Toast.makeText(getBaseContext(), Constants.MESSAGE.DELETE_SUCCESS, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure() {
                super.onFailure();
            }
        });
    }

    // set like cho khóa học
    private void getLikes(ArrayList<KnowledgesFavoritesVO> favorites) {
        for (int j = 0; j < arrKnowledges.size(); j++) {
            if (favorites == null) { // nếu null thì islike = 0 hết
                for (int i = 0; i < favorites.size(); i++) {
                    KnowledgeVO knowledgeVO = arrKnowledges.get(j);
                    if (favorites.get(i).knowledge_id == knowledgeVO.id) {
                        arrKnowledges.get(j).isLike = 1;
                        break;
                    } else
                        arrKnowledges.get(j).isLike = 0;
                }
            } else
                arrKnowledges.get(j).isLike = 0;
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

    @Override
    protected void onStop() {
        super.onStop();
        Favorites.ins(getBaseContext()).setArrayFavorites(arrKnowledgesFavorites);
        if (arrKnowledges.size() != 0 || arrKnowledges != null)
            getLikes(arrKnowledgesFavorites);
        Knowledges.ins(this).setArrayKnowledges(arrKnowledges);
    }
}
