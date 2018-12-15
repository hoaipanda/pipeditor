package com.camera.pipeditor.asyntask;

import android.os.AsyncTask;


import com.camera.pipeditor.App;
import com.camera.pipeditor.data.FrameModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ReadFrameAssets extends AsyncTask<Void, Void, ArrayList<FrameModel>> {
    private OnListenerLoadFrame onListenerLoadFrame;

    public ReadFrameAssets(OnListenerLoadFrame onListenerLoadFrame) {
        this.onListenerLoadFrame = onListenerLoadFrame;
    }

    @Override
    protected ArrayList<FrameModel> doInBackground(Void... voids) {
        ArrayList<FrameModel> arrayList = new ArrayList<>();
        String[] list;
        String json;
        try {
            list = App.getContext().getAssets().list("frame");
            if (list.length > 0) {
                for (String file : list) {
                    FrameModel frameModel;
                    InputStream is = App.getContext().getAssets().open("frame/" + file + "/frame_json.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");
                    frameModel = new Gson().fromJson(json, FrameModel.class);
                    frameModel.setName(file);
                    arrayList.add(frameModel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<FrameModel> frameModels) {
        super.onPostExecute(frameModels);
        onListenerLoadFrame.loadSuccess(frameModels);
    }

    public interface OnListenerLoadFrame {
        void loadSuccess(ArrayList<FrameModel> frameModels);
    }

}
