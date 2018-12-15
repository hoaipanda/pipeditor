package com.camera.pipeditor.activity;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.camera.pipeditor.Contains;
import com.camera.pipeditor.R;
import com.camera.pipeditor.adapter.PuzzleAdapter;
import com.camera.pipeditor.data.Photo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class EditPuzzleActivity extends AppCompatActivity {
    private ArrayList<String> listThumb = new ArrayList<>();
    private RecyclerView rvPuzzle;
    private PuzzleAdapter puzzleAdapter;
    private ArrayList<Photo> listImage = new ArrayList<>();
    private ImageView imBack, imDone;
    private int numPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_puzzle);
        initView();
        initData();
    }

    private void initData() {
        listImage = (ArrayList<Photo>) getIntent().getSerializableExtra(Contains.LISTIMAGEPUZZLE);
        numPhoto = listImage.size();
        getListThumb();
        updateRvPuzzle();
    }

    private void updateRvPuzzle() {
        puzzleAdapter = new PuzzleAdapter(this, listThumb, "puzzle" + numPhoto);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvPuzzle.setLayoutManager(linearLayoutManager);
        rvPuzzle.setAdapter(puzzleAdapter);
        puzzleAdapter.setOnClickItemPuzzle(new PuzzleAdapter.OnClickItemPuzzle() {
            @Override
            public void onClickPuzzle(int pos) {

            }
        });
    }

    private void getListThumb() {
        String[] files = new String[0];
        try {
            AssetManager assetManager = getAssets();
            files = assetManager.list("puzzle/puzzle" + numPhoto + "/thumb");
        } catch (IOException e) {
            e.printStackTrace();
        }
        listThumb = new ArrayList<String>(Arrays.asList(files));
    }

    private void initView() {
        imBack = findViewById(R.id.imBack);
        imDone = findViewById(R.id.imDone);
        rvPuzzle = findViewById(R.id.rvPuzzle);

        imBack.setOnClickListener(lsClick);
        imDone.setOnClickListener(lsClick);
    }

    private View.OnClickListener lsClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imBack:
                    break;
                case R.id.imDone:
                    break;

            }
        }
    };
}
