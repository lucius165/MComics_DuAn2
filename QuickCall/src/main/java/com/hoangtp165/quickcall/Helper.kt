package com.hoangtp165.quickcall

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import java.io.File

class Helper {

    /**
     *  if the method return true, the app already have the permission.
     *  onShouldShowRequestPermissionRationale will be calling if user has denied the previously request
     *  override the onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) to checkout the permission granted or not
     *  more information: https://developer.android.com/training/permissions/requesting.html
     * */
    fun requestPermission(activity: Activity, permission: String, onShouldShowRequestPermissionRationale: () -> Unit, requestCode: Int): Boolean {
        when (ContextCompat.checkSelfPermission(activity, permission)) {
            PackageManager.PERMISSION_GRANTED -> {
                return true
            }
            else -> {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    onShouldShowRequestPermissionRationale()
                } else {
                    ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
                }
            }
        }

        return false
    }

    fun createFile(context: Context, path: String = context.filesDir.absolutePath, fileName: String, onEnd: ((success: Boolean) -> Unit)) {
        val backgroundTask = DoInBackground<Unit, Void, Unit>()
        backgroundTask.setDoInBackground {
            val file = File(path, fileName)
            onEnd(file.createNewFile())
        }.build().execute()
    }

    @SuppressLint("MissingPermission")
    fun isNetworkAvailable(context: Context): Boolean {
        val connectManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected && networkInfo.isAvailable
    }
}