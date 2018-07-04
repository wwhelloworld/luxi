package com.lcsd.luxi.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.Frag01_list;
import com.lcsd.luxi.utils.DateUtils;
import com.lcsd.luxi.utils.GlideUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class Lanmu_adapter extends RecyclerView.Adapter<Lanmu_adapter.ViewHolder> {
    private List<Frag01_list.TNewxmlbList> list;
    /**
     * 事件回调监听
     */
    private Lanmu_adapter.OnItemClickListener onItemClickListener;

    public Lanmu_adapter(List<Frag01_list.TNewxmlbList> list) {
        this.list = list;
    }

    /**
     * 设置回调监听 * * @param listener
     */
    public void setOnItemClickListener(Lanmu_adapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //实例化展示的view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lanmu_recycleview, parent, false);
        //实例化viewholder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        GlideUtils.load(list.get(position).getThumb(), holder.imageView);
        holder.textView.setText(list.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            }
        });
    }




    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_re_lanmu);
            textView = itemView.findViewById(R.id.tv_re_lanmu);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
