package com.egoriku.familybox.barcode;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.widget.CompoundButton;

import com.egoriku.familybox.R;
import com.egoriku.familybox.barcode.barcodescanning.BarcodeScanningProcessor;
import com.egoriku.familybox.camera.CameraSource;
import com.egoriku.familybox.camera.CameraSourcePreview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.egoriku.familybox.MainActivity.BARCODE_VALUE;

/**
 * Demo app showing the various features of ML Kit for Firebase. This class is used to
 * set up continuous frame processing on frames from a camera source.
 */
public final class BarcodeScanningActivity extends AppCompatActivity implements OnRequestPermissionsResultCallback,
        CompoundButton.OnCheckedChangeListener, BarcodeListener {
    private static final String TAG = "BarcodeScanningActivity";
    private static final int PERMISSION_REQUESTS = 1;

    private CameraSource cameraSource = null;
    private CameraSourcePreview preview;

    @Override
    public void onSuccess(String rawValue, int format) {
        preview.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        setResult(RESULT_OK, new Intent().putExtra(BARCODE_VALUE, rawValue));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_live_preview);

        preview = (CameraSourcePreview) findViewById(R.id.firePreview);

        if (allPermissionsGranted()) {
            createCameraSource();
        } else {
            getRuntimePermissions();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TAG, "Set facing");
        if (cameraSource != null) {
            if (isChecked) {
                cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
            } else {
                cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);
            }
        }
        preview.stop();
        startCameraSource();
    }

    private void createCameraSource() {
        if (cameraSource == null) {
            cameraSource = new CameraSource(this);
        }

        cameraSource.setMachineLearningFrameProcessor(new BarcodeScanningProcessor(this));
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null");
                }

                preview.start(cameraSource);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        Log.i(TAG, "Permission granted!");
        if (allPermissionsGranted()) {
            createCameraSource();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }
}
