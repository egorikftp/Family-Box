package com.egoriku.familybox.barcode;

public interface BarcodeListener {
    void onSuccess(String rawValue, int format);
}
