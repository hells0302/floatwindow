package com.study.floatingwindowtest.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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

public class FloatWindowBig extends LinearLayout{
    private Context context;
    private WindowManager windowManager;
    private String msg;
    public WindowManager.LayoutParams layoutParams;
    public FloatWindowBig(Context context,String msg) {
        super(context);
        this.context=context;
        this.msg=msg;
        init();
    }

    private void init()
    {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_big,this);
        View view=findViewById(R.id.floatWindowBigParent);

        layoutParams=new WindowManager.LayoutParams();
        layoutParams.width=view.getLayoutParams().width;
        layoutParams.height=view.getLayoutParams().height;
        //设置位置
        layoutParams.x= Util.getScreen(context).x-layoutParams.width;
        layoutParams.y=Util.getScreen(context).y/2-layoutParams.height/2;

        layoutParams.gravity= Gravity.LEFT|Gravity.TOP;
        layoutParams.format= PixelFormat.RGBA_8888;
        layoutParams.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        layoutParams.type=WindowManager.LayoutParams.TYPE_PHONE;
        init_view();
        update();
    }
    private void init_view()
    {
        TextView textView=(TextView)findViewById(R.id.floatingTextView);
        textView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FloatWindowManager.getInstance(context).closeFloatWindowBig();
                FloatWindowManager.getInstance(context).showFloatWindowSmall(context);
                return true;
            }
        });
    }
    public void update()
    {
        ((TextView)findViewById(R.id.floatingTextView)).setText(msg);
    }
    private float startX = -1;
    private float startY = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                startX=event.getX();
                startY=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                layoutParams.x=(int)(event.getRawX()-startX);
                layoutParams.y=(int)(event.getRawY()-startY);
                windowManager.updateViewLayout(this,layoutParams);
                break;
            case MotionEvent.ACTION_UP:
               break;
        }
        return super.onTouchEvent(event);
    }
}
