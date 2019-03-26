/*
 * Copyright (C) UseKamba Ltda - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential and will be punished by law
 * Written by Alexandre Antonio Juca <corextechnologies@gmail.com>
 *
 */

package io.github.alexjuca.kiyo;

import android.content.Context;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Kiyo extends ActivityCompat {
    /**
     * A Kiyo object allows you to easily setup your Permissions on
     * Android devices running Marshmallow and above. With method chaining and callbacks to notify you if
     * a certain permission your app requested was denied, granted or needs to be processed.
     */
    private String permission;
    private String[] permissions;
    private Context mContext;
    private KiyoListener mKiyoListener;
    private int mRequestCode = 0;

    public Kiyo(Context context) {
        this.mKiyoListener = null;
        this.mContext = context;
    }

    /***
     * Pass in a context to setup a Kiyo object
     * @param context
     * @return
     */
    public static Kiyo with(@NonNull Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context required");
        } else {
            return new Kiyo(context);
        }
    }

    /**
     * Setup a permission request by passing in a permission
     *
     * @throws IllegalArgumentException
     */
    public Kiyo withPermission(@NonNull String permission) {
        if (permission == null) {
            throw new IllegalArgumentException("Response can not be null. See Manifest permissions to set a permission");
        } else {
            this.permission = permission;
        }
        return this;
    }

    private Kiyo withPermissions(@NonNull String[] permissions) {
        if (permissions == null) {
            throw new IllegalArgumentException("Response can not be null. See Manifest permissions to set a permission");
        } else {
            this.permissions = permissions;
        }
        return this;
    }

    /***
     * Pass in a listener to that will listen to permission events
     * and fire a callback
     * @param listener mKiyoListener
     * @return this
     */
    public Kiyo withListener(@NonNull KiyoListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Must have mKiyo Listener");
        } else this.mKiyoListener = listener;
        return this;
    }

    private void checkIfPermissionExists(@NonNull Context context, @NonNull String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            if (mKiyoListener != null)
                mKiyoListener.onPermissionAccepted(PackageManager.PERMISSION_GRANTED);
        } else {
            if (shouldShowRequestPermissionRationale((AppCompatActivity) mContext, permission)) {
                if (mKiyoListener != null) {
                    mKiyoListener.onPermissionDenied(PackageManager.PERMISSION_DENIED);
                }
            } else {
                requestPermissions(((AppCompatActivity) mContext), new String[]{permission}, mRequestCode);
                if (mKiyoListener != null) {
                    mKiyoListener.onShouldShowRequestPermissionRationale();
                }
            }
        }
    }

    private void checkIfMultiplePermissions(@NonNull Context context, @NonNull String[] permission) {
        for (int i = 0; i < permissions.length - 1; ++i) {
            if (ContextCompat.checkSelfPermission(context, permission[i]) == PackageManager.PERMISSION_GRANTED) {
                if (mKiyoListener != null)
                    mKiyoListener.onPermissionAccepted(PackageManager.PERMISSION_GRANTED);
            } else {
                if (shouldShowRequestPermissionRationale((AppCompatActivity) mContext, permission[i])) {
                    if (mKiyoListener != null) {
                        mKiyoListener.onPermissionDenied(PackageManager.PERMISSION_DENIED);
                    }
                } else {
                    requestPermissions(((AppCompatActivity) mContext), permissions, 0);
                    if (mKiyoListener != null) {
                        mKiyoListener.onShouldShowRequestPermissionRationale();
                    }
                }
            }
        }
    }

    /***
     * This method verifies if the permission exists.
     * This method should be the last method to call in the chain.
     * @return this
     */
    public Kiyo verify() {
        checkIfPermissionExists(mContext, permission);
        return this;
    }

    private Kiyo verifyMultiple() {
        checkIfMultiplePermissions(mContext, permissions);
        return this;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (this.mKiyoListener != null)
                    this.mKiyoListener.onPermissionAccepted(PackageManager.PERMISSION_GRANTED);
            } else {
                if (mKiyoListener != null)
                    this.mKiyoListener.onPermissionDenied(PackageManager.PERMISSION_DENIED);
            }
        }
    }
}