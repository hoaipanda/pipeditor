package com.camera.pipeditor.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.camera.pipeditor.App;
import com.camera.pipeditor.AppUtils;
import com.camera.pipeditor.BuildConfig;
import com.camera.pipeditor.Contains;
import com.camera.pipeditor.GPUImageFilterTools;
import com.camera.pipeditor.ImageUtils;
import com.camera.pipeditor.R;
import com.camera.pipeditor.adapter.AdjustAdapter;
import com.camera.pipeditor.adapter.FilterAdapter;
import com.camera.pipeditor.adapter.MenuAdapter;
import com.camera.pipeditor.adapter.StickerAdapter;
import com.camera.pipeditor.data.ItemFilter;
import com.camera.pipeditor.data.Menu;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.StickerView;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropFragment;
import com.yalantis.ucrop.UCropFragmentCallback;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBalanceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLevelsFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToneCurveFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;


public class EditorActivity extends AppCompatActivity implements FilterAdapter.OnClickItemFilter, RangeSeekBar.OnRangeSeekBarChangeListener, UCropFragmentCallback {
    private String path;
    private ArrayList<Menu> listMenu = new ArrayList<>();
    private MenuAdapter menuAdapter;
    private RecyclerView rvMenu, rvAdjust, rvSticker;
    private GPUImageView imEdit;
    private Context context;
    private RecyclerView rvFilter;
    private RelativeLayout lyDetail, lyImage;
    private RelativeLayout.LayoutParams params;
    private int width, height;

    private static final int SAVE_COMPLETE = 13;
    private static final int ADD_ADJUST = 14;
    private static final int LOAD_COMPLETE = 12;
    private static final int LEVELS = 0;
    private static final int CONTRAST = 1;
    private static final int SATURATION = 2;
    private static final int SHARPEN = 3;
    private static final int WB = 4;
    private static final int COLORTEMP = 5;
    private static final int HIGHLIGHT = 6;
    private static final int SHADOW = 7;

    private FilterAdapter filterAdapter;
    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;
    private ArrayList<ItemFilter> listFilter = new ArrayList<>();
    private Bitmap bmImage, bmResource;
    private ArrayList<ItemFilter> listAdjust = new ArrayList<>();
    private RangeSeekBar seek;

    private int[] process;
    private int typeFilter = -1;
    private AdjustAdapter adjustAdapter;
    private GPUImageContrastFilter gpuImageContrastFilter;
    private GPUImageLevelsFilter gpuImageLevelsFilter;
    private GPUImageSaturationFilter gpuImageSaturationFilter;
    private GPUImageSharpenFilter gpuImageSharpenFilter;
    private GPUImageWhiteBalanceFilter gpuImageWhiteBalanceFilter;
    private GPUImageColorBalanceFilter gpuImageColorBalanceFilter;
    private GPUImageHighlightShadowFilter gpuImageHighlightShadowFilter;
    private RelativeLayout lySeek;
    private TextView tvNameAdjust, tvSave;
    private ImageView imClose, imDone, imPhoto;
    private ArrayList<String> listSticker = new ArrayList<>();
    private StickerAdapter stickerAdapter;
    private StickerView sticker;
    private RelativeLayout lyBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        context = this;
        initView();
        path = getIntent().getStringExtra(Contains.PATHEDITOR);
        listMenu = AppUtils.getListMenu();
        updateRvMenu();
        loadAdjust();
        process = new int[listAdjust.size()];
        updateRvAdjust();

        lyImage.post(new Runnable() {
            @Override
            public void run() {
//                bmImage = BitmapFactory.decodeFile(path);
                try {
                    bmImage = ImageUtils.handleSamplingAndRotationBitmap(context, Uri.parse(path));
                    bmResource = ImageUtils.handleSamplingAndRotationBitmap(context, Uri.parse(path));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                width = lyImage.getWidth();
                height = lyImage.getHeight();
                setParamslayout(bmImage);
                imEdit.setImage(bmImage);
                Message message = new Message();
                message.what = LOAD_COMPLETE;
                handler.sendMessage(message);
            }
        });

        listSticker = AppUtils.getListSticker(context);
        updateRvSticker();
    }

