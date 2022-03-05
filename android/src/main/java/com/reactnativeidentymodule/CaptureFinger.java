package com.reactnativeidentymodule;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.ReactActivity;
import com.identy.Attempt;
import com.identy.IdentyError;
import com.identy.IdentyResponse;
import com.identy.IdentyResponseListener;
import com.identy.IdentySdk;
import com.identy.InitializationListener;
import com.identy.TemplateSize;
import com.identy.WSQCompression;
import com.identy.enums.Finger;
import com.identy.enums.FingerDetectionMode;
import com.identy.enums.Hand;
import com.identy.enums.Template;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CaptureFinger extends ReactActivity {

    private static final String TAG = "CAPTURE_FINGER";

    boolean enableSpoofCheck = true;

    String mode = "demo";

    String licenseFile = "1168_com.kivopay2021-07-04 00_00_00.lic";

    static String NET_KEY = "AIzaSyDfN8of_hNmSTiW0HabvySeCKhDiuHCNOA";

    WSQCompression compression = WSQCompression.WSQ_10_1;

    boolean displayboxes = false;

    int base64encoding = Base64.DEFAULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_LOGS, Manifest.permission.INTERNET}, 1);
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_LOGS}, 1);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_LOGS}, 2);
            }
            if (checkSelfPermission(Manifest.permission.READ_LOGS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_LOGS}, 3);
            }
        }

        final HashMap<Template, HashMap<Finger, ArrayList<TemplateSize>>> requiredtemplates = new HashMap<>();
        ArrayList<TemplateSize> templateSizes = new ArrayList<>();
        templateSizes.add(TemplateSize.DEFAULT);
        templateSizes.add(TemplateSize.DEFAULT_MINUS_15);
        templateSizes.add(TemplateSize.DEFAULT_PLUS_15);
        // add fingers you want to get template for
        HashMap<Finger, ArrayList<TemplateSize>> fingerToGetTemplatesFor = new HashMap<>();
        fingerToGetTemplatesFor.put(Finger.INDEX, templateSizes);
        fingerToGetTemplatesFor.put(Finger.MIDDLE, templateSizes);
        fingerToGetTemplatesFor.put(Finger.RING, templateSizes);
        fingerToGetTemplatesFor.put(Finger.LITTLE, templateSizes);
        requiredtemplates.put(Template.WSQ, fingerToGetTemplatesFor);

        FingerDetectionMode[] detectionModes = new FingerDetectionMode[]{
                FingerDetectionMode.L4F
        };

        final boolean showprogressdialog = true;

        final IdentyResponseListener captureListener = new IdentyResponseListener() {
            @Override
            public void onAttempt(Hand hand, int i, Map<Finger, Attempt> map) {
                Log.d(TAG, "Now attempting");
            }
            @Override
            public void onResponse(IdentyResponse identyResponse, HashSet<String> transactionIDS) {

                //return captured image/wsq from identy
                Log.d(TAG, "Captured data is " + identyResponse.toString());
                Toast.makeText(CaptureFinger.this, "SUCCESSFULLY SCANNED",Toast.LENGTH_LONG).show();
                //// Do something after successful response (Appendix A)

                Intent intent = new Intent();
                try {
                    //TODO: Does not return JSON_DATA as expected
                    //JSONObject jsonObject = new JSONObject(identyResponse.toString());
                    //intent.putExtra("data", jsonObject.toString());
                    intent.putExtra("data", identyResponse.toString());
                    setResult(RESULT_OK, intent);
                    finish();
                //} catch (JSONException e) {
                } catch (Exception e) {
                    e.printStackTrace();
                    setResult(RESULT_CANCELED,null);
                    finish();
                }
            }
            @Override
            public void onErrorResponse(IdentyError identyError, HashSet<String> hashSet) {
                // Do something on error response (Appendix A)
                Toast.makeText(CaptureFinger.this, identyError.getMessage(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent();
                setResult(RESULT_CANCELED,null);
                finish();
            }
        };

        try {
            IdentySdk.newInstance( CaptureFinger.this, licenseFile , new InitializationListener<IdentySdk>() {
                @Override
                public void onInit(IdentySdk d) {
                    try {
                        d.setDisplayImages(true)
                                .setMode(mode)
                                .setAS(true)
                                .setDisplayBoxes(true)
                                .setRequiredTemplates(requiredtemplates)
                                .displayImages(displayboxes)
                                .setWSQCompression(compression)
                                .setDetectionMode(detectionModes)
                                .setBase64EncodingFlag(base64encoding)
                                .capture();

                        //More on detection modes ( Appendix B )

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, captureListener,NET_KEY,showprogressdialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}