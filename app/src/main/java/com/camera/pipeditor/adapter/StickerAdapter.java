package com.camera.pipeditor.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.camera.pipeditor.R;

import java.util.ArrayList;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
    private ArrayList<String> listSticker;
    private Context context;

    public StickerAdapter(Context context, ArrayList<String> listSticker) {
        this.context = context;
        this.listSticker = listSticker;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_sticker, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final String sticker = listSticker.get(i);
        Glide.with(context).load(Uri.parse("file:///android_asset/sticker/" + sticker)).into(viewHolder.imSticker);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickItemSticker != null) {
                    onClickItemSticker.onClickSticker(sticker);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSticker.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imSticker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imSticker = itemView.findViewById(R.id.imSticker);
        }
    }

    public interface OnClickItemSticker {
        void onClickSticker(String sticker);
    }

    public OnClickItemSticker onClickItemSticker;

    public void setOnClickItemSticker(OnClickItemSticker onClickItemSticker) {
        this.onClickItemSticker = onClickItemSticker;
    }
}
