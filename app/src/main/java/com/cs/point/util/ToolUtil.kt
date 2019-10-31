package com.cs.point.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager.GET_META_DATA
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.core.content.FileProvider
import com.cs.baselib.ext.toast
import com.cs.point.app.App
import com.dizoo.jixx.util.NetWorkUtil
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.math.BigInteger
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object ToolUtil {

    @JvmStatic
    fun installApk(file: File) {
        if (file.exists()) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val apkUri = FileProvider.getUriForFile(App.instance, "com.cz.point.fileprovider", file)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
            }
            App.instance.startActivity(intent)
        } else
            toast(App.instance, "未找到安装文件，请重新下载安装")
    }

    @JvmStatic
    fun getFileMD5(path: String): String {
        var bi: BigInteger? = null
        try {
            val buffer = ByteArray(8192)
            var len = 0
            val md = MessageDigest.getInstance("MD5")
            val file = File(path)
            val fis = FileInputStream(file)
            do {
                len = fis.read(buffer)
                if (len != -1) {
                    md.update(buffer, 0, len)
                }
            } while (true)
            fis.close()
            val b = md.digest()
            bi = BigInteger(1, b)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bi?.toString(16).toString()
    }

    @SuppressLint("MissingPermission")
    fun getIMEI(): String {
        var imeiStr: String? = ""
        try {
            imeiStr =
                (App.instance.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return imeiStr ?: ""
    }

    /**
     * @slotId slotId为卡槽Id，它的值为 0、1；
     */
    fun getIMEI(slotId: Int): String {
        try {
            val manager =
                App.instance.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val method = manager.javaClass.getMethod("getImei", Int::class.javaPrimitiveType!!)
            return method.invoke(manager, slotId) as String
        } catch (e: Exception) {
            return ""
        }
    }


    @SuppressLint("MissingPermission")
    fun getDeviceId(slotId: Int): String {
        var deviceId = ""
        val manager = App.instance.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var method: Method? = null
        try {
            method = manager.javaClass.getMethod("getDeviceId", Int::class.javaPrimitiveType!!)
            deviceId = if (slotId == 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    manager.meid
                } else {
                    manager.deviceId
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (manager.getMeid(slotId).isNullOrEmpty()) "" else manager.getMeid(slotId)
                } else {
                    method!!.invoke(manager, slotId) as String
                }
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return deviceId
    }

    fun getIPAddress(context: Context): String {
        val info =
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        if (info != null && info.isConnected) {
            if (info.type == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    val en = NetworkInterface.getNetworkInterfaces()
                    while (en.hasMoreElements()) {
                        val intf = en.nextElement()
                        val enumIpAddr = intf.inetAddresses
                        while (enumIpAddr.hasMoreElements()) {
                            val inetAddress = enumIpAddr.nextElement()
                            if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                                return inetAddress.getHostAddress()
                            }
                        }
                    }
                } catch (e: SocketException) {
                    e.printStackTrace()
                }

            } else if (info.type == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInfo = wifiManager.connectionInfo
                return intIP2StringIP(wifiInfo.ipAddress)
            }
        } else {
            //当前无网络连接,请在设置中打开网络
            toast(App.instance, "当前无网络连接,请在设置中打开网络")
        }
        return ""
    }

    /**
     *      * 将得到的int类型的IP转换为String类型
     *      *
     *      * @param ip
     *      * @return
     *      
     */
    private fun intIP2StringIP(ip: Int): String {
        return (ip and 0xFF).toString() + "." +
                (ip shr 8 and 0xFF) + "." +
                (ip shr 16 and 0xFF) + "." +
                (ip shr 24 and 0xFF)
    }

    /**
     * 获取SSID
     *
     * @return WIFI 的SSID
     */
    @SuppressLint("WifiManagerLeak")
    fun getWifiName(): String {
        val netType = NetWorkUtil.getNetworkType()
        if (netType != "WIFI") {
            return "($netType)"
        }
        var ssid = "unknown id"
        val wifiManager =
            App.instance.getSystemService(
                Context.WIFI_SERVICE
            ) as WifiManager
        if (null != wifiManager) {
            val info = wifiManager.connectionInfo
            val networkId = info.networkId
            val configurationList = wifiManager.configuredNetworks
            for (configuration in configurationList) {
                if (configuration.networkId == networkId) {
                    ssid = configuration.SSID
                    break
                }
            }
        }
        if (ssid.contains("\"")) {
            ssid = ssid.replace("\"", "")
        }
        return "$ssid($netType)"
    }

    fun getAppProcessName(): String {
        var pageName = ""
        val packageManager = App.instance.packageManager
        try {
            val packageInfos = packageManager.getInstalledPackages(0) ?: return ""
            for (packageInfo in packageInfos) {
                //过滤掉系统app
                if (ApplicationInfo.FLAG_SYSTEM and packageInfo.applicationInfo.flags != 0) {
                    continue
                }
                val myAppInfo = packageInfo.packageName
                pageName += myAppInfo + ","
            }
        } catch (e: Exception) {
        }

        pageName = if (!TextUtils.isEmpty(pageName) && pageName.endsWith(",")) pageName.substring(
            0,
            pageName.length - 1
        ) else pageName
        return pageName
    }

    fun getChannel(): String {
        try {
            val info = App.instance.packageManager.getApplicationInfo(
                App.instance.packageName,
                GET_META_DATA
            )
            return info.metaData.get("UMENG_CHANNEL")!!.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }
}