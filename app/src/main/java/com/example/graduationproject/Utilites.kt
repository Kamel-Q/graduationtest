package com.example.graduationproject

import android.content.Context

class Utilites {

    companion object {
        @Throws(Exception::class)
        fun getDataDir(context: Context, packageName: String): String {
            return context.packageManager.getPackageInfo(packageName, 0).applicationInfo.dataDir
        }
    }
}