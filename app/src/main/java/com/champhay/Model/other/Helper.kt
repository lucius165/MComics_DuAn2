package com.champhay.Model.other

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

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
}