package youtuvideos.tranty.vn.youtuvideos.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.dao.users.knowledges.KnowledgesUserVO;
import youtuvideos.tranty.vn.youtuvideos.interfaces.ItemKnowledgeListeners;
import youtuvideos.tranty.vn.youtuvideos.mics.Util;


public class KnowledgesUserAdapter extends RecyclerView.Adapter<KnowledgesUserAdapter.ViewHolderKnowledge> {
    private LayoutInflater layoutInflater;
    private static Context mContext;
    private static ArrayList<KnowledgesUserVO> arrKnowledges = new ArrayList<>();
    private ItemKnowledgeListeners clickListener;
    private Boolean showAll = false;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();

    public KnowledgesUserAdapter(Context context, ItemKnowledgeListeners clickListener) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.clickListener = clickListener;
    }

    public void setArrKnowledges(ArrayList<KnowledgesUserVO> arr) {
        arrKnowledges = arr;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderKnowledge onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_knowledge_user, parent, false);
        ViewHolderKnowledge holder = new ViewHolderKnowledge(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderKnowledge holder, int position) {
        KnowledgesUserVO knowledgeVO = arrKnowledges.get(position);
        Picasso.with(mContext)
                .load(knowledgeVO.knowledge.image)
                .placeholder(R.color.colorGrey)
                .into(holder.imKnowledge);
        holder.tvKnowledge.setText(String.valueOf(knowledgeVO.knowledge.title));
        holder.tvTotalVideos.setText(String.valueOf(knowledgeVO.knowledge.total_video + " videos"));
        if (knowledgeVO.schedule_actived == 1) {
            holder.imPause.setImageResource(R.drawable.ic_lock_white);
            holder.imStatus.setImageResource(R.drawable.ic_unlock);
        } else {
            holder.imPause.setImageResource(R.drawable.ic_unlock_white);
            holder.imStatus.setImageResource(R.drawable.ic_lock);
        }
        holder.tvProportion.setText(String.valueOf(knowledgeVO.completed + "%"));

        int width = Util.getScreenWidth(mContext) - Util.dpToPx(106);
        ViewGroup.LayoutParams params = holder.viewCompleted.getLayoutParams();
        params.width = (int)(knowledgeVO.completed * width) / 100;
        binderHelper.bind(holder.swipeLayout, String.valueOf(knowledgeVO.id));

    }

    @Override
    public int getItemCount() {
        return arrKnowledges.size();
    }


    class ViewHolderKnowledge extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.im_knowledge)
        CircleImageView imKnowledge;
        @BindView(R.id.tv_knowledge)
        TextView tvKnowledge;
        @BindView(R.id.tv_proportion)
        TextView tvProportion;
        @BindView(R.id.tv_number_knowledges)
        TextView tvTotalVideos;
        @BindView(R.id.swipe_layout)
        SwipeRevealLayout swipeLayout;
        @BindView(R.id.delete_layout)
        RelativeLayout deleteLayout;
        @BindView(R.id.pause_layout)
        RelativeLayout pauseLayout;
        @BindView(R.id.im_status)
        ImageView imStatus;
        @BindView(R.id.im_pause)
        ImageView imPause;
        @BindView(R.id.view_item)
        LinearLayout view_item;
        @BindView(R.id.view_completed)
        RelativeLayout viewCompleted;

        public ViewHolderKnowledge(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view_item.setOnClickListener(this);
            deleteLayout.setOnClickListener(this);
            pauseLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.view_item:
                    if (clickListener != null) {
                        clickListener.onItemClicked(v, this.getAdapterPosition());
                    }
                    break;
                case R.id.delete_layout:
                    if (clickListener != null) {
                        clickListener.onDeleteClicked(v, this.getAdapterPosition());
                    }
                    break;
                case R.id.pause_layout:
                    if (clickListener != null) {
                        clickListener.onStatusClicked(v, this.getAdapterPosition());
                    }
                    break;

            }
        }

    }

}
