package com.cs.point.util

import com.cs.baselib.ext.toast
import com.cs.point.app.App
import java.io.File

object FileUtil {

    @JvmStatic
    fun deleteFile(rootFile: File, showMsg: Boolean): Boolean {
        var result = false
        if (rootFile.exists()) {
            if (rootFile.isDirectory) {
                val fileList = rootFile.listFiles()
                if (fileList != null) {
                    for (i in fileList.indices) {
                        deleteFile(fileList[i], false)
                    }
                }
            }
            result = rootFile.delete()
            if (showMsg) {
                if (result) {
                    toast(App.instance, "清除成功")
                } else {
                    toast(App.instance, "清除失败")
                }
            }
            return result
        }
        toast(App.instance, "文件不存在")
        return result
    }

    fun deleteFolderApkFile(filePath: String, dedeleteThisPath: Boolean) {
        try {
            val file = File(filePath)//获取SD卡指定路径
            val files = file.listFiles()//获取SD卡指定路径下的文件或者文件夹
            for (i in files.indices) {
                if (files[i].isFile) {//如果是apk直接删除
                    val photoFile = File(files[i].path)
                    if (photoFile.name.endsWith(".apk")) {
                        photoFile.delete()
                    }
                } else {
                    if (dedeleteThisPath) {//如果是文件夹再次迭代进里面找到指定文件路径
                        val myfile = files[i].listFiles()
                        for (d in myfile.indices) {
                            val photoFile = File(myfile[d].path)
                            photoFile.delete()
                        }
                    }

                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
