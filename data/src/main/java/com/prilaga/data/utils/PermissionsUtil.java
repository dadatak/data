package com.prilaga.data.utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.Manifest.permission.*;
import static android.content.pm.PackageManager.PERMISSION_DENIED;

/**
 * Created by Oleg Tarashkevich on 06.03.16.
 */
public final class PermissionsUtil {

    private static final String TAG = PermissionsUtil.class.getSimpleName();

    public static int PERMISSIONS_REQUEST_CODE = 1;
    public static final Set<String> dangerous = new HashSet<>(Arrays.asList(
            READ_CALENDAR,
            WRITE_CALENDAR,
            CAMERA,
            READ_CONTACTS,
            WRITE_CONTACTS,
            GET_ACCOUNTS,
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION,
            RECORD_AUDIO,
            READ_PHONE_STATE,
            CALL_PHONE,
            READ_CALL_LOG,
            WRITE_CALL_LOG,
            ADD_VOICEMAIL,
            USE_SIP,
            PROCESS_OUTGOING_CALLS,
            BODY_SENSORS,
            SEND_SMS,
            RECEIVE_SMS,
            READ_SMS,
            RECEIVE_WAP_PUSH,
            RECEIVE_MMS,
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE));

    /**
     * Request all permissions of current application
     *
     * @param activity needs for permissions request, should not be null
     */
    public static void requestPermissions(final @NonNull Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);

                if (info.requestedPermissions != null) {

                    final List<String> missingPermissions = new ArrayList<>();

                    for (String permission : info.requestedPermissions) {
                        int checkPermission = ContextCompat.checkSelfPermission(activity, permission);
                        if (dangerous.contains(permission) && checkPermission == PERMISSION_DENIED) {
                            missingPermissions.add(permission);
                        }
                    }

                    String[] permissionsArray = missingPermissions.toArray(new String[missingPermissions.size()]);
                    requestPermissions(activity, permissionsArray);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Request part of permissions
     *
     * @param activity    needs for permissions request, should not be null
     * @param permissions The requested permissions.
     */
    public static void requestPermissions(final @NonNull Activity activity, final @NonNull String[] permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity != null && permissions != null && permissions.length > 0) {
            ActivityCompat.requestPermissions(activity, permissions, PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Check for granted permission
     *
     * @param permissions from Manifest
     * @return true if all permissions are granted, otherwise false
     */
    public static boolean isPermissionsGranted(final @NonNull String... permissions) {

        boolean isPermissionsGranted = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (permission != null) {
                    isPermissionsGranted = ActivityCompat.checkSelfPermission(DataUtil.getInstance().getContext(), permission) == PackageManager.PERMISSION_GRANTED;
                    if (!isPermissionsGranted)
                        break;
                }
            }
        }
        return isPermissionsGranted;
    }

    /**
     * Check if we have permissions for write mode
     * and that external storage is exists
     *
     * @return isGranted
     */
    public static boolean isExternalStorageAvailable() {
        boolean isGranted = isPermissionsGranted(WRITE_EXTERNAL_STORAGE);
        if (isGranted) {
            String extStorageState = Environment.getExternalStorageState();
            isGranted = Environment.MEDIA_MOUNTED.equalsIgnoreCase(extStorageState);
        }
        Log.d(TAG, "isExternalStorageAvailable: " + isGranted);
        return isGranted;
    }
}