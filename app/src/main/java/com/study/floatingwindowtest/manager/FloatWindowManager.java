package com.study.floatingwindowtest.manager;

import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.study.floatingwindowtest.view.FloatWindowBig;
import com.study.floatingwindowtest.view.FloatWindowSmall;

/**
 * Created by dnw on 2017/8/11.
 */

public class FloatWindowManager {
    private volatile static FloatWindowManager floatWindowManager=null;
    private static WindowManager windowManager;
    private Context context;
    public boolean isFloatWindowSmall=false;
    public FloatWindowManager(Context context)
    {
        this.context=context;
    }
    //单例模式DCL
    public static FloatWindowManager getInstance(Context context)
    {
        if(floatWindowManager==null)
        {
            synchronized (FloatWindowManager.class)
            {
                if(floatWindowManager==null)
                {
                    floatWindowManager=new FloatWindowManager(context);
                }
            }

        }
        return floatWindowManager;
    }

    private static FloatWindowBig floatWindowBig;
    private static FloatWindowSmall floatWindowSmall;
    //显示小悬浮窗
    public void showFloatWindowSmall(Context context)
    {
        isFloatWindowSmall=true;
        if(floatWindowSmall==null)
        {
            floatWindowSmall=new FloatWindowSmall(context);
            this.context=context;
        }
        getWindowManager().addView(floatWindowSmall,floatWindowSmall.layoutParams);
    }
    //更新小悬浮窗
    public void updateFloatWindowSmall()
    {
        if(floatWindowSmall!=null)
        {

        }
    }
    //关闭小悬浮窗
    public void closeFloatWindowSmall()
    {
        if(floatWindowSmall!=null)
        {
            getWindowManager().removeView(floatWindowSmall);
            floatWindowSmall=null;
        }
    }
    //显示大悬浮窗
    public void showFloatWindowBig(Context context,String msg)
    {
        isFloatWindowSmall=false;
        if(floatWindowBig==null)
        {
            floatWindowBig=new FloatWindowBig(context,msg);
            this.context=context;
        }
        getWindowManager().addView(floatWindowBig,floatWindowBig.layoutParams);
    }
    /**
     * 关闭大悬浮窗
     */
    public void closeFloatWindowBig() {
        if (floatWindowBig != null) {
            getWindowManager().removeView(floatWindowBig);
            floatWindowBig = null;
        }
    }
    //更新大悬浮窗
    public void updateFloatWindowBig()
    {
        if(floatWindowBig!=null)
        {
            floatWindowBig.update();
        }
    }
    /**
     * 悬浮窗是否显示
     * @return
     */
    public boolean isFloatWindowShowing() {
        return floatWindowBig != null || floatWindowSmall != null;
    }
    public boolean isFloatWindowSmallShowing() {
        return floatWindowSmall != null ;
    }
    public boolean isFloatWindowBigShowing() {
        return floatWindowBig != null ;
    }
    /**
     * 关闭所有悬浮窗
     */
    public void closeAllFloatWindow() {
        closeFloatWindowSmall();
        closeFloatWindowBig();
    }
    public WindowManager getWindowManager() {
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return windowManager;
    }
}
