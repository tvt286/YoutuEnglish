package youtuvideos.tranty.vn.youtuvideos.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.mics.Util;


// DefaultItemAnimator la animation mac dinh cua recyclerview

public class KnowledgeItemAnimator extends DefaultItemAnimator {
    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    Map<RecyclerView.ViewHolder, AnimatorSet> likeAnimationsMap = new HashMap<>();
    Map<RecyclerView.ViewHolder, AnimatorSet> heartAnimationsMap = new HashMap<>();

    // Khi một mục được thay đổi, ItemAnimator có thể quyết định nó muốn sử dụng lại ViewHolder tương tự cho hình ảnh động hoặc RecyclerView
    @Override
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    /** Chú thích
     * changeFlags: int: Thông tin thêm về những thay đổi xảy ra trong Adaptor về Item đại diện bởi ViewHolder này.
     * Ví dụ, nếu mục sẽ bị thay đổi khỏi bộ chuyển đổi, FLAG_CHANGED sẽ được thiết lập.
     *
     * payloads: Danh sách tải trọng mà trước đây đã được thông qua để
     * notifyItemChanged(int, Object)hoặc notifyItemRangeChanged(int, int, Object).
     **/
    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state,
                                                     @NonNull RecyclerView.ViewHolder viewHolder,
                                                     int changeFlags, @NonNull List<Object> payloads) {

        if (changeFlags == FLAG_CHANGED) {
            for (Object payload : payloads) {
                if (payload instanceof String) {
                    return new KnowledgeItemHolderInfo((String) payload);
                }
            }
        }

        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads);
    }



    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder,
                                 @NonNull RecyclerView.ViewHolder newHolder,
                                 @NonNull ItemHolderInfo preInfo,
                                 @NonNull ItemHolderInfo postInfo) {
        cancelCurrentAnimationIfExists(newHolder);

        if (preInfo instanceof KnowledgeItemHolderInfo) {
            KnowledgeItemHolderInfo feedItemHolderInfo = (KnowledgeItemHolderInfo) preInfo;
            AddKnowledgeAdapter.KnowledgesViewHolder holder = (AddKnowledgeAdapter.KnowledgesViewHolder) newHolder;

            if (AddKnowledgeAdapter.ACTION_LIKE_BUTTON_CLICKED.equals(feedItemHolderInfo.updateAction))
                animateHeartButton(holder,true);
            else if (AddKnowledgeAdapter.ACTION_DIS_LIKE_BUTTON_CLICKED.equals(feedItemHolderInfo.updateAction))
                animateHeartButton(holder,false);
            updateLikesCounter(holder, holder.getKnowledgeItem().likes);
        }

        return false;
    }

    //dung 1 animation hien tai
    private void cancelCurrentAnimationIfExists(RecyclerView.ViewHolder item) {
        if (likeAnimationsMap.containsKey(item)) {
            likeAnimationsMap.get(item).cancel();
        }
        if (heartAnimationsMap.containsKey(item)) {
            heartAnimationsMap.get(item).cancel();
        }
    }

    private void animateHeartButton(final AddKnowledgeAdapter.KnowledgesViewHolder holder ,final boolean isLike) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.btnLike, "rotation", 0f, 360f);
        rotationAnim.setDuration(300);
        rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.btnLike, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.btnLike, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (isLike)
                    holder.btnLike.setImageResource(R.drawable.ic_heart_red);
                else
                    holder.btnLike.setImageResource(R.drawable.ic_heart_outline_grey);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                heartAnimationsMap.remove(holder);
                dispatchChangeFinishedIfAllAnimationsEnded(holder);
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();

        heartAnimationsMap.put(holder, animatorSet);
    }

    private void updateLikesCounter(AddKnowledgeAdapter.KnowledgesViewHolder holder, int toValue) {
            holder.tvLikes.setText(String.valueOf(toValue));
    }


    private void dispatchChangeFinishedIfAllAnimationsEnded(AddKnowledgeAdapter.KnowledgesViewHolder holder) {
        if (likeAnimationsMap.containsKey(holder) || heartAnimationsMap.containsKey(holder)) {
            return;
        }

        dispatchAnimationFinished(holder);
    }


    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        super.endAnimation(item);
        cancelCurrentAnimationIfExists(item);
    }

    @Override
    public void endAnimations() {
        super.endAnimations();
        for (AnimatorSet animatorSet : likeAnimationsMap.values()) {
            animatorSet.cancel();
        }
    }

    public static class KnowledgeItemHolderInfo extends ItemHolderInfo {
        public String updateAction;

        public KnowledgeItemHolderInfo(String updateAction) {
            this.updateAction = updateAction;
        }
    }
}
