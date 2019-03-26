/*
 * Copyright (C) UseKamba Ltda - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential and will be punished by law
 * Written by Alexandre Antonio Juca <corextechnologies@gmail.com>
 *
 */

package io.github.alexjuca.kiyo;

public interface KiyoListener {
    /**
     * Called when a Permission has been granted by
     * the device. This call back will always return 0
     * correspending to PERMISSION_GRANTED
     * @param response - an integer describing the success/failure value
     */
    void onPermissionAccepted(int response);

    /**
     * Called when a Permission has not been granted by
     * the device. This callback will always return -1
     * corresepending to Android's PERMISSION_DENIED constant.
     * @param response an integer describing the success/failure value
     */
    void onPermissionDenied(int response);

    /**
     * Called when a user dialog needs to be shown requesting a certain
     * permission. It is in the body of this method where the client can implement
     * requestPermissions(Activity activity, String[] permissions, int requestCode)
     */
    void onShouldShowRequestPermissionRationale();
}