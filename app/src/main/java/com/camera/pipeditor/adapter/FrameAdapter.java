/**
 * Created by Jake Gonzales on 10/26/2017 2:48:01 PM
 */
package com.camera.pipeditor.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.camera.pipeditor.R;
import com.camera.pipeditor.data.FrameModel;

import java.util.ArrayList;

public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.FrameViewHolder> {
    private ArrayList<FrameModel> arrayList;
    private OnListenerFrame onListenerFrame;
    private int selected;

    public FrameAdapter(ArrayList<FrameModel> arrayList) {
        this.arrayList = arrayList;
        this.selected = 0;
    }

    public class FrameViewHolder extends RecyclerView.ViewHolder {
        ImageView img_frame, img_select;
        TextView tvName;

        public FrameViewHolder(@NonNull View itemView) {
            super(itemView);
            img_frame = itemView.findViewById(R.id.img_frame);
            img_select = itemView.findViewById(R.id.imgSelected);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    @NonNull
    @Override
    public FrameViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_frame, viewGroup, false);
        FrameViewHolder frameViewHolder = new FrameViewHolder(view);
        return frameViewHolder;
    }

    @Override
    public void onBindViewHolder(FrameViewHolder holder, final int position) {
        final FrameModel frameModel = arrayList.get(position);
        Glide.with(holder.itemView.getContext()).load("file:///android_asset/frame/" +frameModel.getName()+"/"+frameModel.getName()+"_sm.jpg").into(holder.img_frame);
        holder.tvName.setText(frameModel.getTitle());
        if (position == selected) {
            holder.img_select.setVisibility(View.VISIBLE);
            holder.tvName.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.yellow));
        } else {
            holder.img_select.setVisibility(View.GONE);
            holder.tvName.setTextColor(Color.BLACK);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected != position) {
                    selected = position;
                    onListenerFrame.onClickItemFrame(frameModel, position);
                    notifyDataSetChanged();
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface OnListenerFrame {
        void onClickItemFrame(FrameModel frameModel, int position);
    }


    public void setOnListenerFrame(OnListenerFrame onListenerFrame) {
        this.onListenerFrame = onListenerFrame;
    }
}
