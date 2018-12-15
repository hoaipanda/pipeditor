package com.camera.pipeditor.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.camera.pipeditor.AppUtils;
import com.camera.pipeditor.Contains;
import com.camera.pipeditor.ImageUtils;
import com.camera.pipeditor.R;
import com.camera.pipeditor.adapter.FrameAdapter;
import com.camera.pipeditor.asyntask.ReadFrameAssets;
import com.camera.pipeditor.data.FrameModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;
import uk.co.senab.photoview.OnListenerTouchPhotoView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoView2;

public class PicInPicActivity extends AppCompatActivity implements OnListenerTouchPhotoView, FrameAdapter.OnListenerFrame {
    private ImageView imBack, imDone;
    private ImageView img_blur, img_background;
    private RecyclerView rvFrame;
    private ArrayList<FrameModel> listFrame = new ArrayList<>();
    private FrameAdapter frameAdapter;
    private RelativeLayout frameLayout;
    private Bitmap bitmapResult;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_in_pic);
        path = getIntent().getStringExtra(Contains.PATHEDITOR);
        initView();
        try {
            setUpImageView();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setUpRecyclerViewFrame();

    }


    private void setUpImageView() throws IOException {
        //            bitmapResult = ImageUtils.handleSamplingAndRotationBitmap(this, Uri.parse(path));
        bitmapResult = BitmapFactory.decodeFile(path);
        float aspectRatio = bitmapResult.getWidth() /
                (float) bitmapResult.getHeight();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = Math.round(width / aspectRatio);
        bitmapResult = Bitmap.createScaledBitmap(bitmapResult, width, height, false);
        Blurry.with(PicInPicActivity.this).radius(25).from(bitmapResult).into(img_blur);
//            custom_image_view.setImageBitmap(bitmapResult);
    }

    private void initView() {
        rvFrame = findViewById(R.id.rvFrame);
        imBack = findViewById(R.id.imBack);
        imDone = findViewById(R.id.imDone);
        img_blur = findViewById(R.id.img_blur);
        img_background = findViewById(R.id.img_background);
        frameLayout = findViewById(R.id.frame_layout);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        withImageView = displayMetrics.widthPixels;

        imBack.setOnClickListener(lsClick);
        imDone.setOnClickListener(lsClick);
    }

    private View.OnClickListener lsClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imBack:
                    finish();
                    break;
                case R.id.imDone:
                    frameLayout.setDrawingCacheEnabled(true);
                    frameLayout.buildDrawingCache();
                    Bitmap bm = frameLayout.getDrawingCache();
                    AppUtils.createDirectoryAndSaveFile(PicInPicActivity.this, bm);
                    Toast.makeText(PicInPicActivity.this, "Save done!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void setUpRecyclerViewFrame() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvFrame.setLayoutManager(linearLayoutManager);
        frameAdapter = new FrameAdapter(listFrame);
        rvFrame.setAdapter(frameAdapter);
        frameAdapter.setOnListenerFrame(this);
        ReadFrameAssets readFrameAssets = new ReadFrameAssets(new ReadFrameAssets.OnListenerLoadFrame() {
            @Override
            public void loadSuccess(ArrayList<FrameModel> frameModels) {
                updateRecyclerViewFrame(frameModels);
                setUpFrame(frameModels.get(0));
            }
        });
        if (listFrame.size() == 0)
            readFrameAssets.execute();

    }

    private PhotoView photoView;
    private int withImageView;

    private void setUpFrame(final FrameModel frameModel) {
        frameLayout.removeAllViews();
        Glide.with(this).load("file:///android_asset/frame/" + frameModel.getName() + "/" + frameModel.getBackGround()).into(img_background);
        photoView = new PhotoView(this);
        photoView.setOnListenerTouchPhotoView(this);
        photoView.setDraw(true);
        photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        photoView.setBmGoc(bitmapResult);
        photoView.setImageBitmap(bitmapResult);
        try {
            InputStream is = getAssets().open("frame/" + frameModel.getName() + "/" + frameModel.getMask());
            Bitmap bmMask = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is), (int) (frameModel.getWith() * withImageView),
                    (int) (frameModel.getHeight() * withImageView), false);
            photoView.setBmMask(bmMask);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (frameModel.getWith() * withImageView), (int) (frameModel.getHeight() * withImageView));
            params.setMargins((int) (frameModel.getLeft() * withImageView), (int) (frameModel.getTop() * withImageView), 0, 0);
            frameLayout.addView(photoView, params);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void updateRecyclerViewFrame(ArrayList<FrameModel> arrayList) {
        listFrame.clear();
        listFrame.addAll(arrayList);
        frameAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTouchPhotoView(MotionEvent event) {

    }

    @Override
    public void onClickItemFrame(FrameModel frameModel, int position) {
        setUpFrame(frameModel);
    }
}
