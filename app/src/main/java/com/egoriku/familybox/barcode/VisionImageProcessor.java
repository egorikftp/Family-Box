package com.egoriku.familybox.barcode;

import android.graphics.Bitmap;
import android.media.Image;

import com.google.firebase.ml.common.FirebaseMLException;

import java.nio.ByteBuffer;

/**
 * An inferface to process the images with different ML Kit detectors and custom image models.
 */
public interface VisionImageProcessor {

    /**
     * Processes the images with the underlying machine learning models.
     */
    void process(ByteBuffer data, FrameMetadata frameMetadata)
            throws FirebaseMLException;

    /**
     * Processes the bitmap images.
     */
    void process(Bitmap bitmap);

    /**
     * Processes the images.
     */
    void process(Image bitmap, int rotation);

    /**
     * Stops the underlying machine learning model and release resources.
     */
    void stop();
}
