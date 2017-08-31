package youtuvideos.tranty.vn.youtuvideos.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.dao.modules.ModuleVO;
import youtuvideos.tranty.vn.youtuvideos.interfaces.ItemModulesListeners;
import youtuvideos.tranty.vn.youtuvideos.mics.LoaderImage;
import youtuvideos.tranty.vn.youtuvideos.mics.Util;


public class VideoModulesAdapter extends RecyclerView.Adapter<VideoModulesAdapter.ViewHolderLessons>   {

    private LayoutInflater layoutInflater;
    private static Context mContext;
    private static ArrayList<ModuleVO> arrModules = new ArrayList<>();
    private ItemModulesListeners listeners;
    public VideoModulesAdapter(Context context, ItemModulesListeners listeners) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.listeners = listeners;
    }

    public void setArrModules(ArrayList<ModuleVO> arr) {
        arrModules = arr;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolderLessons onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_learning, parent, false);
        ViewHolderLessons holder = new ViewHolderLessons(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolderLessons holder, int position) {
        final ModuleVO moduleVO = arrModules.get(position);
        holder.tvTitle.setText(moduleVO.title);
        holder.tvLength.setText(Util.secondsToString(moduleVO.length));
        LoaderImage.ins(mContext).show(moduleVO.image,holder.imLesson);
        Picasso.with(mContext)
                .load(moduleVO.image)
                .placeholder(R.color.colorGrey)
                .into(holder.imLesson, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (moduleVO.completed == 1)
                            holder.viewCompleted.setVisibility(View.VISIBLE);
                        else
                            holder.viewCompleted.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError() {

                    }
                });
        holder.tvTitle.setText(moduleVO.title);

    }

    @Override
    public int getItemCount() {
        return arrModules.size();
    }


    class ViewHolderLessons extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle, tvLength;
        private ImageView imLesson;
        private LinearLayout item;
        private LinearLayout viewCompleted;
        public ViewHolderLessons(final View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvLength = (TextView) itemView.findViewById(R.id.tv_length);
            imLesson = (ImageView) itemView.findViewById(R.id.image);
            viewCompleted = (LinearLayout) itemView.findViewById(R.id.view_completed);
            item = (LinearLayout) itemView.findViewById(R.id.item_view);
            item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listeners != null)
                listeners.onItemClicked(v,getAdapterPosition());
        }
    }

}
