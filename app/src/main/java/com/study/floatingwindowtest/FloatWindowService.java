package com.study.floatingwindowtest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.study.floatingwindowtest.Util.Util;
import com.study.floatingwindowtest.manager.FloatWindowManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dnw on 2017/8/11.
 */

public class FloatWindowService extends Service {
    private Context context;
    private Timer timer;
    private Handler handler=new Handler();
    private String nMessage;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(timer==null)
        {
            timer=new Timer();
            timer.schedule(new RefreshTask(),0,500);
        }
        if(intent!=null)
        {
            nMessage=intent.getStringExtra("msg");
        }else
        {
            nMessage="今天你努力了么？";
        }
        return super.onStartCommand(intent, flags, startId);
    }
    class RefreshTask extends TimerTask
    {
        @Override
        public void run() {
            //当最近运行的App包名为null，执行刷新悬浮窗
            if(Util.getLatestPackage(context)==null)
            {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //以显示，更新
                    }
                });
                return;
            }
            //当前在桌面显示或更新悬浮窗
            if(Util.isHome(context))
            {
                if(FloatWindowManager.getInstance(context).isFloatWindowShowing())
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //以显示悬浮窗，更新
                        }
                    });
                }else if(!FloatWindowManager.getInstance(context).isFloatWindowShowing())
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(FloatWindowManager.getInstance(context).isFloatWindowSmall)
                            {
                                FloatWindowManager.getInstance(context).showFloatWindowSmall(context);
                            }
                            else
                            {
                                FloatWindowManager.getInstance(context).showFloatWindowBig(context,nMessage);
                            }

                        }
                    });
                }
            }else
            {//不在桌面关闭所有窗口
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        FloatWindowManager.getInstance(context).closeAllFloatWindow();
                    }
                });
            }
        }
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        timer = null;
        FloatWindowManager.getInstance(context).closeAllFloatWindow();
        super.onDestroy();
    }
}
