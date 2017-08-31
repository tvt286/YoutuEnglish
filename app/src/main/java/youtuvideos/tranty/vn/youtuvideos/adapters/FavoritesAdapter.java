package youtuvideos.tranty.vn.youtuvideos.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.KnowledgeVO;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.favorites.KnowledgesFavoritesVO;
import youtuvideos.tranty.vn.youtuvideos.interfaces.ItemFavoritesListeners;


public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_DEFAULT = 1;


    private ArrayList<KnowledgesFavoritesVO> arrFavorites = new ArrayList<>();

    public Context context;
    private ItemFavoritesListeners listener;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    private int lastAnimatedPosition = -1;

    public FavoritesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DEFAULT) { // load item
            View view = LayoutInflater.from(context).inflate(R.layout.item_favorites, parent, false);
            KnowledgesViewHolder knowledgesViewHolder = new KnowledgesViewHolder(view);
            setupClickableViews(view, knowledgesViewHolder);
            return knowledgesViewHolder;
        }
        return null;
    }

    private void setupClickableViews(final View view, final KnowledgesViewHolder knowledgeViewHolder) {
        knowledgeViewHolder.btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCommentsClick(view, knowledgeViewHolder.getAdapterPosition());
            }
        });

        knowledgeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = knowledgeViewHolder.getAdapterPosition();
                listener.onItemClick(view, adapterPosition);
            }
        });

        knowledgeViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = knowledgeViewHolder.getAdapterPosition();
                listener.onDeleteClick(v,adapterPosition);
            }
        });

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        runEnterAnimation(viewHolder.itemView, position);
        ((KnowledgesViewHolder) viewHolder).bindView(arrFavorites.get(position).knowledge, context);

    }

    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public int getItemCount() {
        return arrFavorites.size();
    }

    public void updateItems(ArrayList<KnowledgesFavoritesVO> arr) {
        this.arrFavorites = arr;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(ItemFavoritesListeners listener) {
        this.listener = listener;
    }

    public static class KnowledgesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage)
        ImageView imImage;
        @BindView(R.id.btnComments)
        ImageButton btnComments;
        @BindView(R.id.btn_delete)
        ImageView btnDelete;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_register)
        TextView tvRegister;
        @BindView(R.id.tv_likes)
        TextView tvLikes;
        @BindView(R.id.tv_comment)
        TextView tvComments;
        @BindView(R.id.view_content)
        RelativeLayout viewContent;
        @BindView(R.id.tv_description)
        TextView tvDescription;

        KnowledgeVO knowledgeVO;

        public KnowledgesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindView(KnowledgeVO knowledgeVO, Context context) {
            this.knowledgeVO = knowledgeVO;
            Typeface font1 = Typeface.createFromAsset(context.getAssets(), "Roboto-Italic.ttf");
            Typeface font2 = Typeface.createFromAsset(context.getAssets(), "Roboto-Bold.ttf");
            tvDescription.setTypeface(font1);
            tvRegister.setTypeface(font1);
            tvTitle.setTypeface(font2);
            Picasso.with(context)
                    .load(knowledgeVO.image)
                    .placeholder(R.color.colorGrey)
                    .into(imImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            viewContent.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
            tvLikes.setText(String.valueOf(knowledgeVO.likes));
            tvRegister.setText(String.valueOf(knowledgeVO.registers + " registers"));
            tvTitle.setText(knowledgeVO.title);
            tvComments.setText(String.valueOf(knowledgeVO.comments));
        }




    }


}
