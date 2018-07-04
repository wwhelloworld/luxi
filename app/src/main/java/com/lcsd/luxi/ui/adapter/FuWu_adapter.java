package com.lcsd.luxi.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcsd.luxi.R;
import com.lcsd.luxi.entity.Commen_Info;
import com.lcsd.luxi.entity.FuWu_List_Info;
import com.lcsd.luxi.utils.GlideUtils;

import java.util.List;

public class FuWu_adapter extends RecyclerView.Adapter<FuWu_adapter.ViewHolder> {
    private List<FuWu_List_Info.TContent.TRslist> list1;
    private List<Commen_Info> list2;
    /**
     * 事件回调监听
     */
    private OnItemClickListener onItemClickListener;

    public FuWu_adapter(List<FuWu_List_Info.TContent.TRslist> list1, List<Commen_Info> list2) {
        this.list1 = list1;
        this.list2 = list2;
    }
    /**
     * 设置回调监听 * * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //实例化展示的view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fuwu_recycleview, parent, false);
        //实例化viewholder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (list2 != null) {
            holder.imageView.setBackgroundResource(list2.get(position).getImage());
            // GlideUtils.load((String) list.get(position).getImage(), holder.imageView);
            holder.textView.setText(list2.get(position).getTitle());

        } else {
            GlideUtils.load(list1.get(position).getThumb(), holder.imageView);
            holder.textView.setText(list1.get(position).getTitle());
        }
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
        if (list2 != null) {
            return list2 == null ? 0 : list2.size();
        } else {
            return list1 == null ? 0 : list1.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_item_fuwu);
            textView = itemView.findViewById(R.id.tv_item_fuwu);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
