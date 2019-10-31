package com.cs.point.http.download;

import android.os.Environment;
import android.text.TextUtils;

import com.cs.baselib.ext.ToastExtKt;
import com.cs.point.app.App;
import com.cs.point.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadManager {

    private static final AtomicReference<DownloadManager> INSTANCE = new AtomicReference<>();
    private static OkHttpClient mClient;
    private HashMap<String, Call> downCalls; //用来存放各个下载的请求

    public static DownloadManager getInstance() {
        for (; ; ) {
            DownloadManager current = INSTANCE.get();
            if (current != null) {
                return current;
            }
            current = new DownloadManager();
            if (INSTANCE.compareAndSet(null, current)) {
                return current;
            }
        }
    }

    private static OkHttpClient getOkHttpInstance() {
        if (mClient == null) {
            synchronized (DownloadManager.class) {
                if (mClient == null) {
                    mClient = new OkHttpClient.Builder()
                            .followRedirects(false) //禁制OkHttp的重定向操作，我们自己处理重定向
                            .addInterceptor(new RedirectInterceptor())
                            .build();
                }
            }
        }
        return mClient;
    }

    //处理重定向的拦截器
    private static class RedirectInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            okhttp3.Request request = chain.request();
            Response response = chain.proceed(request);
            int code = response.code();
            if (code == 307 || code == 302) {
                //获取重定向的地址
                String location = response.headers().get("Location");
                //重新构建请求
                Request newRequest = request.newBuilder().url(location).build();
                response = chain.proceed(newRequest);
            }
            return response;
        }
    }


    private DownloadManager() {
        if (downCalls == null)
            downCalls = new HashMap<>();
        mClient = getOkHttpInstance();
    }

    /**
     * 删除文件
     *
     * @param fileName
     * @return
     */
    public static boolean deleteFile(String fileName) {
        boolean status;
        SecurityManager checker = new SecurityManager();
        File file = new File(getFilePath(), fileName);
        if (file.exists()) {
            checker.checkDelete(file.toString());
            if (file.isFile()) {
                try {
                    file.delete();
                    status = true;
                } catch (SecurityException se) {
                    se.printStackTrace();
                    status = false;
                }
            } else
                status = false;
        } else
            status = false;
        return status;
    }

    /**
     * 查看是否在下载任务中
     *
     * @param url
     * @return
     */
    public boolean getDownloadUrl(String url) {
        return downCalls.containsKey(url);
    }

    /**
     * 开始下载
     *
     * @param url              下载请求的网址
     * @param downLoadObserver 用来回调的接口
     */
    public void download(String url, DownloadObserver downLoadObserver) {
        if (TextUtils.isEmpty(url)) {
            ToastExtKt.toast(App.Companion.getInstance(),"下载地址为空");
            return;
        }
        if (!url.startsWith("http")) {
            ToastExtKt.toast(App.Companion.getInstance(),"下载地址有误");
            return;
        }
        Observable.just(url)
                .filter(s -> !downCalls.containsKey(s)) // 过滤 call的map中已经有了,就证明正在下载,则这次不下载
                .flatMap((Function<String, ObservableSource<?>>) s -> Observable.just(createDownInfo(s))) // 生成 DownloadInfo
                .map(o -> getRealFileName((DownloadInfo) o)) // 如果已经下载，重新命名
                .flatMap((Function<DownloadInfo, ObservableSource<DownloadInfo>>) downloadInfo -> Observable.create(new DownloadSubscribe(downloadInfo))) // 下载
                .observeOn(AndroidSchedulers.mainThread()) // 在主线程中回调
                .subscribeOn(Schedulers.io()) //  在子线程中执行
                .subscribe(downLoadObserver); //  添加观察者，监听下载进度
    }

    /**
     * 下载取消或者暂停
     *
     * @param url
     */
    public void pauseDownload(String url) {
        if (url == null) return;
        Call call = downCalls.get(url);
        if (call != null) {
            call.cancel();//取消
        }
        downCalls.remove(url);
    }

    /**
     * 取消下载 删除本地文件
     *
     * @param info
     */
    public void cancelDownload(DownloadInfo info) {
        pauseDownload(info.getUrl());
        info.setProgress(0);
        info.setDownloadStatus(DownloadInfo.DOWNLOAD_CANCEL);
        deleteFile(info.getFileName());
    }

    /**
     * 创建DownInfo
     *
     * @param url 请求网址
     * @return DownInfo
     */
    private DownloadInfo createDownInfo(String url) {
        DownloadInfo downloadInfo = new DownloadInfo(url);
        long contentLength = getContentLength(url);//获得文件大小
        downloadInfo.setTotal(contentLength);
        String fileName = getNameFromUrl(url);
        downloadInfo.setFileName(fileName);
        return downloadInfo;
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    public static String getNameFromUrl(String url) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            md.update(url.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                String str = Integer.toHexString(b & 0xFF);
                if (str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
            }
            result = sb.toString() + ".apk";
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 如果文件已下载重新命名新文件名
     *
     * @param downloadInfo
     * @return
     */
    private DownloadInfo getRealFileName(DownloadInfo downloadInfo) {
        String fileName = downloadInfo.getFileName();
        long downloadLength = 0, contentLength = downloadInfo.getTotal();
        File path = new File(getFilePath());
        if (!path.getParentFile().exists()) {
            path.getParentFile().mkdir();
        }
        File file = new File(getFilePath(), fileName);
        if (file.exists()) {
            //找到了文件,代表已经下载过,则获取其长度
            if (downloadLength > contentLength) {
                if (FileUtil.deleteFile(file, false)) {
                    downloadLength = 0;
                }
            } else
                downloadLength = file.length();
        }
        //设置改变过的文件名/大小
        downloadInfo.setProgress(downloadLength);
        downloadInfo.setFileName(file.getName());
        return downloadInfo;
    }

    private class DownloadSubscribe implements ObservableOnSubscribe<DownloadInfo> {
        private DownloadInfo downloadInfo;

        public DownloadSubscribe(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void subscribe(ObservableEmitter<DownloadInfo> e) throws Exception {
            String url = downloadInfo.getUrl();
            long downloadLength = downloadInfo.getProgress();//已经下载好的长度
            long contentLength = downloadInfo.getTotal();//文件的总长度
            //初始进度信息
            e.onNext(downloadInfo);
            if (downloadLength == contentLength) {
                e.onComplete();//完成
                return;
            }
            Request request = new Request.Builder()
                    //确定下载的范围,添加此头,则服务器就可以跳过已经下载好的部分
                    .addHeader("RANGE", "bytes=" + downloadLength + "-" + contentLength)
                    .url(url)
                    .build();
            mClient = getOkHttpInstance();
            Call call = mClient.newCall(request);
            downCalls.put(url, call);//把这个添加到call里,方便取消
            Response response = call.execute();
            File file = new File(getFilePath(), downloadInfo.getFileName());
            InputStream is = null;
            FileOutputStream fileOutputStream = null;
            try {
                is = response.body().byteStream();
                fileOutputStream = new FileOutputStream(file, true);
                byte[] buffer = new byte[2048];//缓冲数组2kB
                int len;
                while ((len = is.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                    downloadLength += len;
                    downloadInfo.setProgress(downloadLength);
                    e.onNext(downloadInfo);
                }
                fileOutputStream.flush();
                downCalls.remove(url);
            } finally {
                //关闭IO流
                DownloadIO.closeAll(is, fileOutputStream);
            }
            e.onComplete();//完成
        }
    }

    /**
     * 获取下载长度
     *
     * @param downloadUrl
     * @return
     */
    public static long getContentLength(String downloadUrl) {
        Request request = new Request.Builder()
                .url(downloadUrl)
                .addHeader("Accept-Encoding", "identity")
                .build();
        mClient = getOkHttpInstance();
        try {
            Response response = mClient.newCall(request).execute();
            if (response != null) {
                long contentLength = response.body().contentLength();
                response.close();
                contentLength = contentLength == 0 ? DownloadInfo.TOTAL_ERROR : contentLength;
                return contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DownloadInfo.TOTAL_ERROR;
    }

    /**
     * 获取app缓存路径
     *
     * @return
     */
//    public static String getFilePath() {
//        String cachePath;
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
//                || !Environment.isExternalStorageRemovable()) {
//            //外部存储可用
//            cachePath = MyApplication.getappContext().getExternalCacheDir().getAbsolutePath();
//        } else {
//            //外部存储不可用
//            cachePath = MyApplication.getappContext().getCacheDir().getAbsolutePath();
//        }
//        File file = new File(cachePath);
//        if (!file.exists()) {//判断文件目录是否存在
//            file.mkdirs();
//        }
//        return cachePath;
//    }
    public static String getFilePath() {
        String directoryPath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {//判断外部存储是否可用
            directoryPath = App.Companion.getInstance().getExternalFilesDir("chengzhuan").getAbsolutePath();
        } else {//没外部存储就使用内部存储
            directoryPath = App.Companion.getInstance().getFilesDir() + File.separator + "chengzhuan";
        }
        File file = new File(directoryPath);
        if (!file.exists()) {//判断文件目录是否存在
            file.mkdirs();
        }
        return directoryPath;
    }
}
