package uk.co.senab.photoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import uk.co.senab.photoview.object.BFrame;

class MyImage {
    private BFrame bFrame;
    private Context context;
    private int widthView;
    private int heightView;
    private float radius = 3f;

    private float transX = 0f;
    private float transY = 0f;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;

    private Bitmap bmMask;
    private Bitmap bmGoc1;
    private Bitmap bmGoc;

    public void setBmMask(Bitmap bmMask) {
        this.bmMask = bmMask;
    }

    public void setBmGoc(Bitmap bmGoc) {
        this.bmGoc = bmGoc;
    }

    MyImage(Context context) {
        this.context = context;
    }

    void setFrame(BFrame bFrame) {
        this.bFrame = bFrame;
    }

    void setWidthView(int widthView) {
        this.widthView = widthView;
    }

    void setHeightView(int heightView) {
        this.heightView = heightView;
    }

    private Bitmap bmCut;

    void onDraw(Canvas canvas, Matrix matrix) {
        if (matrix != null) {
            float[] value = new float[9];
            matrix.getValues(value);
            transX = value[Matrix.MTRANS_X];
            transY = value[Matrix.MTRANS_Y];
            scaleX = value[Matrix.MSCALE_X];
            scaleY = value[Matrix.MSCALE_Y];
        }
        if (bmGoc != null) {
            try {
                bmGoc1 = Bitmap.createScaledBitmap(Bitmap.createBitmap(bmGoc, (int) Math.abs(transX / scaleX),
                        (int) Math.abs(transY / scaleY), (int) (bmMask.getWidth() / scaleX), (int) (bmMask.getHeight() / scaleY)),
                        bmMask.getWidth(), bmMask.getHeight(), true);
                canvas.drawBitmap(getBitmapMask(), 0, 0, null);
            } catch (Exception ignored) {

            }
        }
    }

    private Bitmap getBitmapMask() {
        Bitmap result = Bitmap.createBitmap(bmMask.getWidth(), bmMask.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        tempCanvas.drawBitmap(bmGoc1, 0, 0, null);
        tempCanvas.drawBitmap(bmMask, 0, 0, paint);
        paint.setXfermode(null);
        return result;
    }
}
