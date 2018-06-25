package com.egoriku.familybox

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.egoriku.familybox.barcode.BarcodeScanningActivity
import com.egoriku.familybox.barcode.encoder.BarcodeEncoder
import com.egoriku.familybox.fragment.RoundedBottomSheetDialogFragment
import com.google.zxing.BarcodeFormat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 101
        const val BARCODE_VALUE = "BARCODE_VALUE"
    }

    private val encoder: BarcodeEncoder = BarcodeEncoder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            startActivityForResult(
                    Intent(this, BarcodeScanningActivity::class.java),
                    REQUEST_CODE)
        }

        bottomAppBar.setNavigationOnClickListener {
            RoundedBottomSheetDialogFragment().apply {
                show(supportFragmentManager, this.tag)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {

            val stringExtra = data.getStringExtra(BARCODE_VALUE)
          /*  val encodeBitmap = encoder.encodeBitmap(stringExtra, BarcodeFormat.CODE_128, imageView.width, imageView.height)
            imageView.setImageBitmap(encodeBitmap)
            textView.text = stringExtra*/
        }
    }
}