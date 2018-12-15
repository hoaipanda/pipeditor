package com.huantansheng.easyphotos.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huantansheng.easyphotos.R;
import com.huantansheng.easyphotos.models.sticker.StickerModel;
import com.huantansheng.easyphotos.models.sticker.entity.TextStickerData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.TextViewHolder> {
    private Context context;
    private ArrayList<String> datas;
    private OnItemClickListener onItemClickListener;

    public BackgroundAdapter(Context cxt, ArrayList<String> datas) {
        this.datas = datas;
        this.context = cxt;
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_sticker_easy_photos, parent, false);
        return new TextViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, int position) {
        final String bg = datas.get(position);
        Log.e("hoaiii", bg);
        Glide.with(context).load(Uri.parse("file:///android_asset/background/" + bg)).into(holder.imBg);
//        holder.imBg.setImageResource(R.drawable.bg1);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(bg);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    public static class TextViewHolder extends RecyclerView.ViewHolder {

        ImageView imBg;

        public TextViewHolder(View itemView) {
            super(itemView);
            imBg = itemView.findViewById(R.id.imBg);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String stickerValue);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
