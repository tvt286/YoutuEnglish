package youtuvideos.tranty.vn.youtuvideos.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.dao.modules.ModuleVO;
import youtuvideos.tranty.vn.youtuvideos.interfaces.ItemModulesListeners;
import youtuvideos.tranty.vn.youtuvideos.mics.Util;


public class ModulesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final Context context;
    private ArrayList<ModuleVO> arrModules = new ArrayList<>();
    public ItemModulesListeners listeners;
    private int iStt =1;
    public ModulesAdapter(Context context) {
        this.context = context;
    }

    public void setArrModules(ArrayList<ModuleVO> arr){
        this.arrModules = arr;
        notifyDataSetChanged();
    }

    public void setListeners(ItemModulesListeners listeners){
        this.listeners = listeners;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_module, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindPhoto((PhotoViewHolder) holder, position);
    }

    private void bindPhoto(final PhotoViewHolder holder, int position) {
        ModuleVO sessionsVO = arrModules.get(position);
        holder.tvTitle.setText(sessionsVO.title);
        holder.tvLength.setText(Util.secondsToString(sessionsVO.length));
        Picasso.with(context)
                .load(arrModules.get(position).image)
                .placeholder(R.color.colorGrey)
                .into(holder.image);
    }


    @Override
    public int getItemCount() {
        return arrModules.size();
    }

     class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.llRoot)
        LinearLayout llRoot;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_length)
        TextView tvLength;
        @BindView(R.id.image)
        ImageView image;

        public PhotoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            llRoot.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (listeners != null)
                listeners.onItemClicked(v,getAdapterPosition());
        }
    }
}
