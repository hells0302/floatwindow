package com.study.floatingwindowtest.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.study.floatingwindowtest.R;
import com.study.floatingwindowtest.Util.Util;
import com.study.floatingwindowtest.manager.FloatWindowManager;

/**
 * Created by dnw on 2017/8/11.
 */

public class FloatWindowSmall extends LinearLayout {
    private Context context;
    private WindowManager windowManager;
    public WindowManager.LayoutParams layoutParams;
    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;
    public FloatWindowSmall(Context context) {
        super(context);
        this.context=context;
        init();
    }
    private void init()
    {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_small,this);
        View view1=findViewById(R.id.floatWindowSmallParent);

        layoutParams=new WindowManager.LayoutParams();
        layoutParams.width=view1.getLayoutParams().width;
        layoutParams.height=view1.getLayoutParams().height;
        //设置位置
        layoutParams.x= Util.getScreen(context).x-layoutParams.width;
        layoutParams.y=Util.getScreen(context).y/2-layoutParams.height/2;

        layoutParams.gravity= Gravity.LEFT|Gravity.TOP;
        layoutParams.format= PixelFormat.RGBA_8888;
        layoutParams.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        layoutParams.type=WindowManager.LayoutParams.TYPE_PHONE;
       // initView();
    }
    private void initView()
    {
        TextView textView=(TextView) findViewById(R.id.textViewSmall);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatWindowManager.getInstance(context).closeFloatWindowSmall();
                FloatWindowManager.getInstance(context).showFloatWindowBig(context,"context.getSharedPreferences(\"msg\",Context.MODE_PRIVATE).getString(\"message\",\"今天你努力了么？\")");
            }
        });
    }
    private float startX = -1;
    private float startY = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                layoutParams.x = (int) (event.getRawX() - startX);
                layoutParams.y = (int) (event.getRawY() - startY);
                windowManager.updateViewLayout(this, layoutParams);
                break;
            case MotionEvent.ACTION_UP:
                if (event.getRawX() == xInScreen && event.getRawY() ==yInScreen) {
                    openFloatWindowBig();
                }
                break;
        }
        return super.onTouchEvent(event);
    }
    private void openFloatWindowBig() {
        FloatWindowManager.getInstance(context).closeFloatWindowSmall();
        FloatWindowManager.getInstance(context).showFloatWindowBig(context,context.getSharedPreferences("msg",Context.MODE_PRIVATE).getString("message","今天你努力了么？"));
    }
}
