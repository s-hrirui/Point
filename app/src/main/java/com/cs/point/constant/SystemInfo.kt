package com.cs.point.constant

import android.text.TextUtils
import com.cs.point.util.ToolUtil
import java.util.ArrayList

object SystemInfo {

    private var meId:String = ""

    fun getImei1(): String = if (TextUtils.isEmpty(ToolUtil.getIMEI(0))) ToolUtil.getIMEI() else ToolUtil.getIMEI(0)

    fun getImei2(): String = if (TextUtils.isEmpty(ToolUtil.getIMEI(1))) "" else ToolUtil.getIMEI(1)

    fun getMeId(): String {
        if (TextUtils.isEmpty(meId)) {
            val list = ArrayList<String>()
            list.add(ToolUtil.getIMEI())
            list.add(ToolUtil.getDeviceId(0))
            list.add(ToolUtil.getDeviceId(1))
            list.add(ToolUtil.getDeviceId(2))
            for (str in list) {
                if (str != null && str.length == 14 && str != "00000000000000") {
                    meId = str
                    break
                }
            }
        }
        return if (TextUtils.isEmpty(meId)) "" else meId
    }

}