package com.camera.pipeditor.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.camera.pipeditor.AppUtils;
import com.camera.pipeditor.Contains;
import com.camera.pipeditor.R;
import com.camera.pipeditor.adapter.PhotoAdapter;
import com.camera.pipeditor.data.Photo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class AllPhotoActivity extends AppCompatActivity {
    private ImageView imBack;
    private RecyclerView rvAllPhoto;
    private ArrayList<Photo> listImage = new ArrayList<>();
    private PhotoAdapter photoAdapter;
    private Context context;
    private TextView tvSelect, tvComplete;
    private RelativeLayout lyNext, lySelect;
    private LinearLayout lyImage;
    private ArrayList<Photo> listAdd = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_photo);
        context = this;
        initView();
        listImage = AppUtils.getAllImageGallery(this);
        updateRvPhoto();
    }

    private void updateRvPhoto() {
        photoAdapter = new PhotoAdapter(context, listImage, listAdd);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        rvAllPhoto.setLayoutManager(gridLayoutManager);
        rvAllPhoto.setAdapter(photoAdapter);
        photoAdapter.setOnClickItemPhoto(new PhotoAdapter.OnClickItemPhoto() {
            @Override
            public void onClickPhoto(Photo photo) {
                if (listAdd.contains(photo)) {
                    lyImage.removeViewAt(listAdd.indexOf(photo));
                    listAdd.remove(photo);
                } else

                {
                    if (listAdd.size() > 4) {
                        Toast.makeText(context, "Max image is 5", Toast.LENGTH_SHORT).show();
                    } else {
                        listAdd.add(photo);
                        addView(photo);
                    }
                }

                updateNum();
                photoAdapter.notifyDataSetChanged();
            }

        });

    }

    private void addView(Photo photo) {
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(180, 200);
        layoutParams.setMargins(10, 5, 10, 5);
        imageView.setLayoutParams(layoutParams);
        Glide.with(context).load(Uri.fromFile(new File(photo.getName()))).into(imageView);
        lyImage.addView(imageView);
    }

    private void updateNum() {
        tvComplete.setText("Complete(" + listAdd.size() + "/5)");
        tvSelect.setText("Select 1-5 (" + listAdd.size() + ")");
    }


    private void initView() {
        lySelect = findViewById(R.id.lySelect);
        imBack = findViewById(R.id.imBack);
        rvAllPhoto = findViewById(R.id.rvAllPhoto);
        tvSelect = findViewById(R.id.tvSelect);
        tvComplete = findViewById(R.id.tvComplete);
        lyImage = findViewById(R.id.lyImage);
        lyNext = findViewById(R.id.lyNext);
        lyImage.setEnabled(true);

        imBack.setOnClickListener(lsClick);
        lyNext.setOnClickListener(lsClick);
    }

    private View.OnClickListener lsClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imBack:
                    break;
                case R.id.lyNext:
                    Intent intent = new Intent(context, EditPuzzleActivity.class);
                    intent.putExtra(Contains.LISTIMAGEPUZZLE, listAdd);
                    break;
                case R.id.lySelect:
                    break;
            }
        }
    };

    public void saveImageBitmap(Bitmap bitmap, String nameFile) {
        String root = getCacheDir().getAbsolutePath();
        File myDir = new File(root);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        File file = new File(myDir, nameFile);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
