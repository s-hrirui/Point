package com.cs.point.ext

import com.cs.point.app.App
import com.cs.point.constant.SystemInfo
import com.cs.point.util.ToolUtil
import java.util.HashMap

fun initMap(): Map<String, String> {
    val param = HashMap<String, String>()
    param["api_token"] = ""
    param["meid"] = SystemInfo.getMeId()
    param["device"] = SystemInfo.getImei1()
    param["device2"] = SystemInfo.getImei2()
    param["platform"] = "android"
    param["timestamp"] = System.currentTimeMillis().toString()
    param["system"] = systemVer()
    param["version"] = App.instance.versionName
    param["mobile_model"] = phoneModel()
    param["ip"] = ToolUtil.getIPAddress(App.instance)
    param["wifiname"] = ToolUtil.getWifiName()
    param["packages"] = ToolUtil.getAppProcessName()
    param["channel"] = ToolUtil.getChannel()
    return param
}