    private void updateRvSticker() {
        stickerAdapter = new StickerAdapter(context, listSticker);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false);
        rvSticker.setLayoutManager(linearLayoutManager);
        rvSticker.setAdapter(stickerAdapter);
        stickerAdapter.setOnClickItemSticker(new StickerAdapter.OnClickItemSticker() {
            @Override
            public void onClickSticker(String sticker) {
                Bitmap bm = AppUtils.getBitmapFromAsset(context, "sticker/" + sticker);
                addIcon(bm);
            }
        });
    }


    private void loadAdjust() {
        listAdjust = new ArrayList<>();
        listAdjust.add(new ItemFilter(R.drawable.gr_color_gamma, "LEVELS"));
        listAdjust.add(new ItemFilter(R.drawable.gr_color_contrast, "CONTRAST"));
        listAdjust.add(new ItemFilter(R.drawable.gr_color_saturation, "SATURATION"));
        listAdjust.add(new ItemFilter(R.drawable.gr_color_sharpen, "SHARPEN"));
        listAdjust.add(new ItemFilter(R.drawable.gr_color_wb, "WB"));
        listAdjust.add(new ItemFilter(R.drawable.gr_color_temperature, "COLORTEMP"));
        listAdjust.add(new ItemFilter(R.drawable.gr_color_highlight, "HIGHLIGHT"));
        listAdjust.add(new ItemFilter(R.drawable.gr_color_shadow, "SHADOW"));
    }


    private void updateRvAdjust() {
        adjustAdapter = new AdjustAdapter(listAdjust);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false);
        rvAdjust.setLayoutManager(linearLayoutManager);
        rvAdjust.setAdapter(adjustAdapter);

        adjustAdapter.setOnClickItemAdjust(new AdjustAdapter.OnClickItemAdjust() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClickAdjust(int pos) {
                lyDetail.setVisibility(View.GONE);
                rvMenu.setVisibility(View.GONE);
                lySeek.setVisibility(View.VISIBLE);
                tvNameAdjust.setText(listAdjust.get(pos).getName());
                typeFilter = pos;
                seek.setSelectedMaxValue(process[typeFilter]);
                if (typeFilter == HIGHLIGHT || typeFilter == SHADOW) {
                    seek.setRangeValues(0, 100);
                } else {
                    seek.setRangeValues(-50, 50);
                }

                GPUImageFilter mFilter = null;
                gpuImageContrastFilter = new GPUImageContrastFilter();
                gpuImageLevelsFilter = new GPUImageLevelsFilter();
                gpuImageSaturationFilter = new GPUImageSaturationFilter();
                gpuImageSharpenFilter = new GPUImageSharpenFilter();
                gpuImageWhiteBalanceFilter = new GPUImageWhiteBalanceFilter();
                gpuImageColorBalanceFilter = new GPUImageColorBalanceFilter();
                gpuImageHighlightShadowFilter = new GPUImageHighlightShadowFilter();
                switch (typeFilter) {
                    case LEVELS:
                        mFilter = gpuImageLevelsFilter;
                        break;

                    case CONTRAST:
                        mFilter = gpuImageContrastFilter;
                        break;

                    case SATURATION:
                        mFilter = gpuImageSaturationFilter;
                        break;

                    case SHARPEN:
                        mFilter = gpuImageSharpenFilter;
                        break;

                    case WB:
                        mFilter = gpuImageWhiteBalanceFilter;
                        break;

                    case COLORTEMP:
                        mFilter = gpuImageColorBalanceFilter;
                        break;

                    case HIGHLIGHT:
                        mFilter = gpuImageHighlightShadowFilter;
                        break;

                    case SHADOW:
                        mFilter = gpuImageHighlightShadowFilter;
                        break;

                    default:
                        break;
                }
                mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
                imEdit.setFilter(mFilter);
                imEdit.requestRender();
            }
        });

    }

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case LOAD_COMPLETE:
//                    load.setVisibility(View.GONE);
                    loadFilter();
                    filterAdapter = new FilterAdapter(context, listFilter, Bitmap.createScaledBitmap(bmImage, 200, 300, false));
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false);
                    rvFilter.setLayoutManager(linearLayoutManager);
                    filterAdapter.setOnClickFilter(EditorActivity.this);
                    rvFilter.setAdapter(filterAdapter);
                    break;
                case ADD_ADJUST:
                    imEdit.getGPUImage().deleteImage();
                    imEdit.setImage(bmImage);
                    imEdit.setFilter(new GPUImageFilter());
                    mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(new GPUImageFilter());
                    imEdit.requestRender();
                    break;
                case SAVE_COMPLETE:
                    Toast.makeText(context, "Save seccessful!", Toast.LENGTH_SHORT).show();
