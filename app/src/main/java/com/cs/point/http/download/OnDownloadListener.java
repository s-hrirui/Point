package com.cs.point.http.download;

public interface OnDownloadListener {

    void onDownloadInfo(DownloadInfo info);

    /**
     * 下载成功之后的文件
     */
    void onDownloadSuccess(String fileName);

    /**
     * 下载进度
     */
    void onDownloading(int progress);

    /**
     * 下载异常信息
     */

    void onDownloadFailed(Throwable e);
}
