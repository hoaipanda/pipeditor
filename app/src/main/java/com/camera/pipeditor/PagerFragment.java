package com.camera.pipeditor;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class PagerFragment extends Fragment {
    private ImageView imShow;
    private Bitmap bitmapImage;
    private String link;
    private RelativeLayout layout;
    private int width, height;
    private RelativeLayout.LayoutParams params;

    public static PagerFragment newInstance(String link) {
        PagerFragment pagerFragment = new PagerFragment();
        pagerFragment.setLink(link);
        return pagerFragment;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);
        imShow = view.findViewById(R.id.imShow);
        layout = view.findViewById(R.id.layout);
        params = (RelativeLayout.LayoutParams) layout.getLayoutParams();

//        layout.post(new Runnable() {
//            @Override
//            public void run() {
//                width = layout.getWidth();
//                height = layout.getHeight();
//                Bitmap bm = BitmapFactory.decodeFile(link);
//                setParamslayout(bm);
//                imShow.setImageBitmap(bm);
//            }
//        });

        Glide.with(getActivity()).load("file:///android_asset/bg/" + link).into(imShow);


//
        return view;
    }

    private void setParamslayout(Bitmap resource) {
        if (ratio(resource)) {
            params.width = width;
            params.height = width * resource.getHeight() / resource.getWidth();
            layout.requestLayout();
        } else {
            params.height = height;
            params.width = height * resource.getWidth() / resource.getHeight();
            layout.requestLayout();
        }
    }

    private boolean ratio(Bitmap bitmap) {
        float w = (float) bitmap.getWidth() / width;
        float h = (float) bitmap.getHeight() / height;

        if (w > h) {
            return true;
        }
        return false;
    }


}
