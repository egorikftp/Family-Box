package com.egoriku.familybox.barcode.barcodescanning;

import android.support.annotation.NonNull;
import android.util.Log;

import com.egoriku.familybox.barcode.BarcodeListener;
import com.egoriku.familybox.camera.FrameMetadata;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.IOException;
import java.util.List;

/**
 * Barcode Detector Demo.
 */
public class BarcodeScanningProcessor extends VisionProcessorBase<List<FirebaseVisionBarcode>> {

    private static final String TAG = "BarcodeScanProc";

    private final FirebaseVisionBarcodeDetector detector;
    private BarcodeListener barcodeListener;

    public BarcodeScanningProcessor(BarcodeListener barcodeListener) {
        this.barcodeListener = barcodeListener;

        new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_CODE_128)
                .build();
        detector = FirebaseVision.getInstance().getVisionBarcodeDetector();
    }

    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception thrown while trying to close Barcode Detector: " + e);
        }
    }

    @Override
    protected Task<List<FirebaseVisionBarcode>> detectInImage(FirebaseVisionImage image) {
        return detector.detectInImage(image);
    }

    @Override
    protected void onSuccess(
            @NonNull List<FirebaseVisionBarcode> barcodes,
            @NonNull FrameMetadata frameMetadata) {

        if (barcodes.size() > 0) {
            FirebaseVisionBarcode firebaseVisionBarcode = barcodes.get(0);
            barcodeListener.onSuccess(firebaseVisionBarcode.getRawValue(), firebaseVisionBarcode.getFormat());
        }
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.e(TAG, "Barcode detection failed " + e);
    }
}
