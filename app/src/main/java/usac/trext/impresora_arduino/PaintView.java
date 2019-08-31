package usac.trext.impresora_arduino;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class PaintView extends View {

    private float mX, mY;
    private Path mPath;
    private Paint mPaint, mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private ArrayList<FingerPath> paths = new ArrayList<>();
    private int currentColor;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Context context;

    public PaintView(Context context) {
        super(context);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);
    }

    public void init(DisplayMetrics metrics) {
        mBitmap = Bitmap.createBitmap(metrics.widthPixels, metrics.heightPixels, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        currentColor = 0;
    }

    public void clear() {
        paths.clear();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        mCanvas.drawColor(Color.WHITE);

        for (FingerPath fp : paths) {
            mPaint.setColor(fp.color);
            mPaint.setStrokeWidth(fp.strokeWidth);
            mPaint.setMaskFilter(null);
            mCanvas.drawPath(fp.path, mPaint);

        }
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }

    private void touchStart(float x, float y) {
        mPath = new Path();
        if(currentColor == 0)
            paths.add(new FingerPath(Color.RED, 15, mPath));
        else if(currentColor == 1)
            paths.add(new FingerPath(Color.BLUE, 15, mPath));
        else
            paths.add(new FingerPath(Color.BLACK, 15, mPath));
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        if (Math.abs(x - mX) >= 4 || Math.abs(y - mY) >= 4) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                touchStart(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE :
                touchMove(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP :
                touchUp();
                break;
        }
        invalidate();
        return true;
    }

    public void changeColor(int color){
        currentColor = color;
    }

    public void imprimir(String name){
        /*String file_path = context.getFilesDir().getAbsolutePath() +
                "/PhysicsSketchpad";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();
        System.out.println(file_path);
        File file = new File(dir, name + ".png");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