//                    imPhoto.setVisibility(View.GONE);
                    imEdit.setVisibility(View.VISIBLE);
                    break;
            }
            return true;
        }
    });

    private void loadFilter() {
        listFilter = new ArrayList<>();
        listFilter.add(new ItemFilter(0, "NORMAL"));
        listFilter.add(new ItemFilter(R.raw.afterglow, "AFTERGLOW"));
        listFilter.add(new ItemFilter(R.raw.august_march, "AUGUST MARCH"));
        listFilter.add(new ItemFilter(R.raw.babyface, "BABYFACE"));
        listFilter.add(new ItemFilter(R.raw.blood_orange, "BLOOD ORANGE"));
        listFilter.add(new ItemFilter(R.raw.colddesert, "COLDDESERT"));
        listFilter.add(new ItemFilter(R.raw.country, "COUNTRY"));
        listFilter.add(new ItemFilter(R.raw.fogy_blue, "FOGY BLUE"));
        listFilter.add(new ItemFilter(R.raw.fresh_blue, "FRESH BLUE"));
        listFilter.add(new ItemFilter(R.raw.ghostsinyourhead, "GHOSTSINYOURHEAD"));
        listFilter.add(new ItemFilter(R.raw.goldenhour, "GOLDENHOUR"));
        listFilter.add(new ItemFilter(R.raw.greenenvy, "GREENENVY"));
        listFilter.add(new ItemFilter(R.raw.kisskiss, "KISSKISS"));
        listFilter.add(new ItemFilter(R.raw.lullabye, "LULLABYE"));
        listFilter.add(new ItemFilter(R.raw.moth_wings, "MOTH WINGS"));
        listFilter.add(new ItemFilter(R.raw.mystery, "MYSTERY"));
        listFilter.add(new ItemFilter(R.raw.old_postcards_ii, "OLD POSTCARDS II"));
        listFilter.add(new ItemFilter(R.raw.pistol, "PISTOL"));
        listFilter.add(new ItemFilter(R.raw.rivers_and_rain, "RIVERS AND RAIN"));
        listFilter.add(new ItemFilter(R.raw.sparks, "SPARKS"));
        listFilter.add(new ItemFilter(R.raw.toes, "TOES"));
        listFilter.add(new ItemFilter(R.raw.tonelemon, "TONELEMON"));
        listFilter.add(new ItemFilter(R.raw.trains, "TRAINS"));
        listFilter.add(new ItemFilter(R.raw.twin_lungs, "TWIN LUNGS"));
        listFilter.add(new ItemFilter(R.raw.wild_at_heart, "WILD AT HEART"));
        listFilter.add(new ItemFilter(R.raw.windowwarmth, "WINDOWWARMTH"));
        listFilter.add(new ItemFilter(R.raw.xanh, "GREEN"));
    }

    private View.OnClickListener lsClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imDone:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                bmImage = imEdit.capture();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Message msg = new Message();
                            msg.what = ADD_ADJUST;
                            handler.sendMessage(msg);
                        }
                    }).start();
                    lySeek.setVisibility(View.GONE);
                    lyDetail.setVisibility(View.VISIBLE);
                    rvMenu.setVisibility(View.VISIBLE);
                    process[typeFilter] = 0;
                    break;
                case R.id.imClose:
                    imEdit.setFilter(new GPUImageFilter());
                    mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(new GPUImageFilter());
                    imEdit.requestRender();
                    process[typeFilter] = 0;
                    lySeek.setVisibility(View.GONE);
                    lyDetail.setVisibility(View.VISIBLE);
                    rvMenu.setVisibility(View.VISIBLE);
                    break;

                case R.id.lyBack:
                    finish();
                    break;
                case R.id.tvSave:
                    try {
                        sticker.setLocked(true);
                        imPhoto.setImageBitmap(imEdit.capture());
                        imEdit.setVisibility(View.GONE);
                        imPhoto.setVisibility(View.VISIBLE);
                        lyImage.setDrawingCacheEnabled(true);
                        lyImage.buildDrawingCache();
                        Bitmap bm = lyImage.getDrawingCache();

                        AppUtils.createDirectoryAndSaveFile(context, bm);
                        finish();
                        Toast.makeText(context, "Save seccessful!", Toast.LENGTH_SHORT).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
    }

    private void handleCropResult(@NonNull final Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
//            lyImage.post(new Runnable() {
//                @Override
//                public void run() {
            try {
                bmImage = ImageUtils.handleSamplingAndRotationBitmap(context, resultUri);
                bmResource = ImageUtils.handleSamplingAndRotationBitmap(context, resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            setParamslayout(bmImage);
            imEdit.setImage(bmImage);
            Message message = new Message();
            message.what = LOAD_COMPLETE;
            handler.sendMessage(message);
//                }
//            });
        } else {
            Toast.makeText(EditorActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateRvMenu() {
        menuAdapter = new MenuAdapter(listMenu);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 4);
        rvMenu.setLayoutManager(linearLayoutManager);
        rvMenu.setAdapter(menuAdapter);
        menuAdapter.setOnClickItemMenu(new MenuAdapter.OnClickItemMenu() {
            @Override
            public void onClickMenu(int menu) {
                switch (menu) {
                    case R.drawable.crop:
                        lyDetail.setVisibility(View.GONE);
                        startCrop(Uri.parse(path));
                        break;
                    case R.drawable.filter:
                        lyDetail.setVisibility(View.VISIBLE);
                        rvAdjust.setVisibility(View.GONE);
                        rvFilter.setVisibility(View.VISIBLE);
                        rvSticker.setVisibility(View.GONE);
                        break;
                    case R.drawable.adjust:
                        lyDetail.setVisibility(View.VISIBLE);
                        rvFilter.setVisibility(View.GONE);
                        rvAdjust.setVisibility(View.VISIBLE);
                        rvSticker.setVisibility(View.GONE);
                        break;
                    case R.drawable.sticker:
                        lyDetail.setVisibility(View.VISIBLE);
                        rvFilter.setVisibility(View.GONE);
                        rvAdjust.setVisibility(View.GONE);
                        rvSticker.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    private void setParamslayout(Bitmap resource) {
        if (ratio(resource)) {
            params.width = width;
            params.height = width * resource.getHeight() / resource.getWidth();
            lyImage.requestLayout();
        } else {
            params.height = height;
            params.width = height * resource.getWidth() / resource.getHeight();
            lyImage.requestLayout();
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


    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";

    private void startCrop(@NonNull Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;

        destinationFileName += ".jpg";


        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop = advancedConfig(uCrop);
        uCrop.start(EditorActivity.this);

    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);
        return uCrop.withOptions(options);
    }


    private void initView() {
        imPhoto = findViewById(R.id.imPhoto);
        lyBack = findViewById(R.id.lyBack);
        tvSave = findViewById(R.id.tvSave);
        imDone = findViewById(R.id.imDone);
        imClose = findViewById(R.id.imClose);
        rvMenu = findViewById(R.id.rvMenu);
        imEdit = findViewById(R.id.imEdit);
        lyDetail = findViewById(R.id.lyDetail);
        rvFilter = findViewById(R.id.rvFilter);
        lyImage = findViewById(R.id.lyImage);
        params = (RelativeLayout.LayoutParams) lyImage.getLayoutParams();
        rvAdjust = findViewById(R.id.rvAdjust);
        rvSticker = findViewById(R.id.rvSticker);
        seek = findViewById(R.id.seekbar);
        lySeek = findViewById(R.id.lySeek);
        tvNameAdjust = findViewById(R.id.tvNameAdjust);
        seek.setOnRangeSeekBarChangeListener(EditorActivity.this);
        imEdit.setScaleType(GPUImage.ScaleType.CENTER_INSIDE);
        seek.setShowLabels(false);
        seek.setRangeValues(-50, 50);
        seek.setSelectedMaxValue(0);
        seek.setOnRangeSeekBarChangeListener(this);
        seek.setNotifyWhileDragging(true);
        imDone.setOnClickListener(lsClick);
        imClose.setOnClickListener(lsClick);
        sticker = findViewById(R.id.sticker);
        lyBack.setOnClickListener(lsClick);
        tvSave.setOnClickListener(lsClick);
    }

    @Override
    public void OnClickItemFilter(int position) {
        if (position != 0) {
            GPUImageToneCurveFilter toneCurveFilter = new GPUImageToneCurveFilter();
            toneCurveFilter.setFromCurveFileInputStream(
                    App.getContext().getResources().openRawResource(listFilter.get(position).getImage()));
            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(toneCurveFilter);
            imEdit.setFilter(toneCurveFilter);
            imEdit.requestRender();
        } else {
            bmImage = bmResource;
            GPUImageFilter gpuImageFilter = new GPUImageFilter();
            imEdit.setFilter(gpuImageFilter);
            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(gpuImageFilter);
            imEdit.getGPUImage().deleteImage();
            imEdit.setImage(bmImage);
            imEdit.requestRender();
        }

    }

    private void addIcon(Bitmap bitmap) {
        float ratio = bitmap.getWidth() / (float) bitmap.getHeight();
        int with = 720;
        int height = Math.round(with / ratio);
        bitmap = Bitmap.createScaledBitmap(
                bitmap, with, height, false);
        Drawable drawable = new BitmapDrawable(bitmap);
        sticker.addSticker(
                new DrawableSticker(drawable));
    }


    @Override
    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
        int max = (int) maxValue;
        process[typeFilter] = max;
        if (typeFilter != HIGHLIGHT && typeFilter != SHADOW) {
            max += 50;
        }
        switch (typeFilter) {
            case LEVELS:
                max += 70;
                break;
            case CONTRAST:
                max *= 0.4f;
                max += 30;
                break;
            case SATURATION:
                break;

            case SHARPEN:
                break;

            case WB:
                break;

            case COLORTEMP:
                max -= 50;
                break;

            case HIGHLIGHT:
                max = 100 - max;
                max /= 100;
                break;
            case SHADOW:
                break;

            default:
                break;
        }
        if (typeFilter != HIGHLIGHT) {
            mFilterAdjuster.adjust(max);
        } else {
            gpuImageHighlightShadowFilter.setHighlights(max);
        }
        imEdit.requestRender();
    }

    @Override
    public void loadingProgress(boolean showLoader) {

    }

    @Override
    public void onCropFinish(UCropFragment.UCropResult result) {

    }
}
