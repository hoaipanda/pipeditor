package com.camera.pipeditor.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.camera.pipeditor.R;
import com.camera.pipeditor.data.Photo;

import java.io.File;
import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private ArrayList<Photo> listImage;
    private Context context;
    private ArrayList<Photo> listAdd;

    public PhotoAdapter(Context context, ArrayList<Photo> listImage, ArrayList<Photo> listAdd) {
        this.context = context;
        this.listImage = listImage;
        this.listAdd = listAdd;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_photo, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Photo photo = listImage.get(i);
        Glide.with(context).load(Uri.fromFile(new File(photo.getName()))).into(viewHolder.imPhoto);
        if (listAdd.contains(photo)) {
            viewHolder.lyNumber.setVisibility(View.VISIBLE);
            viewHolder.tvNum.setText(listAdd.indexOf(photo) + 1 + "");
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickItemPhoto != null) {
                    onClickItemPhoto.onClickPhoto(photo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listImage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imPhoto;
        RelativeLayout lyNumber;
        TextView tvNum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imPhoto = itemView.findViewById(R.id.imPhoto);
            lyNumber = itemView.findViewById(R.id.lyNumber);
            tvNum = itemView.findViewById(R.id.tvNum);
        }
    }

    public OnClickItemPhoto onClickItemPhoto;

    public void setOnClickItemPhoto(OnClickItemPhoto onClickItemPhoto) {
        this.onClickItemPhoto = onClickItemPhoto;
    }

    public interface OnClickItemPhoto {
        void onClickPhoto(Photo photo);
    }
}
