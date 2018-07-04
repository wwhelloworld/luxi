package com.lcsd.luxi.utils;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import com.lcsd.luxi.R;
import com.lcsd.luxi.dialog.UpdataDialog;
import com.lcsd.luxi.entity.UpdateInfo;
import com.lcsd.luxi.http.AppConfig;
import com.lcsd.luxi.http.MyApplication;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.DownloadResponseHandler;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * @author coolszy
 * @date 2012-4-26
 * @blog http://blog.92coding.com
 * 版本更新管理
 */

public class UpdateManager {
    private static final String PATH1 = "/storage/emulated/0";
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 保存解析的XML信息 */
    HashMap<String, String> mHashMap;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;
    private File apkfile;
    private MyOkHttp myOkHttp;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                case 3:
                    checkUpdate();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public UpdateManager(Context context) {
        this.mContext = context;
        myOkHttp = MyApplication.getInstance().getmMyOkHttp();
        Request();
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate() {
        if (isUpdate()) {
            // 显示提示对话框
            showNoticeDialog();
        }
    }

    private HashMap<String, String> Request() {
        final HashMap<String, String> hashMap = new HashMap<>();
        Map<String, String> map = new HashMap<>();
        map.put("f", "version");
        MyApplication.getInstance().getmMyOkHttp().post(mContext, AppConfig.Comment_url, map, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                if (response != null) {
                    Log.d("获取的版本信息----", response);
                    UpdateInfo info = JSON.parseObject(response, UpdateInfo.class);
                    if (info.getStatus().equals("ok")) {
                        hashMap.put("version", info.getContent().get(0).getVersion_no());
                        hashMap.put("url", info.getContent().get(0).getVersion_url());
                        hashMap.put("name", info.getContent().get(0).getVersion_name());
                        hashMap.put("desc", info.getContent().get(0).getVersion_desc());
                        Log.d("版本2", "version2:" + info.getContent().get(0).getVersion_no());
                        mHashMap = hashMap;
                    }

                    if (hashMap.get("version") != null && !hashMap.get("version").equals("")) {
                        mHandler.sendEmptyMessage(3);
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
        if (hashMap != null) {
            return hashMap;
        } else {
            return null;
        }
    }

    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
    private boolean isUpdate() {
        // 获取当前软件版本
        int versionCode = getVersionCode(mContext);
        if (0 != mHashMap.size()) {
            int serviceCode = Integer.valueOf(mHashMap.get("version"));
            // 版本判断
            if (serviceCode > versionCode) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    private int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {
        final UpdataDialog dialog = new UpdataDialog(mContext);
        dialog.SetContent(mHashMap.get("desc"));
        dialog.show();
        dialog.findViewById(R.id.tv_updata_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 显示下载对话框
                if (mHashMap.get("url") != null && !mHashMap.get("url").equals("")) {
                    showDownloadDialog();
                } else {
                    Toast.makeText(mContext, "下载地址调整中，请稍后！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        // 构造软件下载对话框
        Builder builder = new Builder(mContext);
        TextView title = new TextView(mContext);
        title.setGravity(Gravity.CENTER);
        title.setPadding(10, 10, 10, 10);
        title.setText("正在更新");
        title.setTextColor(mContext.getResources().getColor(R.color.orange));
        builder.setCustomTitle(title);
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        builder.setCancelable(false);

       /* // 取消更新
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = false;
                myOkHttp.cancel(AppContext.getContext());
            }
        });*/
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 现在文件
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        // new downloadApkThread().start();
        if (!cancelUpdate) {
            downloadapk();
        }
    }

    private void downloadapk() {

        try {
            String sdpath = PATH1 + "/";
            mSavePath = sdpath + "downloadapk";
            final URL url = new URL(mHashMap.get("url"));
            String name = mHashMap.get("name");

            myOkHttp.download(MyApplication.getContext(), url.toString(), sdpath, name, new DownloadResponseHandler() {
                @Override
                public void onFinish(File download_file) {
                    if (download_file != null) {
                        apkfile = download_file;
                        mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                    }

                }

                @Override
                public void onProgress(long currentBytes, long totalBytes) {
                    // 计算进度条位置
                    progress = (int) (((float) currentBytes / totalBytes) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWNLOAD);
                }

                @Override
                public void onFailure(String error_msg) {
                    Log.d("下载返回的数据错误-----", error_msg);
                    if (error_msg != null) {
                        //'mHandler.sendEmptyMessage(DOWNLOAD);
                        downloadapk();
                    }

                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 安装APK文件
     */
    private void installApk() {
        //File apkfile = new File(mSavePath, mHashMap.get("name"));
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mContext, "com.lcsd.luxi.fileprovider", apkfile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
