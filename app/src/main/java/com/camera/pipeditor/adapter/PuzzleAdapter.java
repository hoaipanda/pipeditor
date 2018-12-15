package com.camera.pipeditor.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.camera.pipeditor.R;

import java.util.ArrayList;

public class PuzzleAdapter extends RecyclerView.Adapter<PuzzleAdapter.ViewHolder> {
    private ArrayList<String> listThumb;
    private Context context;
    private int pos = 0;
    private String type;

    public PuzzleAdapter(Context context, ArrayList<String> listThumb, String type) {
        this.context = context;
        this.listThumb = listThumb;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.item_puzzle, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final String thumb = listThumb.get(i);

        Glide.with(context).load("file:///android_asset/puzzle/" + type + "/" + "thumb/" + thumb).into(viewHolder.imPuzzle);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickItemPuzzle != null) {
                    onClickItemPuzzle.onClickPuzzle(i);
                    pos = viewHolder.itemPos;
                    notifyDataSetChanged();
                }
            }
        });

        if (pos == i) {
            viewHolder.imCheck.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imCheck.setVisibility(View.GONE);
        }

        viewHolder.itemPos = i;
    }

    @Override
    public int getItemCount() {
        return listThumb.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imPuzzle, imCheck;
        int itemPos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imPuzzle = itemView.findViewById(R.id.imPuzzle);
            imCheck = itemView.findViewById(R.id.imCheck);
        }
    }

    public interface OnClickItemPuzzle {
        void onClickPuzzle(int pos);
    }

    public OnClickItemPuzzle onClickItemPuzzle;

    public void setOnClickItemPuzzle(OnClickItemPuzzle onClickItemPuzzle) {
        this.onClickItemPuzzle = onClickItemPuzzle;
    }
}
