package com.wrbug.developerhelper.commonutil

import com.wrbug.developerhelper.commonutil.entity.ApkInfo
import com.wrbug.developerhelper.commonutil.shell.ShellManager
import java.io.File


object AppInfoManager {
    /**
     * 获取所有应用
     */
    private fun getAllApps(): HashMap<String, ApkInfo> {
        val apkMap = HashMap<String, ApkInfo>()
        val pManager = CommonUtils.application.packageManager
        // 获取手机内所有应用
        val paklist = pManager.getInstalledPackages(0)
        for (packageInfo in paklist) {
            apkMap[packageInfo.packageName] = ApkInfo(packageInfo, packageInfo.applicationInfo)
        }
        return apkMap
    }

    fun getAppByPackageName(packageName: String): ApkInfo? {
        return getAllApps()[packageName]
    }


    fun getSharedPreferencesFiles(packageName: String): Array<File> {
        return getSharedPreferencesFiles(Constant.dataDir, packageName)
    }

    private fun getSharedPreferencesFiles(dir: String, packageName: String): Array<File> {
        val path = "$dir/$packageName/shared_prefs"
        val list = ShellManager.lsDir(path)
        val files = ArrayList<File>()
        for (file in list) {
            file?.let {
                if (it.endsWith(".xml")) {
                    files.add(File(path, it))
                }
            }

        }
        return files.toTypedArray()
    }

}