package com.camera.pipeditor.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.camera.pipeditor.App;
import com.camera.pipeditor.R;
import com.camera.pipeditor.data.ItemFilter;

import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageToneCurveFilter;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private final LayoutInflater mInflater;
    private Context context;
    private ArrayList<ItemFilter> filters;
    private Bitmap bitmap;
    private OnClickItemFilter onClickItemFilter;
    private int pos = 0;

    public FilterAdapter(Context context, ArrayList<ItemFilter> filters, Bitmap bitmap) {
        this.context = context;
        this.filters = filters;
        this.bitmap = bitmap;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mAlbumImage;
        private TextView tvFilter;
        private int mAlbumPosition;
        private ImageView ivMask;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mAlbumImage = (ImageView) itemView.findViewById(R.id.iv_filter);
            ivMask = (ImageView) itemView.findViewById(R.id.iv_mask);
            tvFilter = (TextView) itemView.findViewById(R.id.tv_filter);
        }

        public void bind(int position) {
            if (position != 0) {
                mAlbumImage.setImageBitmap(getBitmapFilter(bitmap, filters.get(position).getImage()));
            } else {
                mAlbumImage.setImageResource(R.drawable.none);
                ivMask.setVisibility(View.GONE);
            }
            if (position < 10) {
                tvFilter.setText("0" + position);
            } else {
                tvFilter.setText("" + position);
            }
            if (pos == position) {
                ivMask.setVisibility(View.VISIBLE);
                tvFilter.setTextColor(Color.RED);
            } else {
                ivMask.setVisibility(View.GONE);
                tvFilter.setTextColor(Color.BLACK);
            }
            mAlbumPosition = position;
        }

        @Override
        public void onClick(View v) {
            pos = mAlbumPosition;
            notifyDataSetChanged();
            onClickItemFilter.OnClickItemFilter(mAlbumPosition);
        }
    }

    private Bitmap getBitmapFilter(Bitmap bitmap, int filter) {
        GPUImage gpuImage = new GPUImage(App.getContext());
        GPUImageToneCurveFilter toneCurveFilter = new GPUImageToneCurveFilter();
        toneCurveFilter.setFromCurveFileInputStream(
                context.getResources().openRawResource(filter));
        gpuImage.setFilter(toneCurveFilter);
        gpuImage.setImage(bitmap);
        return gpuImage.getBitmapWithFilterApplied();
    }

    public void setOnClickFilter(OnClickItemFilter onClickItemFilter) {
        this.onClickItemFilter = onClickItemFilter;
    }

    public interface OnClickItemFilter {
        void OnClickItemFilter(int position);
    }
}
