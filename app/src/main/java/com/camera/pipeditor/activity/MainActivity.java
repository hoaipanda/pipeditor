package com.camera.pipeditor.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.camera.pipeditor.AppUtils;
import com.camera.pipeditor.Contains;
import com.camera.pipeditor.GlideEngine;
import com.camera.pipeditor.PagerFragment;
import com.camera.pipeditor.R;
import com.camera.pipeditor.adapter.ViewPagerAdapter;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private LinearLayout lyEditor, lyPicinPic, lyPuzzle, lyPoster;
    private ArrayList<Photo> selectedPhotoList = new ArrayList<>();
    private ViewPager pager;
    private ArrayList<String> listBg = new ArrayList<>();
    private ViewPagerAdapter pagerAdapter;
    private ImageView im1, im2, im3, im4, im5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setupViewPager(pager);

    }

    private void setupViewPager(ViewPager viewPager) {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        int page = listBg.size();
        if (page > 0) {
            for (int i = 0; i < page; i++) {

                PagerFragment fm = new PagerFragment();
//                fm.setIndexPage(i);
                fm.setLink(listBg.get(i));
                pagerAdapter.addFragment(fm);
            }
            viewPager.setAdapter(pagerAdapter);
            viewPager.setOffscreenPageLimit(page);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                setUpImageSelect(i + 1);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private void setUpImageSelect(int i) {
        switch (i) {
            case 1:
                im1.setColorFilter(Color.BLACK);
                im2.setColorFilter(getResources().getColor(R.color.gray));
                im3.setColorFilter(getResources().getColor(R.color.gray));
                im4.setColorFilter(getResources().getColor(R.color.gray));
                im5.setColorFilter(getResources().getColor(R.color.gray));
                break;
            case 2:
                im2.setColorFilter(Color.BLACK);
                im1.setColorFilter(getResources().getColor(R.color.gray));
                im3.setColorFilter(getResources().getColor(R.color.gray));
                im4.setColorFilter(getResources().getColor(R.color.gray));
                im5.setColorFilter(getResources().getColor(R.color.gray));
                break;
            case 3:
                im3.setColorFilter(Color.BLACK);
                im2.setColorFilter(getResources().getColor(R.color.gray));
                im1.setColorFilter(getResources().getColor(R.color.gray));
                im4.setColorFilter(getResources().getColor(R.color.gray));
                im5.setColorFilter(getResources().getColor(R.color.gray));
                break;
            case 4:
                im4.setColorFilter(Color.BLACK);
                im2.setColorFilter(getResources().getColor(R.color.gray));
                im3.setColorFilter(getResources().getColor(R.color.gray));
                im1.setColorFilter(getResources().getColor(R.color.gray));
                im5.setColorFilter(getResources().getColor(R.color.gray));
                break;
            case 5:
                im5.setColorFilter(Color.BLACK);
                im2.setColorFilter(getResources().getColor(R.color.gray));
                im3.setColorFilter(getResources().getColor(R.color.gray));
                im4.setColorFilter(getResources().getColor(R.color.gray));
                im1.setColorFilter(getResources().getColor(R.color.gray));
                break;
        }
    }

    private void initView() {

        im1 = findViewById(R.id.im1);
        im2 = findViewById(R.id.im2);
        im3 = findViewById(R.id.im3);
        im4 = findViewById(R.id.im4);
        im5 = findViewById(R.id.im5);
        lyEditor = findViewById(R.id.lyEditor);
        lyPicinPic = findViewById(R.id.lyPicinPic);
        lyPuzzle = findViewById(R.id.lyPuzzle);
        lyPoster = findViewById(R.id.lyPoster);
        pager = findViewById(R.id.pager);
        listBg = getListSticker(this);

        lyPoster.setOnClickListener(lsClick);
        lyPuzzle.setOnClickListener(lsClick);
        lyPicinPic.setOnClickListener(lsClick);
        lyEditor.setOnClickListener(lsClick);
    }


    public static ArrayList<String> getListSticker(Context context) {
        String[] files = new String[0];
        try {
            AssetManager assetManager = context.getAssets();
            files = assetManager.list("bg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>(Arrays.asList(files));
    }

    private View.OnClickListener lsClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.lyEditor:
//                    EasyPhotos.createAlbum(MainActivity.this, false, GlideEngine.getInstance())
//                            .setCount(1)
//                            .setPuzzleMenu(false)
//                            .start(103);
                    pickFromGallery();
                    break;
                case R.id.lyPicinPic:
                    EasyPhotos.createAlbum(MainActivity.this, false, GlideEngine.getInstance()).setCount(5).setPuzzleMenu(false).start(108);
                    break;
                case R.id.lyPuzzle:

                    EasyPhotos.createAlbum(MainActivity.this, false, GlideEngine.getInstance()).setCount(5).setPuzzleMenu(false).start(105);

                    break;
                case R.id.lyPoster:
                    EasyPhotos.createAlbum(MainActivity.this, false, GlideEngine.getInstance())
                            .setCount(9)
                            .setPuzzleMenu(false)
                            .start(102);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {

            if (requestCode == 102) {

                ArrayList<Photo> resultPhotos =
                        data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                if (resultPhotos.size() == 1) {
                    resultPhotos.add(resultPhotos.get(0));
                }
                selectedPhotoList.clear();
                selectedPhotoList.addAll(resultPhotos);

                EasyPhotos.startPuzzleWithPhotos(this, selectedPhotoList, Environment.getExternalStorageDirectory().getAbsolutePath(), "AlbumBuilder", 103, false, GlideEngine.getInstance());
                return;
            }

            if (requestCode == 1) {
//                ArrayList<Photo> resultPhotos =
//                        data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
//                if (resultPhotos.size() == 1) {
//                    resultPhotos.add(resultPhotos.get(0));
//                }
//                selectedPhotoList.clear();
//                selectedPhotoList.addAll(resultPhotos);
                Uri uri = data.getData();
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra(Contains.PATHEDITOR, uri.toString());
                startActivity(intent);
                return;

            }

            if (requestCode == 108) {
//                Uri uri = data.getData();
                ArrayList<Photo> resultPhotos =
                        data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                if (resultPhotos.size() == 1) {
                    resultPhotos.add(resultPhotos.get(0));
                }
                selectedPhotoList.clear();
                selectedPhotoList.addAll(resultPhotos);
                Intent intent = new Intent(MainActivity.this, PicInPicActivity.class);
                intent.putExtra(Contains.PATHEDITOR, resultPhotos.get(0).path);
                startActivity(intent);
                return;
            }

            if (requestCode == 105) {
                ArrayList<Photo> resultPhotos =
                        data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                if (resultPhotos.size() == 1) {
                    resultPhotos.add(resultPhotos.get(0));
                }
                selectedPhotoList.clear();
                selectedPhotoList.addAll(resultPhotos);
                Intent intent = new Intent(MainActivity.this, EditPuzzleActivity.class);
                intent.putExtra(Contains.LISTIMAGEPUZZLE, resultPhotos);
                startActivity(intent);
            }

        } else if (RESULT_CANCELED == resultCode) {
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
        }
    }

    private void pickFromGallery() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
//                    getString(R.string.permission_read_storage_rationale),
//                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
//        } else {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*")
                .addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
//        }
    }


}
