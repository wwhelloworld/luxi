package com.lcsd.luxi.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.Frag01_list;
import com.lcsd.luxi.http.MyApplication;
import com.lcsd.luxi.utils.CommonUtils;
import com.lcsd.luxi.utils.GlideUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class DSLM_adapter extends RecyclerView.Adapter<DSLM_adapter.ViewHolder> {
    private List<Frag01_list.TColumnClick> list;
    /**
     * 事件回调监听
     */
    private DSLM_adapter.OnItemClickListener onItemClickListener;

    public DSLM_adapter(List<Frag01_list.TColumnClick> list) {
        this.list = list;
    }

    /**
     * 设置回调监听 * * @param listener
     */
    public void setOnItemClickListener(DSLM_adapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //实例化展示的view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dslm_recycleview, parent, false);
        //实例化viewholder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // Glide.with(MyApplication.getContext()).load(list.get(position).getThumb()).into(holder.imageView);
        GlideUtils.load(list.get(position).getThumb(), holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            }
        });
        //让最后一个位置设置一下
        if (position == list.size() - 1) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1); // , 1是可选写的
            lp.setMargins(CommonUtils.dip2px(4), CommonUtils.dip2px(5), CommonUtils.dip2px(8), CommonUtils.dip2px(15));
            holder.imageView.setLayoutParams(lp);
        }
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_item_dslm);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
