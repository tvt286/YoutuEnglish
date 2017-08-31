package youtuvideos.tranty.vn.youtuvideos.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.NativeExpressAdView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.activities.AddKnowledgesActivity;
import youtuvideos.tranty.vn.youtuvideos.activities.MainActivity;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.KnowledgeVO;
import youtuvideos.tranty.vn.youtuvideos.interfaces.ItemAddKnowledgeListeners;
import youtuvideos.tranty.vn.youtuvideos.preferences.UserShared;


public class AddKnowledgeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button";
    public static final String ACTION_DIS_LIKE_BUTTON_CLICKED = "action_dis_like_button_button";
    private static final int KNOWLEDGE_ITEM_VIEW_TYPE = 0;
    private static final int NATIVE_EXPRESS_AD_VIEW_TYPE = 1;

    private ArrayList<Object> mRecyclerViewItems = new ArrayList<>();
    public Context context;
    private ItemAddKnowledgeListeners listener;
    private long mLastClickTime = 0;

    public AddKnowledgeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case KNOWLEDGE_ITEM_VIEW_TYPE:
                View view = LayoutInflater.from(context).inflate(R.layout.item_add_knowledge, parent, false);
                KnowledgesViewHolder knowledgeViewHolder = new KnowledgesViewHolder(view);
                setupClickableViews(view, knowledgeViewHolder);
                return knowledgeViewHolder;
            case NATIVE_EXPRESS_AD_VIEW_TYPE:
                // fall through
            default:
                View nativeExpressLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.native_express_ad_container, parent, false);
                return new NativeExpressAdViewHolder(nativeExpressLayoutView);
        }
    }

    private void setupClickableViews(final View view, final KnowledgesViewHolder knowledgeViewHolder) {

        knowledgeViewHolder.imImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = knowledgeViewHolder.getAdapterPosition();
                listener.onImageClick(view, adapterPosition);
            }
        });

        knowledgeViewHolder.btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCommentsClick(view, knowledgeViewHolder.getAdapterPosition());
            }
        });
        knowledgeViewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // set delay click button like
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                doLike(v,knowledgeViewHolder);
            }
        });

    }

    private void doLike(View v, KnowledgesViewHolder knowledgeViewHolder) {
        boolean isLogin = UserShared.ins(context).getIsLogin();
        int adapterPosition = knowledgeViewHolder.getAdapterPosition();
        KnowledgeVO knowledgeVO = (KnowledgeVO) mRecyclerViewItems.get(adapterPosition);
        if (knowledgeVO.isLike == 1) {
            listener.onLikesClick(v, adapterPosition, false);
            if (isLogin) {
                knowledgeVO.likes--;
                knowledgeVO.isLike = 0;
                notifyItemChanged(adapterPosition, ACTION_DIS_LIKE_BUTTON_CLICKED);
            }
        } else {
            listener.onLikesClick(v, adapterPosition, true);
            if (isLogin) {
                knowledgeVO.likes++;
                knowledgeVO.isLike = 1;
                notifyItemChanged(adapterPosition, ACTION_LIKE_BUTTON_CLICKED);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        int viewType = getItemViewType(position);
        switch (viewType) {
            case KNOWLEDGE_ITEM_VIEW_TYPE:
                KnowledgeVO knowledgeVO = (KnowledgeVO) mRecyclerViewItems.get(position);
                ((KnowledgesViewHolder) viewHolder).bindView(knowledgeVO, context);
                break;
            case NATIVE_EXPRESS_AD_VIEW_TYPE:
                // fall through
            default:
                NativeExpressAdViewHolder nativeExpressHolder =
                        (NativeExpressAdViewHolder) viewHolder;
                NativeExpressAdView adView =
                        (NativeExpressAdView) mRecyclerViewItems.get(position);
                ViewGroup adCardView = (ViewGroup) nativeExpressHolder.itemView;
                if (adCardView.getChildCount() > 0) {
                    adCardView.removeAllViews();
                }
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }

                adCardView.addView(adView);
        }

    }

    /**
     * The {@link NativeExpressAdViewHolder} class.
     */
    public class NativeExpressAdViewHolder extends RecyclerView.ViewHolder {

        NativeExpressAdViewHolder(View view) {
            super(view);
        }

    }


    /**
     * Determines the view type for the given position.
     */
    @Override
    public int getItemViewType(int position) {
        return (position % AddKnowledgesActivity.ITEMS_PER_AD == 0) ? NATIVE_EXPRESS_AD_VIEW_TYPE
                : KNOWLEDGE_ITEM_VIEW_TYPE;
    }

    @Override
    public int getItemCount() {
        return mRecyclerViewItems.size();
    }

    public void setArrayRecyclerview(ArrayList<Object> arr) {
        this.mRecyclerViewItems = arr;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(ItemAddKnowledgeListeners listener) {
        this.listener = listener;
    }


    public class KnowledgesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btnComments)
        ImageButton btnComments;
        @BindView(R.id.view_content)
        RelativeLayout viewContent;
        @BindView(R.id.btnLike)
        ImageButton btnLike;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_likes)
        TextView tvLikes;
        @BindView(R.id.tv_comment)
        TextView tvComments;
        @BindView(R.id.ivImage)
        ImageView imImage;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        @BindView(R.id.tv_register)
        TextView tvRegister;
        KnowledgeVO knowledgeVO;

        public KnowledgesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

        public void bindView(KnowledgeVO knowledgeVO, Context context) {
            this.knowledgeVO = knowledgeVO;
            Picasso.with(context)
                    .load(knowledgeVO.image)
                    .placeholder(R.color.colorGrey)
                    .into(imImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            viewContent.setVisibility(View.VISIBLE);
                            viewContent.setAlpha(0.6f);
                        }

                        @Override
                        public void onError() {

                        }
                    });
            tvTitle.setText(knowledgeVO.title);
            //  tvDescription.setText(knowledgeVO.description);
            tvRegister.setText(String.valueOf(knowledgeVO.registers + " registers"));
            tvLikes.setText(String.valueOf(knowledgeVO.likes));
            tvComments.setText(String.valueOf(knowledgeVO.comments));
            if (knowledgeVO.isLike == 1)
                btnLike.setImageResource(R.drawable.ic_heart_red);
            else
                btnLike.setImageResource(R.drawable.ic_heart_outline_grey);
        }

        public KnowledgeVO getKnowledgeItem() {
            return knowledgeVO;
        }

    }


}
