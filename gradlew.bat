package com.sticker.stickermakerwhatsapp.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sticker.stickermakerwhatsapp.Contains;
import com.sticker.stickermakerwhatsapp.R;
import com.sticker.stickermakerwhatsapp.StickerPack;
import com.sticker.stickermakerwhatsapp.StickerPackLoader;
import com.sticker.stickermakerwhatsapp.adapter.StickerPackAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends AddStickerPackActivity {
    private RecyclerView rvStickerPack;
    private RelativeLayout lyCreate;
    private Dialog dialogCreate;
    private EditText edPackName, edPackAuthor;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ArrayList<StickerPack> listPack = new ArrayList<>();
    private String mJson;
    private StickerPackAdapter stickerPackAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences(Contains.DATA, Context.MODE_PRIVATE);
        editor = preferences.edit();
        mJson = preferences.getString(Contains.CONTENTJSON, "");

        initView();

        new LoadStickerPack().execute();


    }

    private class LoadStickerPack extends AsyncTask<Void, Void, Pair<String, ArrayList<StickerPack>>> {

        @Override
        protected Pair<String, ArrayList<StickerPack>> doInBackground(Void... voids)