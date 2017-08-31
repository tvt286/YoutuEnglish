package youtuvideos.tranty.vn.youtuvideos.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.dao.comments.CommentVO;
import youtuvideos.tranty.vn.youtuvideos.mics.RoundedTransformation;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private Context context;
    private int lastAnimatedPosition = -1;
    private int avatarSize;

    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    private ArrayList<CommentVO> arrComments = new ArrayList<>();

    public CommentsAdapter(Context context) {
        this.context = context;
        avatarSize = context.getResources().getDimensionPixelSize(R.dimen.comment_avatar_size);
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder viewHolder, int position) {
        runEnterAnimation(viewHolder.itemView, position);
       CommentVO commentVO = arrComments.get(position);
        String content = "<strong>" + commentVO.user.name + ":</strong> " + commentVO.content;
        viewHolder.tvComment.setText(Html.fromHtml(content));
        Picasso.with(context)
                .load(commentVO.user.image)
                .centerCrop()
                .resize(avatarSize, avatarSize)
                .transform(new RoundedTransformation())
                .into(viewHolder.ivUserAvatar);
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
    public int getItemCount() {
        return arrComments.size();
    }

    public void updateItems(ArrayList<CommentVO> arr) {
        this.arrComments = arr;
        notifyDataSetChanged();
    }

    public void addItem(CommentVO commentVO) {
        arrComments.add(commentVO);
        notifyItemInserted(arrComments.size() - 1);
    }

    public void setAnimationsLocked(boolean animationsLocked) {
        this.animationsLocked = animationsLocked;
    }

    public void setDelayEnterAnimation(boolean delayEnterAnimation) {
        this.delayEnterAnimation = delayEnterAnimation;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivUserAvatar)
        ImageView ivUserAvatar;
        @BindView(R.id.tvComment)
        TextView tvComment;

        public CommentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
