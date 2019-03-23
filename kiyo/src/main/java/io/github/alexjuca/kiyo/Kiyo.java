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
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.View;

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
    private String mReason;
    private int mRequestCode = 0;
    private View mRootView;

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

    /***
     * Pass in a context to setup a Kiyo object
     * @param context
     * @return
     */
    public Kiyo check(@NonNull Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context required");
        } else {
            return new Kiyo(context);
        }
    }

    public Kiyo withView(@NonNull View view) {
        if (view == null) {
            throw new IllegalArgumentException("Must provide a view");
        } else {
            this.mRootView = view;
        }
        return this;
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

    public Kiyo withPermissions(@NonNull String[] permissions) {
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
        } else {
            this.mKiyoListener = listener;
        }
        return this;
    }

    /***
     * Set's up the text to be shown to the user when the user needs explanation
     * regarding the use of a certain permission.
     * @param reason - A String object containing the reason a permission is needed.
     * @return Kiyo object
     */
    public Kiyo withReason(@NonNull String reason) {
        if (reason == null || reason.isEmpty()) {
            throw new IllegalArgumentException("Kiyo must be passed a reason why this permission is needed and can not be empty");
        } else {
            mReason = reason;
        }
        return this;
    }

    /***
     * Set up the text to be shown to the user when the user needs explanation
     * regarding the use of a certain permission.
     * @param textResourceId - An Android String resource id pointing to the reason
     *                       the app needs a specified permission.
     * @return Kiyo object
     */
    public Kiyo withReason(@NonNull int textResourceId) {
        if (textResourceId == 0) {
            throw new Resources.NotFoundException("Text resource for Id not found");
        } else {
            mReason = mContext.getString(textResourceId);
        }
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
                    if (checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                        mKiyoListener.onPermissionAccepted(PackageManager.PERMISSION_GRANTED);
                    } else {
                        mKiyoListener.onPermissionDenied(PackageManager.PERMISSION_DENIED);
                    }
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

    private void setListener(KiyoListener listener) {
        this.mKiyoListener = listener;
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

    public Kiyo verifyMultiple() {
        checkIfMultiplePermissions(mContext, permissions);
        return this;
    }

    public Kiyo onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (this.mKiyoListener != null)
                    this.mKiyoListener.onPermissionAccepted(0);
            } else {
                if (mKiyoListener != null)
                    this.mKiyoListener.onPermissionDenied(1);
            }
        }
        return this;
    }
}
