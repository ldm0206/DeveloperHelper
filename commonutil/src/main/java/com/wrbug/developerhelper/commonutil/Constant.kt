package com.wrbug.developerhelper.commonutil

import android.os.Build

object Constant {
    const val ONE_KB = 1024L
    const val ONE_MB = ONE_KB * 1024
    const val ONE_GB = ONE_MB * 1024
    private const val DATA_MIRROR_DIR = "/data_mirror/data_ce/null/0"
    private const val DATA_DIR = "/data/data"

    fun getDataDir(packageName: String): String {
        return "$dataDir/$packageName"
    }

    val dataDir by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            DATA_MIRROR_DIR
        } else {
            DATA_DIR
        }
    }
}