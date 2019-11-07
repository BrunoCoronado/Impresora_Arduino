package usac.trext.impresora_arduino;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.Principal;
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
    private ArrayList<Coordenada> coordenadas = new ArrayList<Coordenada>();

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
        float x = event.getX();
        float y = event.getY();

        float x1= (float) (event.getX()/23.17);
        float y1= (float) (event.getY()/23.17);


        char color = '2';
        if(currentColor == 1) color = '1';
        if(currentColor == 0) color = '3';

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                touchStart(x, y);
                invalidate();
                if((x>10&&y>10)&&(x<=927&&y<=927)) coordenadas.add(new Coordenada(x1,y1,color,'1'));
                break;
            case MotionEvent.ACTION_MOVE :
                touchMove(x, y);
                invalidate();
                if((x>10&&y>10)&&(x<=927&&y<=927)) coordenadas.add(new Coordenada(x1,y1,color,'0'));
                break;
            case MotionEvent.ACTION_UP :
                touchUp();
                invalidate();
                if((x>10&&y>10)&&(x<=927&&y<=927)) coordenadas.add(new Coordenada(x1,y1,color,'2'));
                break;
        }

        return true;
    }

    public void changeColor(int color){
        currentColor = color;
    }

    @SuppressLint("WrongThread")
    public Boolean imprimir(String name){
        try {
            if(crearCarpeta()){
                FileOutputStream fileOutputStream = new FileOutputStream("/storage/emulated/0/ImpresoraArduino/" + name + ".png");
                mBitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                fileOutputStream = new FileOutputStream("/storage/emulated/0/ImpresoraArduino/" + name + "gc.txt");
                fileOutputStream.write(generarGcode().getBytes());
                fileOutputStream.close();
                Services services =  new Services(context);
                services.enviarContenido(name + "gc.txt");
                return  true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean crearCarpeta(){
        File folder = new File("/storage/emulated/0/ImpresoraArduino");
        if (!folder.exists())
            return folder.mkdirs();
        return true;
    }

    public String generarGcode(){
        String gcode = "G21 (metric ftw)\n" +
                "G90 (absolute mode)\n" +
                "G92 X0.00 Y0.00 Z0.00 (you are here)\n" +
                "\n" +
                "M300 S30 (pen down)\n" +
                "G4 P150 (wait 150ms)\n" +
                "M300 S50 (pen up)\n" +
                "G4 P150 (wait 150ms)\n" +
                "M18 (disengage drives)\n" +
                "M01 (Was registration test successful?)\n" +
                "M17 (engage drives if YES, and continue)";
        for(int j=0;j < coordenadas.size();j++){
            if(j==0) gcode += "G92 X" + Math.round( coordenadas.get(j).x * 100.0)/100.0 + " Y" + Math.round(Math.round( coordenadas.get(j).y * 100.0)/100.0 * 100.0)/100.0 + " Z0.00\n";
            if(coordenadas.get(j).estado=='1'){
                gcode += "G92 X" + Math.round( coordenadas.get(j).x * 100.0)/100.0 + " Y" + Math.round(Math.round( coordenadas.get(j).y * 100.0)/100.0 * 100.0)/100.0 + " Z0.00\n";
                gcode += "M300 S30.00 \n G4 P150\n";
            }
            gcode += "G1 X" + Math.round( coordenadas.get(j).x * 100.0)/100.0 + " Y" + Math.round( coordenadas.get(j).y * 100.0)/100.0 + " F" + coordenadas.get(j).color + "\n";
            if(coordenadas.get(j).estado=='2') gcode += "M300 S50.00 \n G4 P150\n";
        }
        return gcode;
    }
}
