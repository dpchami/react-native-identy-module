package com.reactnativeidentymodule;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONObject;

@ReactModule(name = IdentyModuleModule.NAME)
public class IdentyModuleModule extends ReactContextBaseJavaModule {
    public static final String NAME = "IdentyModule";

    public static final int IDENTY_SDK_REQUEST = 1;
    private static final String E_CAPTURE_ACTIVITY_DOES_NOT_EXIST = "E_CAPTURE_ACTIVITY_DOES_NOT_EXIST";
    private static final String E_CAPTURE_ACTIVITY_CANCELLED = "E_CAPTURE_ACTIVITY_CANCELLED";
    private static final String E_FAILED_TO_SHOW_CAPTURE = "E_FAILED_TO_SHOW_CAPTURE";
    private static final String E_CAPTURE_DATA_NOT_FOUND = "E_CAPTURE_DATA_NOT_FOUND";

    private Promise mCapturePromise;

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
            //super.onActivityResult(activity, requestCode, resultCode, data);
            if (requestCode == IDENTY_SDK_REQUEST) {
                if (mCapturePromise != null) {
                    if (resultCode == Activity.RESULT_CANCELED) {
                        mCapturePromise.reject(E_CAPTURE_ACTIVITY_CANCELLED, "Fingerprint capture is cancelled");
                    } else if (resultCode == Activity.RESULT_OK) {
                        String responseData = intent.getStringExtra("data");

                        if (responseData == null) {
                            mCapturePromise.reject(E_CAPTURE_ACTIVITY_CANCELLED, "Fingerprint data not found");
                        } else {
                            mCapturePromise.resolve(responseData);
                        }
                    }
                    mCapturePromise = null;
                }
            }

        }
    };

    public IdentyModuleModule(ReactApplicationContext reactContext) {
        super(reactContext);
        // Add the listener for `onActivityResult`
        reactContext.addActivityEventListener(mActivityEventListener);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }


    // Example method
    // See https://reactnative.dev/docs/native-modules-android
    @ReactMethod
    public void multiply(int a, int b, Promise promise) {
        promise.resolve(a * b);
    }

    @ReactMethod
    public void getFingerprint(final Promise promise) {
        Activity currentActivity = getCurrentActivity();

        if (currentActivity == null) {
            promise.reject(E_CAPTURE_ACTIVITY_CANCELLED, "Activity does not exist");
            return;
        }

        // Store the promise to resolve/reject when picker returns data
        mCapturePromise = promise;

        try {
            final Intent identyIntent = new Intent(currentActivity, CaptureFinger.class);
            currentActivity.startActivityForResult(identyIntent,IDENTY_SDK_REQUEST);
        } catch (Exception e) {
            mCapturePromise.reject(E_FAILED_TO_SHOW_CAPTURE, e);
            mCapturePromise = null;
        }
    }

    public static native int nativeMultiply(int a, int b);
}
