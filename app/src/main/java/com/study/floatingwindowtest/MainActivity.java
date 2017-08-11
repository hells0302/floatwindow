package com.study.floatingwindowtest;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ACTION_USAGE_ACCESS_SETTINGS = 10;
    private static final int REQUEST_ACTION_MANAGE_OVERLAY_PERMISSION = 11;
    private EditText editText;
    SharedPreferences sharedPreferences;
    private TextView help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=(EditText)findViewById(R.id.editText);
        help=(TextView)findViewById(R.id.help);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        sharedPreferences=getSharedPreferences("msg",MODE_PRIVATE);
        editText.setText(sharedPreferences.getString("msg",""));
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void startFloatWindowService(String msg) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //版本在6.0以上判断是否有创建悬浮窗的权限
            if (Settings.canDrawOverlays(this)) {
                Intent intent=new Intent(this,FloatWindowService.class);
                intent.putExtra("msg",msg);
                startService(intent);
                Toast.makeText(this,"开启悬浮窗服务成功",Toast.LENGTH_SHORT).show();
            }else{
                openOverlayPermission();
            }
        } else {
            Intent intent1=new Intent(this,FloatWindowService.class);
            intent1.putExtra("msg",msg);
            startService(intent1);
            Toast.makeText(this,"开启悬浮窗服务成功",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 打开悬浮窗权限
     */
    private void openOverlayPermission() {
        new AlertDialog.Builder(this)
                .setTitle("权限请求")
                .setMessage("显示悬浮窗需要开启悬浮窗显示权限，是否去开启？")
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivityForResult(intent, REQUEST_ACTION_MANAGE_OVERLAY_PERMISSION);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
    public void onButtonClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn1:
                if(TextUtils.isEmpty(editText.getText().toString()))
                {
                    Toast.makeText(this,"请输入要显示的内容",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    saveMsg(editText.getText().toString());
                    if(checkUsagePermission())
                    {
                        startFloatWindowService(editText.getText().toString());
                    }else
                    {
                        openUsagePermission();
                    }
                }

                break;
            case R.id.btn2:
                Toast.makeText(this,"已关闭悬浮窗",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,FloatWindowService.class);
                stopService(intent);
                break;
            case R.id.btn3:
                    if(help.getVisibility()==View.GONE)
                    {
                        help.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        help.setVisibility(GONE);
                    }
                break;
        }
    }
    //查看使用情况授权
    private boolean checkUsagePermission() {
        AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = AppOpsManager.MODE_ALLOWED;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(),getPackageName());
        }
        return mode ==AppOpsManager.MODE_ALLOWED;
    }
    /**
     * 打开查看应用使用情况授权
     */
    private void openUsagePermission() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("请开启查看应用使用情况的权限")
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //In some cases, a matching Activity may not exist, so ensure you safeguard against this.
                        startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),REQUEST_ACTION_USAGE_ACCESS_SETTINGS);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_ACTION_USAGE_ACCESS_SETTINGS == requestCode){
            if(checkUsagePermission()) {
                if(TextUtils.isEmpty(editText.getText().toString()))
                {
                    Toast.makeText(this,"请输入要显示的内容",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    saveMsg(editText.getText().toString());
                    startFloatWindowService(editText.getText().toString());
                }
            }
        }else if (REQUEST_ACTION_MANAGE_OVERLAY_PERMISSION == requestCode) {
                if(TextUtils.isEmpty(editText.getText().toString()))
                {
                    Toast.makeText(this,"请输入要显示的内容",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    saveMsg(editText.getText().toString());
                    startFloatWindowService(editText.getText().toString());
                }
        }
    }
    private void saveMsg(String message)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("message",message);
        editor.commit();
    }

}
