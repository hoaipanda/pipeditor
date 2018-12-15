package com.camera.pipeditor.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.camera.pipeditor.R;
import com.camera.pipeditor.data.ItemFilter;

import java.util.ArrayList;

public class AdjustAdapter extends RecyclerView.Adapter<AdjustAdapter.ViewHolder> {
    private ArrayList<ItemFilter> filters;

    public AdjustAdapter(ArrayList<ItemFilter> filters) {
        this.filters = filters;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_adjust, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final ItemFilter itemFilter = filters.get(i);
        viewHolder.imAdjust.setImageResource(itemFilter.getImage());
        viewHolder.tvAdjust.setText(itemFilter.getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickItemAdjust!=null){
                    onClickItemAdjust.onClickAdjust(i);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imAdjust;
        TextView tvAdjust;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imAdjust = itemView.findViewById(R.id.imAdjust);
            tvAdjust = itemView.findViewById(R.id.tvAdjust);
        }
    }

    public interface OnClickItemAdjust {
        void onClickAdjust(int pos);
    }

    public OnClickItemAdjust onClickItemAdjust;

    public void setOnClickItemAdjust(OnClickItemAdjust onClickItemAdjust) {
        this.onClickItemAdjust = onClickItemAdjust;
    }
}
