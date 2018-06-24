package com.egoriku.familybox.barcode;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstract base class for ML Kit frame processors. Subclasses need to implement {@link
 * #onSuccess(T, FrameMetadata, GraphicOverlay)} to define what they want to with the detection
 * results and {@link #detectInImage(FirebaseVisionImage)} to specify the detector object.
 *
 * @param <T> The type of the detected feature.
 */
public abstract class VisionProcessorBase<T> implements VisionImageProcessor {

    // Whether we should ignore process(). This is usually caused by feeding input data faster than
    // the model can handle.
    private final AtomicBoolean shouldThrottle = new AtomicBoolean(false);

    public VisionProcessorBase() {
    }

    @Override
    public void process(
            ByteBuffer data, final FrameMetadata frameMetadata) {
        if (shouldThrottle.get()) {
            return;
        }
        FirebaseVisionImageMetadata metadata =
                new FirebaseVisionImageMetadata.Builder()
                        .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                        .setWidth(frameMetadata.getWidth())
                        .setHeight(frameMetadata.getHeight())
                        .setRotation(frameMetadata.getRotation())
                        .build();

        detectInVisionImage(
                FirebaseVisionImage.fromByteBuffer(data, metadata), frameMetadata);
    }

    // Bitmap version
    @Override
    public void process(Bitmap bitmap) {
        if (shouldThrottle.get()) {
            return;
        }
        detectInVisionImage(FirebaseVisionImage.fromBitmap(bitmap), null);
    }

    /**
     * Detects feature from given media.Image
     *
     * @return created FirebaseVisionImage
     */
    @Override
    public void process(Image image, int rotation) {
        if (shouldThrottle.get()) {
            return;
        }
        // This is for overlay display's usage
        FrameMetadata frameMetadata =
                new FrameMetadata.Builder().setWidth(image.getWidth()).setHeight(image.getHeight
                        ()).build();
        FirebaseVisionImage fbVisionImage =
                FirebaseVisionImage.fromMediaImage(image, rotation);
        detectInVisionImage(fbVisionImage, frameMetadata);
    }

    private void detectInVisionImage(
            FirebaseVisionImage image,
            final FrameMetadata metadata) {
        detectInImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<T>() {
                            @Override
                            public void onSuccess(T results) {
                                shouldThrottle.set(false);
                                VisionProcessorBase.this.onSuccess(results, metadata);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                shouldThrottle.set(false);
                                VisionProcessorBase.this.onFailure(e);
                            }
                        });
        // Begin throttling until this frame of input has been processed, either in onSuccess or
        // onFailure.
        shouldThrottle.set(true);
    }

    @Override
    public void stop() {
    }

    protected abstract Task<T> detectInImage(FirebaseVisionImage image);

    protected abstract void onSuccess(
            @NonNull T results,
            @NonNull FrameMetadata frameMetadata);

    protected abstract void onFailure(@NonNull Exception e);
}
