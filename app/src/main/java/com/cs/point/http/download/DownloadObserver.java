package com.cs.point.http.download;

import android.widget.TextView;

import com.cs.baselib.ext.ToastExtKt;
import com.cs.point.app.App;
import com.cs.point.util.ToolUtil;

import java.io.File;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DownloadObserver implements Observer<DownloadInfo> {

    public Disposable d;//可以用于取消注册的监听者
    public DownloadInfo downloadInfo;

    private OnDownloadListener listener;
    private TextView download_btn;
    private boolean isItselfApk = false;
    private String md5;

    public DownloadObserver setDownloadListener(OnDownloadListener listener) {
        this.listener = listener;
        return this;
    }

    public DownloadObserver setButtonStatus(TextView download_btn) {
        this.download_btn = download_btn;
        return this;
    }

    public DownloadObserver setButtonStatus(TextView download_btn,boolean isItselfApk,String md5) {
        this.download_btn = download_btn;
        this.isItselfApk = isItselfApk;
        this.md5 = md5;
        return this;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
    }

    @Override
    public void onNext(DownloadInfo info) {
        this.downloadInfo = info;
        downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD);
        int progress = (int) (info.getProgress() * 100 / info.getTotal());
        if (listener != null) {
            listener.onDownloading(progress);
            listener.onDownloadInfo(info);
        }
        if (download_btn != null) {
            download_btn.setText("下载中" + (progress <= 0 || progress > 100 ? "" : progress + "%"));
            download_btn.setEnabled(false);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (DownloadManager.getInstance().getDownloadUrl(downloadInfo.getUrl())) {
            DownloadManager.getInstance().pauseDownload(downloadInfo.getUrl());
            downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_ERROR);
        } else {
            downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_PAUSE);
        }
        if (listener != null) {
            listener.onDownloadFailed(e);
        }
        if (download_btn != null) {
            download_btn.setEnabled(true);
        }
    }
    @Override
    public void onComplete() {
        if (downloadInfo != null) {
            downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_OVER);
            if (listener != null) {
                listener.onDownloadSuccess(downloadInfo.getFileName());
            }
            if (download_btn != null) {
                download_btn.setEnabled(true);
                File file = new File(DownloadManager.getFilePath(), downloadInfo.getFileName());
                if (isItselfApk && !ToolUtil.getFileMD5(file.getPath()).equals(md5)){
                    download_btn.setText("重新下载");
                    ToastExtKt.toast(App.Companion.getInstance(),"下载失败");
                    DownloadManager.getInstance().cancelDownload(downloadInfo);
                }else {
                    ToolUtil.installApk(file);
                    download_btn.setText("安装赚钱");
                }
            }
        }
    }
}
