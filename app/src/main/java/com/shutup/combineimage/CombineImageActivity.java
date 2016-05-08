package com.shutup.combineimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CombineImageActivity extends AppCompatActivity {

    private String TAG = "CombineImageActivity";


    private int xStart = 0;
    private int yStart = 0;
    private int x = 0;
    private int y = 0;

    private int screenOffset = 0;

    @InjectView(R.id.combineTwoImageButton)
    Button mCombineTwoImageButton;
    @InjectView(R.id.bgImage)
    ImageView mBgImage;
    @InjectView(R.id.fgImage)
    ImageView mFgImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_image);
        ButterKnife.inject(this);
        initEvent();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Display dis = getWindowManager().getDefaultDisplay();
        DisplayMetrics display = new DisplayMetrics();
        dis.getMetrics(display);
        View v = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        screenOffset = display.heightPixels - v.getHeight();
    }

    private void initEvent() {
        mBgImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    x = (int) ViewCompat.getX(v);
                    y = (int) ViewCompat.getY(v);
                    xStart = (int) event.getRawX();
                    yStart = (int) event.getRawY();
                }else if (event.getAction() == MotionEvent.ACTION_MOVE){
                    int xEnd = (int) event.getRawX();
                    int yEnd = (int) event.getRawY();
                    int xx = xEnd - xStart + x;
                    int yy = yEnd - yStart + y;
                    moveViewByLayout(v,xx,yy);
                    Log.d(TAG, "onTouchMove: "+xx+"_"+yy);
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    int xEnd = (int) event.getRawX();
                    int yEnd = (int) event.getRawY();
                    int xx = xEnd - xStart + x;
                    int yy = yEnd - yStart + y;
                    moveViewByLayout(v,xx,yy);
                    Log.d(TAG, "onTouchUp: "+xx+"_"+yy);
                }
                return true;
            }
        });
    }

    /**
     * 通过layout方法，移动view
     * 优点：对view所在的布局，要求不苛刻，不要是RelativeLayout，而且可以修改view的大小
     *
     * @param view
     * @param rawX
     * @param rawY
     */
    private void moveViewByLayout(View view, int rawX, int rawY) {
        int left = rawX;
        int top = rawY;
        int width = left + view.getWidth();
        int height = top + view.getHeight();
        view.layout(left, top, width, height);
    }



    @OnClick(R.id.combineTwoImageButton)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.combineTwoImageButton:
                combineTwoImage();
                break;

        }
    }

    private void combineTwoImage() {
        Bitmap bg = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        Bitmap fg = BitmapFactory.decodeResource(getResources(), R.mipmap.fg);

        Bitmap bitmap = Bitmap.createBitmap(mFgImage.getWidth(),mFgImage.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bg, ViewCompat.getX(mBgImage),ViewCompat.getY(mBgImage),null);
        canvas.drawBitmap(fg,0,0,null);
        canvas.save(Canvas.ALL_SAVE_FLAG);//保存
        canvas.restore();
    }

    /**
     * get status bar height
     * @return
     */
    private int getStatusBarHeight(){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * get titleBar Height
     * @return
     */
    private int getTitlebarheight(){
        //get the status bar height
        Rect r = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //the root content view
        View v = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        return v.getTop() - r.top;
    }

    /**
     * get screen height
     * @return
     */
    private int getScreenHeight(){
        Display dis = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetric = new DisplayMetrics();
        dis.getMetrics(displayMetric);
        return displayMetric.heightPixels;
    }

    /**
     * get screen width
     * @return
     */
    private int getScreenWidth(){
        Display dis = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetric = new DisplayMetrics();
        dis.getMetrics(displayMetric);
        return displayMetric.widthPixels;
    }
}
